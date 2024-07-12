package com.scopesoftware.quickresq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SubmitReport extends AppCompatActivity {
    private EditText name21, name22, name23,locaation1;
    private Button confirmOrderButton, deleteOrderButton;
    ProgressBar simpleProgressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submit_report);


        name21 = findViewById(R.id.name21);
        name22 = findViewById(R.id.name22);
        name23 = findViewById(R.id.name23);
        locaation1=findViewById(R.id.locaation1);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);
        deleteOrderButton = findViewById(R.id.deleteOrderButton);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);


        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name21S = name21.getText().toString();
                String name22S = name22.getText().toString();
                String name23S = name23.getText().toString();
                String locaation1S = locaation1.getText().toString();
             //   String Cpassword = r_Cpassword.getText().toString();

                simpleProgressBar.setVisibility(View.VISIBLE);



                if (name21.length() == 0 || name22.length() == 0 || name23.length() == 0 || locaation1.length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Please fill the complete information", Toast.LENGTH_SHORT).show();
                }

                else {
                  //  if (password.compareTo(Cpassword) == 0) {
                   //     if (valid(password)) {
                            //----------------------------
                            String url =getString(R.string.onlineserverdomain) + "reportsubmit.php?name21=" + name21S
                                    + "&name22=" + name22S + "&name23=" + name23S + "&locaation1=" + locaation1S;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    simpleProgressBar.setVisibility(View.GONE);




                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            RequestQueue requestQueue = Volley.newRequestQueue(SubmitReport.this);
                            requestQueue.add(stringRequest);
                            //---------------
                            Toast.makeText(getApplicationContext(), "REPORT SUBMIT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SubmitReport.this, Home1.class));
                   //     }    //VSILD END

              //      } //---PASS


                }   //--------------else end






            }
        });

    } //----oncreate  end-----


}