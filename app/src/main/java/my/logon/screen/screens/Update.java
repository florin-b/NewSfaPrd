/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import my.logon.screen.R;
import my.logon.screen.model.UserInfo;

public class Update extends Activity {

	Button buttonUpdate, buttonInstall;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private TextView currVer, installDate, newVersion, newVersionLabel, releaseDate, releaseDateLabel, currVerCode,
			newVersionCodeLabel, newVersionCode;
	public FTPClient mFTPClient = null;
	String version = "0", buildVer = "0";
	String tipAcces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.update);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(true);

		currVer = (TextView) findViewById(R.id.currVer);
		installDate = (TextView) findViewById(R.id.installDate);

		newVersion = (TextView) findViewById(R.id.newVersion);
		newVersionLabel = (TextView) findViewById(R.id.newVersionLabel);

		releaseDate = (TextView) findViewById(R.id.releaseDate);
		releaseDateLabel = (TextView) findViewById(R.id.releaseDateLabel);

		currVerCode = (TextView) findViewById(R.id.currVerCod);
		newVersionCodeLabel = (TextView) findViewById(R.id.newVersionCodeLabel);
		newVersionCode = (TextView) findViewById(R.id.newVersionCode);

		this.buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
		addListenerUpdate();

		this.buttonInstall = (Button) findViewById(R.id.buttonInstall);
		addListenerInstall();

		newVersion.setVisibility(View.GONE);
		newVersionLabel.setVisibility(View.GONE);

		newVersionCodeLabel.setVisibility(View.INVISIBLE);
		newVersionCode.setVisibility(View.INVISIBLE);

		releaseDate.setVisibility(View.GONE);
		releaseDateLabel.setVisibility(View.GONE);

		buttonInstall.setVisibility(View.GONE);

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}

		long lastUpdate = 0;
		if (pInfo != null) {
			version = pInfo.versionName;
			buildVer = String.valueOf(pInfo.versionCode);
			lastUpdate = pInfo.firstInstallTime;
		}

		DateFormat datePattern = new SimpleDateFormat("dd-MMM-yyyy' 'HH:mm:ss", Locale.UK);
		datePattern.setTimeZone(TimeZone.getTimeZone("GMT+2"));

		String dateUpdate = datePattern.format(new Date(lastUpdate));

		currVer.setText(version);
		currVerCode.setText(buildVer);
		installDate.setText(dateUpdate);

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

	public void addListenerUpdate() {
		buttonUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				performCheckUpdateThread();
			}
		});

	}

	public void performCheckUpdateThread() {
		try {
			checkUpdate check = new checkUpdate(this);
			check.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void addListenerInstall() {
		buttonInstall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				performInstallUpdate();
			}
		});

	}

	

	public void performInstallUpdate() {
		try {
			downloadUpdate download = new downloadUpdate(this);
			download.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private class checkUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private checkUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Verificare...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			mFTPClient = new FTPClient();

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/LiteReportsVer.txt";

					FileOutputStream desFile2 = new FileOutputStream("sdcard/download/LiteReportsVer.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

					desFile2.close();

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {
				if (mFTPClient.isConnected()) {
					{
						try {
							mFTPClient.logout();
							mFTPClient.disconnect();
						} catch (IOException f) {
							errMessage = f.getMessage();
						}
					}
				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog != null) {
					this.dialog.dismiss();
					this.dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					validateUpdate();
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private class downloadUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private downloadUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Descarcare...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			mFTPClient = new FTPClient();

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/LiteSFA.apk";

					FileOutputStream desFile1 = new FileOutputStream("sdcard/download/LiteSFA.apk");
					mFTPClient.retrieveFile(sourceFile, desFile1);

					sourceFile = "/Update/LiteSFA/LiteReportsVer.txt";
					FileOutputStream desFile2 = new FileOutputStream("sdcard/download/LiteReportsVer.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

					desFile1.close();
					desFile2.close();

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {
				if (mFTPClient.isConnected()) {
					{
						try {
							mFTPClient.logout();
							mFTPClient.disconnect();
						} catch (IOException f) {
							errMessage = f.getMessage();
						}
					}
				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				startInstall();

				if (dialog != null) {
					this.dialog.dismiss();
					this.dialog = null;
				}

				if (errMessage != null) {
					if (errMessage.length() > 0) {
						Toast toast = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	public void startInstall() {

		String fileUrl = "/download/LiteSFA.apk";
		String file = Environment.getExternalStorageDirectory().getPath() + fileUrl;
		File f = new File(file);

		if (f.exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/"
					+ "LiteSFA.apk")), "application/vnd.android.package-archive");
			startActivity(intent);
			finish();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Fisier corupt, repetati operatiunea!",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void validateUpdate() {

		newVersion.setVisibility(View.GONE);
		newVersionLabel.setVisibility(View.GONE);
		releaseDate.setVisibility(View.GONE);
		releaseDateLabel.setVisibility(View.GONE);
		buttonInstall.setVisibility(View.GONE);
		newVersion.setText("");
		releaseDate.setText("");

		try {

			File fVer = new File(Environment.getExternalStorageDirectory() + "/download/LiteReportsVer.txt");
			FileInputStream fileIS = new FileInputStream(fVer);
			BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = buf.readLine();

			String[] tokenVer = null;

			if (readString != null) {
				tokenVer = readString.split("#");

				fileIS.close();

				if (!tokenVer[2].equals("0")) // 1 - fisierul este gata pentru
												// update, 0 - inca nu
				{
					newVersion.setVisibility(View.VISIBLE);
					newVersionLabel.setVisibility(View.VISIBLE);
					releaseDate.setVisibility(View.VISIBLE);
					releaseDateLabel.setVisibility(View.VISIBLE);

					newVersionCodeLabel.setVisibility(View.VISIBLE);
					newVersionCode.setVisibility(View.VISIBLE);
					
					newVersion.setText(tokenVer[0]);
					releaseDate.setText(tokenVer[1]);
					newVersionCode.setText(tokenVer[3]);

					if (Float.parseFloat(buildVer) < Float.parseFloat(tokenVer[3])) {
						// exista update
						buttonInstall.setVisibility(View.VISIBLE);
					} else {
						buttonInstall.setVisibility(View.GONE);
					}
				} else {
					Toast.makeText(getApplicationContext(), "Nu exista versiune pentru actualizare!",
							Toast.LENGTH_SHORT).show();
				}
			}

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

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