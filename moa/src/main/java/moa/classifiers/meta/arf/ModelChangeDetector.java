package moa.classifiers.meta.arf;

import moa.options.OptionHandler;

public interface ModelChangeDetector extends OptionHandler {
    void init(int ensembleSize);

    void update(int index, boolean correctlyClassifies);

    boolean shouldBePopped(int index);

    boolean shouldBePushed(int index);

    void resetLearning();
}
