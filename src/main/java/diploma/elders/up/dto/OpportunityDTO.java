package diploma.elders.up.dto;

import diploma.elders.up.dao.documents.Opportunity;

import java.util.List;

/**
 * Created by Simonas on 3/2/2016.
 */
public class OpportunityDTO {
    private double matchingPercentage;
    private List<ElderDTO> candidates;
    private List<SkillDTO> skills;
    private Opportunity opportunity;

    public OpportunityDTO(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }

    public List<ElderDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<ElderDTO> candidates) {
        this.candidates = candidates;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public List<SkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDTO> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "OpportunityDTO{" +
                "matchingPercentage=" + matchingPercentage +
                ", candidates=" + candidates +
                ", opportunity=" + opportunity +
                '}';
    }
}
