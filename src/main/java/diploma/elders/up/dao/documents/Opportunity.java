package diploma.elders.up.dao.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Simonas on 3/26/2016.
 */
@Document(collection = "opportunities")
public class Opportunity {

    @Id
    private String id;
    private String title;
    private Company company;
    private List<Skill> skills;

    public Opportunity(String title, Company company) {
        this.title = title;
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public Company getCompany() {
        return company;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Opportunity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", company=" + company +
                ", skills=" + skills +
                '}';
    }
}
