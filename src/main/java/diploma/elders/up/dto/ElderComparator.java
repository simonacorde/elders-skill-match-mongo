package diploma.elders.up.dto;

import java.util.Comparator;

/**
 * Created by Simonas on 3/2/2016.
 */
public class ElderComparator implements Comparator<ElderDTO> {

    @Override
    public int compare(ElderDTO o1, ElderDTO o2) {
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

