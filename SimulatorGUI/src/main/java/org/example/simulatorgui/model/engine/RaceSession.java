package org.example.simulatorgui.model.engine;

import javafx.animation.AnimationTimer;
import org.example.simulatorgui.model.race.RaceSetup;

public class RaceSession {
    private final RaceEngine raceEngine = new RaceEngine();
    private AnimationTimer gameLoop;

    public RaceEngine getRaceEngine() { return raceEngine; }

    public void start(RaceSetup setup, Runnable onUpdate, Runnable onFinish) {
        raceEngine.createRace(setup);
        raceEngine.start();
        gameLoop = new AnimationTimer() {
            @Override public void handle(long now) {
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
}
