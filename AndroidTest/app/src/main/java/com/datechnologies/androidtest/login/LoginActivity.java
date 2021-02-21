package com.datechnologies.androidtest.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.SplashScreen;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 *
 */
public class LoginActivity extends AppCompatActivity {

    //==============================================================================================
    // UI
    //==============================================================================================

    Button loginButton;

    //==============================================================================================
    // Retrofit Instance
    //==============================================================================================

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://dev.rapptrlabs.com/Tests/scripts/login.php/")
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit = builder.build();

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // TODO: Add a ripple effect when the buttons are clicked
        // TODO: Save screen state on screen rotation, inputted username and password should not disappear on screen rotation

        // TODO: Send 'email' and 'password' to http://dev.rapptrlabs.com/Tests/scripts/login.php
        // TODO: as FormUrlEncoded parameters.

        // TODO: When you receive a response from the login endpoint, display an AlertDialog.
        // TODO: The AlertDialog should display the 'code' and 'message' that was returned by the endpoint.
        // TODO: The AlertDialog should also display how long the API call took in milliseconds.
        // TODO: When a login is successful, tapping 'OK' on the AlertDialog should bring us back to the MainActivity

        // TODO: The only valid login credentials are:
        // TODO: email: info@rapptrlabs.com
        // TODO: password: Test123
        // TODO: so please use those to test the login.

        loginButton = (Button)findViewById(R.id.login_button);

        // Create Button on click handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get login credentials from the user save to variables
                String email = "";
                String password = "";
                EditText emailTextField = (EditText)findViewById(R.id.editTextTextEmailAddress);
                EditText passwordTextField = (EditText)findViewById(R.id.editTextTextPassword);

                email = emailTextField.getText().toString();
                password = passwordTextField.getText().toString();

                Log.d("test", email + "," + password);
                executeSendLoginCredentials(email,password);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Handle executing on button press
    private  void executeSendLoginCredentials(String email, String password) {
        LoginPost loginPost = retrofit.create(LoginPost.class);

        Call<ResponseBody> call = loginPost.sendLoginCredentials(
                email,
                password
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("message", String.valueOf(response.code()));
                // Based on the response from the server display a different alert dialog

                // Yay we made it!
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                if(response.code() == 200) {
                    builder.setTitle("Login Successful!");
                    builder.setMessage("This is the correct email and password! You will now be redirected to the main activity page.");
                    builder.setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                            // This is prevent unwanted backtracking to a previous session
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    builder.setTitle("Error! Code: " + response.code());
                    builder.setMessage(response.message());
                    builder.setPositiveButton(R.string.alert_confirm,null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Connection error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
