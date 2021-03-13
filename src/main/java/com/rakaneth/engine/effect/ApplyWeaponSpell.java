package com.rakaneth.engine.effect;

import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.ApplyFromSpell;

import java.io.Serializable;

public class ApplyWeaponSpell implements ApplyFromSpell<Weapon>, Serializable {

    @Override
    public Weapon apply(Spell spell, Combatant caster, Combatant victim) {
        int potency = spell.getPotency();
        int duration = potency * 2;
        return new Weapon(potency, duration, spell.getBaseElement());
    }
}
