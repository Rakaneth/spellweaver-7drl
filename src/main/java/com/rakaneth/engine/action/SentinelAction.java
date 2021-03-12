package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Entity;
import com.rakaneth.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class SentinelAction extends GameAction {
    public SentinelAction(Actor actor) {
        super(actor, 0);
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        state.tick(1);
        state.incrementGameClock();
        state.cleanup();
        return Optional.empty();
    }
}
