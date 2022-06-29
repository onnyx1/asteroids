
package asteroids;

import java.util.Random;
// Created using resources from the University of Helsinki’s Java Mooc
public class Asteroid extends Character {

    private double rotationalMovement;

    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        //change it to 30 from 1
        int accelerationAmount = 10 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            super.accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();

    }

    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }

}
