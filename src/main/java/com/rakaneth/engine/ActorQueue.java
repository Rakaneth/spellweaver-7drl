package com.rakaneth.engine;

import com.rakaneth.engine.action.GameAction;
import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Player;
import com.rakaneth.entity.Sentinel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class ActorQueue implements Serializable {
    private final List<Actor> actors;
    private Actor curActor;
    private static final Logger logger = LoggerFactory.getLogger(ActorQueue.class);

    public ActorQueue(List<Actor> actors) {
        this.actors = actors;
        actors.add(new Sentinel());
    }

    public ActorQueue() {
        this(new ArrayList<>());
    }

    public void reset(List<Actor> actors) {
        this.actors.clear();
        this.actors.add(new Sentinel());
        this.actors.addAll(actors);
    }

    public void update(GameState state) {
        int lastCost = 10;
        while (true) {
            curActor = actors.get(0);
            if (curActor.getNrg() < 0) curActor.changeNrg(curActor.getSpd());
            if (!(curActor instanceof Sentinel)) {
                curActor.updateFOV(state.getCurMap(), curActor.getPos());
            }
            logger.info(
                    "Turn {}: Actor {} getting {} energy, has {}",
                    state.getGameTurn(),
                    curActor.name,
                    curActor.getSpd(),
                    curActor.getNrg());
            while (curActor.getNrg() >= 0 ) {
                if (curActor instanceof Player) return;
                GameAction curAction = curActor.getAI().chooseAction(curActor, state);
                lastCost = curAction.doAction(state) + 100;
                curActor.changeNrg(-lastCost);
                logger.info(
                        "Turn: {} Actor {} acted, spending {}, has {} energy",
                        state.getGameTurn(),
                        curActor.name,
                        lastCost,
                        curActor.getNrg());
            }
            state.cleanup();
            Collections.rotate(actors, 1);
        }
    }



    public void add(Actor e) {
        actors.add(e);
    }

    public void remove(Actor e) {
        this.actors.remove(e);
    }
}
