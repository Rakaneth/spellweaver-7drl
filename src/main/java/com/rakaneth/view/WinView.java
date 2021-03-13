package com.rakaneth.view;

import com.rakaneth.engine.GameState;
import com.valkryst.VTerminal.component.VPanel;

import java.awt.event.KeyEvent;

import static com.rakaneth.view.UIUtils.writeCenter;

public class WinView extends GameView{
    public WinView(GameState gameState) { super(gameState); }

    @Override
    void render(VPanel panel) {
        writeCenter(18, "Once you put your hands on your master's spellbook, you return to his sanctum.", panel);
        writeCenter(19, "He congratulates you on a job well done, leaving the spellbook to you as a reward.", panel);
        writeCenter(20, "Thanks for playing Spellweaver!", panel);
    }

    @Override
    boolean handle(KeyEvent key) {
        System.exit(0);
        return false;
    }
}
