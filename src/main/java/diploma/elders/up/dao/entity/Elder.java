package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Simonas on 12/5/2015.
 */
@Entity
public class Elder {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String cardId;
    private Set<Skill> skills;

    public Elder(){

    }

    public Elder(String firstName, String lastName, String address, String phone, String cardId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.cardId = cardId;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "elder_skills", joinColumns = @JoinColumn(name = "id_elder", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_skill", referencedColumnName = "id"))
    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Elder{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
