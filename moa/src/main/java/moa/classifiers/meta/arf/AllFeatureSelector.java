package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;

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
        super.trainOnInstance(inst);
        if (numberOfFeatures != allFeatures.size()) {
            allFeatures.clear();
            allFeatures.addAll(IntStream.range(0, numberOfFeatures).boxed().collect(Collectors.toList()));
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
}
