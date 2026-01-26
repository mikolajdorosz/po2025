package org.example.simulatorgui.model.race;

import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;

import java.util.List;

public record RaceSetup(
    List<Car> cars,
    Position start,
    Position finish,
    List<Position> checkpoints
) {}
