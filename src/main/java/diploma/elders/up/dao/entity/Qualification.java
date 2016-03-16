package diploma.elders.up.dao.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Qualification {
    private Integer id;
    private Senior seniors;
    private String name;

    public Qualification() {
    }

    public Qualification(Senior seniors,String name) {
        this.seniors = seniors;

        this.name = name;
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


    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
