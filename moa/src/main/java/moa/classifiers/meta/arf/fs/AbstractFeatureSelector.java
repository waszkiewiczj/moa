package moa.classifiers.meta.arf.fs;

import com.github.javacliparser.FileOption;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.meta.FeatureSelectionAdaptiveRandomForest;
import moa.core.ObjectRepository;
import moa.options.AbstractOptionHandler;
import moa.tasks.TaskMonitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

public abstract class AbstractFeatureSelector extends AbstractOptionHandler
        implements FeatureSelector {

    public FileOption dumpFileOption = new FileOption("dumpFile", 'f',
            "File to append selected features csv to.", null, "csv", true);

    protected FeatureSelectionAdaptiveRandomForest forest;
    protected int numberOfFeatures = -1;

    private int instancesSeen = 0;
    private PrintStream selectedFeaturesOutputStream;
    private boolean wasLogged = false;

    @Override
    public void init(FeatureSelectionAdaptiveRandomForest forest) {
        this.forest = forest;

        File dumpFile = dumpFileOption.getFile();
        if (dumpFile != null) {
            try {
                if (dumpFile.exists()) {
                    selectedFeaturesOutputStream = new PrintStream(
                            new FileOutputStream(dumpFile, true), true);
                } else {
                    selectedFeaturesOutputStream = new PrintStream(
                            new FileOutputStream(dumpFile), true);
                }
            } catch (Exception ex) {
                throw new RuntimeException(
                        "Unable to open immediate result file: " + dumpFile, ex);
            }

            selectedFeaturesOutputStream.println("instancesSeen,selectedFeatures");
        }
    }

    @Override
    public void trainOnInstance(Instance inst) {
        instancesSeen++;
        wasLogged = false;
        if (numberOfFeatures < 0) {
            numberOfFeatures = inst.numInputAttributes();
        }
    }

    @Override
    public Set<Integer> getFeatureIndices() {
        Set<Integer> indices = getFeatureIndicesImpl();

        if (selectedFeaturesOutputStream != null && !wasLogged) {
            selectedFeaturesOutputStream.println(instancesSeen + "," + indices);
            wasLogged = true;
        }

        return indices;
    }

    @Override
    public FeatureSelector copy() {
        return (FeatureSelector) super.copy();
    }

    protected abstract Set<Integer> getFeatureIndicesImpl();

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }
}
