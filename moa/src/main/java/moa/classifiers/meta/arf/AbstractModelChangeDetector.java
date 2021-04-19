package moa.classifiers.meta.arf;

import moa.options.AbstractOptionHandler;

public abstract class AbstractModelChangeDetector extends AbstractOptionHandler
        implements ModelChangeDetector {

    @Override
    public ModelChangeDetector copy() {
        return (ModelChangeDetector) super.copy();
    }
}
