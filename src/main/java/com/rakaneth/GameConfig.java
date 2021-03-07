package com.rakaneth;

import com.rakaneth.engine.GameState;
import com.rakaneth.map.MapBuilder;
import squidpony.squidgrid.FOV;
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
                .withCarvers(0, 2, 1)
                .withWaterPct(5)
                .withDoorPct(15)
                .withDoubleDoors(true)
                .withId("test")
                .withName("Test")
                .build();
        state.addMaps(gmap);
        state.setCurMap("test");
        final var curMap = state.getCurMap();
        final var vw = curMap.getWidth();
        final var vh = curMap.getHeight();
        final var player = state.getPlayer();
        final var pos = player.getPos();
        player.moveTo(state.getCurMap().getRandomFloor());
        player.setMapId("test");
        player.setVisible(new double[vw][vh]);
        player.updateFOV(state.getCurMap(), player.getPos());
        return state;
    }
}
