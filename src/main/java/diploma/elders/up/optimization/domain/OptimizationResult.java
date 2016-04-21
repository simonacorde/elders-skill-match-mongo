package diploma.elders.up.optimization.domain;

import diploma.elders.up.dto.ElderDTO;

import java.util.List;

/**
 * @author scorde
 * @since 4/21/2016.
 */
public class OptimizationResult {

    private double matchingScore;
    private List<ElderDTO> elders;
    private List<Double> skillsMatchingOffer;

    public OptimizationResult(double matchingScore, List<ElderDTO> elders) {
        this.matchingScore = matchingScore;
        this.elders = elders;
    }

    public void setSkillsMatchingOffer(List<Double> skillsMatchingOffer) {
        this.skillsMatchingOffer = skillsMatchingOffer;
    }

    public double getMatchingScore() {
        return matchingScore;
    }

    public List<ElderDTO> getElders() {
        return elders;
    }

    public List<Double> getSkillsMatchingOffer() {
        return skillsMatchingOffer;
    }
}
