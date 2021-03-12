package com.rakaneth.engine.action;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.Spell;
import com.rakaneth.entity.Player;
import squidpony.Messaging;

import java.util.Optional;

public class CastAction extends GameAction {
    private final DamageTypes element;

    public CastAction(Player player, DamageTypes element) {
        super(player, 5);
        this.element = element;
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        final Player player = (Player)actor;
        String baseMsg = switch(player.getSpell().getSpellState()) {
            case BASE -> element.firstCast;
            case FIRST_MOD -> element.secondCast;
            case SECOND_MOD -> element.thirdCast;
            case DONE -> "@Name @release$ the spell!";
        };
        state.addMessage(Messaging.transform(
                baseMsg,
                player.name,
                Messaging.NounTrait.NO_GENDER));
        player.chant(element);

        return Optional.empty();
    }
}
