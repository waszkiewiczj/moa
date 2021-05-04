package moa.classifiers.meta.arf.mcd;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.classifiers.meta.arf.EnsembleModelWrapper;
import moa.options.OptionHandler;

import java.util.List;

public interface ModelChangeDetector extends OptionHandler {

    void init(FeatureSelectionAdaptiveRandomForest forest);

    void update();

    List<EnsembleModelWrapper> getModelsToUpdate();

    List<EnsembleModelWrapper> getModelsToPush();

    void resetLearning();
}
