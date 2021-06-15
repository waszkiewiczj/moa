package moa.classifiers.meta.arf.fs;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.classifiers.MultiClassClassifier;
import moa.options.ClassOption;

public class PeriodicBreimanWrapperFeatureSelector extends BreimanFeatureSelector {

    public ClassOption wrapperLearnerOption = new ClassOption("wrapperLearner", 'l',
            "Learner used for feature evaluation.", MultiClassClassifier.class,
            "moa.classifiers.trees.HoeffdingTree");

    public IntOption resetLearnerFrequency = new IntOption("resetLearnerFrequency", 'r',
            "Period length between wrapper learner resets (-1 = never reset).", 1000, -1, Integer.MAX_VALUE);

    private Classifier learner;
    private int periodCount = 0;

    @Override
    public String getPurposeString() {
        return "Selects features according to concept proposed by Breiman. "
                + "Wrapper technique is used as features are evaluated with external model. "
                + "Model is periodically rebuilt.";
    }

    @Override
    public void trainOnInstance(Instance inst) {
        super.trainOnInstance(inst);

        if (periodCount > resetLearnerFrequency.getValue() && resetLearnerFrequency.getValue() != -1) {
            resetLearning();
        }

        getEvaluationClassifier().trainOnInstance(inst);
        periodCount++;
    }

    @Override
    public void resetLearning() {
        periodCount = 0;
        getEvaluationClassifier().resetLearning();
    }

    @Override
    protected Classifier getEvaluationClassifier() {
        if (learner == null) {
            learner = (Classifier) getPreparedClassOption(wrapperLearnerOption);
        }
        return learner;
    }
}
