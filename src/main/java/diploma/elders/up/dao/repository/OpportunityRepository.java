package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.Opportunity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Simonas on 3/11/2016.
 */
public interface OpportunityRepository extends MongoRepository<Opportunity, String> {
}
