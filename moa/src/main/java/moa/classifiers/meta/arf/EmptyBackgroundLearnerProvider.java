package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

public class EmptyBackgroundLearnerProvider extends AbstractBackgroundLearnerProvider {

    @Override
    public String getPurposeString() {
        return "Empty provider that never modifies any model";
    }

    @Override
    public void trainOnInstance(Instance inst) { }

    @Override
    public void updateLearner(EnsembleModelWrapper model) { }

    @Override
    public void pushLearner(EnsembleModelWrapper model) { }

    @Override
    public void resetLearning() { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
