package diploma.elders.up.dao.entity;

import javax.persistence.*;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class LanguageOpportunity {
    private LanguageOpportunityId id;
    private Language languages;
    private Opportunity opportunities;

    public LanguageOpportunity() {
    }

    public LanguageOpportunity(LanguageOpportunityId id, Language languages, Opportunity opportunities) {
        this.id = id;
        this.languages = languages;
        this.opportunities = opportunities;

    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "languageId", column = @Column(name = "language_id", nullable = false)),
            @AttributeOverride(name = "opportunityId", column = @Column(name = "opportunity_id", nullable = false)) })
    public LanguageOpportunityId getId() {
        return this.id;
    }

    public void setId(LanguageOpportunityId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false, insertable = false, updatable = false)
    public Language getLanguages() {
        return this.languages;
    }

    public void setLanguages(Language languages) {
        this.languages = languages;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false, insertable = false, updatable = false)
    public Opportunity getOpportunities() {
        return this.opportunities;
    }

    public void setOpportunities(Opportunity opportunities) {
        this.opportunities = opportunities;
    }
}
