package com.rakaneth.engine.effect;

//also covers debuffs
public class Buff extends Effect {
    public final int atk;
    public final int dfp;
    public final int wil;

    public Buff(String name, int duration, int atk, int dfp, int wil) {
        super(name, duration);
        this.atk = atk;
        this.dfp = dfp;
        this.wil = wil;
    }
}
