package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.entity.Skill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Simonas on 12/5/2015.
 */
public interface SkillRepository extends CrudRepository<Skill, Integer> {

    List<Skill> findByName(String name);
    List<Skill> findByParentName(String parent);
}
