package moa.classifiers.meta.arf;

import moa.core.ObjectRepository;
import moa.tasks.TaskMonitor;

public class NoModelChangeDetector extends AbstractModelChangeDetector {

    @Override
    public String getPurposeString() {
        return "Disables any model changing.";
    }

    @Override
    public void update(int index, boolean correctlyClassifies) { }

    @Override
    public boolean shouldBePopped(int index) { return false; }

    @Override
    public boolean shouldBePushed(int index) { return false; }

    @Override
    public void resetLearning() { }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }
}
