package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.*;
import squidpony.squidmath.Coord;

import java.util.Optional;
import java.util.stream.Collectors;

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
        if (actor instanceof Player) {
            final var pickups = state.getEntitiesAt(to)
                    .stream()
                    .filter(e -> e instanceof PickupItem)
                    .map(e -> (PickupItem)e)
                    .collect(Collectors.toList());

            for (PickupItem p : pickups) {
                p.onPickup((Combatant)actor, state);
                state.removeEntities(p);
            }
        }
        cost = (int)(state.getCurMap().getCost(to) * 10);
        return Optional.empty();
    }
}
