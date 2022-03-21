package my.logon.screen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class UtilsApps {

	private Context context;

	public static boolean isPackageInstalled(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public void installObiectivaCVAApk(Context context) {
		this.context = context;
		new UtilsApps().new DownloadApk(context).execute();

	}

	public void startInstall(Context context) {

		String fileUrl = "/download/ObiectiveCVA.apk";
		String file = Environment.getExternalStorageDirectory().getPath() + fileUrl;
		File f = new File(file);

		if (f.exists()) {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "ObiectiveCVA.apk")),
					"application/vnd.android.package-archive");
			context.startActivity(intent);

		} else {
			Toast toast = Toast.makeText(context, "Fisier corupt, repetati operatiunea!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	class DownloadApk extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;
		private FTPClient mFTPClient = null;

		private DownloadApk(Context context) {
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
		protected String doInBackground(Void... url) {
			String response = "";
			mFTPClient = new FTPClient();

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/ObiectiveCVA.apk";
					FileOutputStream desFile1 = new FileOutputStream("sdcard/download/ObiectiveCVA.apk");
					mFTPClient.retrieveFile(sourceFile, desFile1);

					sourceFile = "/Update/LiteSFA/ObiectiveCVAVer.txt";
					FileOutputStream desFile2 = new FileOutputStream("sdcard/download/ObiectiveCVAVer.txt");
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

			if (dialog != null) {
				dialog.dismiss();
			}

			if (!errMessage.equals("")) {
				Toast toast = Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT);
				toast.show();
			} else {
				startInstall(mContext);
			}

		}

	}

}
