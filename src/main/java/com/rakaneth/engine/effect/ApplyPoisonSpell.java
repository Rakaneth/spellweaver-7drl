package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyPoisonSpell implements ApplyFromSpell<Poison>, Serializable {

    @Override
    public Poison apply(Spell spell, Combatant caster, Combatant victim) {
        final int duration = spell.getPotency() * 2 - victim.getWil();
        return new Poison(spell.getPotency(), duration);
    }
}
