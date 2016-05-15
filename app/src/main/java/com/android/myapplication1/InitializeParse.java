package com.android.myapplication1;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Caesar on 2016-05-05.
 */
public class InitializeParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Parse.initialize(this,"JR9CF7j9QiatGkV2Cwzzoz0AKb8CNfdTxQ9UGdHo","4CxrMoElae78vSiEBqwgwy79aP573Gp3eJ9h1xPT");
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vodbH5IlecLVicHxVUlWMphmuspBoJ4ejx3m8rzD")
                .clientKey("Oso7Flf8l2Vw0ncqBZMSw6HXCI21X2ZmUAch5SBN")
                .server("https://parseapi.back4app.com/")

                .build()
        );

    }


}
