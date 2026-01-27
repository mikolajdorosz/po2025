package org.example.simulatorgui.model.race;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import org.example.simulatorgui.model.components.GearboxType;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.util.Position;

public class RaceConfig {
    private final ObservableList<Car> raceCars;
    private ObservableList<Car> storedCars;
    private final List<Position> checkpointPositions;
    private Position startPosition;
    private Position finishPosition;
    private Pane referenceTrackPane;

    public RaceConfig() {
        this.raceCars = FXCollections.observableArrayList();
        this.storedCars = FXCollections.observableArrayList();
        this.checkpointPositions = new ArrayList<>();

        storedCars.add(new Car("RPR01", "Ford Mustang", 650, 260,
                new Position(0, 0),
                new Engine(6500, "2.3L EcoBoost", 180, 5000),
                new Gearbox(6, GearboxType.MANUAL, "6-Speed Manual", 120, 3000,
                        new Clutch("Standard Clutch", 20, 800))));
        storedCars.add(new Car("RPR02", "Ford F-250", 1000, 180,
                new Position(0, 0),
                new Engine(3200, "Cummins 6.7L TurboDiesel", 350, 12000),
                new Gearbox(6, GearboxType.AUTOMATIC, "TorqShift 6R140", 180, 4000,
                        new Clutch("Heavy Duty Clutch", 25, 1200))));
    }

    public ObservableList<Car> getRaceCars() { return raceCars; }
    public ObservableList<Car> getStoredCars() { return storedCars; }
    public Position getStartPosition() { return startPosition; }
    public void setStartPosition(Position startPosition) { this.startPosition = startPosition; }
    public Position getFinishPosition() { return finishPosition; }
    public void setFinishPosition(Position finishPosition) { this.finishPosition = finishPosition; }
    public List<Position> getCheckpointPositions() { return checkpointPositions; }
    public void addCheckpoint(Position position) { checkpointPositions.add(position); }
    public void clearCheckpoints() { checkpointPositions.clear(); }
    public Pane getReferenceTrackPane() { return referenceTrackPane; }
    public void setReferenceTrackPane(Pane referenceTrackPane) { this.referenceTrackPane = referenceTrackPane; }
}
