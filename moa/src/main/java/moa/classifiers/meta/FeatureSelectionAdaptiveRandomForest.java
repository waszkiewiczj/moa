package moa.classifiers.meta;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.MultiChoiceOption;
import com.yahoo.labs.samoa.instances.Instance;
import moa.capabilities.CapabilitiesHandler;
import moa.capabilities.Capability;
import moa.capabilities.ImmutableCapabilities;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.MultiClassClassifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.classifiers.meta.arf.FSARFLearner;
import moa.classifiers.meta.arf.FeatureSelector;
import moa.classifiers.meta.arf.ModelChangeDetector;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.Measurement;
import moa.options.ClassOption;


public class FeatureSelectionAdaptiveRandomForest extends AbstractClassifier implements MultiClassClassifier, CapabilitiesHandler {

    protected static final int FEATURES_M = 0;
    protected static final int FEATURES_SQRT = 1;
    protected static final int FEATURES_SQRT_INV = 2;
    protected static final int FEATURES_PERCENT = 3;

    public ClassOption treeLearnerOption = new ClassOption("treeLearner", 'l',
            "Random Forest Tree.", ARFHoeffdingTree.class,
            "ARFHoeffdingTree -e 2000000 -g 50 -c 0.01");

    public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
            "The number of trees.", 100, 1, Integer.MAX_VALUE);

    public MultiChoiceOption mFeaturesModeOption = new MultiChoiceOption("mFeaturesMode", 'o',
            "Defines how m, defined by mFeaturesPerTreeSize, is interpreted. M represents the total number of features.",
            new String[]{"Specified m (integer value)", "sqrt(M)+1", "M-(sqrt(M)+1)",
                    "Percentage (M * (m / 100))"},
            new String[]{"SpecifiedM", "SqrtM1", "MSqrtM1", "Percentage"}, 3);

    public IntOption mFeaturesPerTreeSizeOption = new IntOption("mFeaturesPerTreeSize", 'm',
            "Number of features allowed considered for each split. Negative values corresponds to M - m", 60, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public FloatOption lambdaOption = new FloatOption("lambda", 'a',
            "The lambda parameter for bagging.", 6.0, 1.0, Float.MAX_VALUE);

    public ClassOption modelChangeDetectorOption = new ClassOption("modelChangeDetector", 'd',
            "Change detector for changing models in ensemble", ModelChangeDetector.class, "DriftModelChangeDetector");

    public ClassOption featureSelectionMethodOption = new ClassOption("featureSelectionMethod", 'f',
            "Change method of preliminary feature selection", FeatureSelector.class, "AllFeatureSelector");

    private FSARFLearner learner;

    @Override
    public String getPurposeString() {
        return "Adaptive Random Forest algorithm for evolving data streams with dynamic preliminary feature selection";
    }

    @Override
    public double[] getVotesForInstance(Instance inst) {
        if (learner == null) {
            learner = initLearner(inst);
        }
        return learner.getVotesForInstance(inst);
    }

    @Override
    public void resetLearningImpl() {
        if (learner != null) {
            learner.resetLearningImpl();
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        if (learner == null) {
            learner = initLearner(inst);
        }
        learner.trainOnInstanceImpl(inst);
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return learner != null ? learner.getModelMeasurementsImpl() : new Measurement[0];
    }

    @Override
    public void getModelDescription(StringBuilder out, int indent) {
        if (learner != null) {
            learner.getModelDescription(out, indent);
        }
    }

    @Override
    public boolean isRandomizable() {
        return learner != null && learner.isRandomizable();
    }

    @Override
    public ImmutableCapabilities defineImmutableCapabilities() {
        if (this.getClass() == FeatureSelectionAdaptiveRandomForest.class)
            return new ImmutableCapabilities(Capability.VIEW_STANDARD, Capability.VIEW_LITE);
        else
            return new ImmutableCapabilities(Capability.VIEW_STANDARD);
    }

    private FSARFLearner initLearner(Instance inst) {
        ARFHoeffdingTree treeLearner = (ARFHoeffdingTree) getPreparedClassOption(treeLearnerOption);
        int ensembleSize = ensembleSizeOption.getValue();
        int subspaceSize = getSubspaceSize(inst);
        double lambda = lambdaOption.getValue();
        ModelChangeDetector modelChangeDetector = (ModelChangeDetector) getPreparedClassOption(modelChangeDetectorOption);
        FeatureSelector featureSelector = (FeatureSelector) getPreparedClassOption(featureSelectionMethodOption);

        FSARFLearner learner = new FSARFLearner(treeLearner, ensembleSize, subspaceSize, lambda,
                modelChangeDetector, featureSelector);

        return learner;
    }

    private int getSubspaceSize(Instance inst) {
        int subspaceSize = mFeaturesPerTreeSizeOption.getValue();

        // The size of m depends on:
        // 1) mFeaturesPerTreeSizeOption
        // 2) mFeaturesModeOption
        int n = inst.numAttributes() - 1;  // Ignore class label ( -1 )

        switch (this.mFeaturesModeOption.getChosenIndex()) {
            case FeatureSelectionAdaptiveRandomForest.FEATURES_SQRT:
                subspaceSize = (int) Math.round(Math.sqrt(n)) + 1;
                break;
            case FeatureSelectionAdaptiveRandomForest.FEATURES_SQRT_INV:
                subspaceSize = n - (int) Math.round(Math.sqrt(n) + 1);
                break;
            case FeatureSelectionAdaptiveRandomForest.FEATURES_PERCENT:
                // If subspaceSize is negative, then first find out the actual percent, i.e., 100% - m.
                double percent = subspaceSize < 0 ? (100 + subspaceSize) / 100.0 : subspaceSize / 100.0;
                subspaceSize = (int) Math.round(n * percent);
                break;
        }

        // m is negative, use size(features) + -m
        if (subspaceSize < 0)
            subspaceSize = n + subspaceSize;
        // Other sanity checks to avoid runtime errors.
        //  m <= 0 (m can be negative if this.subspace was negative and
        //  abs(m) > n), then use m = 1
        if (subspaceSize <= 0)
            subspaceSize = 1;
        // m > n, then it should use n
        if (subspaceSize > n)
            subspaceSize = n;

        return subspaceSize;
    }
}
