package diploma.elders.up.dto;

import diploma.elders.up.dao.documents.Skill;

/**
 * Created by Simonas on 3/2/2016.
 */
public class SkillDTO {

    private SkillDTO matchingSkill;
    private double matchingScore;
    private Skill skill;
    private boolean matched = false;
    private int opportunityPosition = -1;

    public SkillDTO() {
    }

    public SkillDTO(Skill skill) {
        this.skill = skill;
    }

    public SkillDTO(Skill skill, double matchingScore) {
        this.skill = skill;
        this.matchingScore = matchingScore;
    }

    public SkillDTO getMatchingSkill() {
        return matchingSkill;
    }

    public void setMatchingSkill(SkillDTO matchingSkill) {
        this.matchingSkill = matchingSkill;
    }

    public int getOpportunityPosition() {
        return opportunityPosition;
    }

    public void setOpportunityPosition(int opportunityPosition) {
        this.opportunityPosition = opportunityPosition;
    }

    public double getMatchingScore() {
        return matchingScore;
    }

    public void setMatchingScore(double matchingScore) {
        this.matchingScore = matchingScore;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    @Override
    public String toString() {
        return "SkillDTO{" +
                "matchingSkill=" + matchingSkill +
                ", matchingScore=" + matchingScore +
                ", skill=" + skill +
                ", matched=" + matched +
                ", opportunityPosition=" + opportunityPosition +
                '}';
    }
}
