package com.rakaneth.engine.effect;

import com.rakaneth.engine.AI;
import com.rakaneth.entity.Combatant;

public class Fear extends Effect{
    public final Combatant source;
    private AI prevAI;

    public Fear(Combatant source, int duration) {
        super("Fear", duration);
        this.source = source;
    }

    @Override
    protected void onApply(Combatant entity) {
        prevAI = entity.getAI();
        entity.setAI(AI.FLEE);
        super.onApply(entity);
    }

    @Override
    protected void onMerge(Effect effect, Combatant entity) {
        //Fear stacks duration
        this.duration += effect.duration;
    }

    @Override
    protected void onExpire(Combatant entity) {
        entity.setAI(prevAI);
        super.onExpire(entity);
    }
}
