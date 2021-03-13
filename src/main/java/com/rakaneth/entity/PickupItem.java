package com.rakaneth.entity;

import com.rakaneth.engine.GameState;

import java.awt.*;

abstract public class PickupItem extends Entity {
    //for items that do things on pickup

    public PickupItem(char glyph, String name, String desc, Color color) {
        super(glyph, name, desc, false, 1, color);
    }

    abstract public void onPickup(Combatant looter, GameState state);
}
