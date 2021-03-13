package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyFearSpell implements ApplyFromSpell<Fear>, Serializable {

    @Override
    public Fear apply(Spell spell, Combatant caster, Combatant victim) {
        return new Fear(caster, spell.getPotency());
    }
}
