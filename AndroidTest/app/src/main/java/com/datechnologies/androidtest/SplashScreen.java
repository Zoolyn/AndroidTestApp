package com.datechnologies.androidtest;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
        String activity = "";
        if(b != null){
            activity = b.getString("activity");
        }

        // Was implemented to try to account for multiple screens that may have need to the splash screen for loading times, such as the chat screen
        switch (activity) {
            case "Chat":
                //Log.d("Chat", "Start of getting data for chat");
                new fetchChatData(SplashScreen.this).execute();
                break;
            default:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
                break;
        }

    }
}