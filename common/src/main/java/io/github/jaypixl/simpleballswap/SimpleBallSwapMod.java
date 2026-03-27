package io.github.jaypixl.simpleballswap;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import io.github.jaypixl.simpleballswap.config.ConfigLoader;
import io.github.jaypixl.simpleballswap.config.SimpleBallSwapConfig;
import io.github.jaypixl.simpleballswap.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleBallSwapMod {
    public static final String MOD_ID = "simpleballswap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SimpleBallSwapConfig CONFIG;

    public static void init() {
        ConfigLoader.load();

        CobblemonEvents.COSMETIC_ITEM_PRE.subscribe(EventHandler::handleCosmeticPre);
        PlatformEvents.RIGHT_CLICK_ENTITY.subscribe(EventHandler::handleRightClickEntity);
    }
}
