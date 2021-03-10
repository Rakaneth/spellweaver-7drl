package com.rakaneth.entity;

import java.awt.*;

public class Player extends Combatant {

    public Player(String name, String desc, Color color) {
        super('@', name, desc, 3, color);
    }

}
