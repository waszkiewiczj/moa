package moa.classifiers.meta.arf;

import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.ObjectRepository;
import moa.options.ClassOption;
import moa.tasks.TaskMonitor;

public class DriftModelChangeDetector extends AbstractModelChangeDetector{

    public ClassOption driftDetectionMethodOption = new ClassOption("driftDetectionMethod", 'x',
            "Change detector for drifts and its parameters", ChangeDetector.class, "ADWINChangeDetector -a 1.0E-3");

    public ClassOption warningDetectionMethodOption = new ClassOption("warningDetectionMethod", 'p',
            "Change detector for warnings (start training bkg learner)", ChangeDetector.class, "ADWINChangeDetector -a 1.0E-2");

    protected ChangeDetector[] driftDetectors;
    protected ChangeDetector[] warningDetectors;
    protected boolean[] driftChanged;
    protected boolean[] warningChanged;

    @Override
    public String getPurposeString() {
        return "Detects model change as in classic ARF algorithm through drift warning and detection.";
    }

    @Override
    public void init(int ensembleSize) {
        super.init(ensembleSize);

        ChangeDetector baseDriftDetectionMethod = (ChangeDetector) getPreparedClassOption(driftDetectionMethodOption);
        ChangeDetector baseWarningDetectionMethod = (ChangeDetector) getPreparedClassOption(warningDetectionMethodOption);

        driftDetectors = new ChangeDetector[this.ensembleSize];
        warningDetectors = new ChangeDetector[this.ensembleSize];
        driftChanged = new boolean[this.ensembleSize];
        warningChanged = new boolean[this.ensembleSize];

        for (int i = 0; i < this.ensembleSize; i++) {
            driftDetectors[i] = baseDriftDetectionMethod.copy();
            warningDetectors[i] = baseWarningDetectionMethod.copy();
        }
    }

    @Override
    public void update(int index, boolean correctlyClassifies) {
        double inputValue = correctlyClassifies ? 0 : 1;
        driftDetectors[index].input(inputValue);
        warningDetectors[index].input(inputValue);

        driftChanged[index] = driftDetectors[index].getChange();
        warningChanged[index] = warningDetectors[index].getChange();

        if (driftChanged[index]) {
            driftDetectors[index].resetLearning();
            warningDetectors[index].resetLearning();
        }
    }

    @Override
    public boolean shouldBePopped(int index) {
        return driftChanged[index];
    }

    @Override
    public boolean shouldBePushed(int index) {
        return warningChanged[index];
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
