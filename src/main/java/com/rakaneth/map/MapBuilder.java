package com.rakaneth.map;

import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SerpentDeepMapGenerator;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.IRNG;

public class MapBuilder {
    private boolean lit = false;
    private char[][] tiles;
    private String id = "NOID";
    private String name = "NONAME";
    private final SerpentMapGenerator spt;
    private final DungeonGenerator dgn;
    private final IRNG rng;
    private int caveCarvers = 1;
    private int boxCarvers = 0;
    private int roundRoomCarvers = 0;
    private int doorPct = 0;
    private boolean doubleDoors = false;
    private int waterPct = 0;
    private boolean upStairs = false;
    private boolean downStairs = false;

    public MapBuilder(int width, int height, IRNG rng) {
        this.rng = rng;
        spt = new SerpentMapGenerator(width, height, rng);
        dgn = new DungeonGenerator(width, height, rng);
    }

    public MapBuilder withCarvers(int cave, int box, int roundRoom) {
        caveCarvers = cave;
        boxCarvers = box;
        roundRoomCarvers = roundRoom;
        return this;
    }

    public MapBuilder withDoorPct(int doorPct) {
        this.doorPct = doorPct;
        return this;
    }

    public MapBuilder withDoubleDoors(boolean doubleDoors) {
        this.doubleDoors = doubleDoors;
        return this;
    }

    public MapBuilder withWaterPct(int waterPct) {
        this.waterPct = waterPct;
        return this;
    }

    public MapBuilder withLighting(boolean lighting) {
        this.lit = lighting;
        return this;
    }

    public MapBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MapBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public MapBuilder withStairs(boolean up, boolean down) {
        this.upStairs = up;
        this.downStairs = down;
        return this;
    }

    public GameMap build() {
        GameMap freshMap = new GameMap(rng);

        spt.putCaveCarvers(caveCarvers);
        spt.putBoxRoomCarvers(boxCarvers);
        spt.putRoundRoomCarvers(roundRoomCarvers);

        final char[][] baseDungeon = spt.generate();

        if (doorPct > 0) {
            dgn.addDoors(doorPct, doubleDoors);
        }

        if (waterPct > 0) {
            dgn.addWater(waterPct);
        }

        final char[][] tiles = dgn.generate(baseDungeon);

        freshMap.lit = lit;
        freshMap.id = id;
        freshMap.name = name;
        freshMap.tiles = tiles;
        freshMap.floors = new GreasedRegion(tiles, '.');
        freshMap.temp = new GreasedRegion(freshMap.floors);
        freshMap.resistances = DungeonUtility.generateResistances(tiles);

        if (upStairs) {
            final var upC = freshMap.getRandomFloor();
            freshMap.setTile(upC, '<');
        }

        if (downStairs) {
            final var downC = freshMap.getRandomFloor();
            freshMap.setTile(downC, '>');
        }

        return freshMap;
    }


}
