package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.ObjectRepository;
import moa.evaluation.ClassificationPerformanceEvaluator;
import moa.tasks.TaskMonitor;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class IndividualBackgroundLearnerProvider extends AbstractBackgroundLearnerProvider {

    private final Dictionary<Integer, EnsembleModelWrapper> backgroundModels = new Hashtable<>();

    @Override
    public String getPurposeString() {
        return "Provider that keeps up to one background model for each ensemble model.";
    }

    @Override
    public void trainOnInstance(Instance inst) {
        Enumeration<EnsembleModelWrapper> modelsEnumeration = backgroundModels.elements();

        while(modelsEnumeration.hasMoreElements()) {
            modelsEnumeration.nextElement().trainOnInstance(inst, 1);
        }
    }

    @Override
    public void updateLearner(EnsembleModelWrapper model) {
        EnsembleModelWrapper backgroundModel = backgroundModels.get(model.index);
        if (backgroundModel != null) {
            model.resetLearning(backgroundModel.getModel());
        }
    }

    @Override
    public void pushLearner(EnsembleModelWrapper model) {
        ARFHoeffdingTree treeCopy = model.getModel();
        treeCopy.resetLearning();

        ClassificationPerformanceEvaluator evaluatorCopy = model.getEvaluator();
        evaluatorCopy.reset();

        EnsembleModelWrapper backgroundModel = new EnsembleModelWrapper(
                model.index,
                treeCopy,
                evaluatorCopy
        );
        backgroundModels.put(model.index, backgroundModel);
    }

    @Override
    public void resetLearning() {
        Enumeration<EnsembleModelWrapper> modelsEnumeration = backgroundModels.elements();

        while(modelsEnumeration.hasMoreElements()) {
            modelsEnumeration.nextElement().resetLearning();
        }
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }

}
