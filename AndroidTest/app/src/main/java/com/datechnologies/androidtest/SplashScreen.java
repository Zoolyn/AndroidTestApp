package com.datechnologies.androidtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.datechnologies.androidtest.chat.fetchChatData;

import com.datechnologies.androidtest.chat.ChatActivity;

public class SplashScreen extends AppCompatActivity {

    protected void loadNextPage() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        // Unpack the bundle to find out which screen is next
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String activity = b.getString("activity");

        String screen = activity;
        switch (screen) {
            case "Chat":
                //Log.d("Chat", "Start of getting data for chat");
                new fetchChatData(SplashScreen.this).execute();
                break;
            default:
                Log.d("Nothing","There was no page");
        }

    }
}