/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import my.logon.screen.R;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.Constants;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.OperatiiMeniu;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsDevice;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class LogonScreen extends Activity implements AsyncTaskListener {

	private EditText etUsername;
	private EditText etPassword;
	private TextView lblResult;

	private static final String SOAP_ACTION = "http://SmartScan.org/userLogon";
	private static final String METHOD_NAME = "userLogon";
	private static final String NAMESPACE = "http://SmartScan.org/";
	private static final String URL = "http://10.1.0.58/AndroidWebServices/service1.asmx";

	public static String codInitDepart = "00";
	public static String numeInitDepart = "NEDE", globalMyIP = "0.0.0.0";

	public static Double discMaxAV = 0.0, discMaxSD = 0.0, discMaxDV = 0.0;

	private ProgressBar mProgress;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler logonHandler = new Handler();

	MySwitch slideToUnLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectCustomSlowCalls()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.penaltyFlashScreen()
				.build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
				.build());

		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 30) {
			if (!Environment.isExternalStorageManager()) {
				Intent getpermission = new Intent();
				getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
				startActivity(getpermission);
			}
		}

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		System.setProperty("http.keepAlive", "false");

		// formatare data si numere = en
		String languageToLoad = "en";
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		//

		setTheme(R.style.LiteTheme);
		setContentView(R.layout.main);

		if (isDebugMode(this)) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog()
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog()
					.penaltyDeath().build());
		}

		slideToUnLock = (MySwitch) findViewById(R.id.switchLogon);
		slideToUnLock.toggle();
		slideToUnLock.disableClick();
		slideToUnLock.fixate(false);
		slideToUnLock.requestFocus();

		addListenerSlider();

		etUsername = (EditText) findViewById(R.id.txtUserName);
		etPassword = (EditText) findViewById(R.id.txtPassword);
		lblResult = (TextView) findViewById(R.id.result);

		etUsername.setHint("Utilizator");
		etPassword.setHint("Parola");

		etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		addListenerUserName();
		addListenerPassword();

		globalMyIP = getDeviceId();

		checkBundleExtra();

	}

	private void checkBundleExtra() {

		if (getIntent().hasExtra("UserInfo")) {
			UtilsUser.deserializeUserInfo(getIntent().getExtras().getString("UserInfo"), getApplicationContext());
			startMainMenuActivity();
		}
	}

	private void startMainMenuActivity() {
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	private void addListenerSlider() {
		slideToUnLock.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (buttonView.isChecked()) {

				} else {
					if (etUsername.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati utilizatorul!", Toast.LENGTH_SHORT).show();
						slideToUnLock.setChecked(true);
						return;

					}
					if (etPassword.getText().toString().trim().length() == 0) {
						Toast.makeText(getApplicationContext(), "Completati parola!", Toast.LENGTH_SHORT).show();
						slideToUnLock.setChecked(true);
						return;
					}
					if (etUsername.getText().toString().trim().length() > 0 && etPassword.getText().toString().trim().length() > 0) {

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);

						performLoginThread();

					}

				}

			}
		});
	}

	@SuppressWarnings("deprecation")
	private String getIPConnection() {
		String myIP = "0.0.0.0";

		try {

			ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isAvailable()) {

				WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
				WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
				int ipAddress = myWifiInfo.getIpAddress();

				myIP = android.text.format.Formatter.formatIpAddress(ipAddress);

			} else if (mobile.isAvailable()) {

				myIP = GetLocalIpAddress();
			} else {
				myIP = "0.0.0.0";
			}
		} catch (Exception ex) {
			Log.e("Error", ex.toString());
		}

		return myIP;

	}

	private String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}	
	
	private String GetLocalIpAddress() {
		String retVal = "0.0.0.0";
		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						retVal = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("Error", ex.toString());
			retVal = "IP Error";
		}

		return retVal;
	}

	public static boolean isDebugMode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return true;
	}

	public void addListenerUserName() {

		etUsername.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (etUsername.getText().toString().compareTo("Utilizator") == 0) {
					etUsername.setText("");
				}

				return false;
			}
		});

	}

	public void addListenerPassword() {

		etPassword.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (etPassword.getText().toString().compareTo("Parola") == 0) {
					etPassword.setText("");
				}

				return false;
			}
		});

	}

	public void performLoginThread() {
		// start login thread
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String userN = etUsername.getText().toString().trim();
			String passN = etPassword.getText().toString().trim();

			params.put("userId", userN);
			params.put("userPass", passN);
			
			params.put("ipAdr", globalMyIP);
			params.put("deviceInfo", new OperatiiMeniu(getApplicationContext()).serializeDeviceInfo(new UtilsDevice().getDeviceInfo(getApplicationContext())));

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public String getFiliala(String numeFiliala) {
		String fl = "NN10";

		if (numeFiliala.equals("BACAU"))
			fl = "BC10";

		if (numeFiliala.equals("BUZAU"))
			fl = "BZ10";

		if (numeFiliala.equals("GALATI"))
			fl = "GL10";

		if (numeFiliala.equals("PITESTI"))
			fl = "AG10";

		if (numeFiliala.equals("TIMISOARA"))
			fl = "TM10";

		if (numeFiliala.equals("ORADEA"))
			fl = "BH10";

		if (numeFiliala.equals("FOCSANI"))
			fl = "VN10";

		if (numeFiliala.equals("GLINA"))
			fl = "BU10";

		if (numeFiliala.equals("ANDRONACHE"))
			fl = "BU13";

		if (numeFiliala.equals("OTOPENI"))
			fl = "BU12";

		if (numeFiliala.equals("CLUJ"))
			fl = "CJ10";

		if (numeFiliala.equals("BAIA"))
			fl = "MM10";

		if (numeFiliala.equals("MILITARI"))
			fl = "BU11";

		if (numeFiliala.equals("CONSTANTA"))
			fl = "CT10";

		if (numeFiliala.equals("BRASOV"))
			fl = "BV10";

		if (numeFiliala.equals("PLOIESTI"))
			fl = "PH10";

		if (numeFiliala.equals("PIATRA"))
			fl = "NT10";

		if (numeFiliala.equals("MURES"))
			fl = "MS10";

		if (numeFiliala.equals("IASI"))
			fl = "IS10";

		if (numeFiliala.equals("CRAIOVA"))
			fl = "DJ10";

		if (numeFiliala.equals("SIBIU"))
			fl = "SB10";

		if (numeFiliala.equals("DEVA"))
			fl = "HD10";

		return fl;

	}

	public String getDepart(String numeDepart) {
		String dpt = "00";

		if (numeDepart.equals("CHIM"))
			dpt = "07";

		if (numeDepart.equals("DIVE"))
			dpt = "10";

		if (numeDepart.equals("ELEC"))
			dpt = "05";

		if (numeDepart.equals("FERO"))
			dpt = "02";

		if (numeDepart.equals("GIPS"))
			dpt = "06";

		if (numeDepart.equals("INST"))
			dpt = "08";

		if (numeDepart.equals("LEMN"))
			dpt = "01";

		if (numeDepart.equals("MATE"))
			dpt = "04";

		if (numeDepart.equals("PARC"))
			dpt = "03";

		if (numeDepart.equals("CHIM"))
			dpt = "07";

		if (numeDepart.equals("HIDR"))
			dpt = "09";

		if (numeDepart.equals("LEFA"))
			dpt = "02";

		return dpt;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onUserLeaveHint() { // this only executes when Home is selected.
		finish();
	}

	public void validateLogin(String result) {
		if (!result.equals("-1") && result.length() > 0) {
			String[] token = result.split("#");

			if (token[0].equals("0")) {
				lblResult.setText("Cont inexistent!");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("1")) {
				lblResult.setText("Cont blocat 60 de minute.");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("2")) {
				lblResult.setText("Parola incorecta!");
				slideToUnLock.setChecked(true);
			}
			if (token[0].equals("3")) {

				if (token[5].equals("8") || token[5].equals("9") || token[5].equals("10") || token[5].equals("14") || token[5].equals("12")
						|| token[5].equals("27") || token[5].equals("35") || token[5].equals("17") || token[5].equals("18") || token[5].equals("32")
						|| token[5].equals("41") || token[5].equals("43") || token[5].equals("44") || token[5].equals("45") || token[5].equals("39")
						|| token[5].equals("51") || token[5].equals("50") || token[5].equals("62")) // doar
				// agentii,
				// admin , sd , dv, dd, ka, dir ka, cons, sm
				{

					UserInfo uInfo = UserInfo.getInstance();

					String tempAgCod = token[4].toString();

					if (tempAgCod.equalsIgnoreCase("-1")) {
						lblResult.setText("Utilizator nedefinit!");
						slideToUnLock.setChecked(true);
						return;
					}

					for (int i = 0; i < 8 - token[4].length(); i++) {
						tempAgCod = "0" + tempAgCod;
					}

					// coeficienti consilieri si sm
					if (token[5].equals("17") || token[5].equals("18") || token[10].equals("KA3")) {
						String[] coeficientiCV = token[6].split("!");
						uInfo.setComisionCV(Double.valueOf(coeficientiCV[0]));
						uInfo.setCoefCorectie(coeficientiCV[1]);

					}

					discMaxSD = 0.0;
					discMaxDV = 0.0;

					uInfo.setNume(token[3].toString());
					uInfo.setFiliala(token[2].toString());
					uInfo.setCod(tempAgCod);
					uInfo.setNumeDepart(token[1].toString());
					uInfo.setCodDepart(getDepart(token[1].toString()));
					uInfo.setUnitLog(getFiliala(token[2].toString()));
					uInfo.setInitUnitLog(getFiliala(token[2].toString()));
					uInfo.setTipAcces(token[5].toString());

					if (token[10].toLowerCase(Locale.getDefault()).equals("w")) {
						uInfo.setTipUser("WOOD");
						uInfo.setTipUserSap("WOOD");
					} else {
						uInfo.setTipUser(InfoStrings.getTipUser(token[5].toString()));
						uInfo.setTipUserSap(token[10]);
					}

					uInfo.setParentScreen("logon");
					uInfo.setFilialeDV(token[9]);
					uInfo.setAltaFiliala(false);
					uInfo.setUserSite(token[8]);
					uInfo.setDepartExtra(token[7]);

					uInfo.setExtraFiliale(token[11]);

					if (uInfo.getTipAcces().equals("27") || uInfo.getTipAcces().equals("35") || uInfo.getTipAcces().equals("17")
							|| uInfo.getTipAcces().equals("18")) {

						uInfo.setCodDepart("01");
						uInfo.setNumeDepart("LEMN");
					}

					uInfo.setFilHome(Boolean.valueOf(token[12]));
					uInfo.setFtpIP(token[13]);
					uInfo.setInitDivizie(token[14]);
					uInfo.setIsMeniuBlocat(Boolean.parseBoolean(UtilsGeneral.getStareMeniu(token[15])[0]));
					uInfo.setCodPinMeniu(UtilsGeneral.getStareMeniu(token[15])[1]);
					uInfo.setCodDepart(UtilsGeneral.getDepart(token[1]));

					if (uInfo.getTipUserSap().equals(Constants.tipSuperAv) || uInfo.getTipUserSap().equals(Constants.tipInfoAv) || UtilsUser.isSMR()
							|| UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD())
						uInfo.setCodSuperUser(uInfo.getCod());

					Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
					startActivity(nextScreen);
					finish();

				} else {
					lblResult.setText("Acces interzis!");
					slideToUnLock.setChecked(true);
				}
			}
			if (token[0].equals("4")) {
				lblResult.setText("Cont inactiv!");
				slideToUnLock.setChecked(true);

			}
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Autentificare esuata!", Toast.LENGTH_SHORT);
			toast.show();
			slideToUnLock.setChecked(true);
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals(METHOD_NAME)) {
			validateLogin((String) result);
		}

	}

}
