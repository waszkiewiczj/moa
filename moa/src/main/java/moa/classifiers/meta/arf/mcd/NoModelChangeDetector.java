package moa.classifiers.meta.arf.mcd;

import moa.classifiers.meta.arf.EnsembleModelWrapper;
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
    public void update() { }

    @Override
    public List<EnsembleModelWrapper> getModelsToUpdate() { return new ArrayList<>(); }

    @Override
    public List<EnsembleModelWrapper> getModelsToPush()  { return new ArrayList<>(); }

    @Override
    public void resetLearning() { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
