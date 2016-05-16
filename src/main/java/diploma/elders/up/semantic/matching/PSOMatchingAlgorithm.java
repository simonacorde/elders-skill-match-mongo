package diploma.elders.up.semantic.matching;

import com.google.common.collect.Lists;
import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea Iepure on 4/26/2016.
 */

public class PSOMatchingAlgorithm implements OntologySemanticMatcher  {


    private static final double MAX_DEPTH = 8;
    @Autowired
    private OntologyLikeOperations ontologyOperations;

    public double matchSkills(Skill skill1, Skill skill2) {
      if(skill1.equals(skill2))
          return 1.0;
      else if(ontologyOperations.isAncestor(skill1,skill2))

                return subsumeFunction(skill2,skill1);

            else if(ontologyOperations.isAncestor(skill2,skill1))
                      return subsumeFunction(skill1,skill2);
                 else if(isSiblingRelation(skill1,skill2))
                        return siblingFunction(skill1,skill2);
                      else return 0.0;

    }



    public boolean isSiblingRelation(Skill skill1,Skill skill2)
    {
        Skill commonAncestor =ontologyOperations.getLeastCommonAncestor(skill1,skill2);
        if(commonAncestor!=null &&
                !ontologyOperations.getAncestorHierarchy(commonAncestor,skill1).contains(skill2)  &&
                !ontologyOperations.getAncestorHierarchy(commonAncestor,skill2).contains(skill1) )
           return true;
        else return false;
    }

    public double getConceptMatchScore(Skill skill1,Skill skill2)
    {
        List <Skill> parents=ontologyOperations.getAncestorHierarchy(skill2,skill1);
        return (parents.size()+1)/(MAX_DEPTH+1);
        //double s=ontologyOperations.getDepth(skill)/(MAX_DEPTH+1);
        //return ontologyOperations.getDepth(skill)/(MAX_DEPTH+1);
    }


    ///Skill2 will be always the ancestor  of skill1(convention)
    public double subsumeFunction(Skill skill1, Skill skill2)
    {
        if(skill1.equals(skill2))
            return 1.0;
        else
        {
            List <Skill> hierarchy=new ArrayList<>();
            hierarchy.add(skill2);
            List <Skill> parents=ontologyOperations.getAncestorHierarchy(skill2,skill1);
            for(Skill s:parents)
            {
                hierarchy.add(s);
            }
            List <Skill> reverseHierarchy= Lists.reverse(hierarchy);
            double sum=0.0;
            for(Skill s:reverseHierarchy)           {
                sum=sum+subsumeFunction(s,skill2);
            }
            return ((getConceptMatchScore(skill1,skill2)*sum)*MAX_DEPTH)/Math.pow(ontologyOperations.getDistance2(skill1,skill2),2);

        }

    }
    //In our ontology we don't have more than one common ancestor for two skills
    //so we won't use the sum from the function
    public double siblingFunction(Skill skill1 ,Skill skill2)
    {
        Skill commonAncestor=ontologyOperations.getLeastCommonAncestor(skill1,skill2);
        return (subsumeFunction(skill1,commonAncestor)+subsumeFunction(skill2,commonAncestor))/ontologyOperations.getDistance2(skill1,skill2)*2;
    }






}
