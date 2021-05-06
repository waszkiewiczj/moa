package moa.classifiers.meta.arf;

import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.trees.ARFHoeffdingTree;
import moa.core.DoubleVector;
import moa.core.MiscUtils;
import moa.evaluation.ClassificationPerformanceEvaluator;

import java.util.Random;

public class EnsembleWrapper {

    public final EnsembleModelWrapper[] ensemble;
    public final int ensembleSize;

    private final double lambda;
    private final boolean weightedVote;
    private final Random classifierRandom;

    public EnsembleWrapper(ARFHoeffdingTree baseTree, int ensembleSize, int subspaceSize, double lambda,
                           boolean weightedVote, ClassificationPerformanceEvaluator baseEvaluator,
                           Random classifierRandom) {
        baseTree.resetLearning();
        this.ensembleSize = ensembleSize;
        this.lambda = lambda;
        this.weightedVote = weightedVote;
        this.classifierRandom = classifierRandom;
        this.ensemble = new EnsembleModelWrapper[this.ensembleSize];
        for (int i = 0; i < this.ensembleSize; i++) {
            ARFHoeffdingTree tree = (ARFHoeffdingTree) baseTree.copy();
            tree.subspaceSizeOption.setValue(subspaceSize);
            ensemble[i] = new EnsembleModelWrapper(i, tree, null, (ClassificationPerformanceEvaluator) baseEvaluator.copy());
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

    public void resetLearning() {
        for(EnsembleModelWrapper ensembleModel: ensemble) {
            ensembleModel.resetLearning();
        }
    }

    public void trainOnInstance(Instance inst) {
        for(EnsembleModelWrapper ensembleModel: ensemble) {
            int k = MiscUtils.poisson(lambda, this.classifierRandom);
            if (k > 0) {
                ensembleModel.trainOnInstance(inst, k);
            }
        }
    }
}
