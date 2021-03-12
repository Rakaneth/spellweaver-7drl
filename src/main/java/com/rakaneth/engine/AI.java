package com.rakaneth.engine;

import com.rakaneth.engine.action.GameAction;
import com.rakaneth.engine.action.MoveAction;
import com.rakaneth.engine.action.SentinelAction;
import com.rakaneth.engine.action.WaitAction;
import com.rakaneth.engine.effect.Fear;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Combatant;
import squidpony.squidgrid.LOS;

public enum AI {
    HUNT {
        @Override
        GameAction chooseAction(Actor actor, GameState state) {
            if (state.canSeePlayer(actor)) {
                final var pathToPlayer = state.getCurMap()
                        .getDMap()
                        .findAttackPath(
                                1,
                                actor.getRange(),
                                new LOS(LOS.ORTHO),
                                null,
                                null,
                                actor.getPos(),
                                state.player.getPos());

                if (pathToPlayer.isEmpty()) {
                    return new WaitAction(actor);
                } else {
                    return new MoveAction(actor, pathToPlayer.get(0));
                }
            } else {
                return new WaitAction(actor);
            }
        }
    },
    FLEE {
        @Override
        GameAction chooseAction(Actor actor, GameState state) {
            final var fearSource = ((Combatant)actor).getEffect("Fear");
            if (fearSource.isPresent()) {
                final var fearEffect = (Fear) fearSource.get();
                final var pathAway = state.getCurMap()
                        .getDMap()
                        .findFleePath(
                                1,
                                1.2,
                                null,
                                null,
                                actor.getPos(),
                                fearEffect.source.getPos());
                if (pathAway.isEmpty()) {
                    actor.setAI(HUNT);
                    return HUNT.chooseAction(actor, state);
                } else {
                    return new MoveAction(actor, pathAway.get(0));
                }
            } else {
                actor.setAI(HUNT);
                return HUNT.chooseAction(actor, state);
            }
        }
    },
    WAIT {
        @Override
        GameAction chooseAction(Actor actor, GameState state) {
            return new WaitAction(actor);
        }
    },
    SENTINEL {
        @Override
        GameAction chooseAction(Actor actor, GameState state) {
            return new SentinelAction(actor);
        }
    };

    abstract GameAction chooseAction(Actor actor, GameState state);
}
