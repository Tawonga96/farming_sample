package com.example.sampl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    // Declare UI elements
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button buttonRegister , buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.UsernameID);
        editTextEmail = findViewById(R.id.EmailID);
        editTextPassword = findViewById(R.id.PasswordID);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin= findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                registerUser(username, email, password);
            }
        });
    }

    private void registerUser(String username, String email, String password) {
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Define the request body as JSON
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"username\":\""+username+"\",\"email\":\""+email+"\",\"password\":\""+password+"\"}";
        RequestBody requestBody = RequestBody.create(JSON, json);

        // Define the request
        Request request = new Request.Builder()
                .url("http://10.0.2.2/android_api/register.php") // Replace with your API URL
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
                        Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Registration was successful
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, responseBody, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Registration failed - Log the response code and message
                    final int responseCode = response.code();
                    final String responseMessage = response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Registration failed. Code: " + responseCode + ", Message: " + responseMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}