package org.example.simulatorgui.model.race;

import javafx.animation.AnimationTimer;
import org.example.simulatorgui.model.car.Car;

import java.util.List;

public class RaceSession {
    private final RaceEngine raceEngine = new RaceEngine();
    private AnimationTimer gameLoop;

    public void start(
            List<Car> cars,
            Runnable onUpdate,
            Runnable onFinish
    ) {
        raceEngine.start();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                raceEngine.update();
                onUpdate.run();
                if (raceEngine.getRace().ended()) { stop(); onFinish.run(); }
            }
        };
        gameLoop.start();
    }
    public void stop() {
        if (gameLoop != null) gameLoop.stop();
        raceEngine.stop();
    }
    public RaceEngine getRaceEngine() { return raceEngine; }
}
