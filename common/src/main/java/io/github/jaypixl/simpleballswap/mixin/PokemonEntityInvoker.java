package io.github.jaypixl.simpleballswap.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PokemonEntity.class)
public interface PokemonEntityInvoker {
    @Invoker("showInteractionWheel")
    void simpleballswap$showInteractionWheel(ServerPlayer player, ItemStack itemStack);
}
