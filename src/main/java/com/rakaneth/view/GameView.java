package com.rakaneth.view;

import com.rakaneth.engine.GameState;
import com.valkryst.VTerminal.component.VPanel;
import java.awt.event.KeyEvent;

public abstract class GameView {
    protected GameState gameState;
    public GameView(GameState gameState) {
        this.gameState = gameState;
    }

    abstract void render(VPanel panel);
    abstract void handle(KeyEvent key);
}
