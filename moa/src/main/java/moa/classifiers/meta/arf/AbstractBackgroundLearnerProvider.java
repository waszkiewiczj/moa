package moa.classifiers.meta.arf;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.tasks.TaskMonitor;

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

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
