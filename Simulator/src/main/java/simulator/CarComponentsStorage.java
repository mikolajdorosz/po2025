package simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarComponentsStorage {
    private static final ObservableList<Engine> engines = FXCollections.observableArrayList();
    private static final ObservableList<Gearbox> gearboxes = FXCollections.observableArrayList();
    private static final ObservableList<Clutch> clutches = FXCollections.observableArrayList();

    public static ObservableList<Engine> getEngines() { return engines; }
    public static ObservableList<Gearbox> getGearboxes() { return gearboxes; }
    public static ObservableList<Clutch> getClutches() { return clutches; }

    public static void addEngine(Engine engine) { engines.add(engine); }
    public static void addGearbox(Gearbox gearbox) { gearboxes.add(gearbox); }
    public static void addClutch(Clutch clutch) { clutches.add(clutch); }

    public static void removeEngine(Engine engine) { engines.remove(engine); }
    public static void removeGearbox(Gearbox gearbox) { gearboxes.remove(gearbox); }
    public static void removeClutch(Clutch clutch) { clutches.remove(clutch); }
}
