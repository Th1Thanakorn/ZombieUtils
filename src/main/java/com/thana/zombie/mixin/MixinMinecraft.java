package com.thana.zombie.mixin;

import com.thana.zombie.event.handler.MainEventHandler;
import com.thana.zombie.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Final public Options options;

    @Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
    public void shouldEntityAppearGlowing(Entity entity, CallbackInfoReturnable<Boolean> infoReturnable) {
        infoReturnable.setReturnValue(MainEventHandler.isZombie ? (entity instanceof Witch || entity instanceof WitherSkeleton || entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Giant) && Constants.toggleGlowing : entity.isCurrentlyGlowing() || this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isDown() && entity.getType() == EntityType.PLAYER);
    }
}
