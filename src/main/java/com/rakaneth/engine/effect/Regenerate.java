package com.rakaneth.engine.effect;

import com.rakaneth.entity.Combatant;

public class Regenerate extends Effect {
    private int stax;

    public Regenerate(int duration, int stax) {
        super("Regeneration", duration);
        this.stax = stax;
        this.modifier = String.valueOf(stax) + " stacks";
    }

    @Override
    protected void onTick(Combatant entity, int ticks) {
        int amt = ticks * stax;
        entity.changeHp(amt);
        String msg = String.format("%s regenerates %s HP", entity.name, amt);
        dispatcher.msgIfPlayerCanSee(msg, entity);
        super.onTick(entity, ticks);
    }

    @Override
    protected void onApply(Combatant entity) {
        String msg = String.format("%s gains %d stacks of regeneration.", entity.name, stax);
        dispatcher.msgIfPlayerCanSee(msg, entity);
    }

    @Override
    protected void onMerge(Effect effect, Combatant entity) {
        //Regen stacks intensity and resets duration
        stax += ((Regenerate)effect).stax;
        duration = Math.max(effect.duration, duration);
        modifier = stax + " stacks";
        String msg = String.format("%s regeneration improves!", entity.name);
        dispatcher.msgIfPlayerCanSee(msg, entity);
    }
}
