package diploma.elders.up.semantic.matching;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Simonas on 4/20/2016.
 */
public class NormalizingSemanticMatcher implements OntologySemanticMatcher {

    private static final int MAX_DISTANCE = 17;

    @Autowired
    private OntologyLikeOperations ontologyOperations;

    public double matchSkills(Skill skill1, Skill skill2){
        if(skill1.getName().equals(skill2.getName())){
            return 1.0;
        }
        int distance = ontologyOperations.getDistance(skill1.getName(), skill2.getName());
        return normalizeDistance((double)distance);
    }

    private double normalizeDistance(double distance) {
        return 1.0 - (distance / MAX_DISTANCE);
    }
}
