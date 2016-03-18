package diploma.elders.up.dao.entity;

import javax.persistence.*;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class SkillOpportunity {
    private SkillOpportunityId id;
    private Skill skills;
    private Opportunity opportunities;

    public SkillOpportunity() {
    }

    public SkillOpportunity(Skill skills, Opportunity opportunities) {
        this.skills = skills;
        this.opportunities = opportunities;
    }

    public SkillOpportunity(SkillOpportunityId id, Skill skills, Opportunity opportunities) {
        this.id = id;
        this.skills = skills;
        this.opportunities = opportunities;

    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "skillId", column = @Column(name = "skill_id", nullable = false)),
            @AttributeOverride(name = "opportunityId", column = @Column(name = "opportunity_id", nullable = false)) })
    public SkillOpportunityId getId() {
        return this.id;
    }

    public void setId(SkillOpportunityId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false, insertable = false, updatable = false)
    public Skill getSkills() {
        return this.skills;
    }

    public void setSkills(Skill skills) {
        this.skills = skills;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opportunity_id", nullable = false, insertable = false, updatable = false)
    public Opportunity getOpportunities() {
        return this.opportunities;
    }

    public void setOpportunities(Opportunity opportunities) {
        this.opportunities = opportunities;
    }


}
