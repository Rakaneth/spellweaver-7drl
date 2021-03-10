package com.rakaneth.interfaces;

import com.rakaneth.engine.DamageTypes;

public interface Vitals {
    int getHp();

    int getMaxHp();

    void setHp(int amt);

    default boolean isAlive() {
        return getHp() > 0;
    }

    void takeDamage(int amt, DamageTypes element);
}
