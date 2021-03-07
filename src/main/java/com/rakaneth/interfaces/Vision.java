package com.rakaneth.interfaces;

import com.rakaneth.entity.Player;
import com.rakaneth.map.GameMap;
import squidpony.squidgrid.FOV;
import squidpony.squidmath.Coord;

public interface Vision {
    double[][] getVisibleTiles();
    double getVision();
    default void updateFOV(GameMap gmap, Coord c) {
        FOV.reuseFOV(gmap.getResistances(), getVisibleTiles(), c.x, c.y, getVision());
        if (this instanceof Player) {
            gmap.updateExplored(getVisibleTiles());
        }
    }
    default boolean isVisible(int x, int y) {
        return getVisibleTiles()[x][y] > 0.0;
    }
}
