package com.thana.zombie.event.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thana.zombie.utils.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HUDRendererEventHandler {

    private final Minecraft mc = Minecraft.getInstance();

    public static long lastMoneyShown = -1L;

    @SubscribeEvent
    public void onRenderOverlayGui(RenderGuiOverlayEvent event) {
        PoseStack poseStack = event.getPoseStack();
        long now = System.currentTimeMillis();

        if (MainEventHandler.moneyAll > 0 && now - lastMoneyShown < 3000L) {
            this.drawMoneyLog(poseStack, 0.8F, now);
        }
        else {
            MainEventHandler.moneyAll = 0;
        }

        this.roundHandler();
    }

    @SubscribeEvent
    public void renderEntity(RenderPlayerEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        if (entity != this.mc.player && entity instanceof Player && Constants.hidePlayers && this.mc.player.distanceTo(entity) < 1.25D) {
            event.setCanceled(true);
        }
    }

    public void drawMoneyLog(PoseStack poseStack, float scale, long now) {
        float x = 8;
        float y = 35;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        x = x * 1/scale;
        y = y * 1/scale;

        String text = String.format(ChatFormatting.GOLD + "+%d Gold", MainEventHandler.moneyAll);
        float stringY = y;
        int color = FastColor.ABGR32.color(80, 255, 255, 255);
        this.mc.font.drawShadow(poseStack, text, x, stringY, color);
        poseStack.popPose();
    }

    public void roundHandler() {

    }
}
