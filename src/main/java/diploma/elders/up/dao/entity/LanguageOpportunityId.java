package diploma.elders.up.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Simonas on 2/17/2016.
 */
@Embeddable
public class LanguageOpportunityId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private int languageId;
    private int opportunityId;

    public LanguageOpportunityId() {
    }

    public LanguageOpportunityId(int languageId, int opportunityId) {
        this.languageId = languageId;
        this.opportunityId = opportunityId;
    }

    @Column(name = "language_id", nullable = false)
    public int getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
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
        if (!(other instanceof LanguageOpportunityId))
            return false;
        LanguageOpportunityId castOther = (LanguageOpportunityId) other;

        return (this.getLanguageId() == castOther.getLanguageId())
                && (this.getOpportunityId() == castOther.getOpportunityId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getLanguageId();
        result = 37 * result + this.getOpportunityId();
        return result;
    }

}
