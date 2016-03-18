package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Opportunity {
    private Integer id;
    private Company companies;
    private boolean closed;
    private String title;
    private String description;
    private String startDate;
    private String duration;
    private String deadline;
    private String placeOfWork;
    private Set<SkillOpportunity> skillsOpportunitieses = new HashSet<SkillOpportunity>(0);
    private Set<LanguageOpportunity> languagesOpportunitieses = new HashSet<LanguageOpportunity>(0);
    private Set<Matche> matcheses = new HashSet<Matche>(0);

    public Opportunity() {
    }

    public Opportunity(Company companies, boolean closed, String title,
                       String description, String startDate, String duration, String deadline, String placeOfWork) {
        this.companies = companies;
        this.closed = closed;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.deadline = deadline;
        this.placeOfWork = placeOfWork;
    }

    public Opportunity(Company companies, boolean closed, String title,
                       String description, String startDate, String duration, String deadline, String placeOfWork,
                       Set<SkillOpportunity> skillsOpportunitieses, Set<LanguageOpportunity> languagesOpportunitieses, Set<Matche> matcheses) {
        this.companies = companies;

        this.closed = closed;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.duration = duration;
        this.deadline = deadline;
        this.placeOfWork = placeOfWork;
        this.skillsOpportunitieses = skillsOpportunitieses;
        this.languagesOpportunitieses = languagesOpportunitieses;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    public Company getCompanies() {
        return this.companies;
    }

    public void setCompanies(Company companies) {
        this.companies = companies;
    }


    @Column(name = "closed", nullable = false)
    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Column(name = "title", nullable = false, length = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", nullable = false, columnDefinition="TEXT")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "start_date", nullable = false, length = 100)
    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Column(name = "duration", nullable = false, length = 100)
    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Column(name = "deadline", nullable = false, length = 100)
    public String getDeadline() {
        return this.deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Column(name = "place_of_work", nullable = false, length = 100)
    public String getPlaceOfWork() {
        return this.placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "opportunities")
    public Set<SkillOpportunity> getSkillsOpportunitieses() {
        return this.skillsOpportunitieses;
    }

    public void setSkillsOpportunitieses(Set<SkillOpportunity> skillsOpportunitieses) {
        this.skillsOpportunitieses = skillsOpportunitieses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "opportunities")
    public Set<LanguageOpportunity> getLanguagesOpportunitieses() {
        return this.languagesOpportunitieses;
    }

    public void setLanguagesOpportunitieses(Set<LanguageOpportunity> languagesOpportunitieses) {
        this.languagesOpportunitieses = languagesOpportunitieses;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "opportunities")
    public Set<Matche> getMatcheses() {
        return this.matcheses;
    }

    public void setMatcheses(Set<Matche> matcheses) {
        this.matcheses = matcheses;
    }
}
