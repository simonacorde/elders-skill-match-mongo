package diploma.elders.up.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Simonas on 2/17/2016.
 */
@Embeddable
public class EndorsementId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private int companyId;
    private int seniorId;

    public EndorsementId() {
    }

    public EndorsementId(int companyId, int seniorId) {
        this.companyId = companyId;
        this.seniorId = seniorId;
    }

    @Column(name = "company_id", nullable = false)
    public int getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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
        if (!(other instanceof EndorsementId))
            return false;
        EndorsementId castOther = (EndorsementId) other;

        return (this.getCompanyId() == castOther.getCompanyId()) && (this.getSeniorId() == castOther.getSeniorId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getCompanyId();
        result = 37 * result + this.getSeniorId();
        return result;
    }

}