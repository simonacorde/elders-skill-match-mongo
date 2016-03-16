package diploma.elders.up.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Simonas on 2/17/2016.
 */
@Embeddable
public class LanguageSeniorId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private int languageId;
    private int seniorId;

    public LanguageSeniorId() {
    }

    public LanguageSeniorId(int languageId, int seniorId) {
        this.languageId = languageId;
        this.seniorId = seniorId;
    }

    @Column(name = "language_id", nullable = false)
    public int getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Column(name = "senior_id", nullable = false)
    public int getSeniorId() {
        return this.seniorId;
    }

    public void setSeniorId(int seniorId) {
        this.seniorId = seniorId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof LanguageSeniorId))
            return false;
        LanguageSeniorId castOther = (LanguageSeniorId) other;

        return (this.getLanguageId() == castOther.getLanguageId()) && (this.getSeniorId() == castOther.getSeniorId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getLanguageId();
        result = 37 * result + this.getSeniorId();
        return result;
    }
}
