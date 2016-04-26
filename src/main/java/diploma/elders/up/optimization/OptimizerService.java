package diploma.elders.up.optimization;

import diploma.elders.up.dao.documents.OptimizationResult;
import diploma.elders.up.dto.ElderDTO;

import java.util.List;

/**
 * @author scorde
 * @since 4/21/2016.
 */
public interface OptimizerService {

    OptimizationResult applyOptimization(List<ElderDTO> elders);
}
