package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Player;

public class Armor extends Buff{
    public final DamageTypes element;
    private DamageTypes oldWeakness;
    private DamageTypes oldResistance;

    public Armor(int dfp, int duration, DamageTypes element) {
        super("Armor", duration, 0, dfp, 0, 0);
        this.element = element;
    }

    @Override
    protected void onApply(Combatant entity) {
        oldWeakness = entity.getWeakness();
        oldResistance = entity.getResistance();
        entity.setResistance(element);
        entity.setWeakness(element.getOpposite());
        if (entity instanceof Player) {
            String msg = String.format("%s gains a shield of %s!", entity.name, element.toString());
            MessageDispatcher.getInstance().gameMessage(msg);
        }

        super.onApply(entity);
    }

    @Override
    protected void onExpire(Combatant entity) {
        entity.setResistance(oldResistance);
        entity.setWeakness(oldWeakness);
        if (entity instanceof Player) {
            String msg = String.format("%s no longer has protection from %s.", entity.name, element.toString());
            MessageDispatcher.getInstance().gameMessage(msg);
        }
        super.onExpire(entity);
    }
}
