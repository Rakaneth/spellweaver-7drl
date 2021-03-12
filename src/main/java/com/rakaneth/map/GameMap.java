package com.rakaneth.map;

import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.LOS;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.IRNG;
import squidpony.squidmath.MathExtras;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class GameMap implements Serializable {

    public static class Connection {
        public final Coord dest;
        public final String mapId;

        public Connection(Coord dest, String mapId) {
            this.dest = dest;
            this.mapId = mapId;
        }
    }

    boolean lit;
    char[][] tiles;
    GreasedRegion floors;
    GreasedRegion temp;
    GreasedRegion explored;
    String id;
    String name;
    double[][] resistances;
    final private IRNG rng;
    final private Map<Coord, Connection> connections = new HashMap<>();
    DijkstraMap dmap;
    double[][] costs;

    GameMap(IRNG rng) {
        this.rng = rng;
    }

    //Getters
    public Optional<Character> getTile(int x, int y) {
        return inBounds(x, y) ? Optional.of(tiles[x][y]) : Optional.empty();
    }

    public double getCost(int x, int y) {
        return costs[x][y];
    }

    public double getCost(Coord c) {
        return getCost(c.x, c.y);
    }

    public DijkstraMap getDMap() {
        return dmap;
    }

    public Optional<Character> getTile(Coord c) {
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

    public Connection getConnection(Coord c) {
        return connections.get(c);
    }

    public double[][] getResistances() {
        return resistances;
    }

    public char[][] getTiles() {
        return tiles;
    }

    //Mutators
    public void setTile(int x, int y, char t) {
        tiles[x][y] = t;
    }

    public void setTile(Coord c, char t) {
        setTile(c.x, c.y, t);
    }

    public void connect(Coord from, Coord to, String mapId) {
        final Connection dest = new Connection(to, mapId);
        connections.putIfAbsent(from, dest);
    }

    public void updateExplored(double[][] light) {
        explored.or(temp.refill(light, 0.0).not());
    }

    //Utilities
    public boolean inBounds(int x, int y) {
        int w = getWidth();
        int h = getHeight();

        return x >= 0 && x < w && y >= 0 && y < h;
    }

    public boolean isBlocking(int x, int y) {
        final var t = getTile(x, y);
        String blockers = "#+";
        return t.isEmpty() || blockers.indexOf(t.get()) > -1;
    }

    public Coord getRandomFloor() {
        return floors.singleRandom(rng);
    }

    public Coord getRandomFloorAround(Coord c, int radius) {
        return temp.empty().set(true, c).flood(floors, radius).singleRandom(rng);
    }

    private int calc(int p, int m, int s) {
        return MathExtras.clamp(p - s / 2, 0, Math.max(0, m - s));
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
        return Coord.get(px - camPoint.x, py - camPoint.y);
    }

    public Coord mapToScreen(Coord p, Coord c, Coord s) {
        return mapToScreen(p.x, p.y, c.x, c.y, s.x, s.y);
    }

    public boolean isExplored(Coord curPoint) {
        return explored.contains(curPoint);
    }


}
