package diploma.elders.up.optimization.bird.optimizer;

import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.optimization.domain.*;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.SkillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Simonas on 3/5/2016.
 */
@Service
public class BirdMatingOptimizerService implements OptimizerService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BirdMatingOptimizerService.class);

    private static final double THRESHOLD = 0.78;
    private static final int FEMALE_MATES = 3;
    private static final double MONOGAMOUS_BIRDS_PERCENTAGE = 0.5;
    private static final double MALE_POLYGYNY_BIRDS_PERCENTAGE = 0.3;

    @Override
    public OptimizationResult applyOptimization(List<ElderDTO> elders) {
        Bird bird = applyBirdMatingOptimizer(elders);
        OptimizationResult optimizationResult = new OptimizationResult(bird.getMatchingScore(), bird.getElders());
        optimizationResult.setSkillsMatchingOffer(bird.getGenes());
        LOGGER.info("Result from bird optimization for each opportunity skill: {}.", bird.getGenes());
        return optimizationResult;
    }

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders){
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        long bestMatchNrOSkills;
        List<Bird> population = initializePopulation(elders);

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
                bestMatchNrOSkills = population.get(0).getGenes().stream().count();
                LOGGER.info("Largest score so far: " + largestMatchingScore + "  Population size: " + population.size() + " with nr of skills: " + bestMatchNrOSkills);
            }
        }
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

                List<ElderDTO> collect = femalesToMate.stream().map(Bird::getElders).flatMap(List::stream).collect(Collectors.toList());
                Bird brood = new Bird();
                brood.setMated(false);
                brood.setElders(union(male.getElders(), collect));
                male.setMated(true);
                male.increaseNrOfMates();
                for(Bird femaleToMate: femalesToMate){
                    femaleToMate.increaseNrOfMates();
                    if(femaleToMate.getNrOfMates() >= size){
                        femaleToMate.setMated(true);
                    }
                }
                List<Double> broodDNA = naturalSelectionOfGenes(male, femalesToMate);
                double match = broodDNA.stream().mapToDouble(i->i).average().getAsDouble();
                brood.setMatchingScore(match);
                brood.setGenes(broodDNA);
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
    private List<Double> naturalSelectionOfGenes(Bird male, List<Bird> females) {
        Double[] broodDNA = new Double[male.getGenes().size()];
        for(int i=0; i < male.getGenes().size(); i++){
            List<Double> genesOnPosition = new ArrayList<>();
            genesOnPosition.add(male.getGenes().get(i));
            for(Bird female: females){
                genesOnPosition.add(female.getGenes().get(i));
            }
            OptionalDouble max = genesOnPosition.stream().mapToDouble(g -> g).max();
            broodDNA[i] = max.getAsDouble();
        }
        return Arrays.asList(broodDNA);
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
            bird.addElder(elderDTO);
            bird.setMatchingScore(elderDTO.getMatchingPercentage());
            bird.setGenes(elderDTO.getMatchingSkills().stream().map(SkillDTO::getMatchingScore).collect(Collectors.toList()));
            bird.setMated(false);

            population.add(bird);
        }
        return population;
    }

    private List<ElderDTO> union(List<ElderDTO> male, List<ElderDTO> female){
        List<ElderDTO> al = new ArrayList<>();
        Set<ElderDTO> set = new HashSet<>();
        set.addAll(male);
        set.addAll(female);
        al.clear();
        al.addAll(set);
        return al;
    }
}
