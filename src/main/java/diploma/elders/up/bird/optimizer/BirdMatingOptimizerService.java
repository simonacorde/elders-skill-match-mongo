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
    private static final double MALE__POLYGYNY_BIRDS_PERCENTAGE = 0.3;

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders, OpportunityDTO opportunity) throws NoSuchBirdException {
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        List<Bird> population = initializePopulation(elders);

        while(largestMatchingScore <= THRESHOLD && areThereAnyUnmatedBrids(population)){
            classifyPopulation(population);
            List<Bird> newPopulation = new ArrayList<>();
            for(int i=0; i< population.size(); i++) {
                if(population.get(i).getBirdGender().equals(BirdGender.MALE)) {
                    if (population.get(i).getBirdType().equals(BirdType.MONOGAMOUS)) {
                        List<Integer> femalePositions = randomUnmatedFemaleBirdsFromPopulation(population.subList(0, population.size() / 2), 10);
                        if (femalePositions.isEmpty()) {
                            throw new NoSuchBirdException("There are no more unmated monogamous female birds!");
                        }
                        Map<Integer, Bird> females = new HashMap<>();
                        for (Integer position : femalePositions) {
                            females.put(position, population.get(position));
                        }
                        Bird maxMatchingScoreFemale = getMaxMatchingScore(females);
                        int positionOfFemaleBird = 0;
                        for (Map.Entry<Integer, Bird> entry : females.entrySet()) {
                            if (Objects.equals(maxMatchingScoreFemale, entry.getValue())) {
                                positionOfFemaleBird = entry.getKey();
                            }
                        }
                        population.get(positionOfFemaleBird).setMated(true);
                        population.get(i).setMated(true);
                        Bird brood = new Bird();
                        brood.setMated(false);
                        brood.addGenes(union(population.get(i).getGenes(), population.get(positionOfFemaleBird).getGenes()));
                        //compute new matching after mating
                        List<Skill> broodAllSkills = new ArrayList<>();
                        for (ElderDTO elderDTO : brood.getGenes()) {
                            broodAllSkills.addAll(elderDTO.getElder().getSkills());
                        }
                        double match = semanticMatchingAlgorithm.match(opportunity, broodAllSkills);
                        brood.setMatchingScore(match);
                        newPopulation.add(brood);
                        for (int j = 0; j < population.size(); j++) {
                            if (j != i && j != positionOfFemaleBird)
                                newPopulation.add(population.get(j));
                        }
                    } else {
                        List<Integer> femalePositions = randomFemaleBirdsFromPopulationWithLessThanNrOfMates(population.subList(0, population.size() / 2), 10, FEMALE_MATES);
                        if (femalePositions.isEmpty()) {
                            throw new NoSuchBirdException("There are no more unmated polygynyous female birds!");
                        }
                        Map<Integer, Bird> females = new HashMap<>();
                        for (Integer position : femalePositions) {
                            females.put(position, population.get(position));
                        }
                        Bird maxMatchingScoreFemale = getMaxMatchingScore(females);
                        int positionOfFemaleBird = 0;
                        for (Map.Entry<Integer, Bird> entry : females.entrySet()) {
                            if (Objects.equals(maxMatchingScoreFemale, entry.getValue())) {
                                positionOfFemaleBird = entry.getKey();
                            }
                        }
                        population.get(positionOfFemaleBird).increaseNrOfMates();
                        population.get(i).setMated(true);
                        Bird brood = new Bird();
                        brood.setMated(false);
                        brood.addGenes(union(population.get(i).getGenes(), population.get(positionOfFemaleBird).getGenes()));
                        //compute new matching after mating
                        List<Skill> broodAllSkills = new ArrayList<>();
                        for (ElderDTO elderDTO : brood.getGenes()) {
                            broodAllSkills.addAll(elderDTO.getElder().getSkills());
                        }
                        double match = semanticMatchingAlgorithm.match(opportunity, broodAllSkills);
                        brood.setMatchingScore(match);
                        newPopulation.add(brood);
                        for (int j = 0; j < population.size(); j++) {
                            if (j != i && j != positionOfFemaleBird)
                                newPopulation.add(population.get(j));
                        }
                    }
                }
            }
            population = newPopulation;
            Collections.sort(population, new BirdComparator());
            largestMatchingScore = population.get(0).getMatchingScore();
            LOGGER.info("Largest score so far: "+ largestMatchingScore+"  Population size: "+ population.size());
        }
        return population.get(0);
    }

    private int randomUnmatedMaleBird(List<Bird> population) {
        int pos = randomNumber(0, population.size() - 1);
        Bird bird = population.get(pos);
        if(population.stream().anyMatch(b-> (!b.isMated()) && BirdGender.MALE.equals(b.getBirdGender()))) {
            while (BirdGender.MALE.equals(bird.getBirdGender()) && !bird.isMated()) {
                pos = randomNumber(0, population.size() - 1);
            }
            return pos;
        }else return -1;
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
                if(i <= (population.size() * 0.5) + (population.size() * 0.5) * MALE__POLYGYNY_BIRDS_PERCENTAGE){
                    population.get(i).setBirdGender(BirdGender.MALE);
                }else{
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.POLYGYNOUS);
            }
        }
    }

    private List<Integer> randomUnmatedFemaleBirdsFromPopulation(List<Bird> birds, int nrOfRandomBirds){
        Random rng = new Random();
        List<Integer> generated = new ArrayList<>();
        while (generated.size() < nrOfRandomBirds) {
            Integer next = rng.nextInt(birds.size());
            if(birds.get(next).getBirdGender().equals(BirdGender.FEMALE) && !birds.get(next).isMated()) {
                generated.add(next);
            }
        }
        return generated;
    }

    private List<Integer> randomFemaleBirdsFromPopulationWithLessThanNrOfMates(List<Bird> birds, int nrOfRandomBirds, int nrOfMates){
        Random rng = new Random();
        List<Integer> generated = new ArrayList<>();
        while (generated.size() < nrOfRandomBirds) {
            Integer next = rng.nextInt(birds.size());
            if(birds.get(next).getBirdGender().equals(BirdGender.FEMALE) && birds.get(next).getNrOfMates() < nrOfMates) {
                generated.add(next);
            }
        }
        return generated;
    }

    private Bird getMaxMatchingScore(Map<Integer, Bird> birds){
        return Collections.max(birds.values(), Comparator.comparing(Bird::getMatchingScore));
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

    private int randomNumber(int min, int max) {

        Random random = new Random();

        return random.nextInt(max - min + 1) + min;


    }
}
