package diploma.elders.up.ontology;

import diploma.elders.up.dao.entity.Skill;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Simonas on 2/17/2016.
 */
@Component
public class OntologyOperations {

    @Autowired
    private OntologyReader ontologyReader;

    public int getDistance(String skill1, String skill2) {
        OWLOntology ontology = getOntology();
        Set<OWLClass> entitySet = ontology.getClassesInSignature();
        OWLClass class1 = null;
        OWLClass class2 = null;
        List<OWLClass> classesList1 = new ArrayList<OWLClass>();
        List<OWLClass> classesList2 = new ArrayList<OWLClass>();


        for (OWLClass o : entitySet) {
            if (o.toString().equals(skill1)) {
                class1 = o;
                classesList1.add(o);
            } else if (o.toString().equals(skill2)) {
                class2 = o;
                classesList2.add(o);
            }
        }
        OWLClass parent1 = null;
        OWLClass parent2 = null;

        do {
            parent1 = class1.getSuperClasses(ontology).iterator().next().asOWLClass();
            if (!parent1.isOWLThing()) {
                classesList1.add(parent1);
                class1 = parent1;

            }
            parent2 = class2.getSuperClasses(ontology).iterator().next().asOWLClass();
            if (!parent2.isOWLThing() && !classesList1.contains(class2)) {
                classesList2.add(parent2);
                class2 = parent2;
            }
        }
        while (!classesList2.contains(class1) && !classesList1.contains(class2) && (!parent1.isOWLThing() || !parent2.isOWLThing()));

        return classesList1.size()-1 + classesList2.size() - 1;
    }

    public boolean isAncestor(Skill ancestorSkill, Skill childSkill){
        String ancestor = ancestorSkill.getName();
        String child = childSkill.getName();

        OWLOntology ontology = getOntology();
        boolean isAncestor = false;

        OWLClass ontClass = null;
        Set<OWLClass> entitySet = ontology.getClassesInSignature();

        for (OWLClass o : entitySet) {
            if (o.toString().equals(child)) {
                ontClass = o;
            }
        }


        if (ontClass != null) {
            while (ontClass.getSuperClasses(ontology) != null) {
                ontClass = ontClass.getSuperClasses(ontology).iterator().next().asOWLClass();
                if (ontClass.toString().equals(ancestor)) {
                    isAncestor = true;
                }
            }
        }
        return isAncestor;
    }

    public int getDepth(Skill skill) {
        OWLOntology ontology = getOntology();
        int depth = -1;

        OWLClass ontClass = getOntologyClass(skill.getName());

        if (ontClass == null) {
            return depth;
        } else {
            depth = 0;
        }

        while (ontClass.getSuperClasses(ontology) != null && ontClass.getSuperClasses(ontology).iterator().hasNext()) {
            ontClass = ontClass.getSuperClasses(ontology).iterator().next().asOWLClass();
            depth++;
        }
        return depth;
    }

    public int numberOfChildren(Skill skill) {
        int noOfChildren = 0;

        OWLClass ontClass = getOntologyClass(skill.getName());

        if (ontClass == null) {
            return noOfChildren;
        } else {
            for (OWLClassExpression owlClassExpression : ontClass.getSubClasses(getOntology())) {
                noOfChildren++;
            }
        }
        return noOfChildren;
    }

    private OWLClass getOntologyClass(String skill){
        OWLClass ontClass = null;
        Set<OWLClass> entitySet = getOntology().getClassesInSignature();

        for(OWLClass o : entitySet )
        {
            //Split the array and take only the entity
            String [] skillPart1 = o.toString().split("#");
            String [] skillPart2 = skillPart1[skillPart1.length-1].split(">");
            if (skill.equals(skillPart2[0])) {
                ontClass = o;
            }
        }
        return ontClass;
    }

    public boolean areEqual(Skill skill1, Skill skill2) {
        boolean areEqual = true;
        areEqual = skill1.getName().equals(skill2.getName());
        return areEqual;
    }

    public Skill getLeastCommonAncestor(Skill skill1, Skill skill2) {
        String leastCommonAncestor = null;

        List<String> skill1Parents = new ArrayList<>();
        List<String> skill2Parents = new ArrayList<>();

        OWLClass skill1OntClass = getOntologyClass(skill1.getName());
        OWLClass skill2OntClass = getOntologyClass(skill2.getName());

        while (skill1OntClass.getSuperClasses(getOntology()) != null && !skill1OntClass.getSuperClasses(getOntology()).isEmpty()) {
            skill1OntClass = skill1OntClass.getSuperClasses(getOntology()).iterator().next().asOWLClass();
            skill1Parents.add(skill1OntClass.toString());
        }

        while (skill2OntClass.getSuperClasses(getOntology()) != null && !skill2OntClass.getSuperClasses(getOntology()).isEmpty()) {
            skill2OntClass = skill2OntClass.getSuperClasses(getOntology()).iterator().next().asOWLClass();
            skill2Parents.add(skill1OntClass.toString());
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
        OWLClass leastCAncestorClass = getOntologyClass(leastCommonAncestor);
        return new Skill(leastCommonAncestor, leastCAncestorClass.getSuperClasses(getOntology()).toString());
    }

    public List<Skill> getAncestorHierarchy(Skill parent, Skill child) {
        List<Skill> ancestors = new ArrayList<>();

        OWLClass parentClass = getOntologyClass(parent.getName());
        OWLClass childClass = getOntologyClass(child.getName());

        boolean flag = false;

        if (parentClass != null && childClass != null) {

            while (childClass.getSuperClasses(getOntology()) != null && !childClass.getSuperClasses(getOntology()).isEmpty()) {
                childClass = childClass.getSuperClasses(getOntology()).iterator().next().asOWLClass();
                if (childClass.equals(parentClass)) {
                    flag = true;
                    break;
                }
                Skill childSkill = new Skill(childClass.toString(), childClass.getSuperClasses(getOntology()).toString());
                ancestors.add(0, childSkill);
            }

        }

        if (!flag) {
            ancestors = new ArrayList<>();
        }
        return ancestors;

    }

    public Set<String> getAllChildren(Skill skill) {
        Set<String> children = new HashSet<>();

        OWLClass ontClass = getOntologyClass(skill.getName());
        Set<OWLClassExpression> subClasses = ontClass.getSubClasses(getOntology());
        for (OWLClassExpression subclass : subClasses){
            children.add(getSkillNameFromOWLClass(subclass.asOWLClass()));
        }
        return children;
    }

    private String getSkillNameFromOWLClass(OWLClass ontClass){
        Set<OWLClass> entitySet = getOntology().getClassesInSignature();
        String skillName = "";
        for(OWLClass o : entitySet )
        {
            if(o.equals(ontClass)) {
                String[] skillPart1 = o.toString().split("#");
                String[] skillPart2 = skillPart1[skillPart1.length - 1].split(">");
                skillName = skillPart2[0];
            }
        }
        return skillName;
    }

    private OWLOntology getOntology(){
        return ontologyReader.connectToOntology();
    }
}
