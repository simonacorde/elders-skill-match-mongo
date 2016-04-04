package diploma.elders.up.bird.optimizer;

/**
 * Created by Simonas on 4/3/2016.
 */
public class NoSuchBirdException extends Exception {

    private String message;

    public NoSuchBirdException(String message) {
        this.message = message;
    }
}
