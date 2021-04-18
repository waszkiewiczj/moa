package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.options.OptionHandler;

import java.util.Set;


public interface FeatureSelector extends OptionHandler {
    void trainOnInstance(Instance inst);

    Set<Integer> getFeatureIndexes();

    void resetLearning();
}
