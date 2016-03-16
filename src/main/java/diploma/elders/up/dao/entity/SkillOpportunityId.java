package diploma.elders.up.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Simonas on 2/17/2016.
 */
@Embeddable
public class SkillOpportunityId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private int skillId;
    private int opportunityId;

    public SkillOpportunityId() {
    }

    public SkillOpportunityId(int skillId, int opportunityId) {
        this.skillId = skillId;
        this.opportunityId = opportunityId;
    }

    @Column(name = "skill_id", nullable = false)
    public int getSkillId() {
        return this.skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    @Column(name = "opportunity_id", nullable = false)
    public int getOpportunityId() {
        return this.opportunityId;
    }

    public void setOpportunityId(int opportunityId) {
        this.opportunityId = opportunityId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SkillOpportunityId))
            return false;
        SkillOpportunityId castOther = (SkillOpportunityId) other;

        return (this.getSkillId() == castOther.getSkillId())
                && (this.getOpportunityId() == castOther.getOpportunityId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getSkillId();
        result = 37 * result + this.getOpportunityId();
        return result;
    }

}
