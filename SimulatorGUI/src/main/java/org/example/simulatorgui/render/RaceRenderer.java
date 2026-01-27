package org.example.simulatorgui.render;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceRenderer {
    private final Pane raceTrackPane;
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private double scaleX;
    private double scaleY;

    public RaceRenderer(Pane raceTrackPane) { this.raceTrackPane = raceTrackPane; }

    public void renderTrack(Pane referencePane, Position start, Position finish, List<Position> checkpoints, List<Car> cars) {
        scaleX = raceTrackPane.getWidth() / referencePane.getWidth();
        scaleY = raceTrackPane.getHeight() / referencePane.getHeight();
        if (start != null) placeImage(start, "start.png");
        if (finish != null) placeImage(finish, "finish.png");
        for (Position cp : checkpoints) placeImage(cp, "checkpoint.png");
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            car.setCurrentPosition(new Position(start.getX(), start.getY()));
            ImageView view = placeImage( car.getCurrentPosition(), "cars/car" + (i + 1) + ".png");
            carViews.put(car, view);
        }
    }
    private ImageView placeImage(Position pos, String imgPath) {
        ImageView img = new ImageView(
                new Image(getClass().getResource("/org/example/simulatorgui/images/" + imgPath).toExternalForm())
        );
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setLayoutX(pos.getX() * scaleX);
        img.setLayoutY(pos.getY() * scaleY);
        raceTrackPane.getChildren().add(img);
        return img;
    }
    public void renderCars(List<Car> cars) {
        for (Car car : cars) {
            ImageView view = carViews.get(car);
            if (view == null) continue;
            view.setLayoutX(car.getCurrentPosition().getX() * scaleX);
            view.setLayoutY(car.getCurrentPosition().getY() * scaleY);
        }
    }
}
