package com.rakaneth.entity;

import com.rakaneth.Swatch;
import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.interfaces.Caster;

import java.awt.*;

public class Spellbook extends PickupItem {

    public final DamageTypes element;

    public Spellbook(DamageTypes element) {
        super(
                '?',
                "A spellbook containing the secrets of " + element.toString(),
                "Spellbook of " + element.toString(),
                switch (element) {
                    case FIRE -> Swatch.FIRE_FG;
                    case ICE -> Swatch.ICE_FG;
                    case LIGHTNING -> Swatch.LIGHTNING_FG;
                    case EARTH -> Swatch.EARTH_FG;
                    case FORCE -> Swatch.FORCE_FG;
                    case LIGHT -> Swatch.LIGHT_FG;
                    case DARK -> Swatch.DARK_FG;
                    default -> Color.BLACK;
                });

        this.element = element;
    }

    @Override
    public void onPickup(Combatant looter, GameState state) {
        if (looter instanceof Caster) {
            String msg = String.format("%s has learned the secrets of %s!", looter.name, element.toString());
            MessageDispatcher.getInstance().msgIfPlayerCanSee(msg, looter);
            ((Caster)looter).getKnownElements().add(element);
        }
    }
}
