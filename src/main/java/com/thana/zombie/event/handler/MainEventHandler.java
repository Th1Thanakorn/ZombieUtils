package com.thana.zombie.event.handler;

import com.google.common.collect.Iterables;
import com.thana.zombie.keys.KeyHandler;
import com.thana.zombie.utils.Constants;
import com.thana.zombie.utils.sugarapi.ClientUtils;
import com.thana.zombie.utils.sugarapi.MessageString;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.scores.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainEventHandler {

    private final Minecraft mc = Minecraft.getInstance();

    private static final Pattern COLOR_CODES_PATTERN = Pattern.compile("(?i)§[0-9A-Z]");
    private static final Pattern GOLD_PATTERN_1 = Pattern.compile("\\+(?<gold>[0-9]+) Gold");
    private static final Pattern GOLD_PATTERN_2 = Pattern.compile("\\+(?<gold>[0-9]+) Gold \\(Critical Hit\\)");

    public static boolean isZombie;
    public static boolean isAlienArcadium;
    public static int round;
    public static int moneyAll = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mc.player != null) {
            if (this.mc.level != null) {
                Objective objective = this.mc.level.getScoreboard().getDisplayObjective(1);
                if (objective == null) return;

                Scoreboard scoreboard = this.mc.level.getScoreboard();
                Collection<Score> collection = scoreboard.getPlayerScores(objective);
                List<Score> list = collection.stream().filter((score) -> !score.getOwner().startsWith("#")).toList();
                if (list.size() > 15) {
                    collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15).iterator());
                }
                else {
                    collection = list;
                }
                for (Score score1 : collection) {
                    Team team = scoreboard.getPlayersTeam(score1.getOwner());
                    String scoreText = removeControlCodes(stripColorCodes(PlayerTeam.formatNameForTeam(team, Component.literal(MessageString.unformatted(score1.getOwner()))).getString()));
                    scoreText = cleanScoreboard(scoreText);
                    scoreText = scoreText.trim();
                    if (scoreText.contains("Round")) {
                        round = Integer.parseInt(scoreText.replaceAll("[^0-9.]", ""));
                    }
                    if (scoreText.contains("Alien Arcadium")) {
                        isAlienArcadium = true;
                        break;
                    }
                }

                MainEventHandler.isZombie = COLOR_CODES_PATTERN.matcher(objective.getDisplayName().getString()).replaceAll("").contains("ZOMBIES");
            }
        }
    }

    @SubscribeEvent
    public void onChatReceieved(ClientChatReceivedEvent event) {
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
            }
            else if (KeyHandler.KEY_HIDE_PLAYERS.isDown()) {
                Constants.hidePlayers = !Constants.hidePlayers;
                ClientUtils.printClientMessage("Hide nearby players has been set to: "+ (Constants.hidePlayers ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"));
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
