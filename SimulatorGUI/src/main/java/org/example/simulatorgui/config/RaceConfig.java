package org.example.simulatorgui.config;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import simulator.Car;
import simulator.Position;

public class RaceConfig {
    private final ObservableList<Car> raceCars;
    private final ObservableList<Car> storedCars;
    private final Position startPosition;
    private final Position finishPosition;
    private final List<Position> checkpointPositions;
    private final Pane referenceTrackPane;

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

    public ObservableList<Car> getRaceCars() { return raceCars; }
    public ObservableList<Car> getStoredCars() { return storedCars; }
    public Position getStartPosition() { return startPosition; }
    public Position getFinishPosition() { return finishPosition; }
    public List<Position> getCheckpointPositions() { return checkpointPositions; }
    public Pane getReferenceTrackPane() { return referenceTrackPane; }
}
