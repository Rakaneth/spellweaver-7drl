package com.rakaneth.view;

import com.rakaneth.GameConfig;
import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.action.GameAction;
import com.rakaneth.engine.action.MoveAction;
import com.rakaneth.engine.action.NoAction;
import com.rakaneth.engine.effect.Armor;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.engine.effect.Poison;
import com.rakaneth.engine.effect.Weapon;
import com.rakaneth.entity.Entity;
import com.valkryst.VTerminal.component.VPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.StringKit;
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
        final var moveUp =  player.getPos().translate(Direction.UP);
        final var moveDown = player.getPos().translate(Direction.DOWN);
        final var moveLeft = player.getPos().translate(Direction.LEFT);
        final var moveRight = player.getPos().translate(Direction.RIGHT);
        final GameAction action = switch (key.getKeyCode()) {
            case KeyEvent.VK_W -> new MoveAction(player, moveUp);
            case KeyEvent.VK_S -> new MoveAction(player, moveDown);
            case KeyEvent.VK_A -> new MoveAction(player, moveLeft);
            case KeyEvent.VK_D -> new MoveAction(player, moveRight);
            default -> {
                logger.info("Unhandled key: {} ({})", key.getKeyChar(), key.getKeyCode());
                yield new NoAction(player);
            }
        };
        final int cost = action.doAction(gameState) + 100;
        player.changeNrg(-cost);
        logger.info(
                "Turn: {} Actor {} acted, spending {}, has {} energy",
                gameState.getGameTurn(),
                player.name,
                cost,
                player.getNrg());
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

        int i = 1;
        java.util.List<String> wrapt;
        String nextMsg;
        int wsz;
        final var iterator = gameState.messages.listIterator(gameState.messages.size());
        while (iterator.hasPrevious()) {
            nextMsg = iterator.previous();
            wrapt = StringKit.wrap(nextMsg, msgs.width-2);
            wsz = wrapt.size();
            if (i + wsz > 9) break;
            for (int w=0; w<wsz; w++) {
                msgs.writeString(1, i + w, wrapt.get(w));
            }
            i += wsz;
        }
        msgs.border();
        //DONE: rest of messages
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

        stats.writeString(1, 1, player.name);
        stats.writeString(1, 2, player.desc);
        stats.writeString(1, 3, gameState.getCurMap().getName() + " " + player.getPos());
        stats.writeString(1, 4, "HP: " + player.getHp() + "/" + player.getMaxHp());
        stats.writeString(1, 5, "Atk: " + player.getAtk());
        stats.writeString(1, 6, "Dfp: " + player.getDfp());
        stats.writeString(1, 7, "Spd: " + player.getSpd());
        stats.writeString(1, 8, "Will: " + player.getWil());


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
            if (inView(e.getPos()) && gameState.player.canSee(e)) {
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
