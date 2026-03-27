package io.github.jaypixl.simpleballswap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jaypixl.simpleballswap.SimpleBallSwapMod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            Path.of("config", SimpleBallSwapMod.MOD_ID + ".json"); //config/simpleballswap.json

    public static void load() {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());
                SimpleBallSwapMod.CONFIG = new SimpleBallSwapConfig();

                Files.writeString(CONFIG_PATH, GSON.toJson(SimpleBallSwapMod.CONFIG));
            } else {
                SimpleBallSwapMod.CONFIG = GSON.fromJson(
                        Files.readString(CONFIG_PATH),
                        SimpleBallSwapConfig.class
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Simple Ball Swap config!", e);
        }
    }
}

