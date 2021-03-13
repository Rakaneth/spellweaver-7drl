package com.rakaneth.engine;

import squidpony.squidmath.IRNG;

public class DiceRoller {
    private static DiceRoller instance;
    public final IRNG rng;
    private DiceRoller(IRNG rng) {
        this.rng = rng;
    }

    public static DiceRoller getInstance() {
        return instance;
    }

    public static DiceRoller create(IRNG rng) {
        instance = new DiceRoller(rng);
        return instance;
    }
}
