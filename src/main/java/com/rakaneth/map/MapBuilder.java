package com.rakaneth.map;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.Measurement;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.SerpentMapGenerator;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.IRNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int creatureLevel = 0;
    private int creatureMax = 0;
    private final EntityFactory ef;
    private final static Logger logger = LoggerFactory.getLogger(MapBuilder.class);
    private final GameState state;
    private final List<String> buildIDs = new ArrayList<>();
    private final List<ConnectionInfo> connections = new ArrayList<>();

    private static class ConnectionInfo {
        public final Character toChar;
        public final Character fromChar;
        public final String toMapId;

        public ConnectionInfo(Character toChar, Character fromChar, String toMapId) {
            this.toChar = toChar;
            this.fromChar = fromChar;
            this.toMapId = toMapId;
        }
    }

    public MapBuilder(int width, int height, GameState state) {
        this.rng = state.mapRNG;
        spt = new SerpentMapGenerator(width, height, rng);
        dgn = new DungeonGenerator(width, height, rng);
        this.ef = state.entityFactory;
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

    public MapBuilder withConnection(GameMap otherMap, Character toChar, Character fromChar) {
        connections.add(new ConnectionInfo(toChar, fromChar, otherMap.id));
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
        DungeonUtility.closeDoors(tiles);

        final Map<Character, Double> costs = Map.of(
                '~', 3.0,
                ',', 1.5
        );

        freshMap.lit = lit;
        freshMap.id = id;
        freshMap.name = name;
        freshMap.tiles = tiles;
        freshMap.floors = new GreasedRegion(tiles, '.');
        freshMap.temp = new GreasedRegion(freshMap.floors);
        freshMap.resistances = DungeonUtility.generateSimpleResistances(tiles);
        freshMap.explored = new GreasedRegion(freshMap.getWidth(), freshMap.getHeight());
        freshMap.costs = DungeonUtility.generateCostMap(tiles, costs, 1.0);
        freshMap.dmap = new DijkstraMap(tiles, Measurement.MANHATTAN, rng);
        freshMap.dmap.costMap = freshMap.costs;
        final int mw = freshMap.getWidth();
        final int mh = freshMap.getHeight();

        if (creatureMax > 0) {
            final var numCreatures = rng.nextInt(creatureMax);
            logger.info("Attempting to build {} creatures on map {}", numCreatures, freshMap.name);
            Combatant newCreature;

            for (int i=0; i<numCreatures; i++) {
                newCreature = ef.randomMonster(creatureLevel);
                newCreature.moveTo(freshMap.getRandomFloor());
                newCreature.setMapId(freshMap.id);
                newCreature.setVisible(new double[mw][mh]);
                newCreature.updateFOV(freshMap, newCreature.getPos());
                state.addEntities(newCreature);
                logger.info("Placing new {} on map {} at {}", newCreature.name, freshMap.name, newCreature.getPos());
            }
        }
        Combatant pickedCreature;
        for (String buildID: buildIDs) {
            pickedCreature = ef.monsterFromString(buildID);
            pickedCreature.moveTo(freshMap.getRandomFloor());
            pickedCreature.setMapId(freshMap.id);
            pickedCreature.setVisible(new double[mw][mh]);
            pickedCreature.updateFOV(freshMap, pickedCreature.getPos());
            state.addEntities(pickedCreature);
            logger.info(
                    "Placing chosen creature {} on map {} at {}",
                    pickedCreature.name,
                    freshMap.name,
                    pickedCreature.getPos());
        }

        connections.forEach(ci -> {
            final var other = state.getMap(ci.toMapId);
            final Coord from = freshMap.getRandomFloor();
            final Coord to = other.getRandomFloor();
            freshMap.connect(from, to, ci.toMapId);
            freshMap.setTile(from, ci.fromChar);
            other.connect(to, from, freshMap.id);
            other.setTile(to, ci.toChar);
        });

        return freshMap;
    }


}
