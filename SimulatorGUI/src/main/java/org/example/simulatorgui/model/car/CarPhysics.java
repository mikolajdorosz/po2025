package org.example.simulatorgui.model.car;

import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.components.GearboxType;
import org.example.simulatorgui.model.util.Position;

public class CarPhysics {
    private final Car car;

    public CarPhysics(Car car) { this.car = car; }

    public Position update(double deltaTime) {
        car.getEngine().updateRPM(deltaTime, car.getGasPressed());
        if (car.getBrakePressed()) applyBrake(deltaTime);
        else if (car.getGasPressed()) applyAcceleration(deltaTime);
        else car.setSpeedValue(car.getSpeedValue() * (1.0 - 0.15 * deltaTime));
        if (car.getGearbox().getType() == GearboxType.AUTOMATIC) applyGearChange();

        if (car.getCurrentPosition() == null) return car.getStartingPosition();
        car.getCurrentPosition().moveTowards(car.getCurrentTarget(), car.getSpeedValue(), deltaTime);
        return car.getCurrentPosition();
    }
    private void applyBrake(double deltaTime) {
        double brakeForce = 8000 / car.getWeight();
        double deceleration = brakeForce * deltaTime;
        car.setSpeedValue(Math.max(0, car.getSpeedValue() - deceleration));
    }
    private void applyAcceleration(double deltaTime) {
        Gearbox gearbox = car.getGearbox();
        Engine engine = car.getEngine();
        boolean clutchPressed = gearbox.getType() == GearboxType.MANUAL && gearbox.getClutch().getPressed();
        if (gearbox.getCurrentGear() > 0 && (!clutchPressed || gearbox.getType() == GearboxType.AUTOMATIC)) {
            double rpmFactor = Math.max(0.2, engine.getNormalizedRPM());
            double engineForce = 8000 * rpmFactor;
            double acceleration = engineForce / car.getWeight();
            double gearMaxSpeed = computeSpeed();
            car.setSpeedValue(Math.min(car.getSpeedValue() + acceleration * deltaTime, gearMaxSpeed));
        }
    }
    private double computeSpeed() {
        Gearbox gearbox = car.getGearbox();
        if (gearbox.getCurrentGear() <= 0) return 0;
        double gearRatio = (double) gearbox.getCurrentGear() / gearbox.getGearsNumber();
        return car.getMaxSpeed() * gearRatio;
    }
    private void applyGearChange() {
        Engine engine = car.getEngine();
        Gearbox gearbox = car.getGearbox();
        if (engine.getRpm() > 0.8 * engine.getMaxRpm()) {
            gearbox.gearUp();
            engine.setRpm(engine.getRpm()/2);
        } else if (engine.getRpm() < 0.3 * engine.getMaxRpm()) {
            gearbox.gearDown();
            engine.setRpm((int) Math.round(engine.getRpm() * 1.5));
        }
    }
}
