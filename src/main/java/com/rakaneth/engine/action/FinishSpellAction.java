package com.rakaneth.engine.action;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.Spell;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.engine.effect.InstantDamage;
import com.rakaneth.entity.Combatant;
import com.rakaneth.entity.Player;
import com.rakaneth.interfaces.Caster;

import java.util.Optional;

public class FinishSpellAction extends GameAction {
    public FinishSpellAction(Player player, int finalCost) {
        super(player, finalCost);
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        final Caster caster = (Caster)actor;
        final Spell spell = caster.getSpell();
        final var user = (Combatant)actor;
        spell.setOrigin(user.getPos());
        final var hits = spell.cast().apply(user.getPos(), spell.getTarget());
        hits.forEach((c, v) -> {
            if (v <= 0.0) return;
            final var maybeVictim = state.getBlockerAt(c, Combatant.class);
            maybeVictim.ifPresent(victim -> {
                if (victim == user) return;
                spell.badEffects.forEach( applier -> {
                    Effect finalEffect = applier.apply(spell, user, victim);
                    int roll = state.gameRNG.between(1, 21);
                    victim.tryApplyEffect(finalEffect, roll);
                });
            });
            spell.goodEffects.forEach( applier -> {
                Effect finalEffect = applier.apply(spell, user, user);
                finalEffect.apply(user);
            });
        });
        final int sCost = spell.getCost();
        final int rawPowerTotal = caster.getPower() - sCost;
        caster.changePower(-sCost);
        if (rawPowerTotal < 0) {
            new InstantDamage(Math.abs(rawPowerTotal), DamageTypes.FORCE).apply(user);
            final String msg = String.format("%s overcasts, taking %d damage!", user.name, rawPowerTotal);
            state.addMessage(msg);
        }
        caster.resetSpell(state.getCurMap().getTiles());
        return Optional.empty();
    }
}
