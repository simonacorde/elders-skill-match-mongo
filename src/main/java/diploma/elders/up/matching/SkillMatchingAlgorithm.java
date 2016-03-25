package diploma.elders.up.matching;

/**
 * Created by Simonas on 2/29/2016.
 */

import diploma.elders.up.dao.entity.Skill;
import diploma.elders.up.dao.entity.SkillOpportunity;
import diploma.elders.up.dto.*;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class SkillMatchingAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillMatchingAlgorithm.class);

//    @Autowired
//    private OntologyOperations ontologyOperations;
    @Autowired
    private OntologyLikeOperations ontologyOperations;

    public double match(OpportunityDTO opp, Set<Skill> skills) {
        LOGGER.info("Matching nr of skills: "+ skills.size());
        List<SkillDTO> oppSkills = new ArrayList<>();
        List<SkillDTO> elderSkills = new ArrayList<>();
        for(SkillOpportunity skillOpportunity: opp.getOpportunity().getSkillsOpportunitieses()){
            oppSkills.add(new SkillDTO(skillOpportunity.getSkills()));
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
        return scoreSum / count;
    }

    public List<ElderDTO> findMatchingCandidates(OpportunityDTO op, List<ElderDTO> candidates, int size) {
        List<ElderDTO> matchingElders = new ArrayList<>();
        for (ElderDTO elder : candidates) {
            double matchScore = match(op, elder.getElder().getSkills());
            LOGGER.info("Match score between opportunity: "+ op.toString() + " and elder: "+ elder.getElder().toString());
            elder.setMatchingPercentage(matchScore);
            matchingElders.add(elder);
        }

        Collections.sort(matchingElders, new ElderComparator());
        if (matchingElders.size() > size) {
            return matchingElders.subList(0, size);
        }
        return matchingElders;

    }

    public List<OpportunityDTO> findMatchingOpportunities(ElderDTO elder, List<OpportunityDTO> candidates, int size) {
        List<OpportunityDTO> matchingOpps = new ArrayList<>();
        for (OpportunityDTO opp : candidates) {
            double matchScore = match(opp, elder.getElder().getSkills());
            opp.setMatchingPercentage(matchScore);
            matchingOpps.add(opp);
        }

        Collections.sort(matchingOpps, new OpportunityComparator());
        if (matchingOpps.size() > size) {
            return matchingOpps.subList(0, size);
        }
        return matchingOpps;
    }

    private double matchSkills(Skill fromSkill, Skill toSkill) {
        System.out.println("Matching "+fromSkill.getName() +"   "+toSkill.getName());
        if (ontologyOperations.areEqual(toSkill, fromSkill)) {
            return 1;
        }

        if (ontologyOperations.isAncestor(toSkill, fromSkill) || ontologyOperations.isAncestor(fromSkill, toSkill)) {
            return 1 - distance(fromSkill, toSkill );
        }

        Skill parentSkill = ontologyOperations.getLeastCommonAncestor(toSkill, fromSkill);
        //return 1 - (distance(offerSkill, parentSkill) + distance(cvSkill, parentSkill));
        double distance = (distance(fromSkill, parentSkill) + distance(parentSkill,toSkill));
        if(distance > 1) return 0;
        return 1 - distance;
    }

    //	private double distance(Skill offerSkill, Skill cvSkill) {
//		double milestonOffer = milestone(offerSkill);
//		double milestonCV = milestone(cvSkill);
//		double cvDepth = skillMetrics.getDepth(cvSkill);
//		return Math.abs(milestonOffer - milestonCV) / cvDepth;
//	}
    private double distance( Skill fromSkill, Skill toSkill) {
        double milestoneToSkill = milestone(toSkill);
        double milestoneFromSkill = milestone(fromSkill);
        double fromDepth = ontologyOperations.getDepth(fromSkill);
        double toDepth = ontologyOperations.getDepth(toSkill);
        double distance = 0;
        if(fromDepth > toDepth){
            //cv is child of offer
            distance = Math.abs(milestoneToSkill - milestoneFromSkill);
        }
        else{
            //cv is parent of offer
            List<Skill> chain = ontologyOperations.getAncestorHierarchy(fromSkill, toSkill);
            chain.add(toSkill);
            Skill first = fromSkill;
            //distance = 3 * Math.abs(milestone(first) - milestone(offerSkill));
            for(Skill s: chain){
                LOGGER.info("Number of children "+first.getName() +" "+ontologyOperations.numberOfChildren(first));
                if(ontologyOperations.getDepth(first) != 0) {
                    distance += ontologyOperations.numberOfChildren(first) / ontologyOperations.getDepth(first) * Math.abs(milestone(first) - milestone(s));
                    //distance += 3 * Math.abs(milestone(first) - milestone(s));
                    first = s;
                }
                if(distance > 1) distance = 1;
            }
        }
//		if(cvDepth < offDepth){
//			distance /= 3;
//		}
//		if(cvDepth > offDepth){
//			distance *= 3;
//		}
        LOGGER.info("Distance "+fromSkill.getName() +"   "+toSkill.getName() +" is:  "+distance);
        return distance;
    }
    private double milestone(Skill skill) {

        //return 1.0 / (1 + skillMetrics.getDepth(skill));// Math.pow(2,
        // skillMetrics.getDepth(skill));
        return 1.0 / Math.pow(2, ontologyOperations.getDepth(skill));

    }
}
