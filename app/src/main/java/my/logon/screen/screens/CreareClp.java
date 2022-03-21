/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.UserInfo;
import my.logon.screen.model.ViewPagerCustomDuration;

public class CreareClp extends FragmentActivity {

	CLPPageAdapter clpPageAdapter;
	ViewPagerCustomDuration pager;
	MenuItem mItem1, mItem2;
	Integer menuIndex = 2;
	public static String codClient = " ", codJudet = " ", oras = " ", strada = " ", persCont = " ", telefon = " ",
			codFilialaDest = " ", dataLivrare = " ", tipTransport = "TRAP", tipPlata = "B", tipMarfa = " ",
			masaMarfa = " ";
	public static String comandaFinala = " ", selectedAgent = " ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.creare_clp);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		try {

			final ActionBar actionBar = getActionBar();
			actionBar.setTitle("CLP - Selectie client");
			actionBar.setDisplayHomeAsUpEnabled(true);

			List<Fragment> fragments = getFragments();

			clpPageAdapter = new CLPPageAdapter(getSupportFragmentManager(), fragments);

			pager = (ViewPagerCustomDuration) findViewById(R.id.clpviewpager);
			pager.setAdapter(clpPageAdapter);

			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setScrollDurationFactor(4);

			pager.setOnPageChangeListener(new OnPageChangeListener() {

				public void onPageSelected(int pageNumber) {

					if (clpPageAdapter.getItem(pageNumber).isVisible()) {
						if (pageNumber == 0) {
							actionBar.setTitle("CLP - Selectie client");
							getActionBar().setCustomView(null);
							getActionBar().setDisplayShowCustomEnabled(true);
							menuIndex = 2;
							invalidateOptionsMenu();

						}
						if (pageNumber == 1) {
							actionBar.setTitle("CLP - Selectie articole");
							menuIndex = 1;
							invalidateOptionsMenu();

						}

					}

				}

				public void onPageScrollStateChanged(int arg0) {
				}

				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

			});

		} catch (Exception ex) {
			Toast toast = Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG);
			toast.show();
		}

	}

	private void CreateMenu(Menu menu) {

		switch (menuIndex) {
		case 1:
			mItem1 = menu.add(0, 0, 0, "Client");
			{

				mItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			}
			break;

		case 2:
			mItem1 = menu.add(0, 0, 0, "Articole");
			{
				mItem1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			}
			break;

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			returnToHome();
			return true;
		}

		if (menuIndex == 1) {
			pager.setCurrentItem(pager.getCurrentItem() - 1);
			return true;
		}

		if (menuIndex == 2) {
			pager.setCurrentItem(pager.getCurrentItem() + 1);
			return true;
		}

		return true;

	}

	private void returnToHome() {
		if (codClient.trim().length() == 0) {
			clearVars();
			UserInfo.getInstance().setParentScreen("");

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);

			finish();
		} else {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage("Datele se vor pierde. Continuati?").setCancelable(false)
					.setPositiveButton("Da", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							clearVars();
							UserInfo.getInstance().setParentScreen("");

							Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
							startActivity(nextScreen);

							finish();
						}
					}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}).setTitle("Atentie!").setIcon(R.drawable.warning96);

			AlertDialog alert = builder.create();
			alert.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);

		return true;
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		fList.add(CLPFragment1.newInstance());
		fList.add(CLPFragment2.newInstance());

		return fList;
	}

	private class CLPPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public CLPPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	public void clearVars() {

		codClient = " ";
		codJudet = " ";
		oras = " ";
		strada = " ";
		persCont = " ";
		telefon = " ";
		codFilialaDest = " ";
		dataLivrare = " ";
		comandaFinala = " ";
		tipTransport = "TRAP";
		tipPlata = "B";
		selectedAgent = " ";

	}

	@Override
	public void onBackPressed() {

		returnToHome();
		return;
	}

}
