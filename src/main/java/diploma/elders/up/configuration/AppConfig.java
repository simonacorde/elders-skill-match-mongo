package diploma.elders.up.configuration;

import diploma.elders.up.optimization.OptimizerService;
import diploma.elders.up.optimization.bird.optimizer.BirdMatingOptimizerService;
import diploma.elders.up.semantic.matching.NormalizingSemanticMatcher;
import diploma.elders.up.semantic.matching.OntologySemanticMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Simonas on 4/20/2016.
 */
@Configuration
public class AppConfig {

    @Bean
    public OntologySemanticMatcher ontologySemanticMatcher()
    {
        return new NormalizingSemanticMatcher();
    }

    @Bean
    public OptimizerService optimizerService()
    {
        return new BirdMatingOptimizerService();
    }
}
