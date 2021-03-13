package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyStopSpell implements ApplyFromSpell<Stop>, Serializable {
    @Override
    public Stop apply(Spell spell, Combatant caster, Combatant victim) {
        return new Stop();
    }
}
