package moa.classifiers.meta.featureselectionarf;

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
    protected final ChangeDetector baseDritfDetectionMethod;
    protected final ChangeDetector baseWarningDetectionMethod;
    protected final FeatureSelector featureSelector;

    public FSARFLearner(ARFHoeffdingTree baseTree, int ensembleSize, int subspaceSize, double lambda,
                        ChangeDetector baseDritfDetectionMethod, ChangeDetector baseWarningDetectionMethod,
                        FeatureSelector featureSelector) {
        this.baseTree = baseTree;
        baseTree.resetLearning();
        this.ensembleSize = ensembleSize;
        this.subspaceSize = subspaceSize;
        this.lambda = lambda;
        this.baseDritfDetectionMethod = baseDritfDetectionMethod;
        this.baseWarningDetectionMethod = baseWarningDetectionMethod;
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
    protected Measurement[] getModelMeasurementsImpl() {
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
