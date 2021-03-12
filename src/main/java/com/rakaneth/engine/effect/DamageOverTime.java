package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.entity.Combatant;

public class DamageOverTime extends Effect{
    protected int amt;
    public final DamageTypes element;
    protected int lastAmtTaken;

    public DamageOverTime(String name, DamageTypes element, int amt, int duration) {
        super(name, duration, true);
        this.element = element;
        this.amt = amt;
    }

    @Override
    protected void onTick(Combatant entity, int ticks) {
        lastAmtTaken = amt * ticks;
        entity.takeDamage(lastAmtTaken, element);
        super.onTick(entity, ticks);
    }
}
