package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Player;

import java.util.Optional;

public class FinishSpellAction extends GameAction {
    public FinishSpellAction(Player player, int finalCost) {
        super(player, finalCost);
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        final Player player = (Player)actor;
        player.resetSpell(state.getCurMap().getTiles());
        return Optional.empty();
    }
}
