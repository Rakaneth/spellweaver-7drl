package com.rakaneth.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedInputStream;
import java.io.IOException;

public final class DataReader {
    private final static String creatureFile = "creatures.yml";
    private final static Logger logger = LoggerFactory.getLogger(DataReader.class);

    private static <T extends BlueprintTable<? extends Blueprint>> T loadBlueprints(String fileName, Class<T> klass) {
        Yaml yaml = new Yaml(new Constructor(klass));
        T result = null;
        var stream = DataReader.class.getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            logger.error("Cannot open " + fileName + " data file.");
            return null;
        }
        try (BufferedInputStream in = new BufferedInputStream(stream)) {
            result = yaml.load(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger.info(result.size() + " items loaded from " + fileName + "");
        return result;
    }

    public static CreatureTable loadCreatures() {
        return loadBlueprints(creatureFile, CreatureTable.class);
    }


}
