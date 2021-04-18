package moa.classifiers.meta.arf;

import moa.options.AbstractOptionHandler;

public abstract class AbstractModelChangeDetector extends AbstractOptionHandler
        implements ModelChangeDetector {
    protected int ensembleSize;

    @Override
    public void init(int ensembleSize) {
        this.ensembleSize = ensembleSize;
    }

    @Override
    public void resetLearning() { init(this.ensembleSize); }

    @Override
    public ModelChangeDetector copy() {
        return (ModelChangeDetector) super.copy();
    }
}
