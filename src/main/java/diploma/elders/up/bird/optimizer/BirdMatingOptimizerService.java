package diploma.elders.up.bird.optimizer;

import diploma.elders.up.bird.optimizer.domain.Bird;
import diploma.elders.up.bird.optimizer.domain.BirdComparator;
import diploma.elders.up.bird.optimizer.domain.BirdGender;
import diploma.elders.up.bird.optimizer.domain.BirdType;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
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
public class BirdMatingOptimizerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirdMatingOptimizerService.class);

    private static final double THRESHOLD = 0.78;
    private static final int FEMALE_MATES = 3;
    private static final double MONOGAMOUS_BIRDS_PERCENTAGE = 0.5;
    private static final double MALE_POLYGYNY_BIRDS_PERCENTAGE = 0.3;
    private static final int RANDOM_FEMALES_TO_MATE = 10;

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders, OpportunityDTO opportunity) throws NoSuchBirdException {
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        long bestMatchNrOSkills = elders.get(0).getElder().getSkills().size();
        List<Bird> population = initializePopulation(elders, opportunity.getOpportunity().getSkills().size());

        while(largestMatchingScore <= THRESHOLD && areThereAnyUnmatedBrids(population) && population.size() > 1){
            classifyPopulation(population);
            List<Bird> newPopulation = new ArrayList<>();

            List<Bird> monoMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());
            List<Bird> monoFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());

            List<Bird> newFromMono = giveBirth(monoMales, monoFemales, 1);
            List<Bird> newFromPoly = giveBirth(polyMales, polyFemales, FEMALE_MATES);
            newPopulation.addAll(newFromMono);
            newPopulation.addAll(newFromPoly);

            population = newPopulation;
            Collections.sort(population, new BirdComparator());
            largestMatchingScore = population.get(0).getMatchingScore();
            bestMatchNrOSkills = population.get(0).getGenes().stream().count();
            LOGGER.info("Largest score so far: "+ largestMatchingScore+"  Population size: "+ population.size() + " with nr of skills: "+ bestMatchNrOSkills);
        }
        return population.get(0);
    }

    private List<Bird> giveBirth(List<Bird> males, List<Bird> females, int mateSize){
        List<Bird> newPopulation = new ArrayList<>();

        for(Bird male : males){
            if(!females.isEmpty()){
                Collections.sort(females, new BirdComparator());
                Iterator<Bird> iterator = females.iterator();
                int size = 0;
                List<Bird> femalesToMate = new ArrayList<>();
                while(iterator.hasNext() && size <= mateSize) {
                    femalesToMate.add(iterator.next());
                }

                List<ElderDTO> collect = femalesToMate.stream().map(Bird::getElders).flatMap(List::stream).collect(Collectors.toList());
                Bird brood = new Bird();
                brood.setMated(false);
                brood.setElders(union(male.getElders(), collect));
                male.setMated(true);
                for(Bird femaleToMate: femalesToMate){
                    femaleToMate.setMated(true);
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

        return newPopulation;
    }

    private List<Bird> giveBirthToMatchedBroods(List<Bird> males, List<Bird> females, int mateSize) {
        List<Bird> newPopulation = new ArrayList<>();
        for(Bird male : males) {
            List<Integer> femalePositions = randomFemaleBirdsFromPopulationWithLessThanNrOfMates(females, RANDOM_FEMALES_TO_MATE, mateSize);
            if (femalePositions.isEmpty()) {
                return newPopulation;
            }
            List<Bird> maxMatchingScoreFemales = new ArrayList<>();
            maxMatchingScoreFemales.addAll(getFirstMaxMatchingScores(females, mateSize));
            for(Bird bird : maxMatchingScoreFemales){
                bird.increaseNrOfMates();
            }
            male.setMated(true);
            Bird brood = new Bird();
            brood.setMated(false);
            List<ElderDTO> collect = maxMatchingScoreFemales.stream().map(Bird::getElders).flatMap(List::stream).collect(Collectors.toList());
//            //set union of parent genes to the brood
//            brood.setElders(union(male.getElders(), collect));
//            //compute new matching after mating
//            List<Skill> broodAllSkills = new ArrayList<>();
//            for (ElderDTO elderDTO : brood.getElders()) {
//                broodAllSkills.addAll(elderDTO.getElder().getSkills());
//            }
//            double match = semanticMatchingAlgorithm.match(opportunity, broodAllSkills);
//            //double match = rematchBroodSkills(broodAllSkills);

            //other match
            brood.setElders(union(male.getElders(), collect));
            List<Double> broodDNA = naturalSelectionOfGenes(male, maxMatchingScoreFemales);
            double match = broodDNA.stream().mapToDouble(i->i).average().getAsDouble();
            brood.setMatchingScore(match);
            brood.setGenes(broodDNA);
            newPopulation.add(brood);
            if(match >= THRESHOLD){
                return newPopulation;
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

    private List<Bird> getFirstMaxMatchingScores(List<Bird> females, int femaleMates) {
        Collections.sort(females, new BirdComparator());
        return females.subList(0, femaleMates);
    }

    private boolean areThereAnyUnmatedBrids(List<Bird> birds){
        return birds.stream().anyMatch(b -> !b.isMated());
    }

    //first half monogamous, second half polygynyous. monogamous : 50/50 males/females, polygynyous:  30% males, 70% females
    private void classifyPopulation(List<Bird> population){
        for(int i=0; i < population.size(); i++) {
            //monogamous
            if(i <= population.size() * MONOGAMOUS_BIRDS_PERCENTAGE - 1){
                if(i % 2 == 0 ){
                    population.get(i).setBirdGender(BirdGender.MALE);
                }else{
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.MONOGAMOUS);
            }
            //polygynyous
            else{
                if(i <= (population.size() * 0.5) + (population.size() * 0.5) * MALE_POLYGYNY_BIRDS_PERCENTAGE){
                    population.get(i).setBirdGender(BirdGender.MALE);
                }else{
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.POLYGYNOUS);
            }
        }
    }

    private List<Integer> randomFemaleBirdsFromPopulationWithLessThanNrOfMates(List<Bird> birds, int nrOfRandomBirds, int nrOfMates){
        Random rng = new Random();
        List<Integer> generated = new ArrayList<>();
        while (generated.size() < nrOfRandomBirds) {
            Integer next = rng.nextInt(birds.size());
            if(birds.get(next).getNrOfMates() < nrOfMates) {
                generated.add(next);
            }
        }
        return generated;
    }

    private double rematchBroodSkills(List<SkillDTO> broodSkills){
        double matchScoreSum = 0;
        for(SkillDTO skill: broodSkills){
            matchScoreSum += skill.getMatchingScore();
        }
        return matchScoreSum / (double) broodSkills.size();
    }

    private Bird getMaxMatchingScore(List<Bird> birds){
        return Collections.max(birds, Comparator.comparing(Bird::getMatchingScore));
    }

    private List<Bird> initializePopulation(List<ElderDTO> elders, int opportunitySize){
        List<Bird> population = new ArrayList<>();
        Double[] genes = new Double[opportunitySize];
        for(int i=0;i<genes.length;i++){
            genes[i] = 0.0;
        }

        for(ElderDTO elderDTO : elders){
            Bird bird = new Bird();
            bird.addElder(elderDTO);
            for(int i=0; i < opportunitySize; i++){
                for(SkillDTO skillDTO: elderDTO.getMatchingSkills()){
                    if(skillDTO.getOpportunityPosition() == i){
                        genes[i] = skillDTO.getMatchingScore();
                    }
                }
            }
            List<Double> genesList = Arrays.asList(genes);
            bird.setGenes(genesList);
            bird.setMated(false);
            bird.setMatchingScore(genesList.stream().mapToDouble(i->i).average().getAsDouble());
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
