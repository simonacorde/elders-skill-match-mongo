package diploma.elders.up.dao.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Simonas on 3/26/2016.
 */
@Document(collection = "skills")
public class Skill {

    @Id
    private String id;
    private String name;
    private String parentName;

    public Skill(String id, String name, String parentName) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parentName;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
