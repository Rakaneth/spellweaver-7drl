package com.rakaneth.view;

import com.rakaneth.GameConfig;
import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Entity;
import com.valkryst.VTerminal.component.VPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rakaneth.GameConfig.MAP_SCREEN;
import static com.rakaneth.Swatch.*;

public class PlayView extends GameView{
    public PlayView(GameState gameState) {
        super(gameState);
    }
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<Character, Color> colorTiles = Map.of( //TODO: Add doors & stairs
            '#', WALL_BG,
            '.', FLOOR_BG,
            '~', DEEP_BG,
            ',' ,SHALLOW_BG);
    @Override
    void render(VPanel panel) {
        renderMap(panel);
        renderMessages(panel);
        renderAbilities(panel);
        renderStats(panel);
        renderEntities(panel);
    }

    @Override
    void handle(KeyEvent key) {
        //TODO: handle keypresses
        final var player = gameState.getPlayer();
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W -> player.moveDir(Direction.UP);
            case KeyEvent.VK_S -> player.moveDir(Direction.DOWN);
            case KeyEvent.VK_A -> player.moveDir(Direction.LEFT);
            case KeyEvent.VK_D -> player.moveDir(Direction.RIGHT);
            default -> logger.info("Unhandled key: {} ({})", key.getKeyChar(), key.getKeyCode());
        }
    }

    private boolean inView(Coord c) {
        final var screenPos = gameState.getCurMap()
                .mapToScreen(
                    c,
                    gameState.getPlayer().getPos(),
                    MAP_SCREEN);
        return screenPos.x >= 0
                && screenPos.y >= 0
                && screenPos.x < GameConfig.MAP_W
                && screenPos.y < GameConfig.MAP_H;
    }

    private Coord centerPos() { return gameState.getPlayer().getPos(); }

    //TODO: render functions
    private void renderMap(VPanel panel) {
        final var centerPoint = centerPos();
        final var curMap = gameState.getCurMap();
        final var camPoint = curMap.cam(centerPos(), MAP_SCREEN);
        for (int y=camPoint.y; y<GameConfig.MAP_H + camPoint.y; y++) {
            for (int x=camPoint.x; x<GameConfig.MAP_W + camPoint.x; x++) {
                var t = curMap.getTile(x, y);
                var curPoint = Coord.get(x, y);
                t.ifPresent(tile -> {
                    if (inView(curPoint)) {
                        final var screenPoint = curMap.mapToScreen(curPoint, centerPoint, MAP_SCREEN);
                        final var color = colorTiles.getOrDefault(tile, Color.BLACK);
                        panel.setCodePointAt(screenPoint.x, screenPoint.y, tile);
                        panel.setForegroundAt(screenPoint.x, screenPoint.y, Color.WHITE);
                        panel.setBackgroundAt(screenPoint.x, screenPoint.y, color);
                    }
                });
            }
        }
    }
    private void renderMessages(VPanel panel){}
    private void renderAbilities(VPanel panel){}
    private void renderStats(VPanel panel){}
    private void renderEntities(VPanel panel){
        final var m = gameState.getCurMap();
        final var centerPoint = centerPos();
        final var toDraw = gameState.getCurrentEntities().stream()
                .map(Entity::getPos)
                .map(c -> gameState.getEntitiesAt(c))
                .map(l -> l.stream().max(Comparator.comparing(e -> e.zLayer)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (Entity e: toDraw) {
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
}
