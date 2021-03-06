package diploma.elders.up.optimization.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simonas on 3/5/2016.
 */
public class Bird {

    private BirdGender birdGender;
    private BirdType birdType;
    private double matchingScore;
    private boolean mated;
    private int nrOfMates = 0;
    private List<Genome> genome;

    public Bird() {
        genome = new ArrayList<>();
    }

    public BirdGender getBirdGender() {
        return birdGender;
    }

    public void setBirdGender(BirdGender birdGender) {
        this.birdGender = birdGender;
    }

    public BirdType getBirdType() {
        return birdType;
    }

    public void setBirdType(BirdType birdType) {
        this.birdType = birdType;
    }

    public double getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(double matchingScore) {
        this.matchingScore = matchingScore;
    }

    public List<Genome> getGenome() {
        return genome;
    }

    public void setGenome(List<Genome> genome) {
        this.genome = genome;
    }

    public boolean isMated() {
        return mated;
    }

    public void setMated(boolean mated) {
        this.mated = mated;
    }

    public int getNrOfMates() {
        return nrOfMates;
    }

    public void setNrOfMates(int nrOfMates) {
        this.nrOfMates = nrOfMates;
    }

    public void increaseNrOfMates() {
        this.nrOfMates ++;
        if(this.getBirdType().equals(BirdType.MONOGAMOUS) && this.nrOfMates >= 1){
            this.mated = true;
        }else if(this.getBirdType().equals(BirdType.POLYGYNOUS) && this.nrOfMates >= 3){
            this.mated = true;
        }
    }

    @Override
    public String toString() {
        return "Bird{" +
                "birdGender=" + birdGender +
                ", birdType=" + birdType +
                ", matchingScore=" + matchingScore +
                ", mated=" + mated +
                ", genome=" + genome +
                ", nrOfMates=" + nrOfMates +
                '}';
    }
}
