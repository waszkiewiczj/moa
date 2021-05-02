package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.options.OptionHandler;

public interface BackgroundLearnerProvider extends OptionHandler {

    void init(FeatureSelectionAdaptiveRandomForest forest);

    void trainOnInstance(Instance inst);

    void updateLearner(EnsembleModelWrapper model);

    void pushLearner(EnsembleModelWrapper model);

    void resetLearning();
}
