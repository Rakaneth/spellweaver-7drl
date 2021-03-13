package com.rakaneth.entity;

import com.rakaneth.Swatch;
import com.rakaneth.engine.GameState;
import com.rakaneth.view.UIStack;
import com.rakaneth.view.WinView;

public class MacGuffin extends PickupItem {
    public MacGuffin() {
        super('?',
                "Mastery of Spellweaving",
                "The spellbook your master sent you after,",
                Swatch.LIGHT_FG);
    }
    @Override
    public void onPickup(Combatant looter, GameState state) {
        final var stack = UIStack.getInstance();
        stack.pop();
        stack.push(new WinView(state));
    }
}
