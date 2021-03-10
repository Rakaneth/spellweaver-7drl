package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;

import java.util.Optional;

abstract public class GameAction {
    protected Actor actor;
    public final int cost;

    public GameAction(Actor actor, int cost) { //Actor is the thing performing the action
        this.actor = actor;
        this.cost = cost;
    }

    abstract Optional<GameAction> perform (GameState state);
}
