package org.example.simulatorgui.config;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;

public class RaceConfig {
    private final ObservableList<Car> raceCars;
    private final ObservableList<Car> storedCars;
    private final List<Position> checkpointPositions;
    private Position startPosition;
    private Position finishPosition;
    private Pane referenceTrackPane;

    public RaceConfig(
            ObservableList<Car> raceCars,
            ObservableList<Car> storedCars,
            Position startPosition,
            Position finishPosition,
            List<Position> checkpointPositions,
            Pane referenceTrackPane
    ) {
        this.raceCars = raceCars;
        this.storedCars = storedCars;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
        this.checkpointPositions = checkpointPositions;
        this.referenceTrackPane = referenceTrackPane;
    }
    public RaceConfig() {
        this.raceCars = FXCollections.observableArrayList();
        this.storedCars = FXCollections.observableArrayList();
        this.checkpointPositions = new ArrayList<>();
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
