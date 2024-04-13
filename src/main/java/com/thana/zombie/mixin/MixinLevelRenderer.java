package com.thana.zombie.mixin;

import com.thana.zombie.event.handler.MainEventHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
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
        }
        return entity.getTeamColor();
    }
}