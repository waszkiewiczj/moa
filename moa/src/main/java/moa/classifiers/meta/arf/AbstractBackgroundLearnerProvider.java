package moa.classifiers.meta.arf;

import moa.options.AbstractOptionHandler;

public abstract class AbstractBackgroundLearnerProvider extends AbstractOptionHandler
        implements BackgroundLearnerProvider {

    @Override
    public BackgroundLearnerProvider copy() {
        return (BackgroundLearnerProvider) super.copy();
    }
}
