package com.rakaneth.entity;

import squidpony.squidmath.IRNG;
import squidpony.squidmath.ProbabilityTable;

public interface BlueprintTable<T extends Blueprint> {
    int size();

    ProbabilityTable<T> getProbabilityTable(IRNG rng);
}
