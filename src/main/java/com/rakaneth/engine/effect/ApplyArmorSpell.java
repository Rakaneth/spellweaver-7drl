package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyArmorSpell implements ApplyFromSpell<Armor>, Serializable {

    @Override
    public Armor apply(Spell spell, Combatant caster, Combatant victim) {
        final int potency = spell.getPotency();
        final int duration = potency * 2;
        return new Armor(potency, duration, spell.getBaseElement());
    }
}
