package diploma.elders.up.data;

import diploma.elders.up.dao.entity.*;
import diploma.elders.up.dao.repository.*;
import diploma.elders.up.ontology.OntologyOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Simonas on 3/3/2016.
 */
@Component
public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private static final int MIN_ID = 13387;
    private static final int MAX_ID = 26772;
    private static final int MIN_UNRELATED_SKILLS = 1;
    private static final int MAX_UNRELATED_SKILLS = 3;
    private static final int MIN_RELATED_SKILLS = 4;
    private static final int MAX_RELATED_SKILLS = 6;

    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SkillOpportunityRepository skillOpportunityRepository;
    @Autowired
    private OntologyOperations ontologyOperations;

    public Set<Skill> generateCV()
    {
        int nrOfUnrelatedSkills = randomNumber(MIN_UNRELATED_SKILLS, MAX_UNRELATED_SKILLS);
        Set<Skill> skills=new HashSet<>();
        for(int i=0; i< nrOfUnrelatedSkills; i++){
            Skill skill=skillRepository.findOne(randomNumber(MIN_ID, MAX_ID));
            skills.add(skill);
        }

        int randomSkill = randomNumber(MIN_ID, MAX_ID);
        Skill parentSkill = skillRepository.findOne(randomSkill);
        Set<String> allChildren = ontologyOperations.getAllChildren(parentSkill);
        if(allChildren.isEmpty()){
            allChildren = ontologyOperations.getAllChildren(skillRepository.findByName(parentSkill.getParentName()).get(0));
        }
        List<String> allSkills = new ArrayList<>();
        allSkills.addAll(allChildren);
        int nrOfRelatedSkills = randomNumber(MIN_RELATED_SKILLS, MAX_RELATED_SKILLS);
        int i=0;
        while(i < allSkills.size() && i <= nrOfRelatedSkills){
            Skill skill = skillRepository.findByName(allSkills.get(i)).get(0);
            skills.add(skill);
            i++;
        }
        Elder elder=new Elder();
        elder.setSkills(skills);
        elderRepository.save(elder);

        return skills;
    }

    public Set<SkillOpportunity> generateOpportunities(int minUnrelatedSkills, int maxUnrelatedSkills, int minRelatedSkills, int maxRelatedSkills) {
        Opportunity opportunity = new Opportunity();
        opportunity.setCompanies(companyRepository.findOne(1));
        opportunity.setDeadline("June");
        opportunity.setDuration("100");
        opportunity.setClosed(true);
        opportunity.setDescription("Awesome");
        opportunity.setTitle("Perfect");
        opportunity.setStartDate("now");
        opportunity.setPlaceOfWork("The Office");
        Opportunity save = opportunityRepository.save(opportunity);
        int nrOfUnrelatedSkills = randomNumber(minUnrelatedSkills, maxUnrelatedSkills);
        Set<SkillOpportunity> skillOpportunities = new HashSet<>();
        for(int i=0; i< nrOfUnrelatedSkills; i++){
            Skill skill = skillRepository.findOne(randomNumber(MIN_ID, MAX_ID));
            SkillOpportunity skillOpportunity = new SkillOpportunity(new SkillOpportunityId(skill.getId(), opportunity.getId()), skill, opportunity);
            skillOpportunityRepository.save(skillOpportunities);
            skillOpportunities.add(skillOpportunity);
        }

        int randomSkill = randomNumber(MIN_ID, MAX_ID);
        Skill parentSkill = skillRepository.findOne(randomSkill);
        Set<String> allChildren = ontologyOperations.getAllChildren(parentSkill);
        if(allChildren.isEmpty()){
            allChildren = ontologyOperations.getAllChildren(skillRepository.findByName(parentSkill.getParentName()).get(0));
        }
        List<String> allSkills = new ArrayList<>();
        allSkills.addAll(allChildren);
        int nrOfRelatedSkills = randomNumber(minRelatedSkills, maxRelatedSkills);
        int i=0;
        while(i < allSkills.size() && i <= nrOfRelatedSkills){
            Skill skill = skillRepository.findByName(allSkills.get(i)).get(0);
            SkillOpportunity skillOpportunity = new SkillOpportunity(new SkillOpportunityId(skill.getId(), opportunity.getId()), skill, opportunity);
            skillOpportunityRepository.save(skillOpportunities);
            skillOpportunities.add(skillOpportunity);
            i++;
        }
        LOGGER.info("Saved opp id: " + save.getId());
        Opportunity one = opportunityRepository.findOne(save.getId());
        one.setSkillsOpportunitieses(skillOpportunities);
        opportunityRepository.save(one);

        return skillOpportunities;
    }


    public int randomNumber(int min, int max) {

        Random random = new Random();

        return random.nextInt(max - min + 1) + min;


    }

}
