package com.rakaneth.engine;

import com.rakaneth.entity.Entity;

public class GameState {
    private Entity player = new Entity('@', "Player", "The player!");

    public Entity getPlayer() { return player; }
}
