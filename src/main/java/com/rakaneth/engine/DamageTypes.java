package com.rakaneth.engine;

import squidpony.squidai.BlastAOE;
import squidpony.squidgrid.Radius;
import squidpony.squidmath.Coord;

public enum DamageTypes {
    //TODO: finish baseline info
    FIRE(
            "@Name evoke$ an ember of flame!",
            "@Name empower$ the spell with fiery might!",
            "@Name spread$ power like wildfire!",
            1,
            2,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            spell.setAOE(new BlastAOE(center, radius, Radius.DIAMOND, 1, spell.getRange()));
        }

        @Override
        public void modifySecondCast(Spell spell) {
            spell.changePotency(2);
        }
    },
    ICE(
            "@Name summon$ a sliver of ice!",
            "@Name infuse$ the spell with the chill of ice!",
            "@Name fill$ the air with power!",
            1,
            2,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: CloudAOE
        }

        @Override
        public void modifySecondCast(Spell spell) {
            //TODO: add slowing effect to spell, +2
        }
    },
    EARTH(
            "@Name conjure$ a shard of earth!",
            "@Name encase$ @themself in elemental armor!",
            "@Name bring% the gravity of earth to bear!",
            1,
            1,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: Weapon effect +1
        }

        @Override
        public void modifySecondCast(Spell spell) {
            //TODO: grant shield effect to caster, +1
        }
    },
    LIGHTNING(
            "@Name cast$ a lance of lightning!",
            "@Name strike$ swift as lightning!",
            "@Name focus$$ a beam of power!",
            1,
            3,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BeamAOE
        }

        @Override
        public void modifySecondCast(Spell spell) {
            //TODO: cut cast time in half +3
        }
    },
    FORCE(
            "@Name invoke$ the essence of Will!",
            "@Name bring$ the might of @their will to bear!",
            "@Name infuse$ the magic with binding force!",
            2,
            0,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: PointAOE, stop effect
        }

        @Override
        public void modifySecondCast(Spell spell) {
            spell.multiplyCost(1.5);
            //TODO: Potency doubled, cost * 1.5
        }

    },
    LIGHT(
            "@Name call$ upon innermost light!",
            "@Name soothe$ with the balm of light!",
            "@Name thread$ a weave of light!",
            3,
            3,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BeamAOE
        }

        @Override
        public void modifySecondCast(Spell spell) {
            //TODO: Healing
        }
    },
    DARK(
            "@Name command$ hidden darkness!",
            "@Name instills fear of the dark!",
            "@Name casts the magic in shadow!",

            3,
            2,
            1
    ) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
            //TODO: BurstAOE
        }

        @Override
        public void modifySecondCast(Spell spell) {
            //TODO: fear effect
        }

    }, PHYSICAL("", "", "", 0, 0, 0) {
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
        }

        @Override
        public void modifySecondCast(Spell spell) {
        }
    }, NONE("", "", "", 0, 0, 0) {
        //Null element, no damage should have this type
        @Override
        public void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range) {
        }

        @Override
        public void modifySecondCast(Spell spell) {
        }
    };

    public abstract void modifyThirdCast(Spell spell, Coord origin, Coord center, int radius, int range);

    public abstract void modifySecondCast(Spell spell);

    public DamageTypes getOpposite() {
        return switch(this) {
            case FIRE -> ICE;
            case ICE -> FIRE;
            case EARTH -> LIGHTNING;
            case LIGHTNING -> EARTH;
            case FORCE -> PHYSICAL;
            case PHYSICAL -> FORCE;
            case LIGHT -> DARK;
            case DARK -> LIGHT;
            case NONE -> NONE;
        };
    }

    public final String firstCast;
    public final String secondCast;
    public final String thirdCast;
    public final int baseCost;
    public final int mod1Cost;
    public final int mod2Cost;

    DamageTypes(String firstCast, String secondCast, String thirdCast, int baseCost, int mod1Cost, int mod2Cost) {
        this.firstCast = firstCast;
        this.secondCast = secondCast;
        this.thirdCast = thirdCast;
        this.baseCost = baseCost;
        this.mod1Cost = mod1Cost;
        this.mod2Cost = mod2Cost;
    }
}
