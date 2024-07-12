package com.scopesoftware.quickresq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

public class Police extends AppCompatActivity {

    public static String current_location3="";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_PERMISSION_REQUEST_CODE = 123;
    private WebView webView;
    private FusedLocationProviderClient fusedLocationClient;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_police);
        webView =findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("tel:")) {
                    handleTelLink(url);
                    return true;
                }
                return false;
            }
        });

        // Build the URL with the upzela variable
        String url = "https://www.google.com/maps/search/Police/station+in+" +current_location3;
        Log.d("fayaj", url);
        webView.loadUrl(url);


    }
    //---cheak permission from manifest------------
    private void handleTelLink(String url) {
        if (ContextCompat.checkSelfPermission(Police.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Police.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        } else {
            startCallIntent(url);
        }
    }
    //-------------
    private void startCallIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with the call
                webView.reload(); // Reload the WebView to re-trigger the link click
            } else {
                // Permission was denied, show a message to the user
                Toast.makeText(Police.this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}