package diploma.elders.up.dao.entity;

import javax.persistence.*;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class SeniorSkill {
    private SeniorSkillId id;
    private Skill skills;
    private Senior seniors;

    public SeniorSkill() {
    }

    public SeniorSkill(SeniorSkillId id, Skill skills, Senior seniors) {
        this.id = id;
        this.skills = skills;
        this.seniors = seniors;
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "seniorId", column = @Column(name = "senior_id", nullable = false)),
            @AttributeOverride(name = "skillId", column = @Column(name = "skill_id", nullable = false)) })
    public SeniorSkillId getId() {
        return this.id;
    }

    public void setId(SeniorSkillId id) {
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
    @JoinColumn(name = "senior_id", nullable = false, insertable = false, updatable = false)
    public Senior getSeniors() {
        return this.seniors;
    }

    public void setSeniors(Senior seniors) {
        this.seniors = seniors;
    }

}
