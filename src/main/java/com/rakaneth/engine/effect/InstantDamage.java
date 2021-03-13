package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.entity.Combatant;
import com.rakaneth.interfaces.Vitals;

public class InstantDamage extends Effect {
    private final int amt;
    private final DamageTypes element;

    public InstantDamage(int amt, DamageTypes element) {
        super("damage", Effect.INSTANT);
        this.amt = amt;
        this.element = element;
    }

    @Override
    protected void onApply(Combatant entity) {
        final int dmg = ((Vitals) entity).takeDamage(amt, element);
        if (entity.getWeakness() == element) {
            String weakMsg = String.format("%s is weak against %s!", entity.name, element);
            dispatcher.msgIfPlayerCanSee(weakMsg, entity);
        }

        if (entity.getResistance() == element) {
            String strongMsg = String.format("%s is resistant to %s!", entity.name, element);
            dispatcher.msgIfPlayerCanSee(strongMsg, entity);
        }

        String msg = String.format("%s takes %d %s damage", entity.name, dmg, element.toString());
        dispatcher.msgIfPlayerCanSee(msg, entity);
    }

    @Override
    protected void onExpire(Combatant entity) {}
}
