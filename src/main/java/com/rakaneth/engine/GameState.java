package com.rakaneth.engine;

import com.rakaneth.entity.Actor;
import com.rakaneth.entity.Entity;
import com.rakaneth.map.GameMap;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GWTRNG;
import squidpony.squidmath.IRNG;

import java.util.*;
import java.util.stream.Collectors;

public class GameState {
    private Actor player = new Actor('@', "Player", "The player!");
    private final IRNG mapRNG;
    private final IRNG gameRNG;
    private final Set<Entity> entities = new HashSet<>();
    private final Map<String, GameMap> maps = new HashMap<>();
    private String curMapId;


    //Constructors
    //must seed both or none
    public GameState(long mapSeed, long gameSeed) {
        mapRNG = new GWTRNG(mapSeed);
        gameRNG = new GWTRNG(gameSeed);
        entities.add(player);
    }

    public GameState() {
        mapRNG = new GWTRNG();
        gameRNG = new GWTRNG();
    }

    //Getters
    public Actor getPlayer() { return player; }
    public IRNG getGameRNG() { return gameRNG;}
    public IRNG getMapRNG() { return mapRNG; }
    public Set<Entity> getEntities() { return entities;}

    //Mutators
    public void addMaps(GameMap... maps) {
        for (GameMap m: maps) {
            this.maps.putIfAbsent(m.getId(), m);
        }
    }

    public void addEntities(Entity... entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public void setCurMap(String mapId) {
        curMapId = mapId;
    }

    //Utilities
    public List<Entity> getCurrentEntities() {
        return entities.stream()
                .filter(e -> e.mapId().equals(curMapId))
                .collect(Collectors.toList());
    }

    public GameMap getCurMap() { return maps.get(curMapId); }

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

}
