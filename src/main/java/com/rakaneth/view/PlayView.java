package com.rakaneth.view;

import com.rakaneth.GameConfig;
import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.entity.Entity;
import com.valkryst.VTerminal.component.VPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rakaneth.GameConfig.*;
import static com.rakaneth.Swatch.*;

public class PlayView extends GameView {
    private UIUtils.Console msgs;
    private UIUtils.Console skills;
    private UIUtils.Console info;
    private UIUtils.Console stats;

    public PlayView(GameState gameState) {
        super(gameState);
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<Character, Color> colorTiles = Map.of( //TODO: Add doors & stairs
            '#', WALL_BG,
            '.', FLOOR_BG,
            '~', DEEP_BG,
            ',', SHALLOW_BG,
            '+', DOOR_BG,
            '/', DOOR_BG);

    @Override
    void render(VPanel panel) {
        renderMap(panel);
        renderMessages(panel);
        renderAbilities(panel);
        renderStats(panel);
        renderInfo(panel);
        renderEntities(panel);
    }

    @Override
    void handle(KeyEvent key) {
        //TODO: handle keypresses
        final var player = gameState.player;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W -> player.moveDir(Direction.UP);
            case KeyEvent.VK_S -> player.moveDir(Direction.DOWN);
            case KeyEvent.VK_A -> player.moveDir(Direction.LEFT);
            case KeyEvent.VK_D -> player.moveDir(Direction.RIGHT);
            case KeyEvent.VK_L -> gameState = GameConfig.loadGame();
            case KeyEvent.VK_UP -> player.heal(1);
            case KeyEvent.VK_DOWN -> player.takeDamage(1, DamageTypes.PHYSICAL);
            default -> logger.info("Unhandled key: {} ({})", key.getKeyChar(), key.getKeyCode());
        }
        player.updateFOV(gameState.getCurMap(), player.getPos());
    }

    private boolean inView(Coord c) {
        final var screenPos = gameState.getCurMap()
                .mapToScreen(
                        c,
                        gameState.player.getPos(),
                        MAP_SCREEN);
        return screenPos.x >= 0
                && screenPos.y >= 0
                && screenPos.x < GameConfig.MAP_W
                && screenPos.y < MAP_H;
    }

    private Coord centerPos() {
        return gameState.player.getPos();
    }

    //TODO: render functions
    private void renderMap(VPanel panel) {
        final var centerPoint = centerPos();
        final var curMap = gameState.getCurMap();
        final var camPoint = curMap.cam(centerPos(), MAP_SCREEN);
        for (int y = camPoint.y; y < MAP_H + camPoint.y; y++) {
            for (int x = camPoint.x; x < GameConfig.MAP_W + camPoint.x; x++) {
                var t = curMap.getTile(x, y);
                var curPoint = Coord.get(x, y);
                t.ifPresent(tile -> {
                    if (inView(curPoint)) {
                        final var screenPoint = curMap.mapToScreen(curPoint, centerPoint, MAP_SCREEN);
                        Color bg = Color.BLACK;
                        Color fg = Color.WHITE;
                        if (gameState.player.isVisible(curPoint.x, curPoint.y)) {
                            bg = colorTiles.getOrDefault(tile, Color.BLACK);
                        } else if (curMap.isExplored(curPoint)) {
                            bg = EXPLORED_BG;
                            fg = Color.BLACK;
                        } else {
                            return;
                        }
                        panel.setCodePointAt(screenPoint.x, screenPoint.y, tile);
                        panel.setForegroundAt(screenPoint.x, screenPoint.y, fg);
                        panel.setBackgroundAt(screenPoint.x, screenPoint.y, bg);
                    }
                });
            }
        }
    }

    private void renderMessages(VPanel panel) {
        if (msgs == null) {
            msgs = new UIUtils.Console(0, MAP_H, MSG_W, MSG_H, "Messages", panel);
        }
        msgs.border();
        //TODO: rest of messages
    }

    private void renderAbilities(VPanel panel) {
        if (skills == null) {
            skills = new UIUtils.Console(MSG_W, MAP_H, SKIL_W, SKIL_H, "Magic", panel);
        }

        skills.border();
        //TODO: rest of skills
    }

    private void renderStats(VPanel panel) {
        final var player = gameState.player;
        if (stats == null) {
            stats = new UIUtils.Console(MSG_W + SKIL_W, 0, STAT_W, STAT_H, "Stats", panel);
        }

        stats.writeString(1, 1, player.name + " - " + player.desc);
        stats.writeString(1, 2, "HP: " + player.getHp() + "/" + player.getMaxHp());
        stats.writeString(1, 3, "Atk: " + player.getAtk());
        stats.writeString(1, 4, "Dfp: " + player.getDfp());
        stats.writeString(1, 5, "Will: " + player.getWil());

        stats.border();
        //TODO: rest of Stats
    }

    private void renderEntities(VPanel panel) {
        final var m = gameState.getCurMap();
        final var centerPoint = centerPos();
        final var toDraw = gameState.getCurrentEntities().stream()
                .map(Entity::getPos)
                .map(c -> gameState.getEntitiesAt(c))
                .map(l -> l.stream().max(Comparator.comparing(e -> e.zLayer)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (Entity e : toDraw) {
            if (inView(e.getPos())) {
                final var screenPoint = gameState.getCurMap().mapToScreen(e.getPos(), centerPoint, MAP_SCREEN);
                panel.setCodePointAt(screenPoint.x, screenPoint.y, e.glyph);
                panel.setForegroundAt(screenPoint.x, screenPoint.y, Color.WHITE);
                if (e.color != Color.BLACK) {
                    panel.setBackgroundAt(screenPoint.x, screenPoint.y, e.color);
                }
            }
        }
    }

    private void renderInfo(VPanel panel) {
        if (info == null) {
            info = new UIUtils.Console(MSG_W + SKIL_W, MAP_H, INFO_W, INFO_H, "Effects", panel);
        }

        int i = 1;
        for (Effect e : gameState.player.getEffects()) {
            info.writeString(1, i++, e.toString());
        }

        info.border();
    }

    enum DrawMode {NORMAL, TARGETING}
}
