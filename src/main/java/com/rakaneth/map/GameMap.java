package com.rakaneth.map;

import com.rakaneth.entity.Entity;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.IRNG;
import squidpony.squidmath.MathExtras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameMap {
    boolean lit;
    char[][] tiles;
    GreasedRegion floors;
    GreasedRegion temp;
    String id;
    String name;
    double[][] resistances;
    final private IRNG rng;
    final private Map<Coord, List<Entity>> entities = new HashMap<>();
    final private Map<Coord, String> connections = new HashMap<>();

    GameMap(IRNG rng) {
        this.rng = rng;
    }

    //Getters
    public char getTile(int x, int y) {
        return inBounds(x, y) ? tiles[x][y] : '\0';
    }

    public char getTile(Coord c) {
        return getTile(c.x, c.y);
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isLit() {
        return lit;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    //Mutators
    public void setTile(int x, int y, char t) {
        tiles[x][y] = t;
    }

    public void setTile(Coord c, char t) {
        setTile(c.x, c.y, t);
    }

    public void addEntity(Coord c, Entity e) {
        if (!entities.containsKey(c)) {
            entities.put(c, new ArrayList<>());
        }
        entities.get(c).add(e);
    }

    public void removeEntity(Coord c, Entity e) {
        if (entities.containsKey(c)) {
            entities.get(c).remove(e);
        }
    }

    //caller's responsibility to check plausibility
    public void moveEntity(Coord from, Coord to, Entity e) {
        removeEntity(from, e);
        addEntity(to, e);
    }

    //Utilities
    public boolean inBounds(int x, int y) {
        int w = getWidth();
        int h = getHeight();

        return x >= 0 && x < w && y >= 0 && y < h;
    }

    public Coord getRandomFloor() {
        return floors.singleRandom(rng);
    }

    public Coord getRandomFloorAround(Coord c, int radius) {
        return temp.empty().set(true, c).flood(floors, radius).singleRandom(rng);
    }

    private int calc(int p, int m, int s) {
        return MathExtras.clamp(p - s/2, 0, Math.max(0, m-s));
    }

    public Coord cam(int cx, int cy, int sx, int sy) {
        int left = calc(cx, getWidth(), sx);
        int top = calc(cy, getHeight(), sy);
        return Coord.get(left, top);
    }

    public Coord cam(Coord c, Coord s) {
        return cam(c.x, c.y, s.x, s.y);
    }

    public Coord mapToScreen(int px, int py, int cx, int cy, int sx, int sy) {
        Coord camPoint = cam(cx, cy, sx, sy);
        return camPoint.translate(-px, -py);
    }

    public Coord mapToScreen(Coord p, Coord c, Coord s) {
        return mapToScreen(p.x, p.y, c.x, c.y, s.x, s.y);
    }


}