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

import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.adapters.DrawerMenuAdapter;
import my.logon.screen.model.UserInfo;

public class AdreseGpsClienti extends Activity {

	final String[] fragments = { "my.logon.screen.AdaugaAdresaGpsClienti", "my.logon.screen.AdresaGpsClientiActivity" };

	private ListView listViewMenu;
	private DrawerMenuAdapter menuAdapter;
	private DrawerLayout drawer;

	private static ArrayList<HashMap<String, String>> menuList = new ArrayList<HashMap<String, String>>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_neincasate);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Adrese clienti");
		actionBar.setDisplayHomeAsUpEnabled(true);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.main, new FragmentOne()).commit();

		menuAdapter = new DrawerMenuAdapter(this, menuList, R.layout.rowlayout_menu_item, new String[] { "menuName",
				"menuId" }, new int[] { R.id.textMenuName, R.id.textMenuId });

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		listViewMenu = (ListView) findViewById(R.id.menuDrawer);
		addListViewMenuListener();
		addMenuItems();

	}

	private void addListViewMenuListener() {
		listViewMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
				drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

				});

				drawer.closeDrawer(listViewMenu);

				FragmentTransaction tx = getFragmentManager().beginTransaction();
				tx.replace(R.id.main, Fragment.instantiate(AdreseGpsClienti.this, fragments[pos]));
				tx.commit();

				listViewMenu.setItemChecked(pos, true);

			}

		});

	}

	private void addMenuItems() {

		menuList.clear();

		HashMap<String, String> temp;

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("menuName", "Adaugare adrese");
		temp.put("menuId", "1");
		menuList.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("menuName", "Afisare adrese");
		temp.put("menuId", "2");
		menuList.add(temp);

		listViewMenu.setAdapter(menuAdapter);

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

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 0) {
			if (drawer.isDrawerOpen(Gravity.LEFT))
				drawer.closeDrawers();
			else
				drawer.openDrawer(Gravity.LEFT);

		}

		if (item.getItemId() == android.R.id.home) {

			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(this, MainMenu.class);
			startActivity(nextScreen);

			finish();

		}

		return super.onOptionsItemSelected(item);

	}

}
