package com.rakaneth.entity;

import squidpony.squidmath.IRNG;
import squidpony.squidmath.ProbabilityTable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CreatureTable implements BlueprintTable<CreatureBlueprint> {
    public Map<String, CreatureBlueprint> table = new HashMap<>();

    @Override
    public int size() {
        return table.size();
    }

    public ProbabilityTable<CreatureBlueprint> getProbabilityTable(IRNG rng) {
        final var probTable = new ProbabilityTable<CreatureBlueprint>(rng);
        table.forEach((k, v) -> {
            probTable.add(v, v.freq);
        });
        return probTable;
    }

    public ProbabilityTable<CreatureBlueprint> getProbabilityTable(IRNG rng, Predicate<CreatureBlueprint> pred) {
        final var probTable = new ProbabilityTable<CreatureBlueprint>(rng);
        table.forEach((k, v) -> {
           if (pred.test(v)) {
               probTable.add(v, v.freq);
           }
        });
        return probTable;
    }
}
