package com.rakaneth.view;

import com.rakaneth.GameConfig;
import com.rakaneth.engine.GameState;
import com.valkryst.VTerminal.component.VPanel;

import java.awt.event.KeyEvent;

import static com.rakaneth.view.UIUtils.writeCenter;

public class LoseView extends GameView {
    public LoseView(GameState gameState) { super(gameState); }

    @Override
    void render(VPanel panel) {
        writeCenter(18, "Unfortunately, you have failed your master.", panel);
        writeCenter(19, "A spell he placed upon you whisks you from the jaws of death,", panel);
        writeCenter(
                20,
                "but since you failed his task, he has sent you away, keeping the secrets of Spellweaving for himself.",
                panel);
        writeCenter(21, "Thanks for playing Spellweaver!", panel);
    }

    @Override
    boolean handle(KeyEvent key) {
        System.exit(0);
        return false;
    }
}
