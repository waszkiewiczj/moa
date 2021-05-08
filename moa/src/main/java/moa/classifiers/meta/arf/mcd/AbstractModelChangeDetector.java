package moa.classifiers.meta.arf.mcd;

import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.tasks.TaskMonitor;

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

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
