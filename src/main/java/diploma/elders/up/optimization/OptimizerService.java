package diploma.elders.up.optimization;

import diploma.elders.up.dto.ElderDTO;
import diploma.elders.up.optimization.domain.OptimizationResult;

import java.util.List;

/**
 * @author scorde
 * @since 4/21/2016.
 */
public interface OptimizerService {

    OptimizationResult applyOptimization(List<ElderDTO> elders);
}
