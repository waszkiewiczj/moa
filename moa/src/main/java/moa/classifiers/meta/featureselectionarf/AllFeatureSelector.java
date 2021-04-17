package moa.classifiers.meta.featureselectionarf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AllFeatureSelector extends AbstractFeatureSelector {

    private Set<Integer> allFeatures = new HashSet<>();

    @Override
    public String getPurposeString() {
        return "Selects all available features from instances.";
    }

    @Override
    public void trainOnInstance(Instance inst) {
        int instanceFeatures = inst.numInputAttributes();
        if (instanceFeatures != allFeatures.size()) {
            allFeatures.clear();
            allFeatures.addAll(IntStream.range(0, instanceFeatures).boxed().collect(Collectors.toList()));
        }
    }

    @Override
    public Set<Integer> getFeatureIndexes() {
        return allFeatures;
    }

    @Override
    public void resetLearning() {
        allFeatures.clear();
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
