package moa.classifiers.meta.arf.mcd;

import com.github.javacliparser.IntOption;
import moa.classifiers.meta.arf.EnsembleModelWrapper;
import org.kramerlab.bmad.general.Tuple;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PeriodicModelChangeDetector extends AbstractModelChangeDetector {

    public IntOption pushFrequencyOption = new IntOption("pushFrequency", 'p',
            "Period length between push detection", 1000, 1, Integer.MAX_VALUE);

    public IntOption updateFrequencyOption = new IntOption("updateFrequency", 'u',
            "Period length between update detection", 1000, 1, Integer.MAX_VALUE);

    private final List<Tuple<Integer, PeriodCounter>> periodCounters = new LinkedList<>();

    @Override
    public String getPurposeString() {
        return "Periodically forces ensemble to update learners. " +
                "For each tree in ensemble, after specified amount of instances, " +
                "method marks tree to push new background learner. " +
                "After push tree is marked as to be updated when specified amount of instances occur.";
    }

    @Override
    public void update() {
        if (periodCounters.size() == 0) {
            initPeriodCounters();
        }

        periodCounters.forEach(tuple -> tuple._2.update());
    }

    @Override
    public List<EnsembleModelWrapper> getModelsToUpdate() {
        if (periodCounters.size() == 0) {
            initPeriodCounters();
        }

        Set<Integer> indicesToUpdate = new HashSet<>();
        periodCounters.stream().filter(tuple -> tuple._2.isReadyToUpdate()).forEach(tuple -> indicesToUpdate.add(tuple._1));

        List<EnsembleModelWrapper> modelsToUpdate = new LinkedList<>();
        for (EnsembleModelWrapper model : forest.learner.ensemble) {
            if (indicesToUpdate.contains(model.index)) {
                modelsToUpdate.add(model);
            }
        }

        return modelsToUpdate;
    }

    @Override
    public List<EnsembleModelWrapper> getModelsToPush() {
        if (periodCounters.size() == 0) {
            initPeriodCounters();
        }

        Set<Integer> indicesToPush = new HashSet<>();
        periodCounters.stream().filter(tuple -> tuple._2.isReadyToPush()).forEach(tuple -> indicesToPush.add(tuple._1));

        List<EnsembleModelWrapper> modelsToPush = new LinkedList<>();
        for (EnsembleModelWrapper model : forest.learner.ensemble) {
            if (indicesToPush.contains(model.index)) {
                modelsToPush.add(model);
            }
        }

        return modelsToPush;
    }

    @Override
    public void resetLearning() {
        periodCounters.forEach(tuple -> tuple._2.reset());
    }

    private void initPeriodCounters() {
        int updateFrequency = updateFrequencyOption.getValue();
        int pushFrequency = pushFrequencyOption.getValue();

        for (EnsembleModelWrapper model : forest.learner.ensemble) {
            periodCounters.add(new Tuple<>(model.index, new PeriodCounter(updateFrequency, pushFrequency)));
        }
    }

    private static class PeriodCounter {

        private final int pushPeriodMaxLength;
        private final int updatePeriodMaxLength;
        private int pushPeriodCount = 0;
        private int updatePeriodCount = 0;
        private boolean readyToPush = false;
        private boolean readyToUpdate = false;

        public PeriodCounter(int updatePeriodMaxLength, int pushPeriodMaxLength) {
            this.updatePeriodMaxLength = updatePeriodMaxLength;
            this.pushPeriodMaxLength = pushPeriodMaxLength;
        }

        public void update() {
            if (readyToUpdate) {
                reset();
            }

            pushPeriodCount++;
            readyToPush = (pushPeriodCount == pushPeriodMaxLength);
            if (pushPeriodCount > pushPeriodMaxLength) {
                updatePeriodCount++;
                readyToUpdate = (updatePeriodCount == updatePeriodMaxLength);
            }
        }

        public boolean isReadyToUpdate() {
            return readyToUpdate;
        }

        public boolean isReadyToPush() {
            return readyToPush;
        }

        public void reset() {
            readyToUpdate = readyToPush = false;
            updatePeriodCount = pushPeriodCount = 0;
        }
    }
}
