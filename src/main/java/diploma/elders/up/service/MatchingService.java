package diploma.elders.up.service;

import diploma.elders.up.dao.documents.Opportunity;
import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.dao.documents.Senior;
import diploma.elders.up.dao.repository.MatchingResultRepository;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.semantic.matching.ParallelMatcher;
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
    private SeniorRepository elderRepository;
    @Autowired
    private OptimizerService optimizerService;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private ParallelMatcher parallelMatcher;
    @Autowired
    private MatchingResultRepository matchingResultRepository;

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
        return parallelMatcher.findMatchingCandidates(opportunity, eldersMatched, size);
    }

    public OptimizationResult applyMatchingAlgorithm(int size, String opportunityId) throws ExecutionException, InterruptedException {
        //Opportunity opportunity = opportunityRepository.findAll().get(28);
        Opportunity opportunity = opportunityRepository.findOne(opportunityId);
        OpportunityDTO opportunityDTO = new OpportunityDTO(opportunity);
        LOGGER.info("Applying matching algorithm for opportunity: {} with a number of {} elders.", opportunityDTO, size);
        OptimizationResult optimizationResult = optimizerService.applyOptimization(computeEldersMatchingWithOpportunity(opportunityDTO, size));
        //OptimizationResult optimizationResult = optimizerService.applyOptimization(matchingResultRepository.findOne("5728d761bba552f756fd132c").getElders());
        LOGGER.info("Matching score : {} with a number of {} elders!", optimizationResult.getMatchingScore(), optimizationResult.getElders().size());
        return optimizationResult;
    }

    public List<ElderDTO> computePSOEldersMatchingWithOpportunity(OpportunityDTO opportunity, int size) throws ExecutionException, InterruptedException {
        List<Senior> elders = getElderCVs(size);
        List<ElderDTO> eldersMatched = new ArrayList<>();
        for(Senior elder : elders){
            ElderDTO elderDTO = new ElderDTO(elder);
            eldersMatched.add(elderDTO);
        }
        return parallelMatcher.findMatchingCandidates(opportunity, eldersMatched, size);
    }

    public void applyPSOMatchingAlgorithm(int size) throws  ExecutionException, InterruptedException {
        Opportunity opportunity = opportunityRepository.findAll().get(8);
        OpportunityDTO opportunityDTO = new OpportunityDTO(opportunity);
        LOGGER.info("Applying matching algorithm for opportunity: {} with a number of {} elders.", opportunityDTO, size);
        OptimizationResult optimizationResult = optimizerService.applyOptimization(computePSOEldersMatchingWithOpportunity(opportunityDTO, size));
        LOGGER.info("Matching score : {} with a number of {} elders!", optimizationResult.getMatchingScore(), optimizationResult.getElders().size());
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
