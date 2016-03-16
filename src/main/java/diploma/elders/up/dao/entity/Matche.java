package diploma.elders.up.dao.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
@Table(name = "matches",  uniqueConstraints = @UniqueConstraint(columnNames = { "opportunity_id", "senior_id" }))
public class Matche {
    private Integer id;
    private Senior seniors;
    private Opportunity opportunities;
    private String status;
    private String message;

    public Matche() {
    }

    public Matche(Senior seniors, Opportunity opportunities, String status) {
        this.seniors = seniors;
        this.opportunities = opportunities;
        this.status = status;
    }

    public Matche(Senior seniors, Opportunity opportunities, String status,
                  String message) {
        this.seniors = seniors;
        this.opportunities = opportunities;

        this.status = status;
        this.message = message;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false)
    public Senior getSeniors() {
        return this.seniors;
    }

    public void setSeniors(Senior seniors) {
        this.seniors = seniors;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    public Opportunity getOpportunities() {
        return this.opportunities;
    }

    public void setOpportunities(Opportunity opportunities) {
        this.opportunities = opportunities;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "message", columnDefinition="TEXT")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
