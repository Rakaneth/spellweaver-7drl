package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Player;

public class Weapon extends Buff{
    public final DamageTypes element;
    private DamageTypes oldElement;

    public Weapon(int atk, int duration, DamageTypes element) {
        super("Weapon", duration, atk, 0, 0, 0);
        this.element = element;
        this.modifier = element.toString();
    }

    @Override
    protected void onApply(Combatant entity) {
        oldElement = entity.getDamageType();
        entity.setDamageType(element);
        if (entity instanceof Player) {
            String msg = String.format("%s forges a weapon of %s!", entity.name, element.toString());
            MessageDispatcher.getInstance().gameMessage(msg);
        }
        super.onApply(entity);
    }

    @Override
    protected void onExpire(Combatant entity) {
        entity.setDamageType(oldElement);
        if (entity instanceof Player) {
            String msg = String.format("%s's weapon of %s fades.", entity.name, element.toString());
            MessageDispatcher.getInstance().gameMessage(msg);
        }
        super.onExpire(entity);
    }
}
