package com.example.webviewua.app;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


    private EditText mAddressEditText;
    private WebView mWebView;
    private CheckBox mSetAsDefaultCheckbox;

    private final static String DEFAULT_URL_PREF_KEY = "com.example.webviewua.app.DEFAULT_URL_PREF_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false; 
            }

        });
        String ua = mWebView.getSettings().getUserAgentString() ;
        ((TextView) findViewById(R.id.userAgentTextView)).setText(ua);
        mAddressEditText = (EditText) findViewById(R.id.addressEditText);
        mSetAsDefaultCheckbox = (CheckBox) findViewById(R.id.defaultCheckbox);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(ua);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(getString(R.string.app_name), ua);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(this, getString(R.string.copiedToClipboard), Toast.LENGTH_LONG).show();

        String defaultUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(DEFAULT_URL_PREF_KEY, null);
        if(defaultUrl != null){
            mAddressEditText.setText(defaultUrl);
            onGoClicked(null);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGoClicked(View v){
        String url = mAddressEditText.getText().toString();
        mWebView.loadUrl(url);
        if(mSetAsDefaultCheckbox.isChecked()){
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(DEFAULT_URL_PREF_KEY, url).commit();
        }
    }

}
