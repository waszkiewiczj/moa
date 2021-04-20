package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.MiscUtils;
import moa.evaluation.BasicClassificationPerformanceEvaluator;

import java.util.Random;

public class EnsembleWrapper {

    public final EnsembleModelWrapper[] ensemble;
    public final int ensembleSize;

    protected final ARFHoeffdingTree baseTree;
    protected final int subspaceSize;
    protected final double lambda;
    protected final boolean weightedVote;

    private final Random classifierRandom;

    public EnsembleWrapper(ARFHoeffdingTree baseTree, int ensembleSize, int subspaceSize, double lambda, boolean weightedVote, Random classifierRandom) {
        this.baseTree = baseTree;
        baseTree.resetLearning();
        this.ensembleSize = ensembleSize;
        this.subspaceSize = subspaceSize;
        this.lambda = lambda;
        this.weightedVote = weightedVote;
        this.classifierRandom = classifierRandom;
        this.ensemble = new EnsembleModelWrapper[this.ensembleSize];
        for (int i = 0; i < this.ensembleSize; i++) {
            ensemble[i] = new EnsembleModelWrapper(i, (ARFHoeffdingTree) baseTree.copy(), new BasicClassificationPerformanceEvaluator());
        }
    }

    public double[] getVotesForInstance(Instance inst) {
        Instance testInstance = inst.copy();
        DoubleVector combinedVote = new DoubleVector();

        for(EnsembleModelWrapper ensembleModel: ensemble) {
            DoubleVector vote = new DoubleVector(ensembleModel.getVotesForInstance(testInstance));
            if (vote.sumOfValues() > 0) {
                vote.normalize();
                double acc = ensembleModel.getAccuracy();
                if (weightedVote && acc > 0) {
                    for(int v = 0 ; v < vote.numValues() ; ++v) {
                        vote.setValue(v, vote.getValue(v) * acc);
                    }
                }
                combinedVote.addValues(vote);
            }
        }

        return combinedVote.getArrayRef();
    }

    public void resetLearningImpl() {
        for(EnsembleModelWrapper ensembleModel: ensemble) {
            ensembleModel.resetLearning();
        }
    }

    public void trainOnInstanceImpl(Instance inst) {
        for(EnsembleModelWrapper ensembleModel: ensemble) {
            int k = MiscUtils.poisson(lambda, this.classifierRandom);
            if (k > 0) {
                ensembleModel.trainOnInstance(inst, k);
            }
        }
    }
}
