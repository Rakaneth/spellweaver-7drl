package com.rakaneth.interfaces;

import com.rakaneth.entity.Player;
import com.rakaneth.map.GameMap;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.LOS;
import squidpony.squidgrid.Radius;
import squidpony.squidmath.Coord;

public interface Vision {
    double[][] getVisibleTiles();
    double getVision();
    void setVisible(double[][] visiTiles);

    default void updateFOV(GameMap gmap, Coord c) {
        final int vw = getVisibleTiles().length;
        final int vh = getVisibleTiles()[0].length;
        final int mw = gmap.getWidth();
        final int mh = gmap.getHeight();
        if (mw != vw || vh != mh) {
            setVisible(new double[mw][mh]);
        }
        FOV.reuseFOV(gmap.getResistances(), getVisibleTiles(), c.x, c.y, getVision(), Radius.DIAMOND);
        if (this instanceof Player) {
            gmap.updateExplored(getVisibleTiles());
        }
    }

    default boolean isVisible(int x, int y) {
        return getVisibleTiles()[x][y] > 0.15;
    }
}
