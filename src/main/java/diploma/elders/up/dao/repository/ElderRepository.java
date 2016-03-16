package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.entity.Elder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Simonas on 12/5/2015.
 */
public interface ElderRepository extends CrudRepository<Elder, Integer> {

    List<Elder> findByLastName(String lastName);
}
