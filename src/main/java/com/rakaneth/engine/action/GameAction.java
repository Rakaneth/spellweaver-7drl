package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;

import java.util.Optional;

abstract public class GameAction {
    protected Actor actor;
    public int cost;

    public GameAction(Actor actor, int cost) { //Actor is the thing performing the action
        this.actor = actor;
        this.cost = cost;
    }

    public abstract Optional<GameAction> perform(GameState state);

    public int doAction(GameState state) {
        var curAction = perform(state);
        int lastCost = cost;
        while (curAction.isPresent()) {
            lastCost = curAction.get().cost;
            curAction = curAction.get().perform(state);
        }
        return lastCost;
    }


}
