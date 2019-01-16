package com.groges.wiskulmokerguide;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Moker extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/neosanspro_medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
