package diploma.elders.up.bird.optimizer;

import diploma.elders.up.bird.optimizer.domain.Bird;
import diploma.elders.up.bird.optimizer.domain.BirdComparator;
import diploma.elders.up.bird.optimizer.domain.BirdGender;
import diploma.elders.up.bird.optimizer.domain.BirdType;
import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SemanticMatchingAlgorithm;
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
public class BirdMatingOptimizerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirdMatingOptimizerService.class);

    @Autowired
    private SemanticMatchingAlgorithm semanticMatchingAlgorithm;

    private static final double THRESHOLD = 0.80;
    private static final int FEMALE_MATES = 3;
    private static final double MONOGAMOUS_BIRDS_PERCENTAGE = 0.5;
    private static final double MALE_POLYGYNY_BIRDS_PERCENTAGE = 0.3;
    private static final int RANDOM_FEMALES_TO_MATE = 10;

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders, OpportunityDTO opportunity) throws NoSuchBirdException {
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        List<Bird> population = initializePopulation(elders);

        while(largestMatchingScore <= THRESHOLD && areThereAnyUnmatedBrids(population) && !population.isEmpty()){
            classifyPopulation(population);
            List<Bird> newPopulation = new ArrayList<>();

            List<Bird> monoMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyMales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.MALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());
            List<Bird> monoFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.MONOGAMOUS))).collect(Collectors.toList());
            List<Bird> polyFemales = population.stream().filter(b -> (b.getBirdGender().equals(BirdGender.FEMALE) && b.getBirdType().equals(BirdType.POLYGYNOUS))).collect(Collectors.toList());

            List<Bird> newFromMono = giveBirthToMatchedBroods(monoMales, monoFemales, opportunity, 1);
            List<Bird> newFromPoly = giveBirthToMatchedBroods(polyMales, polyFemales, opportunity, FEMALE_MATES);
            newPopulation.addAll(newFromMono);
            newPopulation.addAll(newFromPoly);

            population = newPopulation;
            Collections.sort(population, new BirdComparator());
            largestMatchingScore = population.get(0).getMatchingScore();
            LOGGER.info("Largest score so far: "+ largestMatchingScore+"  Population size: "+ population.size());
        }
        return population.get(0);
    }

    private List<Bird> giveBirthToMatchedBroods(List<Bird> males, List<Bird> females, OpportunityDTO opportunity, int combiningSize) {
        List<Bird> newPopulation = new ArrayList<>();
        for(Bird male : males) {
            List<Integer> femalePositions = randomFemaleBirdsFromPopulationWithLessThanNrOfMates(females, RANDOM_FEMALES_TO_MATE, combiningSize);
            if (femalePositions.isEmpty()) {
                return newPopulation;
            }
            Bird maxMatchingScoreFemale = getMaxMatchingScore(females);
            maxMatchingScoreFemale.increaseNrOfMates();
            male.setMated(true);
            Bird brood = new Bird();
            brood.setMated(false);
            brood.addGenes(union(male.getGenes(), maxMatchingScoreFemale.getGenes()));
            //compute new matching after mating
            List<Skill> broodAllSkills = new ArrayList<>();
            for (ElderDTO elderDTO : brood.getGenes()) {
                broodAllSkills.addAll(elderDTO.getElder().getSkills());
            }
            double match = semanticMatchingAlgorithm.match(opportunity, broodAllSkills);
            brood.setMatchingScore(match);
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

    private Bird getMaxMatchingScore(List<Bird> birds){
        return Collections.max(birds, Comparator.comparing(Bird::getMatchingScore));
    }

    private List<Bird> initializePopulation(List<ElderDTO> elders){
        List<Bird> population = new ArrayList<>();
        for(ElderDTO elderDTO : elders){
            Bird bird = new Bird();
            bird.addGene(elderDTO);
            bird.setMated(false);
            bird.setMatchingScore(elderDTO.getMatchingPercentage());
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
