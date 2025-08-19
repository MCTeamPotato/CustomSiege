package me.kall.customsiege.config;

import com.google.common.collect.Lists;
import me.kall.customsiege.CustomSiege;
import me.kall.jsonate.api.JsonConfig;
import net.minecraft.world.entity.ai.behavior.WeightedList;

public class SiegeConfig {
    private static final String MAX_SPAWNABLE_ENTITIES_KEY = "MaxSpawnableEntitiesDuringSiege";
    private static final String SPAWNABLE_ENTITIES_KEY = "SpawnableEntitiesDuringSiege";

    private static final JsonConfig SIEGE_CONFIG = JsonConfig.create(CustomSiege.MOD_ID, "1.0.0")
            .put(MAX_SPAWNABLE_ENTITIES_KEY, 20)
            .put(SPAWNABLE_ENTITIES_KEY, Lists.newArrayList("minecraft:zombie;100"))
            .initialize();

    public static int MAX_SPAWNABLE_ENTITIES;

    public static final WeightedList<String> SPAWNABLE_ENTITIES = new WeightedList<>();

    public static void init() {
        MAX_SPAWNABLE_ENTITIES = SIEGE_CONFIG.getInt(MAX_SPAWNABLE_ENTITIES_KEY);
        SIEGE_CONFIG.getStream(SPAWNABLE_ENTITIES_KEY, String.class).forEach(key -> {
            String[] entry = key.split(";");
            String id = entry[0];
            int weight = Integer.parseInt(entry[1]);
            SPAWNABLE_ENTITIES.add(id, weight);
        });
    }
}
