package diploma.elders.up.bin.packing.domain;

import java.util.Comparator;

/**
 * Created by Andreea Iepure on 4/6/2016.
 */
public class TupleComparator implements Comparator<Tuple> {

    @Override
    public int compare(Tuple o1, Tuple o2) {
        if(o1.getElder().getMatchingPercentage()>o2.getElder().getMatchingPercentage())
            return 1;
        else return 0;
    }
}
