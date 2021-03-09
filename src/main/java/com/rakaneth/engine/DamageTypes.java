package com.rakaneth.engine;

import squidpony.squidai.BlastAOE;
import squidpony.squidgrid.Radius;
import squidpony.squidmath.Coord;

public enum DamageTypes {
    //TODO: finish baseline info
    FIRE(
            "@Name evoke$ an ember of flame!",
            "@Name spread$ power like wildfire!",
            "@Name empower$ the spell with fiery might!",
            1
    ) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            spell.setAOE(new BlastAOE(center, radius, Radius.DIAMOND));
            spell.setCost(baseCost + 1);
        }

        @Override
        void modifyThirdCast(Spell spell) {
            spell.setPotency(spell.getPotency() + 2);
            spell.setCost(spell.getCost() + 2);
        }
    },
    ICE(
            "@Name summon$ a sliver of ice!",
            "@Name fill$ the air with power!",
            "@Name infuse$ the spell with the chill of ice!",
            1
    ){
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: CloudAOE
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: add slowing effect to spell, +2
        }
    },
    EARTH(
            "@Name conjure$ a shard of earth!",
            "@Name bring$ the gravity of the earth to bear!",
            "@Name encase$ @themself in the earth!",
            1
    ) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: PointAOE, potency + 2
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: grant shield effect to caster, +1
        }
    },
    LIGHTNING(
            "@Name cast$ a lance of lightning!",
            "@Name focus$$ a beam of power!",
            "@Name strike$ swift as lightning!",
            1
    ) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BeamAOE
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: cut cast time in half +3
        }
    },
    FORCE(
            "@Name invoke$ the essence of Will!",
            "@Name infuse$ the magic with binding force!",
            "@Name bring$ the might of @their will to bear!",
            2
    ) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: PointAOE, stop effect
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: Potency doubled, cost * 1.5
        }

    },
    LIGHT(
            "@Name call$ upon innermost light!",
            "@Name thread$ a weave of light!",
            "@Name soothe$ with the balm of light!",
            3
    ) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BeamAOE
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: Healing
        }
    },
    DARK(
            "@Name command$ hidden darkness!",
            "@Name casts the magic in shadow!",
            "@Name instills fear of the dark!",
            3
    ){
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BurstAOE
        }

        @Override
        void modifyThirdCast(Spell spell) {
            //TODO: fear effect
        }

    }, PHYSICAL("", "", "", 0) {
        @Override
        void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range) {}

        @Override
        void modifyThirdCast(Spell spell) {}
    };

    abstract void modifySecondCast(Spell spell, Coord origin, Coord center, int radius, int range);
    abstract void modifyThirdCast(Spell spell);
    public final String firstCast;
    public final String secondCast;
    public final String thirdCast;
    public final int baseCost;

    DamageTypes(String firstCast, String secondCast, String thirdCast, int baseCost) {
        this.firstCast = firstCast;
        this.secondCast = secondCast;
        this.thirdCast = thirdCast;
        this.baseCost = baseCost;
    }
}
