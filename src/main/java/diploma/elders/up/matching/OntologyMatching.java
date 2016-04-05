package diploma.elders.up.matching;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;

import java.util.List;

/**
 * Created by Simonas on 4/4/2016.
 */
public interface OntologyMatching {

    List<ElderDTO> findMatchingCandidates(OpportunityDTO op, List<ElderDTO> candidates, int size);
    double matchSkills(Skill skill1, Skill skill2);
}
