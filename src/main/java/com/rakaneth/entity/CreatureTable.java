package com.rakaneth.entity;

import java.util.HashMap;
import java.util.Map;

public class CreatureTable implements BlueprintTable<CreatureBlueprint> {
    public Map<String, CreatureBlueprint> table = new HashMap<>();

    @Override
    public int size() {
        return table.size();
    }
}
