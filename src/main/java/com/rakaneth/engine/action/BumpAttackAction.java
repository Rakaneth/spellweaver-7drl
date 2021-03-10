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
    Optional<GameAction> perform(GameState state) {
        //TODO: implement bump attack
        return Optional.empty();
    }
}
