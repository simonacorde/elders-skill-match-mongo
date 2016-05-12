package diploma.elders.up;

import diploma.elders.up.dao.repository.MatchingResultRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import diploma.elders.up.dao.repository.UserRepository;
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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EldersSkillMatchApplication {

    private static final Logger log = LoggerFactory.getLogger(EldersSkillMatchApplication.class);
    private static final String THING = "[owl:Thing]";

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
    private SeniorRepository seniorRepository;
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private MatchingResultRepository matchingResultRepository;
    @Autowired
    private UserRepository userRepository;

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
//            log.info("nr of skills in db: " + repository.findAll().size());
//
//            for(int i = 0; i < 9999; i++) {
//                log.info("CV:" + dataGenerator.generateCV());
//            }
//            List<Senior> all = seniorRepository.findAll();
//            log.info("Nr of elders: ", seniorRepository.findAll().size());
//            log.info("Elder: ", seniorRepository.findOne("56f8328eecdb7333903c1e3e").getName());
//            for(Senior senior : all) {
//                log.info("Elder: ", senior.getId());
//            }
//            Company company = dataGenerator.generateCompany();
//            dataGenerator.generateOpportunity(3, 5, 8, 12);

//            log.info("Opp id: " + matchingResultRepository.findAll().get(0).getOpportunityId());
//            matchingService.applyMatchingAlgorithm(1000);
//            int treeDepth1 = semanticMatchingAlgorithm.findTreeDepth(repository.findByName("domain_specific_skills_and_competences"));
//            int treeDepth2 = semanticMatchingAlgorithm.findTreeDepth(repository.findByName("non_domain_specific_skills_and_competences"));
//
//            log.info("Tree depth 1: "+ treeDepth1);
//            log.info("Tree depth 2: "+ treeDepth2);

//            matchingService.applyMatchingPlusBinPacking();
//            userRepository.save(new User("hr", "hr", UserRole.USER));
//            userRepository.save(new User("admin", "admin", UserRole.ADMIN));
        };
    }
}
