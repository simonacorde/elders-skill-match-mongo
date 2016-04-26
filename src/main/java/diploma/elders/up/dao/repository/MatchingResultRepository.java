package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.MatchingResult;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Simonas on 4/25/2016.
 */
public interface MatchingResultRepository extends MongoRepository<MatchingResult, String>{
}
