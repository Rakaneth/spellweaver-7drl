package com.rakaneth.engine;

import com.rakaneth.entity.Entity;
import squidpony.squidmath.GWTRNG;
import squidpony.squidmath.IRNG;

public class GameState {
    private Entity player = new Entity('@', "Player", "The player!");
    private IRNG mapRNG;
    private IRNG gameRNG;

    //must seed both or none
    public GameState(long mapSeed, long gameSeed) {
        mapRNG = new GWTRNG(mapSeed);
        gameRNG = new GWTRNG(gameSeed);
    }

    public GameState() {
        mapRNG = new GWTRNG();
        gameRNG = new GWTRNG();
    }

    public Entity getPlayer() { return player; }
    public IRNG getGameRNG() { return gameRNG;}
    public IRNG getMapRNG() { return mapRNG; }
}
