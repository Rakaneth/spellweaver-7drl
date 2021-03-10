package com.rakaneth.engine.effect;

import com.rakaneth.entity.Entity;

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

    protected void onTick(Entity entity, int ticks) {
    } //what happens each tick

    protected void onApply(Entity entity) {
    } //what happens when applied

    protected void onExpire(Entity entity) {
    } //what happens when expires - use this for instant effects

    protected void onMerge(Effect effect) { //what happens when receiving an effect of the same type
        //default behavior is to overwrite duration
        this.duration = effect.duration;
    }

    public void tick(Entity entity, int ticks) {
        if (duration > 0) {
            this.onTick(entity, ticks);
        }
        if (this.duration == 0) this.onExpire(entity);
    }

    public void merge(Effect effect) {
        this.onMerge(effect);
    }

    public boolean isExpired() {
        return this.duration == 0;
    }

    @Override
    public String toString() {
        return name + "(" + duration + ")";
    }
}
