package diploma.elders.up.dao.entity;

import javax.persistence.*;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Endorsement {
    private EndorsementId id;
    private Senior seniors;
    private Company companies;


    public Endorsement() {
    }

    public Endorsement(EndorsementId id, Senior seniors, Company companies) {
        this.id = id;
        this.seniors = seniors;
        this.companies = companies;

    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "companyId", column = @Column(name = "company_id", nullable = false)),
            @AttributeOverride(name = "seniorId", column = @Column(name = "senior_id", nullable = false)) })
    public EndorsementId getId() {
        return this.id;
    }

    public void setId(EndorsementId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false, insertable = false, updatable = false)
    public Senior getSeniors() {
        return this.seniors;
    }

    public void setSeniors(Senior seniors) {
        this.seniors = seniors;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, insertable = false, updatable = false)
    public Company getCompanies() {
        return this.companies;
    }

    public void setCompanies(Company companies) {
        this.companies = companies;
    }

}
