package com.rakaneth.view;

import com.rakaneth.engine.GameState;
import com.valkryst.VTerminal.component.VPanel;

import java.awt.event.KeyEvent;

public class PlayView extends GameView{
    public PlayView(GameState gameState) {
        super(gameState);
    }
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
    }

    //TODO: render functions
    private void renderMap(VPanel panel) {}
    private void renderMessages(VPanel panel){}
    private void renderAbilities(VPanel panel){}
    private void renderStats(VPanel panel){}
    private void renderEntities(VPanel panel){}
}
