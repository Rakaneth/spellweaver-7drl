package com.rakaneth.engine.effect;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Player;

public class Poison extends DamageOverTime{
    public Poison(int amt, int duration) {
        super("Poison", DamageTypes.EARTH, amt, duration);
        modifier = String.valueOf(amt) + " stacks";
    }

    @Override
    protected void onTick(Combatant entity, int ticks) {
        super.onTick(entity, ticks);
        if (entity instanceof Player) {
            dispatcher.gameMessage(entity.name + " takes " + lastAmtTaken + " damage from poison!");
        }
    }

    @Override
    protected void onApply(Combatant entity) {
        if (entity instanceof Player) {
            dispatcher.gameMessage(entity.name + " is poisoned!");
        }
    }

    @Override
    protected void onMerge(Effect effect, Combatant entity) {
        //Poison stacks intensity and resets duration
        amt += ((Poison)effect).amt;
        modifier = String.valueOf(amt) + " stacks";
        duration = Math.max(effect.duration, duration);
        dispatcher.msgIfPlayerCanSee(entity.name + "'s poison worsens!");
    }
}
