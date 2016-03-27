package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Simonas on 12/5/2015.
 */
public interface SkillRepository extends MongoRepository<Skill, String>{

    public Skill findByName(String name);
    public List<Skill> findByParentName(String parentName);
}
