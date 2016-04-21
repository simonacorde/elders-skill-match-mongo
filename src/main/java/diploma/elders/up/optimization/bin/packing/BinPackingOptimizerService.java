package diploma.elders.up.optimization.bin.packing;

import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.optimization.domain.Bin;
import diploma.elders.up.optimization.domain.OptimizationResult;
import diploma.elders.up.optimization.domain.Tuple;
import diploma.elders.up.optimization.domain.TupleComparator;
import diploma.elders.up.dto.ElderDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andreea Iepure on 4/6/2016.
 */

@Service
public class BinPackingOptimizerService implements OptimizerService{

    private static final double BIN_CAPACITY = 1;

    @Override
    public OptimizationResult applyOptimization(List<ElderDTO> elders) {
        Bin bin = applyBinPackingOptimizer(elders);
        return new OptimizationResult(bin.getValue(), bin.getResult().stream().map(Tuple::getElder).collect(Collectors.toList()));
    }

    public Bin applyBinPackingOptimizer(List<ElderDTO> elders) {
        List<Tuple> tuples=new ArrayList<>();
        for(ElderDTO elder : elders) {
            tuples.add(new Tuple(elder,false));
        }

        // Order in decreasing order the tuples     Tuple(offer,elder CV,matchingScore)
        Collections.sort(tuples,new TupleComparator());
        Bin bin1=new Bin();

        for (Tuple tuple : tuples) {
            if (bin1.getValue() + tuple.getElder().getMatchingPercentage() <= BIN_CAPACITY ) {
                bin1.setValue(bin1.getValue() + tuple.getElder().getMatchingPercentage());
                bin1.addTuple(tuple);
                tuple.setIsInBin(true);
            }
        }

        return bin1;
    }
}
