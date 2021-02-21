package com.datechnologies.androidtest.chat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.chat.ChatActivity;
import com.datechnologies.androidtest.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// Allow us to fetch the data from the url without hanging up the app while waiting for the data to get here
public class fetchChatData extends AsyncTask<Void, Void, Void> {

    String chatData = "";
    Context context;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    public fetchChatData(Context context) {
        this.context = context.getApplicationContext();
    }

    // Fetch Data from the URL
    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection httpURLConnection = null;
        try {
            // Establish connection to URL
            URL url = new URL("http://dev.rapptrlabs.com/Tests/scripts/chat_log.php");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // Read in the data
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                chatData = chatData +  line;
            }
            // format the chat data
            JSONObject jsonObj = new JSONObject(chatData);
            JSONArray data = jsonObj.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
                String name = d.getString("name");
                String avatar_url = d.getString("avatar_url");
                String message = d.getString("message");
                messages.add(message);
                names.add(name);
                Log.d("data",name + ": " + message);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // If there is a connection close it
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Log.d("data", chatData);
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra("messages", messages);
        i.putExtra("names", names);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); context.startActivity(i);
        context.startActivity(i);
    }
}
