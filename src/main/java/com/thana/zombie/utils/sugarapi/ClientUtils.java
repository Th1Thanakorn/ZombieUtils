package com.thana.zombie.utils.sugarapi;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ClientUtils {

    public static void printClientMessage(String text) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(text));
    }

    public static void printClientMessage(Object obj) {
        printClientMessage(obj.toString());
    }
}
