package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.Senior;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Simonas on 12/5/2015.
 */
public interface SeniorRepository extends MongoRepository<Senior, String> {

    public Senior findByName(String name);
}
