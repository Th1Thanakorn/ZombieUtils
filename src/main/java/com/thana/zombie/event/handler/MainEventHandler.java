package com.thana.zombie.event.handler;

import com.google.common.collect.Iterables;
import com.thana.zombie.keys.KeyHandler;
import com.thana.zombie.utils.Constants;
import com.thana.zombie.utils.sugarapi.ClientUtils;
import com.thana.zombie.utils.sugarapi.MessageString;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.scores.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainEventHandler {

    private final Minecraft mc = Minecraft.getInstance();

    private static final Pattern COLOR_CODES_PATTERN = Pattern.compile("(?i)§[0-9A-Z]");
    private static final Pattern GOLD_PATTERN_1 = Pattern.compile("\\+(?<gold>[0-9]+) Gold");
    private static final Pattern GOLD_PATTERN_2 = Pattern.compile("\\+(?<gold>[0-9]+) Gold \\(Critical Hit\\)");

    public static boolean isZombie;
    public static boolean isSheep;
    public static boolean isAlienArcadium;
    public static boolean foundMaxAmmo;
    public static boolean foundDoubleGold;
    public static boolean foundShoppingSpree;
    public static int round;
    public static int moneyAll = 0;
    public static int health = 0;
    public static int currentHotbar = 1;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mc.player != null) {
            if (this.mc.level != null) {
                Objective objective = this.mc.level.getScoreboard().getDisplayObjective(1);
                Objective belowName = this.mc.level.getScoreboard().getDisplayObjective(2);

                if (objective == null) return;
                if (belowName != null) {
                    Score score = this.mc.level.getScoreboard().getOrCreatePlayerScore(this.mc.player.getScoreboardName(), belowName);
                    health = score.getScore();
                }

                Scoreboard scoreboard = this.mc.level.getScoreboard();
                Collection<Score> collection = scoreboard.getPlayerScores(objective);
                List<Score> list = collection.stream().filter((score) -> !score.getOwner().startsWith("#")).toList();
                if (list.size() > 15) {
                    collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15).iterator());
                } else {
                    collection = list;
                }
                for (Score score1 : collection) {
                    Team team = scoreboard.getPlayersTeam(score1.getOwner());
                    String scoreText = removeControlCodes(stripColorCodes(PlayerTeam.formatNameForTeam(team, Component.literal(MessageString.unformatted(score1.getOwner()))).getString()));
                    scoreText = cleanScoreboard(scoreText);
                    scoreText = scoreText.trim();
//                    if (scoreText.contains("Round")) {
//                        round = Integer.parseInt(scoreText.replaceAll("[^0-9.]", ""));
//                    }
                    if (scoreText.contains("Alien Arcadium")) {
                        isAlienArcadium = true;
                        break;
                    }
                }
                for (Score score1 : collection) {
                    Team team = scoreboard.getPlayersTeam(score1.getOwner());
                    String scoreText = PlayerTeam.formatNameForTeam(team, Component.literal(MessageString.unformatted(score1.getOwner()))).getString();
                    if (scoreText.contains("Round")) {
                        round = Integer.parseInt(scoreText.replaceAll("[^0-9]", ""));
                        if (Constants.debugging) {
                            this.mc.level.getEntitiesOfClass(ArmorStand.class, this.mc.player.getBoundingBox().inflate(100, 100, 100)).forEach(a -> {
                                String name = Objects.requireNonNullElse(a.getCustomName(), Component.empty()).getString();
                                ClientUtils.printClientMessage(name);
                            });
//                            ClientUtils.printClientMessage(scoreText);
//                            ClientUtils.printClientMessage(round);
                        }
                    }
                }

                MainEventHandler.isZombie = COLOR_CODES_PATTERN.matcher(objective.getDisplayName().getString()).replaceAll("").contains("ZOMBIES");
                MainEventHandler.isSheep = COLOR_CODES_PATTERN.matcher(objective.getDisplayName().getString()).replaceAll("").contains("SHEEP");

                if (this.mc.player.tickCount % 10 == 0) {
                    foundMaxAmmo = !this.mc.level.getEntitiesOfClass(ArmorStand.class, this.mc.player.getBoundingBox().inflate(50.0D), (stand) -> stand.hasCustomName() && MessageString.unformatted(stand.getCustomName().getString()).toLowerCase().contains("max ammo")).isEmpty();
                    foundDoubleGold = !this.mc.level.getEntitiesOfClass(ArmorStand.class, this.mc.player.getBoundingBox().inflate(50.0D), (stand) -> stand.hasCustomName() && MessageString.unformatted(stand.getCustomName().getString()).toLowerCase().contains("double gold")).isEmpty();
                    foundShoppingSpree = !this.mc.level.getEntitiesOfClass(ArmorStand.class, this.mc.player.getBoundingBox().inflate(50.0D), (stand) -> stand.hasCustomName() && MessageString.unformatted(stand.getCustomName().getString()).toLowerCase().contains("shopping spree")).isEmpty();
                }

                int rand = this.mc.level.getRandom().nextInt(5);
                if (this.mc.player.tickCount % (2+rand) == 0) {
                    if (Constants.swapping) {
                        currentHotbar %= 2;
                        currentHotbar++;
                        this.mc.player.getInventory().selected = currentHotbar;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = MessageString.unformatted(event.getMessage().getString());

        Matcher goldMatcher1 = GOLD_PATTERN_1.matcher(message);
        Matcher goldMatcher2 = GOLD_PATTERN_2.matcher(message);

        int gold = 0;
        if (goldMatcher1.matches()) {
            gold = Integer.parseInt(goldMatcher1.group("gold"));
        }
        if (goldMatcher2.matches()) {
            gold = Integer.parseInt(goldMatcher2.group("gold"));
        }
        moneyAll += gold;
        HUDRendererEventHandler.lastMoneyShown = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onKeyDown(InputEvent.Key event) {
        if (this.mc.player != null) {
            if (KeyHandler.KEY_TOGGLE_HIGHLIGHTS.isDown()) {
                Constants.toggleGlowing = !Constants.toggleGlowing;
                ClientUtils.printClientMessage("Glowing effect has been set to: " + (Constants.toggleGlowing ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"));
            } else if (KeyHandler.KEY_HIDE_PLAYERS.isDown()) {
                Constants.hidePlayers = !Constants.hidePlayers;
                ClientUtils.printClientMessage("Hide nearby players has been set to: " + (Constants.hidePlayers ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"));
            } else if (KeyHandler.KEY_DEBUGGING.isDown()) {
                Constants.debugging = !Constants.debugging;
                ClientUtils.printClientMessage("Debugging has been set to: " + (Constants.debugging ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"));
            } else if (KeyHandler.KEY_SWAP_WEAPONS.isDown()) {
                Constants.swapping = !Constants.swapping;
                ClientUtils.printClientMessage("Swapping weapons has been set to: " + (Constants.swapping ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"));
            }
        }
    }

    public static String stripColorCodes(String text) {
        return COLOR_CODES_PATTERN.matcher(text).replaceAll("");
    }

    public static String removeControlCodes(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '§' && i + 2 <= text.length()) {
                return text.substring(0, i) + text.substring(i + 2);
            }
        }
        return text;
    }

    public static String cleanScoreboard(String scoreboard) {
        char[] nvString = StringUtil.stripColor(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127) {
                cleaned.append(c);
            }
        }
        return cleaned.toString();
    }
}
