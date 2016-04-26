package diploma.elders.up.dao.documents;

import diploma.elders.up.dto.ElderDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author scorde
 * @since 4/21/2016.
 */
@Document(collection = "optimization-result")
public class OptimizationResult {

    @Id
    private String id;
    private double matchingScore;
    private List<ElderDTO> elders;
    private List<Double> skillsMatchingOffer;
    private String optimizationParamsId;

    public OptimizationResult(double matchingScore, List<ElderDTO> elders) {
        this.matchingScore = matchingScore;
        this.elders = elders;
    }

    public OptimizationResult(double matchingScore, List<ElderDTO> elders, String optimizationParamsId) {
        this.matchingScore = matchingScore;
        this.elders = elders;
        this.optimizationParamsId = optimizationParamsId;
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

    public String getOptimizationParamsId() {
        return optimizationParamsId;
    }

    @Override
    public String toString() {
        return "OptimizationResult{" +
                "id='" + id + '\'' +
                ", matchingScore=" + matchingScore +
                ", elders=" + elders +
                ", skillsMatchingOffer=" + skillsMatchingOffer +
                '}';
    }
}
