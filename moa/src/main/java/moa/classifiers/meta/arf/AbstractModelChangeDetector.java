package moa.classifiers.meta.arf;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.options.AbstractOptionHandler;

public abstract class AbstractModelChangeDetector extends AbstractOptionHandler
        implements ModelChangeDetector {

    protected FeatureSelectionAdaptiveRandomForest forest;

    @Override
    public void init(FeatureSelectionAdaptiveRandomForest forest) {
        this.forest = forest;
    }

    @Override
    public ModelChangeDetector copy() {
        return (ModelChangeDetector) super.copy();
    }
}
