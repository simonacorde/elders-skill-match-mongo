package diploma.elders.up.ontology;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private SeniorRepository elderRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;

    public boolean isAncestor(Skill ancestorSkill, Skill childSkill){
        String ancestor = ancestorSkill.getName();
        String child = childSkill.getName();

        boolean isAncestor = false;

        String childTemp = child;
        while (childTemp != null && !THING.equals(childTemp) && getParentSkillName(childTemp) != null) {
            childTemp = getParentSkillName(childTemp).getParent();
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
        while (skillName != null && !THING.equals(skillName)  && getParentSkillName(skillName) != null) {
            skillName = getParentSkillName(skillName).getParent();
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
        while(skill1OntClass != null && !THING.equals(skill1OntClass)  && getParentSkillName(skill1OntClass) != null) {
            skill1OntClass = getParentSkillName(skill1OntClass).getParent();
            skill1Parents.add(skill1OntClass);
        }

        while(skill2OntClass != null && !THING.equals(skill2OntClass)  && getParentSkillName(skill2OntClass) != null) {
            skill2OntClass = getParentSkillName(skill2OntClass).getParent();
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
        if(getParentSkillName(leastCommonAncestor) != null) {
            return new Skill("1",leastCommonAncestor, getParentSkillName(leastCommonAncestor).getParent());
        }else {
            return new Skill("1",leastCommonAncestor, null);
        }
    }

    public List<Skill> getAncestorHierarchy(Skill parent, Skill child) {
        List<Skill> ancestors = new ArrayList<>();

        String parentClass = parent.getName();
        String childClass = child.getName();

        boolean flag = false;
        while(childClass != null && !THING.equals(childClass) && getParentSkillName(childClass) != null) {
            childClass = getParentSkillName(childClass).getParent();
            if (childClass.equals(parentClass)) {
                flag = true;
                break;
            }
            if(getParentSkillName(childClass) != null) {
                Skill childSkill = new Skill("1",childClass, getParentSkillName(childClass).getParent());
                ancestors.add(0, childSkill);
            }
        }

        if (!flag) {
            ancestors = new ArrayList<>();
        }
        return ancestors;

    }

    public List<String> getAllChildren(Skill skill) {
        List<String> children = new ArrayList<>();
        List<Skill> skills = getSubclasses(skill.getName());
        for(Skill skillChild: skills){
            children.add(skillChild.getName());
        }
        return children;
    }

    private Skill getParentSkillName(String childName){
        return skillRepository.findByName(childName);
    }

    private List<Skill> getSubclasses(String name){
        return skillRepository.findByParentName(name);
    }
}
