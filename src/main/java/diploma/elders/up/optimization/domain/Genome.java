package diploma.elders.up.optimization.domain;

import diploma.elders.up.dto.ElderDTO;

/**
 * Created by Simonas on 5/4/2016.
 */
public class Genome {

    private ElderDTO elder;
    private double geneScore;

    public Genome(ElderDTO elder, double geneScore) {
        this.elder = elder;
        this.geneScore = geneScore;
    }

    public ElderDTO getElder() {
        return elder;
    }

    public void setElder(ElderDTO elder) {
        this.elder = elder;
    }

    public double getGeneScore() {
        return geneScore;
    }

    public void setGeneScore(double geneScore) {
        this.geneScore = geneScore;
    }
}
