package moa.classifiers.meta.arf;

import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

import java.util.ArrayList;
import java.util.List;

public class NoModelChangeDetector extends AbstractModelChangeDetector {

    @Override
    public String getPurposeString() {
        return "Disables any model changing.";
    }

    @Override
    public void update(EnsembleWrapper ensemble) { }

    @Override
    public List<EnsembleModelWrapper> getModelsToUpdate(EnsembleWrapper ensemble) { return new ArrayList<>(); }

    @Override
    public List<EnsembleModelWrapper> getModelsToPush(EnsembleWrapper ensemble)  { return new ArrayList<>(); }

    @Override
    public void resetLearning() { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
