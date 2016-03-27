package diploma.elders.up.service;

import diploma.elders.up.bird.optimizer.BirdMatingOptimizerService;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    private List<Elder> getElderCVs(int size){
//        Iterator<Elder> all = elderRepository.findAll().iterator();
//        List<Elder> elders = new ArrayList<>();
//        while (all.hasNext()) {
//            elders.add(all.next());
//        }
//        return elders.subList(0, size);
//    }

//    public List<ElderDTO> computeEldersMatchingWithOpportunity(OpportunityDTO opportunity, int size){
//        List<Elder> elders = getElderCVs(20);
//        List<ElderDTO> eldersMatched = new ArrayList<>();
//        for(Elder elder : elders){
//            ElderDTO elderDTO = new ElderDTO(elder);
//            eldersMatched.add(elderDTO);
//        }
//        return skillMatchingAlgorithm.findMatchingCandidates(opportunity, eldersMatched, size);
//    }
//
//    public void applyMatchingAlgorithm(){
//        Opportunity opportunity = opportunityRepository.findOne(6);
//        OpportunityDTO opportunityDTO = new OpportunityDTO(opportunity);
//        Bird bird = birdMatingOptimizerService.applyBirdMatingOptimizer(computeEldersMatchingWithOpportunity(opportunityDTO, 20), opportunityDTO);
//        LOGGER.info("Found for opportunity: " + opportunity.getId() + " the solution containing skills: " + bird.getGenes() + " with matching score: " + bird.getMatchingScore());
//    }
}
