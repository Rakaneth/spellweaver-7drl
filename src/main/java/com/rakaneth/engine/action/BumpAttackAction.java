package com.rakaneth.engine.action;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.effect.Weapon;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Entity;

import java.util.Optional;

public class BumpAttackAction extends GameAction {
    private final Combatant defender;
    public BumpAttackAction(Actor actor,Combatant defender) {
        super(actor, 10);
        this.defender = defender;
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        final var attacker = (Combatant)actor;
        final int rawDmg = attacker.getAtk() - defender.getDfp(); //FIXME: better attack formula
        final boolean playerCanSee =state.canSeePlayer(attacker) || state.canSeePlayer(defender);
        if (rawDmg > 0) {
            final var element = attacker.getDamageType();
            defender.takeDamage(rawDmg, element);
            if (playerCanSee) {
                String msg = String.format(
                        "%s strikes %s for %d %s damage",
                        attacker.name,
                        defender.name,
                        rawDmg,
                        element.toString());
                state.addMessage(msg);
            }
        } else if (playerCanSee) {
            String msg = String.format(
                    "%s's blow isn't hard enough to harm %s",
                    attacker.name,
                    defender.name);
            state.addMessage(msg);
        }
        state.cleanup();
        return Optional.empty();
    }
}
