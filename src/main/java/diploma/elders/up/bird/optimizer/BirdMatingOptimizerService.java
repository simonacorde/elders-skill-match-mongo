package diploma.elders.up.bird.optimizer;

import diploma.elders.up.bird.optimizer.domain.Bird;
import diploma.elders.up.bird.optimizer.domain.BirdComparator;
import diploma.elders.up.bird.optimizer.domain.BirdGender;
import diploma.elders.up.bird.optimizer.domain.BirdType;
import diploma.elders.up.dao.entity.Skill;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
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
    private SkillMatchingAlgorithm skillMatchingAlgorithm;

    private static final double THRESHOLD = 0.50;

    public Bird applyBirdMatingOptimizer(List<ElderDTO> elders, OpportunityDTO opportunity){
        double largestMatchingScore = elders.get(0).getMatchingPercentage(); // largest score in elders
        List<Bird> population = initializePopulation(elders);

        while(largestMatchingScore <= THRESHOLD){
            classifyPopulation(population);
            List<Bird> newPopulation = new ArrayList<>();
            for(Bird bird : population){
                if(bird.getBirdType().equals(BirdType.MONOGAMOUS)){
                    if(bird.getBirdGender().equals(BirdGender.MALE)){
                        List<Integer> femalePositions = randomUnmatedFemaleBirdsFromPopulation(population.subList(0, population.size() / 2), 10);
                        Map<Integer,Bird> females = new HashMap<>();
                        for(Integer position : femalePositions){
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
                        bird.setMated(true);
                        Bird brood = new Bird();
                        brood.setMated(false);
                        brood.addGenes(union(bird.getGenes(), population.get(positionOfFemaleBird).getGenes()));
                        //compute new matching after mating
                        Set<Skill> broodAllSkills = new HashSet<>();
                        for(ElderDTO elderDTO : brood.getGenes()){
                            broodAllSkills.addAll(elderDTO.getElder().getSkills());
                        }
                        double match = skillMatchingAlgorithm.match(opportunity, broodAllSkills);
                        brood.setMatchingScore(match);
                        newPopulation.add(brood);
                    }
                }else{
                    if(bird.getBirdGender().equals(BirdGender.MALE)){
                        List<Integer> femalePositions = randomFemaleBirdsFromPopulationWithLessThanNrOfMates(population.subList(0, population.size() / 2), 10, 3);
                        Map<Integer,Bird> females = new HashMap<>();
                        for(Integer position : femalePositions){
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
                        bird.setMated(true);
                        Bird brood = new Bird();
                        brood.setMated(false);
                        brood.addGenes(union(bird.getGenes(), population.get(positionOfFemaleBird).getGenes()));
                        //compute new matching after mating
                        Set<Skill> broodAllSkills = new HashSet<>();
                        for(ElderDTO elderDTO : brood.getGenes()){
                            broodAllSkills.addAll(elderDTO.getElder().getSkills());
                        }
                        double match = skillMatchingAlgorithm.match(opportunity, broodAllSkills);
                        brood.setMatchingScore(match);
                        newPopulation.add(brood);
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

    private void classifyPopulation(List<Bird> population){
        for(int i=0; i < population.size(); i++) {
            if(i <= population.size() /2 - 1){
                if(i % 2 == 0 ){
                    population.get(i).setBirdGender(BirdGender.MALE);
                }else{
                    population.get(i).setBirdGender(BirdGender.FEMALE);
                }
                population.get(i).setBirdType(BirdType.MONOGAMOUS);
            }else{
                if(i > population.size() / 2 && i <= ((population.size() / 2) / 10) * 3){
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
            bird.setMatchingScore(elderDTO.getMatchingPercentage());
            population.add(bird);
        }

        for(Bird bird : population){
            bird.setMated(false);
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
