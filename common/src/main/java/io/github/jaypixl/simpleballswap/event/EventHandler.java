package io.github.jaypixl.simpleballswap.event;

import com.cobblemon.mod.common.api.events.pokemon.HeldItemEvent;
import com.cobblemon.mod.common.api.tags.CobblemonItemTags;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.item.PokeBallItem;
import com.cobblemon.mod.common.platform.events.ServerPlayerEvent;
import io.github.jaypixl.simpleballswap.SimpleBallSwapMod;
import io.github.jaypixl.simpleballswap.mixin.PokemonEntityInvoker;
import net.minecraft.world.InteractionHand;

public final class EventHandler {
    public static void handleCosmeticPre(HeldItemEvent.Pre e) {
        if (e.getReceiving().getTags().anyMatch(i -> i.equals(CobblemonItemTags.POKE_BALLS))) {
            if (!(e.getReceiving().getItem() instanceof PokeBallItem pokeBallItem)) {
                return;
            }

            e.getPokemon().setCaughtBall(pokeBallItem.getPokeBall());
            if (e.getDecrement()) {
                e.getReceiving().shrink(1);
            }
            e.cancel();
        }
    }

    public static void handleRightClickEntity(ServerPlayerEvent.RightClickEntity e) {
        if (e.getHand() != InteractionHand.MAIN_HAND) {
            // SimpleBallSwapMod.LOGGER.info("NOT MAIN HAND: " + e.getHand());
            return;
        }

        if (!e.getPlayer().isShiftKeyDown()) {
            // SimpleBallSwapMod.LOGGER.info("NOT SHIFT: " + e.getPlayer().isShiftKeyDown());
            return;
        }

        if (!(e.getEntity() instanceof PokemonEntity pokemonEntity)) {
            // SimpleBallSwapMod.LOGGER.info("NOT POKEMON: " + e.getEntity().isShiftKeyDown());
            return;
        }

        if (!e.getItem().is(CobblemonItemTags.POKE_BALLS)) {
            // SimpleBallSwapMod.LOGGER.info("NOT POKEBALL: " + e.getItem().getTags());
            return;
        }

        if (pokemonEntity.getPokemon().getOwnerPlayer() != e.getPlayer()) {
            // SimpleBallSwapMod.LOGGER.info("NOT OWNER: " + pokemonEntity.getPokemon().getOwnerPlayer());
            return;
        }

        e.cancel();
        ((PokemonEntityInvoker) pokemonEntity).simpleballswap$showInteractionWheel(e.getPlayer(), e.getItem());
    }
}
