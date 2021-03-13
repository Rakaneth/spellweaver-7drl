package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyRegenerateSpell implements ApplyFromSpell<Regenerate>, Serializable {

    @Override
    public Regenerate apply(Spell spell, Combatant caster, Combatant victim) {
        final int potency = spell.getPotency();
        return new Regenerate(potency, potency);
    }
}
