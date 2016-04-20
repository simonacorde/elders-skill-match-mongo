package diploma.elders.up.semantic.matching;

import diploma.elders.up.dao.documents.Skill;

/**
 * Created by Simonas on 4/4/2016.
 */
public interface OntologySemanticMatcher {

    double matchSkills(Skill skillOpportunity, Skill skillElder);
}
