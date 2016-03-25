package diploma.elders.up.ontology;

import diploma.elders.up.dao.entity.Skill;
import diploma.elders.up.dao.repository.ElderRepository;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SkillOpportunityRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Simonas on 3/23/2016.
 */
@Component
public class OntologyLikeOperations {

    private static final String THING = "[owl:Thing]";

    @Autowired
    private OntologyReader ontologyReader;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ElderRepository elderRepository;
    @Autowired
    private SkillOpportunityRepository skillOpportunityRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;

    public boolean isAncestor(Skill ancestorSkill, Skill childSkill){
        String ancestor = ancestorSkill.getName();
        String child = childSkill.getName();

        boolean isAncestor = false;

        String childTemp = child;
        while (childTemp != null && !THING.equals(childTemp) && getParentSkillName(childTemp).isPresent()) {
            childTemp = getParentSkillName(childTemp).get().getParentName();
            if (childTemp.equals(ancestor)) {
                isAncestor = true;
            }
        }
        return isAncestor;
    }

    public int getDepth(Skill skill) {
        int depth = -1;

        String skillName = skill.getName();

        if (skillName == null) {
            return depth;
        } else {
            depth = 0;
        }
        while (skillName != null && !THING.equals(skillName)  && getParentSkillName(skillName).isPresent()) {
            skillName = getParentSkillName(skillName).get().getParentName();
            depth++;
        }
        return depth;
    }

    public int numberOfChildren(Skill skill) {
        return getSubclasses(skill.getName()).size();
    }

    public boolean areEqual(Skill skill1, Skill skill2) {
        boolean areEqual;
        areEqual = skill1.getName().equals(skill2.getName());
        return areEqual;
    }

    public Skill getLeastCommonAncestor(Skill skill1, Skill skill2) {
        String leastCommonAncestor = null;

        List<String> skill1Parents = new ArrayList<>();
        List<String> skill2Parents = new ArrayList<>();

        String skill1OntClass = skill1.getName();
        String skill2OntClass = skill2.getName();

        //get all parents of skill until owlThing
        while(skill1OntClass != null && !THING.equals(skill1OntClass)  && getParentSkillName(skill1OntClass).isPresent()) {
            skill1OntClass = getParentSkillName(skill1OntClass).get().getParentName();
            skill1Parents.add(skill1OntClass);
        }

        while(skill2OntClass != null && !THING.equals(skill2OntClass)  && getParentSkillName(skill2OntClass).isPresent()) {
            skill2OntClass = getParentSkillName(skill2OntClass).get().getParentName();
            skill2Parents.add(skill2OntClass);
        }

        boolean flag = false;

        for (String s1 : skill1Parents) {
            for (String s2 : skill2Parents) {
                if (s1.equals(s2)) {
                    leastCommonAncestor = s1;
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        if(getParentSkillName(leastCommonAncestor).isPresent()) {
            return new Skill(leastCommonAncestor, getParentSkillName(leastCommonAncestor).get().getParentName());
        }else {
            return new Skill(leastCommonAncestor, null);
        }
    }

    public List<Skill> getAncestorHierarchy(Skill parent, Skill child) {
        List<Skill> ancestors = new ArrayList<>();

        String parentClass = parent.getName();
        String childClass = child.getName();

        boolean flag = false;
        while(childClass != null && !THING.equals(childClass) && getParentSkillName(childClass).isPresent()) {
            childClass = getParentSkillName(childClass).get().getParentName();
            if (childClass.equals(parentClass)) {
                flag = true;
                break;
            }
            if(getParentSkillName(childClass).isPresent()) {
                Skill childSkill = new Skill(childClass, getParentSkillName(childClass).get().getParentName());
                ancestors.add(0, childSkill);
            }
        }

        if (!flag) {
            ancestors = new ArrayList<>();
        }
        return ancestors;

    }

    private Optional<Skill> getParentSkillName(String childName){
        return skillRepository.findByName(childName).stream().findFirst();
    }

    private List<Skill> getSubclasses(String name){
        return skillRepository.findByParentName(name);
    }
}
