package diploma.elders.up.dao.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Simonas on 4/25/2016.
 */
@Document(collection = "optimization-params")
public class OptimizationParams {

    @Id
    private String id;
    private long time;
    private double matchingScore;
    private double monoBirdsPercentage;
    private double polyMaleBirdsPercentage;
    private int maxPolyFemalesToMate;
    private int initialPopulationSize;
    private int nrOfSkillsInOffer;
    private int nrOfEldersInSolution;

    public OptimizationParams(long time, double matchingScore, double monoBirdsPercentage, double polyMaleBirdsPercentage, int maxPolyFemalesToMate, int initialPopulationSize, int nrOfSkillsInOffer, int nrOfEldersInSolution) {
        this.time = time;
        this.matchingScore = matchingScore;
        this.monoBirdsPercentage = monoBirdsPercentage;
        this.polyMaleBirdsPercentage = polyMaleBirdsPercentage;
        this.maxPolyFemalesToMate = maxPolyFemalesToMate;
        this.initialPopulationSize = initialPopulationSize;
        this.nrOfSkillsInOffer = nrOfSkillsInOffer;
        this.nrOfEldersInSolution = nrOfEldersInSolution;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public double getMatchingScore() {
        return matchingScore;
    }

    public double getMonoBirdsPercentage() {
        return monoBirdsPercentage;
    }

    public double getPolyMaleBirdsPercentage() {
        return polyMaleBirdsPercentage;
    }

    public int getMaxPolyFemalesToMate() {
        return maxPolyFemalesToMate;
    }

    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public int getNrOfSkillsInOffer() {
        return nrOfSkillsInOffer;
    }

    public int getNrOfEldersInSolution() {
        return nrOfEldersInSolution;
    }

    @Override
    public String toString() {
        return "OptimizationParams{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", matchingScore=" + matchingScore +
                ", monoBirdsPercentage=" + monoBirdsPercentage +
                ", polyMaleBirdsPercentage=" + polyMaleBirdsPercentage +
                ", maxPolyFemalesToMate=" + maxPolyFemalesToMate +
                ", initialPopulationSize=" + initialPopulationSize +
                ", nrOfSkillsInOffer=" + nrOfSkillsInOffer +
                ", nrOfEldersInSolution=" + nrOfEldersInSolution +
                '}';
    }
}
