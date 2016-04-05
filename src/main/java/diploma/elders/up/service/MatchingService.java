package diploma.elders.up.service;

import diploma.elders.up.bird.optimizer.BirdMatingOptimizerService;
import diploma.elders.up.bird.optimizer.NoSuchBirdException;
import diploma.elders.up.bird.optimizer.domain.Bird;
import diploma.elders.up.dao.documents.Opportunity;
import diploma.elders.up.dao.documents.Senior;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SemanticMatchingAlgorithm;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simonas on 3/5/2016.
 */
@Service
public class MatchingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingService.class);

    @Autowired
    private SkillMatchingAlgorithm skillMatchingAlgorithm;
    @Autowired
    private SeniorRepository elderRepository;
    @Autowired
    private BirdMatingOptimizerService birdMatingOptimizerService;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private SemanticMatchingAlgorithm semanticMatchingAlgorithm;

    private List<Senior> getElderCVs(int size){
        Iterator<Senior> all = elderRepository.findAll().iterator();
        List<Senior> elders = new ArrayList<>();
        while (all.hasNext()) {
            elders.add(all.next());
        }
        return randomList(elders, size);
    }

    public List<ElderDTO> computeEldersMatchingWithOpportunity(OpportunityDTO opportunity, int size) throws ExecutionException, InterruptedException {
        List<Senior> elders = getElderCVs(size);
        List<ElderDTO> eldersMatched = new ArrayList<>();
        for(Senior elder : elders){
            ElderDTO elderDTO = new ElderDTO(elder);
            eldersMatched.add(elderDTO);
        }
        return semanticMatchingAlgorithm.findMatchingCandidates(opportunity, eldersMatched, size);
    }

    public void applyMatchingAlgorithm(int size) throws NoSuchBirdException, ExecutionException, InterruptedException {
        Opportunity opportunity = opportunityRepository.findAll().get(0);
        OpportunityDTO opportunityDTO = new OpportunityDTO(opportunity);
        Bird bird = birdMatingOptimizerService.applyBirdMatingOptimizer(computeEldersMatchingWithOpportunity(opportunityDTO, size), opportunityDTO);
        LOGGER.info("Found for opportunity: " + opportunity.getId() + " the solution containing skills: " + bird.getGenes() + " with matching score: " + bird.getMatchingScore());
        int skills = 0;
        for(ElderDTO elderDTO : bird.getGenes()){
            skills += elderDTO.getElder().getSkills().size();
        }
        LOGGER.info("Matching score : {} with a number of {} elders and with a total number of skills : {}!", bird.getMatchingScore(), bird.getGenes().size(),skills);
    }

    private List<Senior> randomList(List<Senior> seniors, int size){
        Random rng = new Random();
        List<Senior> generated = new ArrayList<>();
        while (generated.size() < size) {
            Integer next = rng.nextInt(seniors.size());
            generated.add(seniors.get(next));
        }
        return generated;
    }
}
