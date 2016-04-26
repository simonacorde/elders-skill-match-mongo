package diploma.elders.up.dao.documents;

import diploma.elders.up.dto.ElderDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Simonas on 4/25/2016.
 */
@Document(collection = "matching-result")
public class MatchingResult {

    @Id
    private String id;
    private List<ElderDTO> elders;
    private String opportunityId;

    public MatchingResult(List<ElderDTO> elders, String opportunityId) {
        this.elders = elders;
        this.opportunityId = opportunityId;
    }

    public String getId() {
        return id;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public List<ElderDTO> getElders() {
        return elders;
    }

    @Override
    public String toString() {
        return "MatchingResult{" +
                "id='" + id + '\'' +
                ", elders=" + elders +
                ", opportunityId='" + opportunityId + '\'' +
                '}';
    }
}
