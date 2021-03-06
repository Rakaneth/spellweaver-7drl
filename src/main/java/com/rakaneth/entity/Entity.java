package com.rakaneth.entity;

import com.rakaneth.map.GameMap;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

public class Entity {
    private Coord position = Coord.get(0, 0);
    public final String name;
    public final String desc;
    private String mapId = "none";
    public final char glyph;
    public final boolean isBlocker;
    public final int zLayer; //0 = corpse, 1 = item, 2 = actor, 3 = player

    public Entity(char glyph, String name, String desc, boolean isBlocker, int zLayer) {
        this.name = name;
        this.glyph = glyph;
        this.desc = desc;
        this.isBlocker = isBlocker;
        this.zLayer = zLayer;
    }

    public Entity(char glyph, String name, String desc) {
        this(glyph, name, desc, false, 2);
    }

    //Getters
    public Coord getPos() { return position; }
    public String mapId() { return mapId;}

    //Mutators
    public void moveTo(Coord c) {
        if (c.x < 0 || c.y < 0) return;
        position = c;
    }

    public void moveBy(int dx, int dy) {
        position = position.translate(dx, dy);
    }

    public void moveDir(Direction dir) {
        position = position.translate(dir);
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public void changeMap(GameMap.Connection conn) {
        setMapId(conn.mapId);
        moveTo(conn.dest);
    }
}
