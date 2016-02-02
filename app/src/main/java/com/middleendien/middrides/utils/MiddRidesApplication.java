package com.middleendien.middrides.utils;

import android.app.Application;

import com.middleendien.middrides.R;
import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Peter on 10/15/15.
 * To avoid parse not being initialised upon quick log-back-in after terminating the app
 */
public class MiddRidesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Set up Parse Environment
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "II5Qw9I5WQ5Ezo9mL8TdYj3mEoiSFcdt8GFMAgsm", "EIepTgb590NQw5DDu1EccT7YvprP2ovLesj1t3Nd");

        // The fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("TikalSans-Medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }
}
