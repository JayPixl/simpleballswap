package io.github.jaypixl.simpleballswap.fabric;

import io.github.jaypixl.simpleballswap.SimpleBallSwapMod;
import net.fabricmc.api.ModInitializer;

public class SimpleBallSwapModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SimpleBallSwapMod.init();
    }

}
