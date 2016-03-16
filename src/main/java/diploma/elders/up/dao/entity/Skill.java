package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 12/5/2015.
 */
@Entity
public class Skill {

    private Integer id;
    private String parentName;
    private String name;
    private Set<SeniorSkill> seniorsSkillses = new HashSet<SeniorSkill>(0);
    private Set<SkillOpportunity> skillsOpportunitieses = new HashSet<SkillOpportunity>(0);

    public Skill() {
    }

    public Skill(String name) {

        this.name = name;
    }

    public Skill(String name, String parentName) {
        this.name = name;
        this.parentName = parentName;
    }

    public Skill(String parentName,  String name, Set<SeniorSkill> seniorsSkillses,
                 Set<SkillOpportunity> skillsOpportunitieses) {
        this.parentName = parentName;

        this.name = name;
        this.seniorsSkillses = seniorsSkillses;
        this.skillsOpportunitieses = skillsOpportunitieses;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "parent")
    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    public Set<SeniorSkill> getSeniorsSkillses() {
        return this.seniorsSkillses;
    }

    public void setSeniorsSkillses(Set<SeniorSkill> seniorsSkillses) {
        this.seniorsSkillses = seniorsSkillses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    public Set<SkillOpportunity> getSkillsOpportunitieses() {
        return this.skillsOpportunitieses;
    }

    public void setSkillsOpportunitieses(Set<SkillOpportunity> skillsOpportunitieses) {
        this.skillsOpportunitieses = skillsOpportunitieses;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();


        sb.append("Skill: ");
        sb.append(this.getName());

        sb.append("Parent Skill: ");
        sb.append(this.getParentName());

//        Set<SeniorSkill> seniors = this.getSeniorsSkillses();
//        sb.append("Seniors: ");
//
//        for (SeniorSkill seniorSkill : seniors) {
//            Senior senior = seniorSkill.getSeniors();
//            sb.append(senior.getId());
//
//            User user = senior.getUserses().get(0);
//            sb.append("User:  ");
//            sb.append(user.getId());
//            sb.append(user.getForename());
//            sb.append(user.getSurname());
//        }

//
//        Set<SkillOpportunity> opps = this.getSkillsOpportunitieses();
//        sb.append("Opportunitites: ");
//        for (SkillOpportunity opp : opps) {
//            sb.append(opp.getOpportunities().getTitle() + "-" + opp.getOpportunities().getCompanies().getName());
//        }
        return sb.toString();
    }
}
