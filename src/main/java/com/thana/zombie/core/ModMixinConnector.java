package com.thana.zombie.core;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class ModMixinConnector implements IMixinConnector {

    public void connect() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.zombieutils.json");
    }
}
