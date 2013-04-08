package com.scaveture.android;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Main extends Activity {
	private static final int CAMERA_RESULT = 1;
	private WebView webView = null;
	public static final String REQUEST_URL_KEY = "request_url";
	private boolean isFirstStart = true;
	private static final String host = "scaveture-two.appspot.com";

	@Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        webView = (WebView)this.findViewById(R.id.webView);
        webView.setWebViewClient(new MainWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

    }

    @Override
    protected void onStart() {
    	super.onStart();
    	if(isFirstStart) {
	        webView.loadUrl("http://" + host + "/Scaveture.html?mc=1");
	        isFirstStart = false;
    	}
    }

    private class MainWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	String prefix = view.getOriginalUrl() + "&path=";
            if(url.startsWith(prefix)) {
            	Intent intent = new Intent(Main.this, Picture.class);
            	intent.putExtra(REQUEST_URL_KEY, url);
            	startActivityForResult(intent, CAMERA_RESULT);
            	return true;
            }
            return false;
        }
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case (CAMERA_RESULT): {
				if (resultCode == Activity.RESULT_OK) {
					try {
						String id = data.getStringExtra(Picture.ID);
						String path = data.getStringExtra(Picture.PATH);
						String latitude = data.getStringExtra(Picture.LATITUDE);
						String longitude = data.getStringExtra(Picture.LONGITUDE);
						HttpClient httpClient = new DefaultHttpClient();
						final String url = "http://" + host + "/scaveture/submission?hunt=" + id + "&lat=" + latitude + "&long=" + longitude;
						android.util.Log.d(getClass().getName(), "Post URL: " + url);
						HttpPost httpPost = new HttpPost(url);
						File file = new File(path);
						FileEntity entity = new FileEntity(file, "image/jpeg");
						httpPost.setEntity(entity);
						HttpResponse response = httpClient.execute(httpPost);
						android.util.Log.d(getClass().getName(), "Servelet POST in onActivityResult returns status code: " + response.getStatusLine().getStatusCode());
					} 
					catch (Exception e) {
						android.util.Log.e(getClass().getName(), "Exception in onActivityResult", e);
					}
				}
				break;
			}
		}
	}
}