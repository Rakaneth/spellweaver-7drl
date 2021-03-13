package com.rakaneth.interfaces;

import com.rakaneth.engine.Spell;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.entity.Combatant;

import java.io.Serializable;


public interface ApplyFromSpell<T extends Effect>  {
    abstract T apply(Spell spell, Combatant caster, Combatant victim);
}
