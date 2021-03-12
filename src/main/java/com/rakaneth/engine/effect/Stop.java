package com.rakaneth.engine.effect;

import com.rakaneth.entity.Combatant;

public class Stop extends Effect {
    public Stop() {
        super("Stop", Effect.INSTANT, true);
    }

    @Override
    protected void onApply(Combatant entity) {
        entity.changeNrg(-100);
        super.onApply(entity);
    }
}
