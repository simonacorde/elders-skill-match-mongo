package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.OptimizationParams;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Simonas on 4/25/2016.
 */
public interface OptimizationParamsRepository extends MongoRepository<OptimizationParams, String> {
}
