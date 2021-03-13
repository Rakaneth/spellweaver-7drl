package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Entity;
import com.rakaneth.entity.Player;
import squidpony.squidmath.Coord;

import java.util.Optional;

public class MoveAction extends GameAction {
    private final Coord to;

    public MoveAction(Actor actor, Coord to) {
        super(actor, 10);
        this.to = to;
    }


    @Override
    public Optional<GameAction> perform(GameState state) {
        Optional<Combatant> blocker;
        if (state.isBlocked(to)) {
            blocker = state.getBlockerAt(to, Combatant.class);
            if (blocker.isPresent()) {
                return Optional.of(new BumpAttackAction(actor, blocker.get()));
            } else if (actor instanceof Player && state.getCurMap().isClosedDoor(to)) {
                return Optional.of(new DoorOpenAction(actor, to));
            } else {
                return Optional.empty();
            }
        }
        actor.moveTo(to);
        cost = (int)(state.getCurMap().getCost(to) * 10);
        return Optional.empty();
    }
}
