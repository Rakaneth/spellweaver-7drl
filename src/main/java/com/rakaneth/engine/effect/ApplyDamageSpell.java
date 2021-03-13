package com.rakaneth.engine.effect;

import com.rakaneth.engine.DiceRoller;
import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyDamageSpell implements ApplyFromSpell<InstantDamage>, Serializable {
    final int spellDmg;

    public ApplyDamageSpell(int spellDmg) {
        this.spellDmg = spellDmg;
    }
    @Override
    public InstantDamage apply(Spell spell, Combatant caster, Combatant victim) {
        //FIXME later: improve this
        final int potency = spell.getPotency();
        return new InstantDamage(potency + spellDmg , spell.getBaseElement());
    }
}
