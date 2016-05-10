package diploma.elders.up.optimization.bird.optimizer;

import diploma.elders.up.dao.documents.OptimizationParams;
import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.dao.repository.OptimizationParamsRepository;
import diploma.elders.up.dao.repository.OptimizationResultRepository;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.SkillDTO;
import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.optimization.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Simonas on 3/5/2016.
 */
@Service
public class BirdMatingOptimizerService implements OptimizerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BirdMatingOptimizerService.class);

    @Autowired
    private OptimizationParamsRepository optimizationParamsRepository;
    @Autowired
    private OptimizationResultRepository optimizationResultRepository;

    private static final double THRESHOLD = 0.98;
    private static final int FEMALE_MATES = 3;
    private static final double MONOGAMOUS_BIRDS_PERCENTAGE = 0.5;
    private static final double MALE_POLYGYNY_BIRDS_PERCENTAGE = 0.2;

    @Override
    public OptimizationResult applyOptimization(List<ElderDTO> elders) {
        long start = System.currentTimeMillis();
        Bird bird = applyBirdMatingOptimizer(elders);
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Time needed to execute optimization in milliseconds: " + time);
        OptimizationParams optimizationParams = new OptimizationParams(time, bird.getMatchingScore(), MONOGAMOUS_BIRDS_PERCENTAGE, MALE_POLYGYNY_BIRDS_PERCENTAGE, FEMALE_MATES, elders.size(), bird.getGenome().size(), bird.getGenome().stream().map(Genome::getElder).distinct().collect(Collectors.toList()).size());
        OptimizationParams save = optimizationParamsRepository.save(optimizationParams);
        OptimizationResult optimizationResult = new OptimizationResult(bird.getMatchingScore(), bird.getGenome().stream().map(Genome::getElder).distinct().collect(Collectors.toList()), save.getId());
        optimizationResult.setSkillsMatchingOffer(bird.getGenome().stream().map(Genome::getGeneScore).collect(Collectors.toList()));
        LOGGER.info("Result from bird optimization for each opportunity skill: {}.", bird.getGenome().stream().map(Genome::getGeneScore).collect(Collectors.toList()));
        optimizationResultRepository.save(optimizationResult);
        return optimizationResult;
    }

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders){
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        List<Bird> population = initializePopulation(elders);
        List<Bird> bestBirds = new ArrayList<>();

        while(largestMatchingScore <= THRESHOLD && areThereAnyUnmatedBrids(population) && population.size() > 2) {
            classifyPopulation(population);
            List<Bird> newPopulation = new ArrayList<>();

            List<Bird> monoMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());
            List<Bird> monoFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());

            List<Bird> newFromMono = giveBirth(monoMales, monoFemales, 1);
            newPopulation.addAll(newFromMono);
            List<Bird> newFromPoly = giveBirth(polyMales, polyFemales, FEMALE_MATES);
            newPopulation.addAll(newFromPoly);

            population = newPopulation;
            if (!population.isEmpty()) {
                Collections.sort(population, new BirdComparator());
                largestMatchingScore = population.get(0).getMatchingScore();
                LOGGER.info("Largest score so far: " + largestMatchingScore + "  Population size: " + population.size() + " with nr of elders in solution: " + population.get(0).getGenome().stream().map(Genome::getElder).distinct().collect(Collectors.toList()).size());
                bestBirds.add(population.get(0));
            }
        }
        //choose from birds with duplicate score the ones with the smallest nr of CVs
        population.clear();
        ListIterator<Bird> birdListIterator = bestBirds.listIterator();
        while(birdListIterator.hasNext()){
            Bird next = birdListIterator.next();
            Bird min = bestBirds.stream().filter(b -> b.getMatchingScore() == next.getMatchingScore()).min(Comparator.comparing(b -> b.getGenome().stream().map(Genome::getElder).distinct().collect(Collectors.toList()).size())).get();
            population.add(min);
            bestBirds.remove(next);
            birdListIterator = bestBirds.listIterator();
        }
        Collections.sort(population, new BirdComparator());
        return population.get(0);
    }

    private List<Bird> giveBirth(List<Bird> males, List<Bird> females, int mateSize){
        List<Bird> newPopulation = new ArrayList<>();
        Bird bestBird = males.get(0);
        for(Bird male : males){
            if(!females.isEmpty()){
                Collections.sort(females, new BirdComparator());
                Iterator<Bird> iterator = females.iterator();
                int size = 0;
                List<Bird> femalesToMate = new ArrayList<>();
                while(iterator.hasNext() && size <= mateSize) {
                    femalesToMate.add(iterator.next());
                }

                if(femalesToMate.isEmpty()){
                    return Collections.singletonList(bestBird);
                }

                male.setMated(true);
                male.increaseNrOfMates();
                for(Bird femaleToMate: femalesToMate){
                    femaleToMate.increaseNrOfMates();
                    if(femaleToMate.getNrOfMates() >= size){
                        femaleToMate.setMated(true);
                    }
                }

                List<Genome> genome = naturalSelectionOfGenes(male, femalesToMate);;
                double match = genome.stream().mapToDouble(Genome::getGeneScore).average().getAsDouble();
                Bird brood = new Bird();
                brood.setMated(false);
                brood.setMatchingScore(match);
                brood.setGenome(genome);
                newPopulation.add(brood);
                if(match >= THRESHOLD){
                    return newPopulation;
                }
            }
        }
        List<Bird> unmatedMales = males.stream().filter(b -> !b.isMated()).collect(Collectors.toList());
        List<Bird> unmatedFemales = females.stream().filter(b -> !b.isMated()).collect(Collectors.toList());

        if(!unmatedFemales.isEmpty()){
            newPopulation.addAll(unmatedFemales);
        }
        if(!unmatedMales.isEmpty()){
            newPopulation.addAll(unmatedMales);
        }
        return newPopulation;
    }
    private List<Genome> naturalSelectionOfGenes(Bird male, List<Bird> females) {
        Genome[] genome = new Genome[male.getGenome().size()];
        for(int i=0; i < male.getGenome().size(); i++){
            List<Bird> birds = new ArrayList<>();
            birds.add(male);
            birds.addAll(females);
            Genome maxGenome = findMaxOnPosition(birds, i);
            genome[i] = maxGenome;
        }
        return Arrays.asList(genome);
    }

    private Genome findMaxOnPosition(List<Bird> birds, int i) {
        double asDouble = birds.stream().mapToDouble(b -> b.getGenome().get(i).getGeneScore()).max().getAsDouble();
        return birds.stream().filter(b->b.getGenome().get(i).getGeneScore() == asDouble).map(b -> b.getGenome().get(i)).findFirst().get();
    }

    private boolean areThereAnyUnmatedBrids(List<Bird> birds){
        return birds.stream().anyMatch(b -> !b.isMated());
    }

    //first half monogamous, second half polygynyous. monogamous : 50/50 males/females, polygynyous:  30% males, 70% females
    private void classifyPopulation(List<Bird> population) {
        for (int i = 0; i < population.size(); i++) {
            //monogamous
            if (i <= population.size() * MONOGAMOUS_BIRDS_PERCENTAGE - 1) {
                if (i % 2 == 0) {
                    population.get(i).setBirdGender(BirdGender.MALE);
                } else {
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.MONOGAMOUS);
            }
            //polygynyous
            else {
                if (i <= (population.size() * 0.5) + (population.size() * 0.5) * MALE_POLYGYNY_BIRDS_PERCENTAGE) {
                    population.get(i).setBirdGender(BirdGender.MALE);
                } else {
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.POLYGYNOUS);
            }
        }
    }

    private List<Bird> initializePopulation(List<ElderDTO> elders){
        List<Bird> population = new ArrayList<>();

        for(ElderDTO elderDTO : elders){
            Bird bird = new Bird();
            List<Genome> genome = new ArrayList<>();
            for(SkillDTO skillDTO : elderDTO.getMatchingSkills()){
                Genome gene = new Genome(elderDTO, skillDTO.getMatchingScore());
                genome.add(gene);
            }
            bird.setMatchingScore(elderDTO.getMatchingPercentage());
            bird.setMated(false);
            bird.setGenome(genome);
            population.add(bird);
        }
        return population;
    }
}
