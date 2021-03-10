package com.rakaneth.entity;

import com.rakaneth.engine.DamageTypes;

public class CreatureBlueprint extends Blueprint {
    public char glyph = '@';
    public String color = "black";
    public int hp = 0;
    public int will = 0;
    public DamageTypes damType = DamageTypes.PHYSICAL;
    public int atk = 0;
    public int dfp = 0;
    public int spd = 0;
    public DamageTypes weakness = null;
    public DamageTypes resistance = null;
    public String ai = "hunt";
    public String name = "No name";
    public String desc = "No desc";
    public double vision = 6.0;
    public int level = 0;
}
