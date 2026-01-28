package org.example.simulatorgui.repo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.components.GearboxType;

public class CarComponentsRepository {
    private static final CarComponentsRepository INSTANCE = new CarComponentsRepository();
    public static CarComponentsRepository getInstance() { return INSTANCE; }

    private final ObservableList<Engine> engines = FXCollections.observableArrayList(
        new Engine(6000, "Engine A", 120.0, 1500.0),
        new Engine(7000, "Engine B", 130.0, 2000.0)
    );
    private final ObservableList<Clutch> clutches = FXCollections.observableArrayList(
        new Clutch("Standard Clutch", 35, 2000),
        new Clutch("Performance Clutch", 45, 3500)
    );
    private final ObservableList<Gearbox> gearboxes = FXCollections.observableArrayList(
        new Gearbox(6, GearboxType.MANUAL, "Manual 6-Speed", 85.0, 2500.0, clutches.getFirst()),
        new Gearbox(8, GearboxType.AUTOMATIC, "Automatic 8-Speed", 95.0, 3000.0)
    );

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
