package com.example.k_talk.Aulas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.k_talk.R;

public class Aula02Activity extends AppCompatActivity {

    private WebView myAula02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula02);

        myAula02 = (WebView) findViewById(R.id.webview_aula02);
        myAula02.loadUrl("https://aula02-f900e.web.app/");

        myAula02.setWebViewClient(new Aula02Activity.MyWebViewClient());

        WebSettings webSettings = myAula02.getSettings();
        //Habilitando JavaScript
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myAula02.canGoBack()) {
            myAula02.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("aula02-f900e.web.app".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    public void gotoHome(View view) {
        finish();
    }
}
