package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.trees.ARFHoeffdingTree;

public class EnsembleWrapper {

    public final EnsembleModelWrapper[] ensemble;
    public final int ensembleSize;

    protected final ARFHoeffdingTree baseTree;
    protected final int subspaceSize;
    protected final double lambda;

    public EnsembleWrapper(ARFHoeffdingTree baseTree, int ensembleSize, int subspaceSize, double lambda) {
        this.baseTree = baseTree;
        baseTree.resetLearning();
        this.ensembleSize = ensembleSize;
        this.subspaceSize = subspaceSize;
        this.lambda = lambda;
        this.ensemble = new EnsembleModelWrapper[this.ensembleSize];
    }

    public double[] getVotesForInstance(Instance inst) {
        Instance testInstance = inst.copy();
        return new double[0];
    }

    public void resetLearning() {

    }

    public void trainOnInstanceImpl(Instance inst) {

    }
}
