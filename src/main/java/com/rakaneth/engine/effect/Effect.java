package com.rakaneth.engine.effect;

import com.rakaneth.engine.MessageDispatcher;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Player;

import java.io.Serializable;

abstract public class Effect implements Serializable {
    protected int duration;
    public static final int INFINITE = -1;
    public static final int INSTANT = 0;
    public final String name;

    public Effect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    protected void onTick(Combatant entity, int ticks) {
    } //what happens each tick

    protected void onApply(Combatant entity) {
    } //what happens when applied

    protected void onExpire(Combatant entity) {
        if (entity instanceof Player)
            MessageDispatcher.getInstance().gameMessage(name + " has ended.");
    } //what happens when expires - use this for instant effects

    protected void onMerge(Effect effect, Combatant entity) { //what happens when receiving an effect of the same type
        //default behavior is to overwrite duration
        this.duration = effect.duration;
    }

    public void tick(Combatant entity, int ticks) {
        if (duration > 0) {
            this.onTick(entity, ticks);
            duration = Math.max(0, duration - ticks);
        }
        if (this.duration == 0) this.onExpire(entity);
    }

    public void apply(Combatant entity) {
        final var maybeEff = entity.getEffect(name);
        if (maybeEff.isPresent()) {
            maybeEff.get().merge(this, entity);
        } else {
            entity.getEffects().add(this);
            onApply(entity);
        }
    }

    public void remove(Combatant entity) {
        onExpire(entity);
    }

    public void merge(Effect effect, Combatant entity) {
        this.onMerge(effect, entity);
    }

    public boolean isExpired() {
        return this.duration == 0;
    }

    @Override
    public String toString() {
        return name + "(" + duration + ")";
    }
}
