package com.rakaneth.entity;

import com.rakaneth.engine.effect.Effect;
import com.rakaneth.interfaces.Vision;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Actor extends Entity implements Vision {

    protected double[][] visibleTiles;
    protected double vision = 6.0;
    protected int nrg = 0;
    protected List<Effect> effects = new ArrayList<>();

    //Constructors
    public Actor(char glyph, String name, String desc, Color color) {
        super(glyph, name, desc, true, 2, color);
    }

    public Actor(char glyph, String name, String desc, int zLayer, Color color) {
        super(glyph, name, desc, true, zLayer, color);
    }

    //Getters
    @Override
    public double[][] getVisibleTiles() {
        return visibleTiles;
    }

    @Override
    public double getVision() {
        return vision;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    //Mutators
    public void setVisible(double[][] visiTiles) {
        this.visibleTiles = visiTiles;
    }

    public void setVision(double vision) {
        this.vision = vision;
    }

    public void tick(int ticks) {
        for (Effect e : effects) {
            e.tick(this, ticks);
        }
        effects = effects.stream()
                .filter(e -> !e.isExpired())
                .collect(Collectors.toList());
    }

    public void removeEffect(String name) {
        effects = effects.stream()
                .filter(e -> !e.name.equals(name))
                .collect(Collectors.toList());
    }

    public void addEffect(Effect effect) {
        Optional<Effect> maybeIdentical = effects.stream()
                .filter(e -> e.name.equals(effect.name))
                .findFirst();

        if (maybeIdentical.isPresent()) {
            maybeIdentical.get().merge(effect);
        } else {
            effects.add(effect);
        }
    }
}
