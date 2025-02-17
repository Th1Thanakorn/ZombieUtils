package com.thana.zombie.mixin;

import com.thana.zombie.event.handler.MainEventHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"))
    public int getRenderColor(Entity entity) {
        if (MainEventHandler.isZombie) {
            if (entity instanceof Zombie zombie) {
                // Baby ? Green : White
                return zombie.isBaby() ? 5635925 : 16777215;
            }
            if (entity instanceof Skeleton) {
                return 16755200; // Gold
            }
            if (entity instanceof Giant) {
                return 16733525; // Red
            }
            if (entity instanceof Witch) {
                return 16777215; // White
            }
            if (entity instanceof WitherSkeleton) {
                return 5636095; // Aqua
            }
        }
        if (MainEventHandler.isSheep/* && Config.healthTracking*/) {
            if (entity instanceof Player player) {
                float f = player.getHealth() / (player.getMaxHealth());

                return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
            }
        }
        return entity.getTeamColor();
    }
}