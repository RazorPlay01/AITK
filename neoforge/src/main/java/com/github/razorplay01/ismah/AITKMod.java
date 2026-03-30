package com.github.razorplay01.ismah;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class AITKMod {

    public AITKMod(IEventBus eventBus) {
        CommonClass.init();
    }
}