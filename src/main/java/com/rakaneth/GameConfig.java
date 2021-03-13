package com.rakaneth;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.DiceRoller;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.engine.effect.Buff;
import com.rakaneth.engine.effect.Poison;
import com.rakaneth.map.GameMap;
import com.rakaneth.map.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidmath.Coord;

import java.io.*;

public final class GameConfig {
    private final static Logger logger = LoggerFactory.getLogger(GameConfig.class);
    public final static int GAME_W = 100;
    public final static int GAME_H = 40;
    public final static int MAP_W = 60;
    public final static int MAP_H = 30;
    public final static int MSG_W = 40;
    public final static int MSG_H = 10;
    public final static int SKIL_W = 30;
    public final static int SKIL_H = 10;
    public final static int INFO_W = 30;
    public final static int INFO_H = 10;
    public final static int STAT_W = 30;
    public final static int STAT_H = 30;
    public final static Coord MAP_SCREEN = Coord.get(MAP_W, MAP_H);
    public final static String saveFile = System.getProperty("user.home")
            + File.separatorChar + "Spellweaver" + File.separatorChar + "game.sav";

    private static void connect(GameMap fromMap, GameMap toMap, Character from, Character to) {
        final Coord fromC = fromMap.getRandomFloor();
        final Coord toC = toMap.getRandomFloor();
        fromMap.connect(fromC, toC, toMap.getId());
        fromMap.setTile(fromC, from);
        toMap.setTile(toC, to);
        toMap.connect(toC, fromC, fromMap.getId());
    }

    public static GameState newGame() {
        final var state = new GameState();
        MessageDispatcher.create(state);
        DiceRoller.create(state.gameRNG);
        final var firstFloor = new MapBuilder(50, 50, state)
                .withCarvers(0, 1, 1)
                .withDoorPct(85)
                .withDoubleDoors(true)
                .withId("floor1")
                .withName("Cellar")
                .withLighting(true)
                .withCreaturesOfLevel(0)
                .withMaxCreatures(20)
                .build();

        final var secondFloor = new MapBuilder(50, 50, state)
                .withCarvers(1, 1, 0)
                .withDoorPct(35)
                .withId("floor2")
                .withName("Second Basement")
                .withLighting(true)
                .withCreaturesOfLevel(1)
                .withMaxCreatures(25)
                .withSpellbook(DamageTypes.FORCE)
                .build();

        final var thirdFloor = new MapBuilder(75, 50, state)
                .withCarvers(1, 0, 0)
                .withWaterPct(15)
                .withId("floor3")
                .withName("Caverns")
                .withCreaturesOfLevel(1)
                .withMaxCreatures(30)
                .withSpellbook(DamageTypes.LIGHT)
                .build();

        final var fourthFloor = new MapBuilder(75, 75, state)
                .withCarvers(2, 0, 0)
                .withWaterPct(30)
                .withId("floor4")
                .withName("Deep Caverns")
                .withCreaturesOfLevel(2)
                .withMaxCreatures(35)
                .withSpellbook(DamageTypes.DARK)
                .build();

        final var fifthFloor = new MapBuilder(100, 100, state)
                .withCarvers(1, 2, 2)
                .withWaterPct(40)
                .withDoorPct(50)
                .withId("floor5")
                .withName("Catacombs")
                .withCreaturesOfLevel(2)
                .withMaxCreatures(40)
                .withCreature("greaterShadow")
                .withMacGuffin()
                .build();

        state.addMaps(firstFloor, secondFloor, thirdFloor, fourthFloor, fifthFloor);

        connect(firstFloor, secondFloor, '>', '<');
        connect(secondFloor, thirdFloor, '>', '<');
        connect(thirdFloor, fourthFloor, '>', '<');
        connect(fourthFloor, fifthFloor, '>', '<');

        final var player = state.player;
        player.setMapId("floor1");
        state.setCurMap("floor1");
        final var curMap = state.getCurMap();
        final var vw = curMap.getWidth();
        final var vh = curMap.getHeight();
        player.moveTo(state.getCurMap().getRandomFloor());
        player.setVisible(new double[vw][vh]);
        player.updateFOV(state.getCurMap(), player.getPos());

        /* TODO later: debug saving
        File file = new File(saveFile);

        try {
            boolean dirs = file.getParentFile().mkdirs();
            if (dirs) {
                boolean created = file.createNewFile();
                if (!created) {
                    logger.info("Save file already exists");
                }
            } else {
                logger.info("Directory structure already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        return state;
    }

    /*
    public static void saveGame(GameState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            out.writeObject(state);
            logger.info("Gamestate saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGame() {
        GameState state = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile))) {
            state = (GameState) in.readObject();
            logger.info("Game loaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("Game failed to load");
            e.printStackTrace();
        }
        return state;
    }
     */
}
