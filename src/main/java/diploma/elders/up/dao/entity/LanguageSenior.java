package diploma.elders.up.dao.entity;

import javax.persistence.*;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class LanguageSenior {

    private LanguageSeniorId id;
    private Language languages;
    private Senior seniors;

    public LanguageSenior() {
    }

    public LanguageSenior(LanguageSeniorId id, Language languages, Senior seniors) {
        this.id = id;
        this.languages = languages;
        this.seniors = seniors;
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "languageId", column = @Column(name = "language_id", nullable = false)),
            @AttributeOverride(name = "seniorId", column = @Column(name = "senior_id", nullable = false)) })
    public LanguageSeniorId getId() {
        return this.id;
    }

    public void setId(LanguageSeniorId id) {
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
    @JoinColumn(name = "senior_id", nullable = false, insertable = false, updatable = false)
    public Senior getSeniors() {
        return this.seniors;
    }

    public void setSeniors(Senior seniors) {
        this.seniors = seniors;
    }

}
