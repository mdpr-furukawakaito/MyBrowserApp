package com.example.mybrowserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private EditText urlText;

    private static final String INITAL_WEBSITE = "https://dotinstall.com";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = (WebView) findViewById(R.id.myWebView);
        urlText = (EditText) findViewById(R.id.urlText);

        //脆弱性に弱くなる
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                getSupportActionBar().setSubtitle(view.getTitle());
                urlText.setText(url);

            }
        });

        myWebView.loadUrl(INITAL_WEBSITE);
    }

    public void showWebsite(View view) {
        String url = urlText.getText().toString().trim();

        // https://dotinstall.com
        // dotinstall.com
        if (Patterns.WEB_URL.matcher(url).matches()){
            urlText.setError("Invalid URL");
        }else {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            myWebView.loadUrl(url);
        }

    }

    public void clearUrl(View view) {
        urlText.setText("");
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myWebView != null) {
            myWebView.stopLoading();
            myWebView.setWebViewClient(null);
            myWebView.destroy();
        }
        myWebView = null;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem forwardItem = (MenuItem) menu.findItem(R.id.action_forward);
        MenuItem backItem = (MenuItem) menu.findItem(R.id.action_back);
        forwardItem.setEnabled(myWebView.canGoForward());
        backItem.setEnabled(myWebView.canGoBack());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id){
            case R.id.action_reload:
                myWebView.reload();
                return true;
            case R.id.action_forward:
                myWebView.goForward();
                return true;
            case R.id.action_back:
                myWebView.goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
