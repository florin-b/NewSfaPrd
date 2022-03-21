package my.logon.screen.utils;

import my.logon.screen.model.UserInfo;
import android.content.Context;
import android.net.wifi.WifiManager;
import my.logon.screen.beans.FtpAccess;

public class UtilsConn {

	private static final String FTP_CENTRAL_IP = "10.1.0.6";

	private static final String FTP_CENTRAL_USR = "litesfa";
	private static final String FTP_CENTRAL_PASS = "egoo4Ur";
	private static final String FTP_CENTRAL_VER_FILE = "/Update/LiteSFA/LiteReportsVer.txt";
	private static final String FTP_CENTRAL_APK_FILE = "/Update/LiteSFA/LiteSFA.apk";

	private static final String FTP_LOCAL_USR = "update";
	private static final String FTP_LOCAL_PASS = "Umig9eep";
	private static final String FTP_LOCAL_VER_FILE = "Update/LiteSFA/LiteReportsVer.txt";
	private static final String FTP_LOCAL_APK_FILE = "Update/LiteSFA/LiteSFA.apk";

	private static final String NO_IP = "0.0.0.0";

	public static boolean wifiEnabled(Context context) {
		WifiManager mng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return mng.isWifiEnabled();
	}

	public static FtpAccess ftpAccess(Context context) {
		FtpAccess ftpAccess = new FtpAccess();

		if (wifiEnabled(context) && !UserInfo.getInstance().getFtpIP().equals(NO_IP)) {
			ftpAccess.setIp(UserInfo.getInstance().getFtpIP());
			ftpAccess.setUser(FTP_LOCAL_USR);
			ftpAccess.setPass(FTP_LOCAL_PASS);
			ftpAccess.setVerFile(FTP_LOCAL_VER_FILE);
			ftpAccess.setApkFile(FTP_LOCAL_APK_FILE);
		} else {
			ftpAccess.setIp(FTP_CENTRAL_IP);
			ftpAccess.setUser(FTP_CENTRAL_USR);
			ftpAccess.setPass(FTP_CENTRAL_PASS);
			ftpAccess.setVerFile(FTP_CENTRAL_VER_FILE);
			ftpAccess.setApkFile(FTP_CENTRAL_APK_FILE);
		}

		return ftpAccess;
	}

}
