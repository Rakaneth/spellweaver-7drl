package com.rakaneth;

import com.rakaneth.engine.DiceRoller;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.engine.effect.Buff;
import com.rakaneth.engine.effect.Poison;
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

    public static GameState newGame() {
        final var state = new GameState(0xDEADBEEF, 0xDEADBEEF);
        MessageDispatcher.create(state);
        DiceRoller.create(state.gameRNG);
        final var gmap = new MapBuilder(75, 50, state.mapRNG, state.entityFactory, state)
                .withCarvers(0, 2, 1)
                .withWaterPct(5)
                .withDoorPct(15)
                .withDoubleDoors(true)
                .withId("test")
                .withName("Test")
                .withCreaturesOfLevel(2)
                .withMaxCreatures(20)
                .withCreature("greaterShadow")
                .build();
        state.addMaps(gmap);
        final var player = state.player;
        player.setMapId("test");
        state.setCurMap("test");
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
