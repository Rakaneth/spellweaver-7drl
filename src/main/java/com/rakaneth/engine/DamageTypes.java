package com.rakaneth.engine;

public enum DamageTypes {
    FIRE, ICE, EARTH, LIGHTNING, FORCE, LIGHT, DARK, PHYSICAL;

    public String firstCast() {
        return switch (this) {
            case FIRE -> "@Name @evoke$ an ember of flame...";
            case ICE -> "@Name summon$ a sliver of ice...";
            case EARTH -> "@Name conjure$ a shard of earth...";
            case LIGHTNING -> "@Name cast$ a lance of lightning...";
            case FORCE -> "@Name invoke$ the essence of Will...";
            case LIGHT -> "@Name call$ upon innermost light...";
            case DARK -> "@Name command$ hidden darkness...";
            default -> "";
        };
    }
}
