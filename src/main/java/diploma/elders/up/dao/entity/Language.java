package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Language {

    private Integer id;
    private String name;
    private Set<LanguageSenior> languagesSeniorses = new HashSet<LanguageSenior>(0);
    private Set<LanguageOpportunity> languagesOpportunitieses = new HashSet<LanguageOpportunity>(0);

    public Language() {
    }

    public Language( String name) {

        this.name = name;
    }

    public Language(String name, Set<LanguageSenior> languagesSeniorses, Set<LanguageOpportunity> languagesOpportunitieses) {

        this.name = name;
        this.languagesSeniorses = languagesSeniorses;
        this.languagesOpportunitieses = languagesOpportunitieses;
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

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "languages")
    public Set<LanguageSenior> getLanguagesSeniorses() {
        return this.languagesSeniorses;
    }

    public void setLanguagesSeniorses(Set<LanguageSenior> languagesSeniorses) {
        this.languagesSeniorses = languagesSeniorses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "languages")
    public Set<LanguageOpportunity> getLanguagesOpportunitieses() {
        return this.languagesOpportunitieses;
    }

    public void setLanguagesOpportunitieses(Set<LanguageOpportunity> languagesOpportunitieses) {
        this.languagesOpportunitieses = languagesOpportunitieses;
    }
}
