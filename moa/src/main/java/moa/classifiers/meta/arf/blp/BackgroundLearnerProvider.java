package moa.classifiers.meta.arf.blp;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.classifiers.meta.arf.EnsembleModelWrapper;
import moa.options.OptionHandler;

import java.util.Optional;

public interface BackgroundLearnerProvider extends OptionHandler {

    void init(FeatureSelectionAdaptiveRandomForest forest);

    void trainOnInstance(Instance inst);

    void updateLearner(EnsembleModelWrapper model);

    Optional<EnsembleModelWrapper> getLearner(EnsembleModelWrapper model);

    void pushLearner(EnsembleModelWrapper model);

    void resetLearning();
}
