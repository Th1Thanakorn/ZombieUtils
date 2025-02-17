package com.thana.zombie.keys;

import com.thana.zombie.core.ZombieUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ZombieUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyHandler {

    public static final KeyMapping KEY_TOGGLE_HIGHLIGHTS = new KeyMapping("key.zombieutils.toggle_highlights", GLFW.GLFW_KEY_K, "key.categories.zombieutils");
    public static final KeyMapping KEY_HIDE_PLAYERS = new KeyMapping("key.zombieutils.hide_players", GLFW.GLFW_KEY_M, "key.categories.zombieutils");
    public static final KeyMapping KEY_DEBUGGING = new KeyMapping("key.zombieutils.debugging", GLFW.GLFW_KEY_N, "key.categories.zombieutils");
    public static final KeyMapping KEY_SWAP_WEAPONS = new KeyMapping("key.zombieutils.swap_weapons_1", GLFW.GLFW_KEY_X, "key.categories.zombieutils");

    @SubscribeEvent
    public static void init(RegisterKeyMappingsEvent event) {
        event.register(KEY_TOGGLE_HIGHLIGHTS);
        event.register(KEY_HIDE_PLAYERS);
        event.register(KEY_DEBUGGING);
        event.register(KEY_SWAP_WEAPONS);
    }
}
