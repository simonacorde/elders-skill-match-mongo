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

    public Skill() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;

        if (id != null ? !id.equals(skill.id) : skill.id != null) return false;
        if (name != null ? !name.equals(skill.name) : skill.name != null) return false;
        return !(parentName != null ? !parentName.equals(skill.parentName) : skill.parentName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentName != null ? parentName.hashCode() : 0);
        return result;
    }
}
