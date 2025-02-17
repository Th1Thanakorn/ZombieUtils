package com.thana.zombie.event.handler;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thana.zombie.utils.Constants;
import com.thana.zombie.utils.sugarapi.FontColor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HUDRendererEventHandler {

    private final Minecraft mc = Minecraft.getInstance();

    public static long lastMoneyShown = -1L;

    @SubscribeEvent
    public void onRenderOverlayGui(RenderGuiOverlayEvent event) {
        PoseStack poseStack = event.getPoseStack();
        long now = System.currentTimeMillis();
        Window window = this.mc.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        if (this.mc.player == null) return;

        if (MainEventHandler.moneyAll > 0 && now - lastMoneyShown < 3000L) {
            this.drawMoneyLog(poseStack, 0.8F, now);
        }
        else {
            MainEventHandler.moneyAll = 0;
        }

        if (MainEventHandler.isZombie) {
            if (MainEventHandler.isAlienArcadium) {
                this.roundHandler(poseStack);
            }
            Gui.drawCenteredString(poseStack, this.mc.font, ChatFormatting.RED.toString() + MainEventHandler.health + "/" + (int) (this.mc.player.getMaxHealth()), width / 2 - 110, height - 16, FontColor.WHITE);
        }

        int index = 0;
        if (MainEventHandler.foundMaxAmmo) {
            renderBuffText(poseStack, ChatFormatting.BLUE + "Max Ammo", index++);
        }
        if (MainEventHandler.foundDoubleGold) {
            renderBuffText(poseStack, ChatFormatting.GOLD + "Double Gold", index++);
        }
        if (MainEventHandler.foundShoppingSpree) {
            renderBuffText(poseStack, ChatFormatting.DARK_PURPLE + "Shopping Spree", index);
        }
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

    public void renderRoundText(PoseStack poseStack, String text) {
        int hotbarWidth = 182;
        float renderX = hotbarWidth / 2.0F + this.mc.getWindow().getGuiScaledWidth() / 2.0F + 6.0F;
        float renderY = this.mc.getWindow().getGuiScaledHeight() - this.mc.font.lineHeight - 6.0F;
        this.mc.font.drawShadow(poseStack, text, renderX, renderY, FontColor.WHITE);
    }

    public void renderBuffText(PoseStack poseStack, String text, int index) {
        float width = this.mc.font.width(text);
        float renderX = this.mc.getWindow().getGuiScaledWidth() / 2.0F - width / 2.0F;
        float renderY = this.mc.getWindow().getGuiScaledHeight() / 2.0F + 16.0F + ((4 + this.mc.font.lineHeight) * index);
        this.mc.font.drawShadow(poseStack, text, renderX, renderY, FontColor.WHITE);
    }

    public void roundHandler(PoseStack poseStack) {
        int round = MainEventHandler.round;
        if (round == 15) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "1 Giant");
        }
        if (round == 20) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "2 Giants");
        }
        if (round == 22) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "2 Giants");
        }
        if (round == 24) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "3 Giants");
        }
        if (round == 25) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "Mega Blob");
        }
        if (round == 30) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "4 Giants");
        }
        if (round == 35) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "Mega Magma");
        }
        if (round >= 36 && round <= 39) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "3 Giants");
        }
        if (round == 40) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "4 Giants + 1 Old One");
        }
        if (round == 41) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "4 Giants");
        }
        if (round == 42 || round == 43) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "6 Giants");
        }
        if (round == 44) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "9 Giants");
        }
        if (round == 45) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "3 Giants + 2 Old Ones");
        }
        if (round == 46) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "1 Old One");
        }
        if (round == 47) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "4 Giants");
        }
        if (round == 48) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "1 Old One");
        }
        if (round >= 50 && round <= 54) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "4 Giants");
        }
        if (round == 55) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "5 Giants");
        }
        if (round == 56) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "Mega Blob");
        }
        if (round == 57) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "Mega Magma");
        }
        if (round == 58) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "6 Giants + 5 Old Ones");
        }
        if (round == 59) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "13 Old Ones");
        }
        if (round > 60 && round <= 100) {
            if (round % 10 == 0) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Giants and Old Ones");
            }
            if (round % 10 == 1) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Wolves and Magma cubes");
            }
            if (round % 10 == 2) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Clowns");
            }
            if (round % 10 == 3) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Wolves and Creepers");
            }
            if (round % 10 == 4) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Worms");
            }
            if (round % 10 == 5) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Giants and Old Ones");
            }
            if (round % 10 == 6) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Creepers and Magma cubes");
            }
            if (round % 10 == 7) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Fire Lord and Magma cubes");
            }
            if (round % 10 == 8) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "3 Old Ones");
            }
            if (round % 10 == 9) {
                this.renderRoundText(poseStack, ChatFormatting.RED + "Fire Lord and Magma cubes");
            }
        }
        if (round == 101) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "World Ender");
        }
        if (round > 101) {
            this.renderRoundText(poseStack, ChatFormatting.RED + "Fake Old Ones");
        }
    }
}
