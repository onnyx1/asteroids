
package asteroids;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


// Created using resources from the University of Helsinki’s Java Mooc
public class Projectile extends Character {

    

    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        this.getCharacter().setStroke(Color.WHITE);
        startSelfDestruct(2000);

    }
    
    private void startSelfDestruct(long time) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            setAlive(false);
        });

        new Thread(task).start();
    }


}
