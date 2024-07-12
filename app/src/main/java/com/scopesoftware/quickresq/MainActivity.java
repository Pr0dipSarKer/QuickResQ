package com.scopesoftware.quickresq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText login_username ,login_password;
    Button login_button,adminBtn,SholderBtn;
    TextView register_nu;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//----------------referencing part-------------------------------
        login_username= findViewById(R.id.r_email);
        login_password= findViewById(R.id.r_Cpassword);
        login_button= findViewById(R.id.add_cart_med);
        register_nu= findViewById(R.id.registered);

        //----referencing part end-------

        requestQueue = Volley.newRequestQueue(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
                //  startActivity(new Intent(MainActivity.this,Home1.class));

            }
        });

        register_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, signup.class));
            }
        } );



    }   //-----oncreate end---------

    private void loginUser() {
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
// Check if username or password is empty
        if (username.isEmpty()) {
            login_username.setError("Username is required");
        }
        if (password.isEmpty()) {
            login_password.setError("Password is required");
        }
        if (username.isEmpty() || password.isEmpty()) {
            // Show a toast message for overall feedback
            Toast.makeText(MainActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
            return; // Exit the method if inputs are empty
        }

        // Replace with your server URL
        String url = getString(R.string.onlineserverdomain)+"signupview.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                String storedUsername = user.getString("username");
                                String storedPassword = user.getString("password");

                                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                                    // Login successful
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,Home1.class));
                                  //  EditProfile.login_username2=username;
                                    return;
                                }
                            }
                            // Login failed
                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle error
                        Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }
}