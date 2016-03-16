package diploma.elders.up.dto;

import java.util.Comparator;

/**
 * Created by Simonas on 3/2/2016.
 */
public class OpportunityComparator implements Comparator<OpportunityDTO> {

    @Override
    public int compare(OpportunityDTO o1, OpportunityDTO o2) {
        double diff = o2.getMatchingPercentage() - o1.getMatchingPercentage();
        if (diff < 0) {
            return -1;
        }
        if (diff > 0) {
            return 1;
        }
        return 0;
    }

}

