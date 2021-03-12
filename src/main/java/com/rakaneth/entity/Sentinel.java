package com.rakaneth.entity;

import com.rakaneth.engine.AI;

import java.awt.*;

public class Sentinel extends Actor {

    protected AI ai = AI.SENTINEL;
    public Sentinel() {
        super('\0', "Sentinel", "Round Tracker", Color.BLACK);
    }

    @Override
    public int getSpd() {
        return 10;
    }

    @Override
    public AI getAI() {
        return AI.SENTINEL;
    }
}
