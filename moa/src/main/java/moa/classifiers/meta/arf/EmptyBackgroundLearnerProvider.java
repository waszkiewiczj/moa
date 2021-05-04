package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

import java.util.Optional;

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
    public Optional<EnsembleModelWrapper> getLearner(EnsembleModelWrapper model) { return Optional.empty(); }

    @Override
    public void pushLearner(EnsembleModelWrapper model) { }

    @Override
    public void resetLearning() { }
}
