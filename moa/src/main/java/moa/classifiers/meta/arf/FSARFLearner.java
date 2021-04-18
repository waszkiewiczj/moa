package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.MultiClassClassifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.Measurement;

public class FSARFLearner extends AbstractClassifier implements MultiClassClassifier {

    protected final ARFHoeffdingTree baseTree;
    protected final int ensembleSize;
    protected final int subspaceSize;
    protected final double lambda;
    protected final ModelChangeDetector modelChangeDetector;
    protected final FeatureSelector featureSelector;

    public FSARFLearner(ARFHoeffdingTree baseTree, int ensembleSize, int subspaceSize, double lambda,
                        ModelChangeDetector modelChangeDetector, FeatureSelector featureSelector) {
        this.baseTree = baseTree;
        baseTree.resetLearning();
        this.ensembleSize = ensembleSize;
        this.subspaceSize = subspaceSize;
        this.lambda = lambda;
        this.modelChangeDetector = modelChangeDetector;
        this.featureSelector = featureSelector;
    }

    @Override
    public double[] getVotesForInstance(Instance inst) {
        Instance testInstance = inst.copy();
        return new double[0];
    }

    @Override
    public void resetLearningImpl() {

    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {

    }

    @Override
    public Measurement[] getModelMeasurementsImpl() {
        return null;
    }

    @Override
    public void getModelDescription(StringBuilder out, int indent) {
    }

    @Override
    public boolean isRandomizable() {
        return true;
    }
}
