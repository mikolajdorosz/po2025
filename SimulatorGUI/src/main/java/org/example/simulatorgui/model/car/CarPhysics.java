package org.example.simulatorgui.model.car;

import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.components.GearboxType;
import org.example.simulatorgui.model.util.Utils;

public class CarPhysics {
    private final Car car;
    private boolean aiChangingGear = false;
    private int targetGear = 0;
    private double clutchTimer = 0;
    private double shiftCooldown = 0;
    private final double minTimeBetweenShifts = 0.5;

    public CarPhysics(Car car) { this.car = car; }

    public void update(double deltaTime) {
        if (!car.getPlayerControlled()) {
            double distToTarget = Utils.distance(car.getCurrentPosition(), car.getCurrentTarget());
            car.setGasPressed(distToTarget > 1 && Math.random() > 0.1);
            car.setBrakePressed(Math.random() < 0.05);
        }
        car.getEngine().updateRPM(deltaTime, car.getGasPressed());
        if (!car.getPlayerControlled() && car.getGearbox().getType() == GearboxType.MANUAL) aiManualGearChange(deltaTime);
        if (car.getBrakePressed()) applyBrake(deltaTime);
        else if (car.getGasPressed()) applyAcceleration(deltaTime);
        else car.setSpeedValue(car.getSpeedValue() * (1.0 - 0.15 * deltaTime));
        if (car.getGearbox().getType() == GearboxType.AUTOMATIC) applyGearChange();
        car.getCurrentPosition().moveTowards(car.getCurrentTarget(), car.getSpeedValue(), deltaTime);
        syncRPMWithSpeed();
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
        if (engine.getRpm() > 0.8 * engine.getMaxRpm()) gearbox.gearUp();
        else if (engine.getRpm() < 0.3 * engine.getMaxRpm()) gearbox.gearDown();
    }
    private void syncRPMWithSpeed() {
        Gearbox gearbox = car.getGearbox();
        Engine engine = car.getEngine();
        if (gearbox.getCurrentGear() <= 0) return;
        boolean clutchPressed = gearbox.getType() == GearboxType.MANUAL && gearbox.getClutch().getPressed();
        if (clutchPressed) return;
        double rpm = (car.getSpeedValue() / car.getMaxSpeed()) * gearbox.getGearsNumber() / gearbox.getCurrentGear() * engine.getMaxRpm();
        engine.setRpm((int) rpm);
    }
    private void aiManualGearChange(double deltaTime) {
        if (shiftCooldown > 0) { shiftCooldown -= deltaTime; return; }
        if (!aiChangingGear) {
            double rpm = car.getEngine().getNormalizedRPM();
            if (rpm > 0.8) {
                targetGear = Math.min(car.getGearbox().getCurrentGear() + 1, car.getGearbox().getGearsNumber());
                aiChangingGear = true;
                clutchTimer = 0;
            } else if (rpm < 0.3) {
                targetGear = Math.max(1, car.getGearbox().getCurrentGear() - 1);
                aiChangingGear = true;
                clutchTimer = 0;
            }
        } else {
            Clutch clutch = car.getGearbox().getClutch();
            if (!clutch.getPressed()) clutch.press();
            clutchTimer += deltaTime;
            if (clutchTimer >= 0.3) {
                car.getGearbox().setCurrentGear(targetGear);
                clutch.release();
                aiChangingGear = false;
                shiftCooldown = minTimeBetweenShifts;
            }
        }
    }
}
