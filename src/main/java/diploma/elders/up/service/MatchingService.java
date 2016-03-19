package diploma.elders.up.service;

import diploma.elders.up.bird.optimizer.BirdMatingOptimizerService;
import diploma.elders.up.bird.optimizer.domain.Bird;
import diploma.elders.up.dao.entity.Elder;
import diploma.elders.up.dao.entity.Opportunity;
import diploma.elders.up.dao.repository.ElderRepository;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Simonas on 3/5/2016.
 */
@Service
public class MatchingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingService.class);

    @Autowired
    private SkillMatchingAlgorithm skillMatchingAlgorithm;
    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private BirdMatingOptimizerService birdMatingOptimizerService;
    @Autowired
    private OpportunityRepository opportunityRepository;

    private List<Elder> getElderCVs(){
        Iterator<Elder> all = elderRepository.findAll().iterator();
        List<Elder> elders = new ArrayList<>();
        while (all.hasNext()) {
            elders.add(all.next());
        }
        return elders;
    }

    public List<ElderDTO> computeEldersMatchingWithOpportunity(OpportunityDTO opportunity, int size){
        List<Elder> elders = getElderCVs();
        List<ElderDTO> eldersMatched = new ArrayList<>();
        for(Elder elder : elders){
            ElderDTO elderDTO = new ElderDTO(elder);
            eldersMatched.add(elderDTO);
        }
        return skillMatchingAlgorithm.findMatchingCandidates(opportunity, eldersMatched, size);
    }

    public void applyMatchingAlgorithm(){
        Opportunity opportunity = opportunityRepository.findOne(7);
        OpportunityDTO opportunityDTO = new OpportunityDTO(opportunity);
        Bird bird = birdMatingOptimizerService.applyBirdMatingOptimizer(computeEldersMatchingWithOpportunity(opportunityDTO, 10), opportunityDTO);
        LOGGER.info("Found for opportunity: " + opportunity.getId() + " the solution containing skills: " + bird.getGenes() + " with matching score: " + bird.getMatchingScore());
    }
}
