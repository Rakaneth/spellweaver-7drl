package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;

import java.util.Optional;

public class WaitAction extends GameAction{
    public WaitAction(Actor actor) {
        super(actor, 10);
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        return Optional.empty();
    }
}
