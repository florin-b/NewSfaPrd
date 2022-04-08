/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.DrawerMenuAdapter;
import my.logon.screen.model.UserInfo;
import my.logon.screen.model.VanzariAgenti;

public class VanzariAgentiActivity extends Activity {

	final String[] fragments = { "my.logon.screen.screens.SelectArticolVanzariAg", "my.logon.screen.screens.SelectClientVanzariAg",
			"my.logon.screen.screens.SelectAgentVanzariAg", "my.logon.screen.screens.SelectIntervalVanzariAg", "my.logon.screen.screens.SelectTipComandaVanzariAg",
			"my.logon.screen.screens.AfisRaportVanzariAg", };

	public static String var1 = "Main var";
	private static ArrayList<HashMap<String, String>> menuList = new ArrayList<HashMap<String, String>>();

	ListView listViewMenu;
	DrawerMenuAdapter menuAdapter;
	DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_vanzari_agenti);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Vanzari");
		actionBar.setDisplayHomeAsUpEnabled(true);

		VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

		try {

			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
					|| UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("41")) // ag,
			// ka,
			// cv
			{
				vanzariInstance.selectedAgent = UserInfo.getInstance().getCod();
				vanzariInstance.selectedFiliala = UserInfo.getInstance().getUnitLog();
			}

			if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("12")
					|| UserInfo.getInstance().getTipAcces().equals("18") || UserInfo.getInstance().getTipAcces().equals("35")
					|| UserInfo.getInstance().getTipAcces().equals("32")) // sd,sm
			{
				vanzariInstance.selectedAgent = "0";
				vanzariInstance.selectedFiliala = UserInfo.getInstance().getUnitLog();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			String currentDate = sdf.format(new Date());

			vanzariInstance.startIntervalRap = currentDate;
			vanzariInstance.stopIntervalRap = currentDate;

			menuAdapter = new DrawerMenuAdapter(this, menuList, R.layout.rowlayout_menu_item, new String[] { "menuName", "menuId" }, new int[] {
					R.id.textMenuName, R.id.textMenuId });

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main, new FragmentOne()).commit();

			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

			listViewMenu = (ListView) findViewById(R.id.menuDrawer);
			addListViewMenuListener();
			addMenuItems();

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Optiuni");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {

			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(this, MainMenu.class);
			startActivity(nextScreen);

			finish();

		}

		if (item.getItemId() == 0) {
			if (drawer.isDrawerOpen(Gravity.LEFT))
				drawer.closeDrawers();
			else
				drawer.openDrawer(Gravity.LEFT);

		}

		return super.onOptionsItemSelected(item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void addListViewMenuListener() {
		listViewMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
				drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

				});

				drawer.closeDrawer(listViewMenu);

				if (pos <= 5) {
					FragmentTransaction tx = getFragmentManager().beginTransaction();
					tx.replace(R.id.main, Fragment.instantiate(VanzariAgentiActivity.this, fragments[pos]));
					tx.commit();

					listViewMenu.setItemChecked(pos, true);

				} else {

					if (AfisRaportVanzariAg.arrayListRapVanz == null) {
						Toast.makeText(getApplicationContext(), "Nimic de expediat.", Toast.LENGTH_LONG).show();
					} else {

						if (AfisRaportVanzariAg.arrayListRapVanz.size() == 0) {
							Toast.makeText(getApplicationContext(), "Nimic de expediat.", Toast.LENGTH_LONG).show();
						}
					}

				}

			}

		});

	}

	private void addMenuItems() {

		menuList.clear();

		HashMap<String, String> temp;

		temp = new HashMap<String, String>();
		temp.put("menuName", "Articole/sintetice");
		temp.put("menuId", "1");
		menuList.add(temp);

		temp = new HashMap<String, String>();
		temp.put("menuName", this.getResources().getString(R.string.strClienti));
		temp.put("menuId", "2");
		menuList.add(temp);

		temp = new HashMap<String, String>();
		temp.put("menuName", "Agent");
		temp.put("menuId", "3");
		menuList.add(temp);

		temp = new HashMap<String, String>();
		temp.put("menuName", "Interval");
		temp.put("menuId", "4");
		menuList.add(temp);

		temp = new HashMap<String, String>();
		temp.put("menuName", "Comenzi");
		temp.put("menuId", "5");
		menuList.add(temp);

		temp = new HashMap<String, String>();
		temp.put("menuName", this.getResources().getString(R.string.strAfisRaport));
		temp.put("menuId", "6");
		menuList.add(temp);

		// directori
		if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("35")) {
			temp = new HashMap<String, String>();
			temp.put("menuName", this.getResources().getString(R.string.strMail));
			temp.put("menuId", "7");
			menuList.add(temp);
		}

		listViewMenu.setAdapter(menuAdapter);

	}






	private void showSendMailScreen() {
		try {

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "Raport vanzari");
			intent.putExtra(Intent.EXTRA_TEXT, "Fisier generat de aplicatia mobila LiteSFA.");

			File file = new File(Environment.getExternalStorageDirectory() + "/download/", "RaportVanzari.xls");

			if (!file.exists() || !file.canRead()) {
				Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			Uri uri = Uri.parse("file://" + file);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(intent, "Expediere mail..."));

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu);

	}

	@Override
	public void onBackPressed() {

		clearAllData();
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(this, MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	private void clearAllData() {

		VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

		vanzariInstance.clientListName.clear();
		vanzariInstance.clientListCode.clear();
		vanzariInstance.articolListName.clear();
		vanzariInstance.articolListCode.clear();

		vanzariInstance.startIntervalRap = "";
		vanzariInstance.stopIntervalRap = "";
		vanzariInstance.selectedFiliala = "-1";
		vanzariInstance.selectedAgent = "0";
		vanzariInstance.tipComanda = "E";

	}

}
