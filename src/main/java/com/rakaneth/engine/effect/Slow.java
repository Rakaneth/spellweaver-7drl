package com.rakaneth.engine.effect;

public class Slow extends Buff{
    public Slow(int duration, int penalty) {
        super("Slow", duration, 0, 0, 0, -penalty, true);
    }



}
