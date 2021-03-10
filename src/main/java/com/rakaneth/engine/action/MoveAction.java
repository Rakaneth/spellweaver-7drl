package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Entity;
import squidpony.squidmath.Coord;

import java.util.Optional;

public class MoveAction extends GameAction {
    private Coord to;

    public MoveAction(Actor actor, Coord to) {
        super(actor, 10);
        this.to = to;
    }


    @Override
    Optional<GameAction> perform(GameState state) {
        Optional<Entity> blocker;
        if (state.isBlocked(to)) {
            blocker = state.getBlockerAt(to);
            return blocker.map(entity -> new BumpAttackAction(actor, entity));
        }
        actor.moveTo(to);
        return Optional.empty();
    }
}
