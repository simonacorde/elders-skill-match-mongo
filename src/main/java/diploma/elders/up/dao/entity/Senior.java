package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Senior {
    private Integer id;
    private boolean setupComplete;
    private String previousEmploymentPosition;
    private String profile;
    private String availability;

    private List<SeniorSkill> seniorsSkillses = new ArrayList<SeniorSkill>(0);
    private List<LanguageSenior> languagesSeniorses = new ArrayList<LanguageSenior>(0);
    private List<User> userses = new ArrayList<User>(0);
    private List<Endorsement> endorsementses = new ArrayList<Endorsement>(0);
    private List<Qualification> qualificationses = new ArrayList<Qualification>(0);
    private List<Matche> matcheses = new ArrayList<Matche>(0);

    public Senior() {
    }

    public Senior(boolean setupComplete, String previousEmploymentPosition,
                  String profile, String availability) {

        this.setupComplete = setupComplete;

        this.previousEmploymentPosition = previousEmploymentPosition;
        this.profile = profile;
        this.availability = availability;
    }

    public Senior(boolean setupComplete, String previousEmploymentPosition,
                  String profile, String availability,  List<SeniorSkill> seniorsSkillses,
                  List<LanguageSenior> languagesSeniorses, List<User> userses, List<Endorsement> endorsementses,
                  List<Qualification> qualificationses, List<Matche> matcheses) {

        this.setupComplete = setupComplete;
        this.previousEmploymentPosition = previousEmploymentPosition;
        this.profile = profile;
        this.availability = availability;
        this.seniorsSkillses = seniorsSkillses;
        this.languagesSeniorses = languagesSeniorses;
        this.userses = userses;
        this.endorsementses = endorsementses;
        this.qualificationses = qualificationses;
        this.matcheses = matcheses;
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


    @Column(name = "setup_complete", nullable = false)
    public boolean isSetupComplete() {
        return this.setupComplete;
    }

    public void setSetupComplete(boolean setupComplete) {
        this.setupComplete = setupComplete;
    }

    @Column(name = "previous_employment_position", nullable = false, length = 250)
    public String getPreviousEmploymentPosition() {
        return this.previousEmploymentPosition;
    }

    public void setPreviousEmploymentPosition(String previousEmploymentPosition) {
        this.previousEmploymentPosition = previousEmploymentPosition;
    }

    @Column(name = "profile", nullable = false, columnDefinition = "TEXT")
    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Column(name = "availability", nullable = false, columnDefinition = "TEXT")
    public String getAvailability() {
        return this.availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }



    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<SeniorSkill> getSeniorsSkillses() {
        return this.seniorsSkillses;
    }

    public void setSeniorsSkillses(List<SeniorSkill> seniorsSkillses) {
        this.seniorsSkillses = seniorsSkillses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<LanguageSenior> getLanguagesSeniorses() {
        return this.languagesSeniorses;
    }

    public void setLanguagesSeniorses(List<LanguageSenior> languagesSeniorses) {
        this.languagesSeniorses = languagesSeniorses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<User> getUserses() {
        return this.userses;
    }

    public void setUserses(List<User> userses) {
        this.userses = userses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<Endorsement> getEndorsementses() {
        return this.endorsementses;
    }

    public void setEndorsementses(List<Endorsement> endorsementses) {
        this.endorsementses = endorsementses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<Qualification> getQualificationses() {
        return this.qualificationses;
    }

    public void setQualificationses(List<Qualification> qualificationses) {
        this.qualificationses = qualificationses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "seniors")
    public List<Matche> getMatcheses() {
        return this.matcheses;
    }

    public void setMatcheses(List<Matche> matcheses) {
        this.matcheses = matcheses;
    }

}
