package moa.classifiers.meta.arf.fs;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BreimanFeatureSelector extends AbstractFeatureSelector {

    public IntOption slidingWindowSize = new IntOption("slidingWindowSize",
            'W', "slidingWindowSize", 1000);

    public IntOption slidingWindowStep = new IntOption("slidingWindowStep",
            'j', "slidingWindowStep", 1);

    private final List<Instance> slidingWindow = new LinkedList<>();

    @Override
    public String getPurposeString() {
        return "Selects features according to concept proposed by Breiman. "
                + "Feature is recognised as irrelevant if rearranging its values does not affect prediction accuracy. "
                + "Features are evaluated on a sliding window.";
    }

    @Override
    public void trainOnInstance(Instance inst) {
        slidingWindow.add(inst);
        if (slidingWindow.size() == slidingWindowSize.getValue() + slidingWindowStep.getValue()) {
            slidingWindow.subList(0, slidingWindowStep.getValue()).clear();
        }
    }

    @Override
    public Set<Integer> getFeatureIndexes() {
        Set<Integer> relevantFeatures = new HashSet<>(numberOfFeatures);

        for (int i = 0; i < numberOfFeatures; i++) {
            double baseAccuracy = getAccuracy(slidingWindow);

            List<Instance> shuffledWindow = getShuffledWindow(i);
            double shuffledAccuracy = getAccuracy(shuffledWindow);

            // relevant if accuracy decreased after shuffle
            if (shuffledAccuracy < baseAccuracy) {
                relevantFeatures.add(i);
            }
        }
        return relevantFeatures;
    }

    @Override
    public void resetLearning() { }

    private double getAccuracy(List<Instance> instances) {
        OptionalDouble avg = instances.stream().mapToInt(inst -> forest.correctlyClassifies(inst) ? 1 : 0).average();
        return avg.isPresent() ? avg.getAsDouble() : 0;
    }

    private List<Instance> getShuffledWindow(int attributeIndex) {
        List<Double> attributeValues = slidingWindow.stream().map(inst -> inst.value(attributeIndex)).collect(Collectors.toList());
        Collections.shuffle(attributeValues, forest.classifierRandom);

        return IntStream.range(0, slidingWindow.size()).mapToObj(idx -> {
            Instance instCopy = slidingWindow.get(idx).copy();
            instCopy.setValue(attributeIndex, attributeValues.get(idx));
            return instCopy;
        }).collect(Collectors.toList());
    }


}
