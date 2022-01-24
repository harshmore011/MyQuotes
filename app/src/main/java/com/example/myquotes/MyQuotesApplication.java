package com.example.myquotes;

import android.app.Application;

public class MyQuotesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing ObjectBox
        ObjectBox.init(this);
    }
}
