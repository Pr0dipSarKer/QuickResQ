package com.scopesoftware.quickresq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Nearestblood extends AppCompatActivity {

    GridView recyclerview1,recyclerview2;
    public static String current_location5="";

    Button buttnId2,buttnId1;

    ProgressBar simpleProgressBar;
    ImageView action_menu_image;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList();
    HashMap<String, String> hashMap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearestblood);


        recyclerview1=findViewById(R.id.recyclerview1);


        buttnId1=findViewById(R.id.buttnId1);

        //  action_menu_image=findViewById(R.id.action_menu_image);
        simpleProgressBar=findViewById(R.id.simpleProgressBar);

        buttnId1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Nearestblood.this,Blood.class));
            }
        });

        Createtable1();
        // Createtable2();



    }  //----oncreate end-----

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.design_rider, null);
            TextView usernameId = myView.findViewById(R.id.usernameId);
            TextView reId = myView.findViewById(R.id.reId);
            TextView ridId = myView.findViewById(R.id.ridId);
            TextView location = myView.findViewById(R.id.location);
            TextView phoneNumbId=myView.findViewById(R.id.phoneNumbId);
            TextView bloodId=myView.findViewById(R.id.bloodId);
            ImageView callId=myView.findViewById(R.id.callId);




            HashMap<String, String> hashMap = arrayList.get(i);




            String id1 = hashMap.get("id");
            String bloodgrp1 = hashMap.get("bloodgrp");
            String phone1 = hashMap.get("phone");
            String fullName1 = hashMap.get("fullName");
            String UpzilaName1 = hashMap.get("UpzilaName");

            //  String video_url= hashMap.get("video_url");
            //String image_url="title";


//            Picasso.get()
//                    .load(imgUrl)
//                    .into(holder.desgnIimage_button);


            usernameId.setText(id1);
            reId.setText("Reg id no: "+id1);
            ridId.setText("Full name: "+fullName1);
            location.setText("Location: "+UpzilaName1);
            phoneNumbId.setText(phone1);
            bloodId.setText("Blood group: "+bloodgrp1);


            // code_id1.setText(pruduct_code1);

          /*  Picasso.get()
                    .load(poster1)
                    .into(desgnIimage_button);

           */

            //---------------------------------
            callId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone1));
                    if (ContextCompat.checkSelfPermission(Nearestblood.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    } else {
                        ActivityCompat.requestPermissions(Nearestblood.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                    }

                }
            });



//------------------
    /*        postdesignid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Homedetails.startmonth=startmonth1;
                    Homedetails.location=location1;
                    Homedetails.details=details1;
                    Homedetails.flatdetails=flatdetails1;
                    Homedetails.phoneone= String.valueOf(phoneone);
                    Homedetails.whatsapp=whatsapp1;
                    Homedetails.gmail=gmail1;
                    Bitmap bitmap=((BitmapDrawable)imgid1.getDrawable()).getBitmap();
                    Homedetails.MY_BITMAP=bitmap;



                }
            });
            //========================================

            phoneone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < arrayList.size(); i++) {
                        for (int video_url = 0; video_url < arrayList.size(); video_url++) {
                            //   tvdisply1.loadUrl(String.valueOf(video_url));

                        }


                    }
                }
            });

     */

            return myView;


        }


    }

    //-----------------
    public void Createtable1 () {

//---ei link basa vara post view er jonno---------
        String url = getString(R.string.onlineserverdomain) + "/bloodsarch.php";


// Request a string response from the provided URL.


        JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("serverres", String.valueOf(jsonArray));
                //if WebView load successfully then VISIBLE fab Button
                simpleProgressBar.setVisibility(View.GONE);
                //  homefrg_lin.setVisibility(View.VISIBLE);

                try {

                    for (int x=0; x<jsonArray.length(); x++){

                        JSONObject jsonObject=jsonArray.getJSONObject(x);
                        String id1=jsonObject.getString("id"); //ei dan pasher key gola php json theke aasbe
                        String bloodgrp1=jsonObject.getString("bloodgrp");
                        String phone1=jsonObject.getString("phone");
                        String fullName1=jsonObject.getString("fullName");
                        String UpzilaName1=jsonObject.getString("UpzilaName");



                        // String video_url=jsonObject.getString("video_url");


                        // fsttext.append(title);
                        hashMap = new HashMap<>();
                        hashMap.put("id", id1);
                        hashMap.put("bloodgrp", bloodgrp1);
                        hashMap.put("phone", phone1);
                        hashMap.put("fullName", fullName1);
                        hashMap.put("UpzilaName", UpzilaName1);


                        //  hashMap.put("video_url", video_url);
                        arrayList.add(hashMap);

                    }


                    MyAdapter myAdapter=new MyAdapter();
                    recyclerview1.setAdapter(myAdapter);
                    //etar name jodio recycleview eta sodo id..full  kaj korchi grid niye


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

// Add the request to the RequestQueue.

        RequestQueue requestQueue= Volley.newRequestQueue(Nearestblood.this);
        ((RequestQueue) requestQueue).add(arrayRequest);


        // set the maximum width and height of the ImageView to 200 pixels


    }

    @Override
    public void onBackPressed() {
        // Navigate back to Blood activity
        super.onBackPressed();
        Intent intent = new Intent(Nearestblood.this, Home1.class);
        startActivity(intent);
        finish(); // Optional: Close Emergency2 activity to prevent going back to it on back press
    }

}