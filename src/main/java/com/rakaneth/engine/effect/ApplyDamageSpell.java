package com.rakaneth.engine.effect;

import com.rakaneth.engine.DiceRoller;
import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyDamageSpell implements ApplyFromSpell<InstantDamage>, Serializable {

    @Override
    public InstantDamage apply(Spell spell, Combatant caster, Combatant victim) {
        //FIXME later: improve this
        final int potency = spell.getPotency();
        final int bonusDmg = DiceRoller.getInstance().rng.between(-5, 5);
        return new InstantDamage(potency + bonusDmg , spell.getBaseElement());
    }
}
