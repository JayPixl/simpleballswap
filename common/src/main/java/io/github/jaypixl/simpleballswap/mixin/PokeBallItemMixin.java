package io.github.jaypixl.simpleballswap.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.item.PokeBallItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PokeBallItem.class)
public abstract class PokeBallItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void simpleballswap$preventThrowWhileOpeningWheel(
            Level world,
            Player player,
            InteractionHand usedHand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        if (!player.isShiftKeyDown()) {
            return;
        }

        EntityHitResult hitResult = simpleballswap$findOwnedPokemonHit(player);
        if (hitResult == null) {
            return;
        }

        cir.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(usedHand)));
    }

    private EntityHitResult simpleballswap$findOwnedPokemonHit(Player player) {
        double range = player.entityInteractionRange();
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getViewVector(1.0F).scale(range));
        AABB searchBox = player.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D);

        return ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                start,
                end,
                searchBox,
                entity -> entity instanceof PokemonEntity pokemonEntity
                        && pokemonEntity.getPokemon().getOwnerPlayer() == player
        );
    }
}
