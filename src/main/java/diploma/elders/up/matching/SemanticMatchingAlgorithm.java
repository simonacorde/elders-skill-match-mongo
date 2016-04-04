package diploma.elders.up.matching;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.SkillRepository;
import diploma.elders.up.dto.ElderComparator;
import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.dto.SkillDTO;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simonas on 3/31/2016.
 */
@Component
public class SemanticMatchingAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticMatchingAlgorithm.class);

    private static final String THING = "[owl:Thing]";
    private static final int MAX_DISTANCE = 17;
    private static final int MIN_DISTANCE = 1;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private OntologyLikeOperations ontologyOperations;

    public List<ElderDTO> findMatchingCandidates(OpportunityDTO op, List<ElderDTO> candidates, int size) {
        List<ElderDTO> matchingElders = new ArrayList<>();
        for (ElderDTO elder : candidates) {
            double matchScore = match(op, elder.getElder().getSkills());
            LOGGER.info("Match score between opportunity: "+ op.getOpportunity().getId() + " and elder: "+ elder.getElder().getId());
            elder.setMatchingPercentage(matchScore);
            matchingElders.add(elder);
        }

        Collections.sort(matchingElders, new ElderComparator());
        if (matchingElders.size() > size) {
            return matchingElders.subList(0, size);
        }
        return matchingElders;

    }

    public double matchSkills(Skill skill1, Skill skill2){
        int distance = ontologyOperations.getDistance(skill1.getName(), skill2.getName());
        return normalizeDistance((double)distance);
    }

    private double normalizeDistance(double distance) {
        return 1.0 - (distance / MAX_DISTANCE);
    }

    public double match(OpportunityDTO opp, List<Skill> skills) {
        LOGGER.info("Matching nr of skills: "+ skills.size());
        List<SkillDTO> oppSkills = new ArrayList<>();
        List<SkillDTO> elderSkills = new ArrayList<>();
        for(Skill skillOpportunity: opp.getOpportunity().getSkills()){
            oppSkills.add(new SkillDTO(skillOpportunity));
        }
        for(Skill skill: skills){
            elderSkills.add(new SkillDTO(skill));
        }

        double scoreSum = 0;
        double count = 0;
        for (SkillDTO oppSkill : oppSkills) {
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
            count++;
            scoreSum += maxScore;
        }
        double v = scoreSum / count;
        LOGGER.info("Returned match: {}", v);
        return v;
    }

    public int findTreeDepth(Skill root){
        int deepest = 0;
        List<Skill> allChildren = getAllChildren(root);
        for (Skill child:allChildren){
            deepest = max(deepest, findTreeDepth(child));
        }
        return deepest + 1;
    }

    private int max(int a, int b){
        if(a >= b){
            return a;
        }else{
            return b;
        }
    }

    public List<Skill> getAllChildren(Skill skill) {
        List<Skill> children = new ArrayList<>();
        List<Skill> skills = getSubclasses(skill.getName());
        for(Skill skillChild: skills){
            children.add(skillChild);
        }
        return children;
    }
    private List<Skill> getSubclasses(String name){
        return skillRepository.findByParentName(name);
    }
}
