package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.InstanceImpl;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.DoubleVector;
import moa.core.InstanceExample;
import moa.evaluation.ClassificationPerformanceEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EnsembleModelWrapper {
    public final int index;
    public boolean correctlyClassifies;
    public Set<Integer> selectedFeatureIndices;

    private final ClassificationPerformanceEvaluator evaluator;
    private ARFHoeffdingTree model;

    private int instancesSeen = 0;
    private int lastReplacedOn = 0;

    public EnsembleModelWrapper(int index, ARFHoeffdingTree model, Set<Integer> selectedFeatureIndices, ClassificationPerformanceEvaluator evaluator) {
        this.index = index;
        this.model = model;
        this.evaluator = evaluator;
        this.selectedFeatureIndices = selectedFeatureIndices;
        if (selectedFeatureIndices != null) {
            int numOfSelectedFeatures = selectedFeatureIndices.size();
            if (model.subspaceSizeOption.getValue() > numOfSelectedFeatures) {
                model.subspaceSizeOption.setValue(numOfSelectedFeatures);
            }
        }
    }

    public void trainOnInstance(Instance inst, double weight) {
        instancesSeen++;

        inst = SelectFeatures(inst);
        DoubleVector vote = new DoubleVector(model.getVotesForInstance(inst));

        correctlyClassifies = model.correctlyClassifies(inst);

        InstanceExample example = new InstanceExample(inst);
        evaluator.addResult(example, vote.getArrayRef());

        Instance weightedInstance = inst.copy();
        weightedInstance.setWeight(weight);

        model.trainOnInstance(weightedInstance);
    }

    public double[] getVotesForInstance(Instance inst) {
        inst = SelectFeatures(inst);
        return model.getVotesForInstance(inst);
    }

    public double getAccuracy() {
        return evaluator.getPerformanceMeasurements()[1].getValue();
    }

    public void resetLearning() {
        model.resetLearning();
        evaluator.reset();
    }

    public void resetLearning(EnsembleModelWrapper newModelWrapper) {
        model = newModelWrapper.getModel();
        selectedFeatureIndices = newModelWrapper.selectedFeatureIndices;
        lastReplacedOn = instancesSeen;
        evaluator.reset();
    }

    public ARFHoeffdingTree getModel() {
        return (ARFHoeffdingTree) model.copy();
    }

    public ClassificationPerformanceEvaluator getEvaluator() {
        return (ClassificationPerformanceEvaluator) evaluator.copy();
    }

    private Instance SelectFeatures(Instance inst) {
        if (selectedFeatureIndices == null || selectedFeatureIndices.size() == 0) {
            return inst;
        }

        List<Integer> newFeatures = new ArrayList<>(selectedFeatureIndices);
        newFeatures.add(inst.classIndex());

        int[] indices = newFeatures.stream().mapToInt(idx -> idx).toArray();
        double[] values = Arrays.stream(indices).mapToDouble(inst::value).toArray();

        Instance newInst = new InstanceImpl(inst.weight(), values, indices, indices.length);
        newInst.setDataset(inst.dataset());

        return newInst;
    }
}
