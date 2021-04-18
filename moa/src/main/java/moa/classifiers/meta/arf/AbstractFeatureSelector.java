package moa.classifiers.meta.arf;

import moa.options.AbstractOptionHandler;

public abstract class AbstractFeatureSelector extends AbstractOptionHandler
        implements FeatureSelector {
    @Override
    public FeatureSelector copy() {
        return (FeatureSelector) super.copy();
    }
}
