package com.rakaneth;

import com.rakaneth.engine.GameState;
import com.rakaneth.map.MapBuilder;
import squidpony.squidmath.Coord;

public final class GameConfig {
    public final static int GAME_W = 100;
    public final static int GAME_H = 40;
    public final static int MAP_W = 60;
    public final static int MAP_H = 30;
    public final static Coord MAP_SCREEN = Coord.get(MAP_W, MAP_H);

    public static GameState newGame() {
        final var state = new GameState(0xDEADBEEF, 0xDEADBEEF);
        final var gmap = new MapBuilder(75, 50, state.getMapRNG())
                .withCarvers(2, 0, 0)
                .withWaterPct(25)
                .withDoorPct(15)
                .withDoubleDoors(true)
                .withId("test")
                .withName("Test")
                .build();
        state.addMaps(gmap);
        state.setCurMap("test");
        state.getPlayer().moveTo(state.getCurMap().getRandomFloor());
        state.getPlayer().setMapId("test");
        return state;
    }
}
