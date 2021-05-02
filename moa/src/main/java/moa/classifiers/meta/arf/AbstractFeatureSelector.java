package moa.classifiers.meta.arf;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.options.AbstractOptionHandler;

public abstract class AbstractFeatureSelector extends AbstractOptionHandler
        implements FeatureSelector {
    protected FeatureSelectionAdaptiveRandomForest forest;

    @Override
    public void init(FeatureSelectionAdaptiveRandomForest forest) {
        this.forest = forest;
    }

    @Override
    public FeatureSelector copy() {
        return (FeatureSelector) super.copy();
    }
}
