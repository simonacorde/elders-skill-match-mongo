package diploma.elders.up.dao.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Simonas on 3/26/2016.
 */
@Document(collection = "seniors")
public class Senior {

    @Id
    private String id;
    private String name;
    private List<Skill> skills;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Senior{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", skills=" + skills.toString() +
                '}';
    }
}
