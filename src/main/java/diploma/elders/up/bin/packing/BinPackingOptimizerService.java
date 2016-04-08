package diploma.elders.up.bin.packing;

import diploma.elders.up.bin.packing.domain.Bin;
import diploma.elders.up.bin.packing.domain.Tuple;
import diploma.elders.up.bin.packing.domain.TupleComparator;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreea Iepure on 4/6/2016.
 */

@Service
public class BinPackingOptimizerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinPackingOptimizerService.class);
    private List<Tuple> tuples=new ArrayList<>();
    private static final double binCapacity = 1;


    @Autowired
    private SkillMatchingAlgorithm skillMatchingAlgorithm;

    public Bin applyBinPackingOptimizer(List<ElderDTO> elders, OpportunityDTO opportunity)
    {
        for(ElderDTO elder : elders)
        {
            tuples.add(new Tuple(elder,false));
        }

    //// Order in decreasing order the tuples     Tuple(offer,elder CV,matchingScore)
    Collections.sort(tuples,new TupleComparator());
        Bin bin1=new Bin();


            for (Tuple tuple : tuples) {

                    if (bin1.getValue() + tuple.getElder().getMatchingPercentage() <=binCapacity ) {
                        bin1.setValue(bin1.getValue() + tuple.getElder().getMatchingPercentage());
                        bin1.addTuple(tuple);
                        tuple.setIsInBin(true);
                    }
            }

    return bin1;
    }
}
