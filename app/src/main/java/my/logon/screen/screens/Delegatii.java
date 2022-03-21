package my.logon.screen.screens;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import my.logon.screen.model.UserInfo;
import my.logon.screen.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Delegatii extends Activity {

	private static final String URL_TO_LOAD = "https://delegatii.arabesque.ro/redirect";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.delegatii);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Delegatii");
		actionBar.setDisplayHomeAsUpEnabled(true);

		WebView webView = (WebView) findViewById(R.id.mdelegatii);
		loadResource(webView, URL_TO_LOAD);

	}

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

			postData.append("&unitLog=");
			postData.append(URLEncoder.encode(UserInfo.getInstance().getUnitLog(), "UTF-8"));
			
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
