package diploma.elders.up.matching;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.dto.SkillDTO;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Simonas on 4/5/2016.
 */
public class Matcher implements Callable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Matcher.class);

    private static final int MAX_DISTANCE = 17;

    private OntologyLikeOperations ontologyOperations;

    private OpportunityDTO opp;
    private ElderDTO elder;

    public Matcher(OntologyLikeOperations ontologyOperations, OpportunityDTO opportunityDTO, ElderDTO elder) {
        this.opp = opportunityDTO;
        this.elder = elder;
        this.ontologyOperations = ontologyOperations;
    }

    @Override
    public ElderDTO call() {
        LOGGER.info("Matching nr of skills: " + elder.getElder().getSkills().size());
        List<SkillDTO> oppSkills = new ArrayList<>();
        List<SkillDTO> elderSkills = new ArrayList<>();
        for (Skill skillOpportunity : opp.getOpportunity().getSkills()) {
            oppSkills.add(new SkillDTO(skillOpportunity, 0));
        }
        for (Skill skill : elder.getElder().getSkills()) {
            elderSkills.add(new SkillDTO(skill, 0));
        }

        double scoreSum = 0;
        for(SkillDTO oppSkill: oppSkills){
            double maxScore = 0;
            SkillDTO matchingSkill = null;
            for (SkillDTO elderSkill : elderSkills) {
                double matchingScore = matchSkills(oppSkill.getSkill(), elderSkill.getSkill());
                if (matchingScore > maxScore) {
                    maxScore = matchingScore;
                    matchingSkill = elderSkill;
                }
            }
            oppSkill.setMatchingSkill(matchingSkill);
            oppSkill.setMatchingScore(maxScore);
            oppSkill.setMatched(true);
            scoreSum += maxScore;
        }
        double match = scoreSum / oppSkills.size();
        LOGGER.info("Returned match: {}", match);
        elder.setMatchingPercentage(match);
        elder.setMatchingSkills(oppSkills);
        opp.setSkills(oppSkills);
        elder.setMatchingOffer(opp);
        return elder;
    }

    public double matchSkills(Skill skill1, Skill skill2){
        int distance = ontologyOperations.getDistance(skill1.getName(), skill2.getName());
        return normalizeDistance((double)distance);
    }

    private double normalizeDistance(double distance) {
        return 1.0 - (distance / MAX_DISTANCE);
    }

    private Integer randomSkill(List<SkillDTO> skills){
        Random rng = new Random();
        return  rng.nextInt(skills.size());
    }
}
