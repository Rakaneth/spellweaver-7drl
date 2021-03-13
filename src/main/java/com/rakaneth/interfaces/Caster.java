package com.rakaneth.interfaces;

import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.Spell;
import com.rakaneth.engine.effect.ApplyDamageSpell;
import squidpony.squidai.PointAOE;
import squidpony.squidmath.Coord;

import java.util.List;

import static com.rakaneth.engine.SpellState.DONE;

public interface Caster {
    public Spell getSpell();
    public int getPotency();
    public int getPower();
    public int getMaxPower();
    public void changePower(int amt);
    public List<DamageTypes> getKnownElements();
    //public void setSpell(Spell spell);
    public void resetSpell(char[][] tiles);

    default public void finish(Spell spell) {
        final var tech = spell.cast();
        if (tech.aoe == null) {
            tech.aoe = new PointAOE(spell.getTarget());
        }
        tech.aoe.setOrigin(spell.getOrigin());
    }

    default public void chant(DamageTypes element) {
        final var spell = getSpell();
        switch(spell.getSpellState()) {
            case BASE -> {
                spell.setBaseCost(element.baseCost);
                spell.setPotency(getPotency());
                spell.setBaseElement(element);
                spell.charge(false);
                spell.addElement(element);
                spell.badEffects.add(new ApplyDamageSpell());
            }
            case FIRST_MOD -> {
                element.modifySecondCast(spell);
                spell.charge(false);
                spell.changeCost(element.mod1Cost);
                spell.addElement(element);
            }
            case SECOND_MOD -> {
                element.modifyThirdCast(
                        spell,
                        spell.getOrigin(),
                        spell.getTarget(),
                        spell.getPotency(),
                        spell.getRange());
                spell.charge(false);
                spell.addElement(element);
                spell.setMap();
            }
            case DONE -> {
                finish(spell);
            }
        }
    }
}
