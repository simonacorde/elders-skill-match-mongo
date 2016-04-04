package diploma.elders.up;

import diploma.elders.up.dao.documents.Opportunity;
import diploma.elders.up.dao.documents.Senior;
import diploma.elders.up.dao.documents.Skill;
import diploma.elders.up.dao.repository.OpportunityRepository;
import diploma.elders.up.dao.repository.SeniorRepository;
import diploma.elders.up.dao.repository.SkillRepository;
import diploma.elders.up.dto.OpportunityDTO;
import diploma.elders.up.matching.SemanticMatchingAlgorithm;
import diploma.elders.up.matching.SkillMatchingAlgorithm;
import diploma.elders.up.ontology.OntologyLikeOperations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EldersSkillMatchApplication.class)
@WebAppConfiguration
public class EldersSkillMatchApplicationTests {

	@Autowired
	private SemanticMatchingAlgorithm semanticMatchingAlgorithm;
	@Autowired
	private SkillMatchingAlgorithm skillMatchingAlgorithm;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private OntologyLikeOperations ontologyLikeOperations;
	@Autowired
	private SeniorRepository seniorRepository;
	@Autowired
	private OpportunityRepository opportunityRepository;

	@Test
	public void contextLoads() {
		Skill fSkill = skillRepository.findByName("auditing");
		Skill sSkill = skillRepository.findByName("calligraphy");
		Opportunity opportunity = opportunityRepository.findAll().get(0);
		Senior one = seniorRepository.findOne("56f8328eecdb7333903c1e3c");
		double match = semanticMatchingAlgorithm.match(new OpportunityDTO(opportunity), one.getSkills());
		//double m1 = semanticMatchingAlgorithm.matchSkills(fSkill, sSkill);
		///double m2 = skillMatchingAlgorithm.matchSkills(fSkill, sSkill);

		double error =  match;

	}

}
