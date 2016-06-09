package diploma.elders.up.optimization.hybrid.bin.packing;

import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.optimization.domain.Bin;
import diploma.elders.up.optimization.domain.Tuple;
import diploma.elders.up.optimization.domain.TupleComparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andreea Iepure on 6/8/2016.
 */

@Service
public class HybridBinPackingOptimizerService implements OptimizerService {

    private static final double BIN_CAPACITY = 1;
    private static final double k = 0.5;
    @Override
    public OptimizationResult applyOptimization(List<ElderDTO> elders) {
        Bin bin = applyHybridBinPackingOptimizer(elders);
        return new OptimizationResult(bin.getValue(), bin.getResult().stream().map(Tuple::getElder).collect(Collectors.toList()));
    }

    public List<Bin> applyBinPacking(List<ElderDTO> elders) {
        List<Tuple> tuples=new ArrayList<>();
        for(ElderDTO elder : elders) {
            tuples.add(new Tuple(elder,false));
        }

        // Order in decreasing order the tuples     Tuple(offer,elder CV,matchingScore)
        Collections.sort(tuples, new TupleComparator());
        List<Bin> bin=new ArrayList();
        bin.add(new Bin());
        int n=0;

        for (Tuple tuple : tuples) {
            if (bin.get(n).getValue() + tuple.getElder().getMatchingPercentage() <= BIN_CAPACITY ) {
                bin.get(n).setValue(bin.get(n).getValue() + tuple.getElder().getMatchingPercentage());
                bin.get(n).addTuple(tuple);
                tuple.setIsInBin(true);
            }
            else {n++;
                bin.add(new Bin());
                if (bin.get(n).getValue() + tuple.getElder().getMatchingPercentage() <= BIN_CAPACITY ) {
                    bin.get(n).setValue(bin.get(n).getValue() + tuple.getElder().getMatchingPercentage());
                    bin.get(n).addTuple(tuple);
                    tuple.setIsInBin(true);
                }

            }
        }

        return bin;
    }


    public double computeBinSpaces(Bin bin1,Bin bin2)
    {
        double sum1=0;
        double sum2=0;
        for(Tuple tuple : bin1.getResult())
        {
           sum1+= tuple.getElder().getMatchingPercentage();
        }
        for(Tuple tuple : bin2.getResult())
        {
            sum2 += tuple.getElder().getMatchingPercentage();
        }
        return Math.pow(sum1,2)+Math.pow(sum2,2);
    }
    public double computeBinResidualCapacity(Bin bin)
    {
        return (BIN_CAPACITY-bin.getValue())/BIN_CAPACITY;
    }
    public double computeWeightOfBin(Bin bin)
    {
        return 1+k*computeBinResidualCapacity(bin);
    }
    public void swapElementsBetweenTwoBins(Bin bin1,Bin bin2)
    {
        double bin1Weight=computeWeightOfBin(bin1);
        double bin2Weight=computeWeightOfBin(bin2);
        double minDifference =1000;
        Tuple candidate1= new Tuple();
        Tuple candidate2= new Tuple();
        int index1=0;
        int index2=0;

        for(Tuple tuple1 : bin1.getResult())
        {
            for(Tuple tuple2 : bin2.getResult())
            {
                if(Math.abs(tuple1.getElder().getMatchingPercentage()*bin1Weight)-
                        Math.abs(tuple2.getElder().getMatchingPercentage()*bin2Weight)<minDifference)
                {
                    candidate1=tuple1;
                    candidate2=tuple2;
                    minDifference=Math.abs(tuple1.getElder().getMatchingPercentage()*bin1Weight)-
                            Math.abs(tuple2.getElder().getMatchingPercentage()*bin2Weight);
                    index1=bin1.getResult().indexOf(candidate1);
                    index2=bin2.getResult().indexOf(candidate2);
                }

            }
        }
       bin1.getResult().remove(index1);
       bin1.getResult().add(index1,candidate1);
        bin2.getResult().remove(index2);
        bin2.getResult().add(index2,candidate2);

    }
    public Bin applyHybridBinPackingOptimizer(List<ElderDTO> elders)
    {
        List<Bin> bin =new ArrayList<>();
        bin=applyBinPacking(elders);
        Bin bin1=maxValueBin(bin);
        Bin bin2=secondMaxValueBin(bin,bin1.getValue());
        double originalSpace=computeBinSpaces(bin1,bin2);
        swapElementsBetweenTwoBins(bin1,bin2);
        double transformedSpace=computeBinSpaces(bin1,bin2);
        if(computeBinResidualCapacity(bin1)>computeBinResidualCapacity(bin2))
            return bin1;
        else return bin2;

    }
    public Bin maxValueBin(List<Bin> bins)
    {
        double max=0;
        Bin bin1 = new Bin();
        for(Bin bin : bins)
        {
            if(bin.getValue()>max) {
                max = bin.getValue();
                bin1=bin;
            }
        }
        return bin1;
    }
    public Bin secondMaxValueBin(List<Bin> bins,double max1)
    {
        double max2=0;
        Bin bin2 = new Bin();
        for(Bin bin : bins)
        {
            if(bin.getValue()>max2 && bin.getValue()!=max1) {
                max2 = bin.getValue();
                bin2=bin;
            }
        }
        return bin2;
    }


}
