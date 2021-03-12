package com.rakaneth.engine;

import com.rakaneth.entity.*;
import com.rakaneth.map.GameMap;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GWTRNG;
import squidpony.squidmath.IRNG;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameState implements Serializable {
    public final Player player;
    public final IRNG mapRNG;
    public final IRNG gameRNG;
    private final Set<Entity> entities = new HashSet<>();
    private final Map<String, GameMap> maps = new HashMap<>();
    private String curMapId;
    public transient final EntityFactory entityFactory;
    public final List<String> messages = new ArrayList<>();
    private int gameTurns = 0;
    private final ActorQueue queue = new ActorQueue();

    //Constructors
    //must seed both or none
    public GameState(long mapSeed, long gameSeed) {
        mapRNG = new GWTRNG(mapSeed);
        gameRNG = new GWTRNG(gameSeed);
        entityFactory = new EntityFactory(mapRNG);
        player = entityFactory.createPlayer("Farin");
        entities.add(player);
    }

    public GameState() {
        mapRNG = new GWTRNG();
        gameRNG = new GWTRNG();
        entityFactory = new EntityFactory(mapRNG);
        player = entityFactory.createPlayer("Farin");
        entities.add(player);
    }

    //Getters
    public int getGameTurn() { return gameTurns;}

    //Mutators
    public void addMaps(GameMap... maps) {
        for (GameMap m : maps) {
            this.maps.putIfAbsent(m.getId(), m);
        }
    }

    public void incrementGameClock() { gameTurns++;}

    public void addEntities(Entity... entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public void removeEntities(Entity... entities) {
        for (Iterator<Entity> i = Arrays.stream(entities).iterator(); i.hasNext();) {
            Entity e = i.next();
            this.entities.remove(e);
            if (e instanceof Actor) {
                this.queue.remove((Actor)e);
            }
        }
    }

    public void resetQueue() {
        queue.reset(getCurrentActors());
    }

    private List<Actor> getCurrentActors() {
        return getCurrentEntities().stream()
                .filter(e -> e instanceof Actor)
                .map(e -> (Actor) e)
                .collect(Collectors.toList());
    }

    public void setCurMap(String mapId) {
        curMapId = mapId;
        resetQueue();
        player.resetSpell(getCurMap().getTiles());
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void tick(int ticks) {
        getCurrentEntities().stream()
                .filter(e -> e instanceof Combatant)
                .map(e -> (Combatant)e)
                .forEach(e -> e.tick(ticks));
    }

    public void update() {
        queue.update(this);
    }

    //Utilities
    public List<Entity> getCurrentEntities() {
        return entities.stream()
                .filter(e -> e.mapId().equals(curMapId))
                .collect(Collectors.toList());
    }

    public GameMap getCurMap() {
        return maps.get(curMapId);
    }

    public List<Entity> getEntitiesAt(final Coord c, final String mapId) {
        return entities.stream()
                .filter(e -> e.mapId().equals(mapId) && e.getPos() == c)
                .collect(Collectors.toList());
    }

    public List<Entity> getEntitiesAt(final Coord c) {
        return getEntitiesAt(c, curMapId);
    }

    public Optional<Entity> getBlockerAt(final Coord c, final String mapId) {
        return getEntitiesAt(c, mapId).stream()
                .filter(e -> e.isBlocker)
                .findFirst();
    }

    public Optional<Entity> getBlockerAt(final Coord c) {
        return getBlockerAt(c, curMapId);
    }

    public boolean isBlocked(final Coord c, final String mapId) {
        return getBlockerAt(c, mapId).isPresent() || maps.get(mapId).isBlocking(c.x, c.y);
    }

    public boolean isBlocked(final Coord c) {
        return isBlocked(c, curMapId);
    }

    public boolean canSeePlayer(Actor entity) {
        return entity.canSee(player);
    }


    public void cleanup() {
        final var toRemove = getCurrentEntities()
                .stream()
                .filter(e -> e instanceof Combatant)
                .filter(e -> !(e instanceof Player))
                .filter(e -> !((Combatant)e).isAlive())
                .map(e -> (Combatant)e)
                .toArray(Entity[]::new);

        removeEntities(toRemove);
    }
}
