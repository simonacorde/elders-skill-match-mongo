package diploma.elders.up.dto;


import diploma.elders.up.dao.documents.Senior;

import java.util.List;

/**
 * Created by Simonas on 3/2/2016.
 */
public class ElderDTO {

    private Senior elder;
    private double matchingPercentage;
    private OpportunityDTO matchingOffer;
    private List<SkillDTO> matchingSkills;

    public ElderDTO(Senior elder) {
        this.elder = elder;
    }

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }

    public OpportunityDTO getMatchingOffer() {
        return matchingOffer;
    }

    public void setMatchingOffer(OpportunityDTO matchingOffer) {
        this.matchingOffer = matchingOffer;
    }

    public Senior getElder() {
        return elder;
    }

    public List<SkillDTO> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<SkillDTO> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    @Override
    public String toString() {
        return "ElderDTO{" +
                "elder=" + elder +
                ", matchingPercentage=" + matchingPercentage +
                ", matchingOffer=" + matchingOffer +
                '}';
    }
}
