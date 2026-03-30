package com.github.razorplay01.ismah;

import com.github.razorplay01.ismah.platform.Services;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Initializing {} on {}", Constants.MOD_ID, Services.PLATFORM.getPlatformName());
    }
}