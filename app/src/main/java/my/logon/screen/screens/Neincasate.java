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
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.adapters.DrawerMenuAdapter;
import my.logon.screen.model.UserInfo;

public class Neincasate extends Activity {

	final String[] fragments = { "my.logon.screen.screens.SelectClientNeincasate", "my.logon.screen.screens.SelectAgentNeincasate",
			"my.logon.screen.screens.AfisRaportNeincasate" };

	ListView listViewMenu;
	DrawerMenuAdapter menuAdapter;
	DrawerLayout drawer;

	public static ArrayList<String> clientListName = new ArrayList<String>();
	public static ArrayList<String> clientListCode = new ArrayList<String>();

	public static String selectedFiliala = "-1", selectedAgent = "-1";

	private static ArrayList<HashMap<String, String>> menuList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_neincasate);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		try {

			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
					|| UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("41")) // ag,
			// ka,
			// cv
			{
				selectedAgent = UserInfo.getInstance().getCod();
				selectedFiliala = UserInfo.getInstance().getUnitLog();
			}

			if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("12")
					|| UserInfo.getInstance().getTipAcces().equals("18")
					|| UserInfo.getInstance().getTipAcces().equals("35")) // sd,sm
			{
				selectedAgent = "0";
				selectedFiliala = UserInfo.getInstance().getUnitLog();
			}

			final ActionBar actionBar = getActionBar();
			actionBar.setTitle("Neincasate");
			actionBar.setDisplayHomeAsUpEnabled(true);

			menuAdapter = new DrawerMenuAdapter(this, menuList, R.layout.rowlayout_menu_item, new String[] {
					"menuName", "menuId" }, new int[] { R.id.textMenuName, R.id.textMenuId });

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main, new FragmentOne()).commit();

			drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

			listViewMenu = (ListView) findViewById(R.id.menuDrawer);
			addListViewMenuListener();
			addMenuItems();

		} catch (Exception ex) {
			Toast toast = Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG);
			toast.show();
		}

	}

	private void addListViewMenuListener() {
		listViewMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
				drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

				});

				drawer.closeDrawer(listViewMenu);

				if (pos <= 5) {

					FragmentTransaction tx = getFragmentManager().beginTransaction();
					tx.replace(R.id.main, Fragment.instantiate(Neincasate.this, fragments[pos]));
					tx.commit();

					listViewMenu.setItemChecked(pos, true);

				}

			}

		});

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Optiuni");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}	
	
	private void addMenuItems() {

		menuList.clear();

		HashMap<String, String> temp;

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("menuName", this.getResources().getString(R.string.strClienti));
		temp.put("menuId", "1");
		menuList.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("menuName", "Agent");
		temp.put("menuId", "2");
		menuList.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("menuName", this.getResources().getString(R.string.strAfisRaport));
		temp.put("menuId", "3");
		menuList.add(temp);

		listViewMenu.setAdapter(menuAdapter);

	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 0) {
			if (drawer.isDrawerOpen(Gravity.LEFT))
				drawer.closeDrawers();
			else
				drawer.openDrawer(Gravity.LEFT);
				
		}		
		
		if (item.getItemId() == android.R.id.home) {

			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(this, MainMenu.class);
			startActivity(nextScreen);

			finish();

		}

		return super.onOptionsItemSelected(item);

	}

	private void clearAllData() {

		clientListName.clear();
		clientListCode.clear();
		selectedFiliala = "-1";
		selectedAgent = "-1";

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

}
