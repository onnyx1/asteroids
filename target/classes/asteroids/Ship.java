/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

// Created using resources from the University of Helsinki’s Java Mooc
public class Ship extends Character {

    private boolean isFiring;

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);

        this.getCharacter().setStroke(Color.WHITE);
        isFiring = false;
    }

    public void setFiring(boolean isFiring) {
        this.isFiring = isFiring;
    }

    public boolean getFiring() {
        return isFiring;
    }

    
    
    
}
