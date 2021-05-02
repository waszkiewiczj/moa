package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.tasks.TaskMonitor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractFeatureSelector extends AbstractOptionHandler
        implements FeatureSelector {
    protected FeatureSelectionAdaptiveRandomForest forest;
    protected int numberOfFeatures = -1;

    @Override
    public void init(FeatureSelectionAdaptiveRandomForest forest) {
        this.forest = forest;
    }

    @Override
    public void trainOnInstance(Instance inst) {
        if (numberOfFeatures < 0) {
            numberOfFeatures = inst.numInputAttributes();
        }
    }

    @Override
    public FeatureSelector copy() {
        return (FeatureSelector) super.copy();
    }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }
}
