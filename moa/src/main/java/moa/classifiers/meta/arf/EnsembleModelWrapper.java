package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.DoubleVector;
import moa.core.InstanceExample;
import moa.evaluation.ClassificationPerformanceEvaluator;

public class EnsembleModelWrapper {
    public final int index;
    public boolean correctlyClassifies;

    private final ClassificationPerformanceEvaluator evaluator;
    private ARFHoeffdingTree model;

    public EnsembleModelWrapper(int index, ARFHoeffdingTree model, ClassificationPerformanceEvaluator evaluator) {
        this.index = index;
        this.model = model;
        this.evaluator = evaluator;
    }

    public void trainOnInstance(Instance inst, double weight) {
        DoubleVector vote = new DoubleVector(model.getVotesForInstance(inst));

        correctlyClassifies = model.correctlyClassifies(inst);

        InstanceExample example = new InstanceExample(inst);
        evaluator.addResult(example, vote.getArrayRef());

        Instance weightedInstance = inst.copy();
        weightedInstance.setWeight(weight);

        model.trainOnInstance(weightedInstance);
    }

    public double[] getVotesForInstance(Instance inst) {
        return model.getVotesForInstance(inst);
    }

    public double getAccuracy() {
        return evaluator.getPerformanceMeasurements()[1].getValue();
    }

    public void resetLearning() {
        model.resetLearning();
        evaluator.reset();
    }

    public void resetLearning(ARFHoeffdingTree newModel) {
        model = newModel;
        evaluator.reset();
    }

    public ARFHoeffdingTree getModel() {
        return (ARFHoeffdingTree) model.copy();
    }

    public ClassificationPerformanceEvaluator getEvaluator() {
        return (ClassificationPerformanceEvaluator) evaluator.copy();
    }
}
