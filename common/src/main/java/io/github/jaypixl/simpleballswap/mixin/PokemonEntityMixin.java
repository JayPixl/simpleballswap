package io.github.jaypixl.simpleballswap.mixin;

import com.cobblemon.mod.common.api.tags.CobblemonItemTags;
import com.cobblemon.mod.common.CobblemonCosmeticItems;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.item.PokeBallItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.cosmetic.CosmeticItemDefinition;
import io.github.jaypixl.simpleballswap.SimpleBallSwapMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin {
    @Redirect(
            method = "showInteractionWheel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/CobblemonCosmeticItems;findValidCosmeticForPokemonAndItem(Lnet/minecraft/core/RegistryAccess;Lcom/cobblemon/mod/common/pokemon/Pokemon;Lnet/minecraft/world/item/ItemStack;)Lcom/cobblemon/mod/common/pokemon/cosmetic/CosmeticItemDefinition;"
            )
    )
    private CosmeticItemDefinition simpleballswap$allowPokeBallTagAsCosmetic(
            RegistryAccess registryAccess,
            Pokemon pokemon,
            ItemStack itemStack
    ) {
        CosmeticItemDefinition result = CobblemonCosmeticItems.findValidCosmeticForPokemonAndItem(registryAccess, pokemon, itemStack);
        if (result != null || !itemStack.is(CobblemonItemTags.POKE_BALLS)) {
            return result;
        }

        return new CosmeticItemDefinition();
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void simpleballswap$preventClientBallUseWhenOpeningWheel(
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!player.level().isClientSide() || hand != InteractionHand.MAIN_HAND || !player.isShiftKeyDown()) {
            return;
        }

        PokemonEntity pokemonEntity = (PokemonEntity) (Object) this;
        if (!player.getUUID().equals(pokemonEntity.getOwnerUUID())) {
            return;
        }

        if (!player.getItemInHand(hand).is(CobblemonItemTags.POKE_BALLS)) {
            return;
        }

        cir.setReturnValue(InteractionResult.SUCCESS_NO_ITEM_USED);
    }

    @Inject(method = "offerItem", at = @At("HEAD"), cancellable = true)
    private void simpleballswap$handlePokeBallCosmeticOffer(
            Player player,
            ItemStack stack,
            boolean isCosmetic,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!isCosmetic || !(stack.getItem() instanceof PokeBallItem pokeBallItem)) {
            return;
        }

        PokemonEntity pokemonEntity = (PokemonEntity) (Object) this;
        if (pokemonEntity.isBusy() || !player.getUUID().equals(pokemonEntity.getOwnerUUID())) {
            cir.setReturnValue(false);
            return;
        }

        if (player.level().isClientSide()) {
            cir.setReturnValue(false);
            return;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();
        if (pokemon.getCaughtBall() == pokeBallItem.getPokeBall()) {
            // Add "Same ball" message
            player.sendSystemMessage(Component.literal(pokemon.getDisplayName(false).getString() + "'s ball is already " + pokeBallItem.getName(stack).getString() + "!").withStyle(ChatFormatting.RED));
            cir.setReturnValue(true);
            return;
        }

        if (SimpleBallSwapMod.CONFIG.returnOldBall) {
            player.addItem(pokemon.getCaughtBall().stack(1));
        }

        pokemon.setCaughtBall(pokeBallItem.getPokeBall());
        if (!player.isCreative() && SimpleBallSwapMod.CONFIG.consumeBall) {
            stack.shrink(1);
        }

        // Fix this mess
        player.sendSystemMessage(Component.literal("Changed " + pokemon.getDisplayName(false).getString() + "'s ball to " + pokeBallItem.getName(stack).getString() + "!"));


        cir.setReturnValue(true);
    }
}
