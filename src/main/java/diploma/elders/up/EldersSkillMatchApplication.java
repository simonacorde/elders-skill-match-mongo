package diploma.elders.up;

import diploma.elders.up.dao.repository.ElderRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import diploma.elders.up.data.DataGenerator;
import diploma.elders.up.ontology.OntologyReader;
import diploma.elders.up.service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EldersSkillMatchApplication {

    private static final Logger log = LoggerFactory.getLogger(EldersSkillMatchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EldersSkillMatchApplication.class, args);
    }

    @Autowired
    private OntologyReader ontologyReader;
    @Autowired
    private SkillRepository repository;
    @Autowired
    private DataGenerator dataGenerator;
    @Autowired
    private ElderRepository seniorRepository;
    @Autowired
    private MatchingService matchingService;

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
//            repository.deleteAll();
//            seniorRepository.deleteAll();
//            OWLOntology ontology = ontologyReader.connectToOntology();
//            int nodes = ontologyReader.generateRepository(ontology, repository);
//            // fetch all customers
//            log.info("Skills found with findAll():");
//            log.info("-------------------------------");
//            for (Skill skill : repository.findAll()) {
//                log.info(skill.toString());
//            }
//            log.info("Nr of nodes: "+nodes);
//
//             //fetch customers by last name
//            log.info("Skill found with findByName('\"<http://www.semanticweb.org/ontologies/skills-ontology#Biometry>\"'):");
//            log.info("--------------------------------------------");
//            for (Skill bauer : repository.findByName("<http://www.semanticweb.org/ontologies/skills-ontology#Biometry>")) {
//                log.info(bauer.toString());
//            }
//            log.info("");
//
//            for(int i = 0; i < 9999; i++) {
//                log.info("CV:" + dataGenerator.generateCV());
//            }
//            log.info("Elder: ", seniorRepository.findOne(3).getSkills().toString());
            dataGenerator.generateOpportunities(5, 15, 7, 18);
//            matchingService.applyMatchingAlgorithm();
        };
    }
}
