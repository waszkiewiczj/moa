package moa.classifiers.meta.arf;

import moa.options.OptionHandler;

import java.util.List;

public interface ModelChangeDetector extends OptionHandler {

    void update(EnsembleWrapper ensemble);

    List<EnsembleModelWrapper> getModelsToUpdate(EnsembleWrapper ensemble);

    List<EnsembleModelWrapper> getModelsToPush(EnsembleWrapper ensemble);

    void resetLearning();
}
