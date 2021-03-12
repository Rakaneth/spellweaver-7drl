package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Entity;

import java.util.Optional;

public class BumpAttackAction extends GameAction {
    private final Entity defender;
    public BumpAttackAction(Actor actor, Entity defender) {
        super(actor, 10);
        this.defender = defender;

    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        state.addMessage(actor.name + " bumps " + defender.name);
        return Optional.empty();
    }
}
