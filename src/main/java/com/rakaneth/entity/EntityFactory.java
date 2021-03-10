package com.rakaneth.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.squidmath.IRNG;
import squidpony.squidmath.ProbabilityTable;

import java.awt.*;

import static java.lang.Integer.parseInt;

public final class EntityFactory {
    private final CreatureTable creatures = DataReader.loadCreatures();
    private final ProbabilityTable<CreatureBlueprint> randomCreatures;
    private static final Logger logger = LoggerFactory.getLogger(EntityFactory.class);

    public EntityFactory(IRNG rng) {
        randomCreatures = creatures.getProbabilityTable(rng);
    }

    private CreatureBlueprint getRandomCreatureBP() {
        return randomCreatures.random();
    }

    private CreatureBuilder preCreatureBuild(CreatureBlueprint bp) {
        final var colorCompsRaw = bp.color.split(",");
        Color color = Color.BLACK;
        if (colorCompsRaw.length != 3) {
            logger.error("Color string for " + bp.name + " is incorrect; check YAML. Creature will have default color.");
        } else {
            color = new Color(
                    parseInt(colorCompsRaw[0]),
                    parseInt(colorCompsRaw[1]),
                    parseInt(colorCompsRaw[2]));
        }

        return new CreatureBuilder()
                .withAtk(bp.atk)
                .withDfp(bp.dfp)
                .withWil(bp.will)
                .withColor(color)
                .withGlyph(bp.glyph)
                .withName(bp.name)
                .withDesc(bp.desc)
                .withWeakness(bp.weakness)
                .withResistance(bp.resistance)
                .withVision(bp.vision)
                .withHp(bp.hp);
    }

    private Combatant monsterFromBP(CreatureBlueprint bp) {
        return preCreatureBuild(bp).buildMonster();
    }

    private Player playerFromBP(String playerName) {
        final var bp = creatures.table.get("player");
        return preCreatureBuild(bp)
                .withName(playerName)
                .buildPlayer();
    }

    public Combatant monsterFromString(String id) {
        return monsterFromBP(creatures.table.get(id));
    }

    public Player createPlayer(String playerName) {
        return playerFromBP(playerName);
    }

}
