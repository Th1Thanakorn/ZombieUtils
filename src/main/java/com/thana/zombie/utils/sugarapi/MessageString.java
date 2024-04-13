package com.thana.zombie.utils.sugarapi;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class MessageString {

    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");

    @NotNull
    public static String unformatted(@NotNull String message) {
        return STRIP_FORMATTING_PATTERN.matcher(message).replaceAll("");
    }

    @NotNull
    public static String formatted(@NotNull String message, ChatFormatting color) {
        return color.toString() + message;
    }

    @NotNull
    public static String formatted(@NotNull String message, ChatFormatting color, ChatFormatting modifier) {
        return color.toString() + modifier.toString() + message;
    }
}
