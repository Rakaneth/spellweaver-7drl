package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;
import squidpony.squidmath.Coord;

import java.util.Optional;

public class DoorOpenAction extends GameAction{
    private final Coord door;
    public DoorOpenAction(Actor actor, Coord door) {
        super(actor, 10);
        this.door = door;
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        state.getCurMap().openDoor(door);
        return Optional.empty();
    }
}
