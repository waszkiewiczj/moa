package moa.classifiers.meta.arf;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.options.AbstractOptionHandler;

public abstract class AbstractBackgroundLearnerProvider extends AbstractOptionHandler
        implements BackgroundLearnerProvider {
    protected FeatureSelectionAdaptiveRandomForest forest;

    @Override
    public void init(FeatureSelectionAdaptiveRandomForest forest) {
        this.forest = forest;
    }

    @Override
    public BackgroundLearnerProvider copy() {
        return (BackgroundLearnerProvider) super.copy();
    }
}
