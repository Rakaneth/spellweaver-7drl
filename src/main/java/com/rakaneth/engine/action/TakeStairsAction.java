package com.rakaneth.engine.action;

import com.rakaneth.engine.GameState;
import com.rakaneth.entity.Player;
import squidpony.squidmath.Coord;

import java.util.Optional;

public class TakeStairsAction extends GameAction {
    private final Player player;
    public TakeStairsAction(Player player) {
        super(player, 20);
        this.player = player;
    }

    @Override
    public Optional<GameAction> perform(GameState state) {
        final Coord stair = player.getPos();
        final var conn = state.getCurMap().getConnection(stair);
        player.setMapId(conn.mapId);
        state.setCurMap(conn.mapId);
        player.moveTo(conn.dest);
        return Optional.empty();
    }
}
