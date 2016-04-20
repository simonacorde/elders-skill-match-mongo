package diploma.elders.up.semantic.matching;

import diploma.elders.up.dto.ElderComparator;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Simonas on 4/8/2016.
 */
@Component
public class ParallelMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelMatcher.class);

    @Autowired
    private OntologySemanticMatcher ontologySemanticMatcher;

    public List<ElderDTO> findMatchingCandidates(OpportunityDTO op, List<ElderDTO> candidates, int size) {
        List<ElderDTO> matchingElders = new ArrayList<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        List<Future> resultList = new ArrayList<>();

        for (ElderDTO elder : candidates) {
            Matcher matcher = new Matcher(ontologySemanticMatcher, op, elder);
            Future result = executor.submit(matcher);
            resultList.add(result);
        }

        for(Future future : resultList)
        {
            try {
                ElderDTO matchedElder = (ElderDTO)future.get();
                matchingElders.add(matchedElder);
                LOGGER.info("Match score between opportunity: "+ op.getOpportunity().getId() + " and elder: "+ matchedElder.getElder().getId());
            }
            catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Thread was interrupted!", e);
            }
        }
        executor.shutdown();

        Collections.sort(matchingElders, new ElderComparator());
        if (matchingElders.size() > size) {
            return matchingElders.subList(0, size);
        }
        return matchingElders;

    }
}
