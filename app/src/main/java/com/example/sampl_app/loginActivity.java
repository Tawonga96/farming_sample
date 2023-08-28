package com.example.sampl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity {
    // Declare UI elements
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.passwordID);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Define the request body as JSON
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
        RequestBody requestBody = RequestBody.create(JSON, json);

        // Define the request
        Request request = new Request.Builder()
                .url("http://10.0.2.2/android_api/login.php") // Replace with your API URL
                .post(requestBody)
                .header("Content-Type", "application/json") // Add this line
                .build();

        // Make the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle request failure (e.g., network issues)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(loginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Login was successful
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(loginActivity.this, responseBody, Toast.LENGTH_SHORT).show();
                            // You can navigate to another activity or perform other actions here on successful login
                        }
                    });
                } else {
                    // Login failed - Log the response code and message
                    final int responseCode = response.code();
                    final String responseMessage = response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(loginActivity.this, "Login failed. Code: " + responseCode + ", Message: " + responseMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}