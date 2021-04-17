package moa.classifiers.meta.featureselectionarf;

import moa.options.AbstractOptionHandler;

public abstract class AbstractFeatureSelector extends AbstractOptionHandler
        implements FeatureSelector {
    @Override
    public FeatureSelector copy() {
        return (FeatureSelector) super.copy();
    }
}
