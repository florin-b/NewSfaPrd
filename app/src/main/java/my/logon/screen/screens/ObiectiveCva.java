package my.logon.screen.screens;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import my.logon.screen.model.UserInfo;
import my.logon.screen.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ObiectiveCva extends Activity {

	private static final String URL_TO_LOAD = "https://delegatii.arabesque.ro/ObiectiveCva/LoginFromLiteSfa";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.obiectivecva);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("ObiectiveCva");
		actionBar.setDisplayHomeAsUpEnabled(true);

		WebView webView = (WebView) findViewById(R.id.mobiectivecva);
		loadResource(webView, URL_TO_LOAD);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadResource(WebView webView, String resource) {

		StringBuilder postData = new StringBuilder();

		try {

			postData.append("nume=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getNume(), "UTF-8"));

			postData.append("&cod=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getCod(), "UTF-8"));

			postData.append("&filiala=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getFiliala(), "UTF-8"));

			postData.append("&tipAcces=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getTipAcces(), "UTF-8"));

			postData.append("&codDepart=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getCodDepart(), "UTF-8"));

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		webView.clearCache(true);
		webView.setWebViewClient(new WebViewClient());

		webView.postUrl(resource, postData.toString().getBytes());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		webView.setWebChromeClient(new WebChromeClient());

		webView.getSettings().setUseWideViewPort(true);

		webView.getSettings().setGeolocationEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {

			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
			}
		}

		);

		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	@Override
	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();

		return;
	}

}
