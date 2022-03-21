package my.logon.screen.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import my.logon.screen.beans.BeanDeviceInfo;

public class UtilsDevice {
	public BeanDeviceInfo getDeviceInfo(Context context) {

		BeanDeviceInfo deviceInfo = new BeanDeviceInfo();

		try {

			PackageInfo pInfo = null;
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			deviceInfo.setSdkVer(String.valueOf(Build.VERSION.SDK_INT));
			deviceInfo.setMan(Build.MANUFACTURER);
			deviceInfo.setModel(Build.MODEL);
			deviceInfo.setAppName(pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
			deviceInfo.setAppVer(String.valueOf(pInfo.versionCode));

		} catch (Exception e) {

		}

		return deviceInfo;

	}
}
