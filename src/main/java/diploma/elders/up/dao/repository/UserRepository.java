package diploma.elders.up.dao.repository;

import diploma.elders.up.dao.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Simonas on 5/10/2016.
 */
public interface UserRepository extends MongoRepository<User, String>{
    public User findByUsername(String username);
}
