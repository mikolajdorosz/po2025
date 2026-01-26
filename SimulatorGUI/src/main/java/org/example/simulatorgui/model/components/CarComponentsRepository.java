package org.example.simulatorgui.model.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarComponentsRepository {
    private static final CarComponentsRepository INSTANCE = new CarComponentsRepository();
    public static CarComponentsRepository getInstance() { return INSTANCE; }

    private final ObservableList<Engine> engines = FXCollections.observableArrayList();
    private final ObservableList<Gearbox> gearboxes = FXCollections.observableArrayList();
    private final ObservableList<Clutch> clutches = FXCollections.observableArrayList();

    public ObservableList<Engine> getEngines() { return engines; }
    public ObservableList<Gearbox> getGearboxes() { return gearboxes; }
    public ObservableList<Clutch> getClutches() { return clutches; }

    public void addEngineIfAbsent(Engine engine) { if (engine != null && !engines.contains(engine)) engines.add(engine); }
    public void addGearboxIfAbsent(Gearbox gearbox) { if (gearbox != null && !gearboxes.contains(gearbox)) gearboxes.add(gearbox); }
    public void addClutchIfAbsent(Clutch clutch) { if (clutch != null && !clutches.contains(clutch)) clutches.add(clutch); }

    public void removeEngine(Engine engine) { engines.remove(engine); }
    public void removeGearbox(Gearbox gearbox) { gearboxes.remove(gearbox); }
    public void removeClutch(Clutch clutch) { clutches.remove(clutch); }

    public boolean isDuplicateEngineName(String name) { return engines.stream().anyMatch(e -> e.getName().equalsIgnoreCase(name)); }
    public boolean isDuplicateGearboxName(String name) { return gearboxes.stream().anyMatch(g -> g.getName().equalsIgnoreCase(name)); }
    public boolean isDuplicateClutchName(String name) { return clutches.stream().anyMatch(c -> c.getName().equalsIgnoreCase(name)); }
}
