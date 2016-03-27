package diploma.elders.up.dto;

import diploma.elders.up.dao.documents.Skill;

/**
 * Created by Simonas on 3/2/2016.
 */
public class SkillDTO {

    private SkillDTO matchingSkill;
    private double matchingScore;
    private Skill skill;

    public SkillDTO(Skill skill) {
        this.skill = skill;
    }

    public SkillDTO getMatchingSkill() {
        return matchingSkill;
    }

    public void setMatchingSkill(SkillDTO matchingSkill) {
        this.matchingSkill = matchingSkill;
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

    @Override
    public String toString() {
        return "SkillDTO{" +
                "matchingSkill=" + matchingSkill +
                ", matchingScore=" + matchingScore +
                ", skill=" + skill +
                '}';
    }
}
