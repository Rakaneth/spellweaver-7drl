package com.rakaneth.entity;

import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

public class Entity {
    private Coord position = Coord.get(0, 0);
    private final String name;
    private final String desc;
    private final char glyph;

    public Entity(char glyph, String name, String desc) {
        this.name = name;
        this.glyph = glyph;
        this.desc = desc;
    }

    //Getters
    public Coord getPos() { return position; }
    public String getName() { return name; }
    public String getDesc() { return desc;}
    public char getGlyph() { return glyph; }

    //Mutators
    public void move(int x, int y) {
        if (x<0 || y<0) { return; }
        position = Coord.get(x, y);
    }

    public void moveBy(int dx, int dy) {
        position = position.translate(dx, dy);
    }

    public void moveDir(Direction dir) {
        position = position.translate(dir);
    }
}
