package io.github.jaypixl.simpleballswap.neoforge;

import io.github.jaypixl.simpleballswap.SimpleBallSwapMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SimpleBallSwapMod.MOD_ID)
public class SimpleBallSwapModNeoForge {

    public SimpleBallSwapModNeoForge(IEventBus eventBus) {
        SimpleBallSwapMod.init();
    }
}
