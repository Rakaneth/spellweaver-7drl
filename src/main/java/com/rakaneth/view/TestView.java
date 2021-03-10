package com.rakaneth.view;

import com.rakaneth.engine.GameState;
import com.valkryst.VTerminal.component.VPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidgrid.Direction;

import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class TestView extends GameView {
    private final Logger logger = LoggerFactory.getLogger(TestView.class);

    public TestView(GameState gameState) {
        super(gameState);
    }

    @Override
    public void render(VPanel panel) {
        final var playerPos = gameState.player.getPos();
        final var playerGlyph = gameState.player.glyph;
        panel.setCodePointAt(playerPos.x, playerPos.y, playerGlyph);
    }

    @Override
    public void handle(KeyEvent key) {
        final var player = gameState.player;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W -> player.moveDir(Direction.UP);
            case KeyEvent.VK_S -> player.moveDir(Direction.DOWN);
            case KeyEvent.VK_A -> player.moveDir(Direction.LEFT);
            case KeyEvent.VK_D -> player.moveDir(Direction.RIGHT);
            default -> logger.info("Unhandled key: {} ({})", key.getKeyChar(), key.getKeyCode());
        }
    }

    private int[][] randomCodePoints(VPanel panel) {
        int h = panel.getHeightInTiles();
        int w = panel.getWidthInTiles();
        int[][] result = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                result[y][x] = ThreadLocalRandom.current().nextInt(33, 126);
            }
        }
        return result;
    }
}
