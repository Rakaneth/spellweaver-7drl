package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplySlowSpell implements ApplyFromSpell<Slow>, Serializable {

    @Override
    public Slow apply(Spell spell, Combatant caster, Combatant victim) {
        final int potency = spell.getPotency();
        return new Slow(potency, potency * 2);
    }
}
