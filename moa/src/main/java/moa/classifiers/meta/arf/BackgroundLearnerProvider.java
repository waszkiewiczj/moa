package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.options.OptionHandler;

public interface BackgroundLearnerProvider extends OptionHandler {

    void trainOnInstance(Instance inst);

    void updateLearner(EnsembleModelWrapper model);

    void pushLearner(EnsembleModelWrapper model, FeatureSelector featureSelector);

    void resetLearning();
}
