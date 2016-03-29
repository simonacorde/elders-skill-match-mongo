package diploma.elders.up.data;

import diploma.elders.up.dao.documents.Company;
import diploma.elders.up.dao.documents.Opportunity;
import diploma.elders.up.dao.documents.Senior;
import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.CompanyRepository;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simonas on 3/3/2016.
 */
@Component
public class DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private static final int MIN_ID = 0;
    private static final int MAX_ID = 13386;
    private static final int MIN_UNRELATED_SKILLS = 1;
    private static final int MAX_UNRELATED_SKILLS = 3;
    private static final int MIN_RELATED_SKILLS = 4;
    private static final int MAX_RELATED_SKILLS = 6;

    @Autowired
    private SeniorRepository elderRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OntologyLikeOperations ontologyOperations;

    public List<Skill> generateCV()
    {
        int nrOfUnrelatedSkills = randomNumber(MIN_UNRELATED_SKILLS, MAX_UNRELATED_SKILLS);
        List<Skill> skills=new ArrayList<>();
        for(int i=0; i< nrOfUnrelatedSkills; i++){
            Skill skill=skillRepository.findOne(""+randomNumber(MIN_ID, MAX_ID));
            skills.add(skill);
        }

        int randomSkill = randomNumber(MIN_ID, MAX_ID);
        Skill parentSkill = skillRepository.findOne(""+randomSkill);
        if(parentSkill!=null) {
            List<String> allChildren = ontologyOperations.getAllChildren(parentSkill);
            if (allChildren.isEmpty()) {
                allChildren = ontologyOperations.getAllChildren(skillRepository.findByName(parentSkill.getParent()));
            }
            List<String> allSkills = new ArrayList<>();
            allSkills.addAll(allChildren);
            int nrOfRelatedSkills = randomNumber(MIN_RELATED_SKILLS, MAX_RELATED_SKILLS);
            int i = 0;
            while (i < allSkills.size() && i <= nrOfRelatedSkills) {
                Skill skill = skillRepository.findByName(allSkills.get(i));
                skills.add(skill);
                i++;
            }
            Senior elder = new Senior();
            elder.setSkills(skills);
            elderRepository.save(elder);
        }
        return skills;
    }

    public Opportunity generateOpportunity(int minUnrelatedSkills, int maxUnrelatedSkills, int minRelatedSkills, int maxRelatedSkills) {
        Company company = companyRepository.findAll().get(0);
        Opportunity opportunity = new Opportunity("opportunity", company);
        Opportunity saved = opportunityRepository.save(opportunity);
        int nrOfUnrelatedSkills = randomNumber(minUnrelatedSkills, maxUnrelatedSkills);
        List<Skill> skills = new ArrayList<>();
        for(int i=0; i< nrOfUnrelatedSkills; i++){
            Skill skill = skillRepository.findOne(""+randomNumber(MIN_ID, MAX_ID));
            skills.add(skill);
        }

        int randomSkill = randomNumber(MIN_ID, MAX_ID);
        Skill parentSkill = skillRepository.findOne(""+randomSkill);
        List<String> allChildren = ontologyOperations.getAllChildren(parentSkill);
        if(allChildren.isEmpty()){
            allChildren = ontologyOperations.getAllChildren(skillRepository.findByName(parentSkill.getParent()));
        }
        List<String> allSkills = new ArrayList<>();
        allSkills.addAll(allChildren);
        int nrOfRelatedSkills = randomNumber(minRelatedSkills, maxRelatedSkills);
        int i=0;
        while(i < allSkills.size() && i <= nrOfRelatedSkills){
            Skill skill = skillRepository.findByName(allSkills.get(i));
            skills.add(skill);
            i++;
        }
        LOGGER.info("Saved opp id: " + saved.getId());
        Opportunity one = opportunityRepository.findOne(saved.getId());
        one.setSkills(skills);
        Opportunity opp = opportunityRepository.save(one);

        return opp;
    }


    public int randomNumber(int min, int max) {

        Random random = new Random();

        return random.nextInt(max - min + 1) + min;


    }

}
