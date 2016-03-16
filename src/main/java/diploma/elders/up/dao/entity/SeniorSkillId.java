package diploma.elders.up.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Simonas on 2/17/2016.
 */
@Embeddable
public class SeniorSkillId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private int seniorId;
    private int skillId;

    public SeniorSkillId() {
    }

    public SeniorSkillId(int seniorId, int skillId) {
        this.seniorId = seniorId;
        this.skillId = skillId;
    }

    @Column(name = "senior_id", nullable = false)
    public int getSeniorId() {
        return this.seniorId;
    }

    public void setSeniorId(int seniorId) {
        this.seniorId = seniorId;
    }

    @Column(name = "skill_id", nullable = false)
    public int getSkillId() {
        return this.skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SeniorSkillId))
            return false;
        SeniorSkillId castOther = (SeniorSkillId) other;

        return (this.getSeniorId() == castOther.getSeniorId()) && (this.getSkillId() == castOther.getSkillId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getSeniorId();
        result = 37 * result + this.getSkillId();
        return result;
    }
}