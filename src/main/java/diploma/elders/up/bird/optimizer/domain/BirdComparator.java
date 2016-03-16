package diploma.elders.up.bird.optimizer.domain;

import java.util.Comparator;

/**
 * Created by Simonas on 3/11/2016.
 */
public class BirdComparator implements Comparator<Bird>{

    @Override
    public int compare(Bird o1, Bird o2) {
        double diff = o2.getMatchingScore() - o1.getMatchingScore();
        if (diff < 0) {
            return -1;
        }
        if (diff > 0) {
            return 1;
        }
        return 0;
    }

}
