package moa.classifiers.meta.arf.blp;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.meta.arf.EnsembleModelWrapper;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.evaluation.ClassificationPerformanceEvaluator;

import java.util.*;

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
            model.resetLearning(backgroundModel);
        }
    }

    @Override
    public Optional<EnsembleModelWrapper> getLearner(EnsembleModelWrapper model) {
        EnsembleModelWrapper backgroundModel = backgroundModels.get(model.index);
        return backgroundModel != null ? Optional.of(backgroundModel) : Optional.empty();
    }

    @Override
    public void pushLearner(EnsembleModelWrapper model) {
        ARFHoeffdingTree treeCopy = model.getModel();
        treeCopy.resetLearning();

        ClassificationPerformanceEvaluator evaluatorCopy = model.getEvaluator();
        evaluatorCopy.reset();

        Set<Integer> features = forest.featureSelector.getFeatureIndexes();
        EnsembleModelWrapper backgroundModel = new EnsembleModelWrapper(
                model.index,
                treeCopy,
                features,
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
}
