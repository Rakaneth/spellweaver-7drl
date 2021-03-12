package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Player;

import java.util.Optional;

public class NoAction extends GameAction {
    public NoAction(Player player) {
        super(player, 0);
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        return Optional.empty();
    }
}
