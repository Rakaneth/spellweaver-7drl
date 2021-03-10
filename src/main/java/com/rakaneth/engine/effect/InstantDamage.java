package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.entity.Entity;
import com.rakaneth.interfaces.Vitals;

public class InstantDamage extends Effect {
    private int amt;
    private DamageTypes element;

    public InstantDamage(int amt, DamageTypes element) {
        super("damage", Effect.INSTANT);
        this.amt = amt;
        this.element = element;
    }

    @Override
    protected void onExpire(Entity entity) {
        ((Vitals) entity).takeDamage(amt, element);
    }
}