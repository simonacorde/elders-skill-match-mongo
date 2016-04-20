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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillDTO skillDTO = (SkillDTO) o;

        if (Double.compare(skillDTO.matchingScore, matchingScore) != 0) return false;
        if (matched != skillDTO.matched) return false;
        if (opportunityPosition != skillDTO.opportunityPosition) return false;
        if (matchingSkill != null ? !matchingSkill.equals(skillDTO.matchingSkill) : skillDTO.matchingSkill != null)
            return false;
        return !(skill != null ? !skill.equals(skillDTO.skill) : skillDTO.skill != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = matchingSkill != null ? matchingSkill.hashCode() : 0;
        temp = Double.doubleToLongBits(matchingScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (skill != null ? skill.hashCode() : 0);
        result = 31 * result + (matched ? 1 : 0);
        result = 31 * result + opportunityPosition;
        return result;
    }
}
