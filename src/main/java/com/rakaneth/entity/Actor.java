package com.rakaneth.entity;

import com.rakaneth.interfaces.Vision;

public class Actor extends Entity implements Vision {

    private double[][] visibleTiles;
    private double vision = 6.0;
    private int nrg = 0;

    //Constructors
    public Actor(char glyph, String name, String desc) {
        super(glyph, name, desc);
    }

    //Getters
    @Override
    public double[][] getVisibleTiles() {
        return visibleTiles;
    }

    @Override
    public double getVision() {
        return vision;
    }

    //Mutators
    public void setVisible(double[][] visiTiles) {
        this.visibleTiles = visiTiles;
    }

    public void setVision(double vision) {
        this.vision = vision;
    }
}
