package com.rakaneth.map;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.IRNG;

import java.util.ArrayList;
import java.util.List;

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
    private int creatureLevel = 0;
    private int creatureMax = 0;
    private final EntityFactory ef;
    private final static Logger logger = LoggerFactory.getLogger(MapBuilder.class);
    private final GameState state;
    private final List<String> buildIDs = new ArrayList<>();

    public MapBuilder(int width, int height, IRNG rng, EntityFactory ef, GameState state) {
        this.rng = rng;
        spt = new SerpentMapGenerator(width, height, rng);
        dgn = new DungeonGenerator(width, height, rng);
        this.ef = ef;
        this.state = state;
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

    public MapBuilder withCreaturesOfLevel(int level){
        creatureLevel = level;
        return this;
    }

    public MapBuilder withMaxCreatures(int creatures) {
        creatureMax = creatures;
        return this;
    }

    public MapBuilder withCreature(String id) {
        buildIDs.add(id);
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
        freshMap.explored = new GreasedRegion(freshMap.getWidth(), freshMap.getHeight());

        if (creatureMax > 0) {
            final var numCreatures = rng.nextInt(creatureMax);
            logger.info("Attempting to build {} creatures on map {}", numCreatures, freshMap.name);
            Combatant newCreature;
            for (int i=0; i<numCreatures; i++) {
                newCreature = ef.randomMonster(creatureLevel);
                newCreature.moveTo(freshMap.getRandomFloor());
                newCreature.setMapId(freshMap.id);
                state.addEntities(newCreature);
                logger.info("Placing new {} on map {} at {}", newCreature.name, freshMap.name, newCreature.getPos());
            }
        }
        Combatant pickedCreature;
        for (String buildID: buildIDs) {
            pickedCreature = ef.monsterFromString(buildID);
            pickedCreature.moveTo(freshMap.getRandomFloor());
            pickedCreature.setMapId(freshMap.id);
            state.addEntities(pickedCreature);
            logger.info(
                    "Placing chosen creature {} on map {} at {}",
                    pickedCreature.name,
                    freshMap.name,
                    pickedCreature.getPos());
        }

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
