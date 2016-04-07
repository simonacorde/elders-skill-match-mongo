package diploma.elders.up.ontology;

import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.SkillRepository;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

/**
 * Created by Simonas on 12/5/2015.
 */
@Component
public class OntologyReader {

    private static final Logger log = LoggerFactory.getLogger(OntologyReader.class);

    public OWLOntology connectToOntology(){
        OWLOntologyManager manager= OWLManager.createOWLOntologyManager();
        IRI iri = IRI.create("http://www.co-ode.org/ontologies/skills/skills.owl");
        File file = new File("D:\\Licenta\\Program Licenta\\skills.owl");
        // Load the local copy
        OWLOntology localSkills = null;
        try {
            localSkills = manager.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            log.error("Couldn't open ontology from document {}", e);
        }
        log.info("Loaded ontology: " + localSkills);
        // We can always obtain the location where an ontology was loaded from
        IRI documentIRI = manager.getOntologyDocumentIRI(localSkills);
        log.info(" from: " + documentIRI);

        manager.removeOntology(localSkills);
        // When a local copy of one of more ontologies is used, an ontology IRI mapper can be used
        // to provide a redirection mechanism. This means that ontologies can be loaded as if they
        // were located on the Web. In this example, we simply redirect the loading from
        // http://www.co-ode.org/ontologies/pizza/pizza.owl to our local copy above.
        manager.addIRIMapper(new SimpleIRIMapper(iri, IRI.create(file)));
        // Load the ontology as if we were loading it from the Web (from its ontology IRI)
        IRI skillsOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/skills/skills.owl");
        OWLOntology ontology = null;
        try {
            ontology = manager.loadOntology(skillsOntologyIRI);
        } catch (OWLOntologyCreationException e) {
            log.error("Cannot load ontology from web! {}",e);
        }
        log.info("Loaded ontology: " + ontology);
        log.info(" from: " + manager.getOntologyDocumentIRI(ontology));

        return ontology;
    }

    public int generateRepository(OWLOntology ontology, SkillRepository skillRepository){
        int nodes=0;
        Integer id = 0;
        Set<OWLClass> entitySet=ontology.getClassesInSignature();
        for(OWLClass o : entitySet )
        {
            //Split the array and take only the entity
            String [] skillPart1 = o.toString().split("#");
            String [] skillPart2 = skillPart1[skillPart1.length-1].split(">");
            String [] parentPart1 = o.getSuperClasses(ontology).toString().split("#");
            String [] parentPart2 = parentPart1[parentPart1.length-1].split(">");

            log.info(skillPart2[0] + " has parent " +parentPart2[0]);
            skillRepository.save(new Skill(String.valueOf(id), skillPart2[0], parentPart2[0]));
            id++;
            nodes++;
            log.info(o + "  has node parent:  " + o.getSuperClasses(ontology));
        }
        log.info("Number of ontology nodes: "+nodes);
        return nodes;
    }
}
