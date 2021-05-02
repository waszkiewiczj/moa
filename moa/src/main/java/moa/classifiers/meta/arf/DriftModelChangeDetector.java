package moa.classifiers.meta.arf;

import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.ObjectRepository;
import moa.options.ClassOption;
import moa.tasks.TaskMonitor;

import java.util.*;

public class DriftModelChangeDetector extends AbstractModelChangeDetector{

    public ClassOption driftDetectionMethodOption = new ClassOption("driftDetectionMethod", 'x',
            "Change detector for drifts and its parameters", ChangeDetector.class, "ADWINChangeDetector -a 1.0E-3");

    public ClassOption warningDetectionMethodOption = new ClassOption("warningDetectionMethod", 'p',
            "Change detector for warnings (start training bkg learner)", ChangeDetector.class, "ADWINChangeDetector -a 1.0E-2");

    protected Dictionary<Integer, DriftModelObserver> observers;

    @Override
    public String getPurposeString() {
        return "Detects model change as in classic ARF algorithm through drift warning and detection.";
    }


    @Override
    public void update() {
        EnsembleWrapper ensemble = this.forest.learner;
        if (observers == null) {
            initDetectors(ensemble);
        }

        for(EnsembleModelWrapper ensembleModel: ensemble.ensemble) {
            observers.get(ensembleModel.index).update(ensembleModel);
        }
    }

    @Override
    public List<EnsembleModelWrapper> getModelsToUpdate() {
        EnsembleWrapper ensemble = this.forest.learner;

        if (observers == null) {
            initDetectors(ensemble);
        }

        ArrayList<EnsembleModelWrapper> modelsToUpdate = new ArrayList<>();

        for(EnsembleModelWrapper ensembleModel: ensemble.ensemble) {
            boolean update = observers.get(ensembleModel.index).driftOccured();
            if (update) {
                modelsToUpdate.add(ensembleModel);
            }
        }

        return modelsToUpdate;
    }

    @Override
    public List<EnsembleModelWrapper> getModelsToPush()  {
        EnsembleWrapper ensemble = this.forest.learner;

        if (observers == null) {
            initDetectors(ensemble);
        }

        ArrayList<EnsembleModelWrapper> modelsToPush = new ArrayList<>();

        for(EnsembleModelWrapper ensembleModel: ensemble.ensemble) {
            boolean update = observers.get(ensembleModel.index).warningOccured();
            if (update) {
                modelsToPush.add(ensembleModel);
            }
        }

        return modelsToPush;
    }

    @Override
    public void resetLearning() {
        if (observers != null) {
            Enumeration<DriftModelObserver> observersEnumeration = observers.elements();
            while(observersEnumeration.hasMoreElements()) {
                observersEnumeration.nextElement().resetLearning();
            }
        }
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) { }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) { }

    protected void initDetectors(EnsembleWrapper ensemble) {
        int size = ensemble.ensembleSize;
        observers = new Hashtable<>(size);

        ChangeDetector driftDetectionMethod = (ChangeDetector) getPreparedClassOption(driftDetectionMethodOption);
        ChangeDetector warningDetectionMethod = (ChangeDetector) getPreparedClassOption(warningDetectionMethodOption);

        for(EnsembleModelWrapper ensembleModel: ensemble.ensemble) {
            DriftModelObserver observer = new DriftModelObserver(driftDetectionMethod.copy(), warningDetectionMethod.copy());
            observers.put(ensembleModel.index, observer);
        }

    }
}

class DriftModelObserver {

    private final ChangeDetector driftDetector;
    private final ChangeDetector warningDetector;
    private boolean driftOccured = false;
    private boolean warningOccured = false;

    public DriftModelObserver(ChangeDetector driftDetector, ChangeDetector warningDetector) {
        this.driftDetector = driftDetector;
        this.warningDetector = warningDetector;
    }

    public void update(EnsembleModelWrapper model) {
        double input = model.correctlyClassifies ? 0 : 1;

        driftDetector.input(input);
        warningDetector.input(input);

        driftOccured = driftDetector.getChange();
        warningOccured = warningDetector.getChange();

        if(driftOccured) {
            driftDetector.resetLearning();
            warningDetector.resetLearning();
        }
    }

    public boolean driftOccured() { return driftOccured; }

    public boolean warningOccured() { return warningOccured; }

    public void resetLearning() {
        driftDetector.resetLearning();
        warningDetector.resetLearning();
    }
}