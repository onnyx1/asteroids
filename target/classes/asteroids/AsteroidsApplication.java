package asteroids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


// Created using resources from the University of Helsinki’s Java Mooc
public class AsteroidsApplication extends Application {

    public static int WIDTH = 400;
    public static int HEIGHT = 300;
    public static Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();

        Text text = new Text(10, 25, "0");
        text.setFont(Font.font("Vector Battle", 20));

        text.setStroke(Color.WHITE);

        pane.getChildren().add(text);

        AtomicInteger points = new AtomicInteger();

        pane.setPrefSize(WIDTH, HEIGHT);

        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);

        for (int i = 0; i < 7; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }

        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        Scene scene = new Scene(pane);
        scene.setFill(Color.web("#000000"));

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.SPACE) {
                    KeyCode codeString = event.getCode();
                    if (!pressedKeys.containsKey(codeString)) {
                        pressedKeys.put(codeString, true);
                    }
                } else {
                    pressedKeys.put(event.getCode(), Boolean.TRUE);
                }
            }
        }
        );
        scene.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.SPACE) {
                pressedKeys.remove(event.getCode());

            } else {

                pressedKeys.put(event.getCode(), Boolean.FALSE);

            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                if (removeActiveKey(KeyCode.SPACE) && projectiles.size() < 10) {
                    // we shoot

                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.accelerate();
                    projectile.accelerate();
                    projectile.accelerate();
                    projectile.accelerate();

                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    pane.getChildren().add(projectile.getCharacter());

                }

                if (Math.random() < 0.030) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

                ship.move();

                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            text.setText("" + points.addAndGet(20));
                            asteroid.setAlive(false);
                        }
                    });

                });

                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));

            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }

    private boolean removeActiveKey(KeyCode x) {
        Boolean isActive = pressedKeys.get(x);

        if (isActive != null && isActive) {
            pressedKeys.put(x, false);
            return true;
        } else {
            return false;
        }

    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

}
