package my.logon.screen.screens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import my.logon.screen.listeners.NecesarArticoleListener;
import my.logon.screen.model.MaterialNecesar;
import my.logon.screen.model.NecesarArticole;
import my.logon.screen.model.UserInfo;
import my.logon.screen.adapters.ArticoleNecesarAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import my.logon.screen.enums.EnumFiliale;
import my.logon.screen.R;

public class NecesarArticoleActivity extends Activity implements NecesarArticoleListener, OnClickListener {

	TextView textNumeMaterial, textCodMaterial, textNumeSintetic, textCodSintetic, textCons30, textStoc, textPropunere, intrariI1, intrariI2, intrariI3;
	boolean sortAscNumeMaterial = true;
	boolean sortAscCodMaterial = true;
	boolean sortAscNumeSintetic = true;
	boolean sortAscCodSintetic = true;
	boolean sortAscCons30 = true;
	boolean sortAscStoc = true;
	boolean sortAscPropunere = true;

	private String month1, month2, month3;

	ListView listMaterialeNecesar;
	SimpleAdapter adapterMateriale;
	NecesarArticole necesarArticole;
	LinearLayout layoutNecesar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_necesar);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Articole avarie");
		actionBar.setDisplayHomeAsUpEnabled(true);

		layoutNecesar = (LinearLayout) findViewById(R.id.headerNecesar);
		layoutNecesar.setVisibility(View.INVISIBLE);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		necesarArticole = NecesarArticole.getInstance();
		necesarArticole.setNecesarArticoleListener(this);

		addLayoutComponents();

		if (isDV())
			populateListFiliale();
		else {
			loadListaMateriale(UserInfo.getInstance().getUnitLog());
		}

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

	boolean isDV() {
		return UserInfo.getInstance().getTipUser().equals("DV");
	}

	private void populateListFiliale() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, EnumFiliale.getFiliale());

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
		final Spinner spinnerView = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

		spinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (spinnerView.getSelectedItemPosition() > 0)
					loadListaMateriale(EnumFiliale.getCodFiliala(spinnerView.getSelectedItem().toString()));

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerView.setAdapter(adapter);
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);
	}

	private void addLayoutComponents() {

		textNumeMaterial = (TextView) findViewById(R.id.textNumeMaterial);
		textNumeMaterial.setOnClickListener(this);

		textNumeSintetic = (TextView) findViewById(R.id.textNumeSintetic);
		textNumeSintetic.setOnClickListener(this);

		textCodSintetic = (TextView) findViewById(R.id.textCodSintetic);
		textCodSintetic.setOnClickListener(this);

		textCodMaterial = (TextView) findViewById(R.id.textCodMaterial);
		textCodMaterial.setOnClickListener(this);

		textCons30 = (TextView) findViewById(R.id.textCons30);
		textCons30.setOnClickListener(this);

		textStoc = (TextView) findViewById(R.id.textStoc);
		textStoc.setOnClickListener(this);

		textPropunere = (TextView) findViewById(R.id.textPropunere);
		textPropunere.setOnClickListener(this);

		listMaterialeNecesar = (ListView) findViewById(R.id.listMaterialeNecesar);
		

		intrariI1 = (TextView) findViewById(R.id.intrariI1);
		intrariI2 = (TextView) findViewById(R.id.intrariI2);
		intrariI3 = (TextView) findViewById(R.id.intrariI3);

		Calendar cal = Calendar.getInstance();

		SimpleDateFormat month_date = new SimpleDateFormat("MMM");
		String month_name = month_date.format(cal.getTime());
		int mYear = cal.get(Calendar.YEAR);
		month1 = month_name + "-" + String.valueOf(mYear);

		cal.add(Calendar.MONTH, 1);
		month_name = month_date.format(cal.getTime());
		mYear = cal.get(Calendar.YEAR);
		month2 = month_name + "-" + String.valueOf(mYear);

		cal.add(Calendar.MONTH, 1);
		month_name = month_date.format(cal.getTime());
		mYear = cal.get(Calendar.YEAR);
		month3 = month_name + "-" + String.valueOf(mYear);

		intrariI1.setText(month1);
		intrariI2.setText(month2);
		intrariI3.setText(month3);

	}

	

	private void loadListaMateriale(String filiala) {
		necesarArticole.getListaMateriale(filiala, UserInfo.getInstance().getCodDepart(), this);
	}

	private void populateListMateriale(ArrayList<MaterialNecesar> listaArticole) {
		layoutNecesar.setVisibility(View.VISIBLE);
		ArticoleNecesarAdapter adapter = new ArticoleNecesarAdapter(listaArticole, this);
		listMaterialeNecesar.setAdapter(adapter);

	}

	public void onTaskComplete() {
		sortAscNumeMaterial = false;
		sortNumeMaterial();
	}

	private void setHeaderFontStyle() {
		textNumeMaterial.setTypeface(Typeface.DEFAULT);
		textCodMaterial.setTypeface(Typeface.DEFAULT);
		textNumeSintetic.setTypeface(Typeface.DEFAULT);
		textCodSintetic.setTypeface(Typeface.DEFAULT);
		textCons30.setTypeface(Typeface.DEFAULT);
		textStoc.setTypeface(Typeface.DEFAULT);
		textPropunere.setTypeface(Typeface.DEFAULT);
	}

	private void sortNumeMaterial() {
		setHeaderFontStyle();

		if (sortAscNumeMaterial) {
			textNumeMaterial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_green, 0, 0, 0);
		} else {
			textNumeMaterial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_green, 0, 0, 0);
		}

		populateListMateriale(necesarArticole.sortByNumeArticol(necesarArticole.getListaMateriale(), !sortAscNumeMaterial));

		sortAscNumeMaterial = !sortAscNumeMaterial;
		textNumeMaterial.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortCodMaterial() {
		setHeaderFontStyle();

		if (sortAscCodMaterial) {
			textCodMaterial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_green, 0, 0, 0);
		} else {
			textCodMaterial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_green, 0, 0, 0);
		}

		populateListMateriale(necesarArticole.sortByCodArticol(necesarArticole.getListaMateriale(), !sortAscCodMaterial));

		sortAscCodMaterial = !sortAscCodMaterial;
		textCodMaterial.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortNumeSintetic() {
		setHeaderFontStyle();

		if (sortAscNumeSintetic) {
			textNumeSintetic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_green, 0, 0, 0);
		} else {
			textNumeSintetic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_green, 0, 0, 0);
		}

		populateListMateriale(necesarArticole.sortByNumeSintetic(necesarArticole.getListaMateriale(), !sortAscNumeSintetic));

		sortAscNumeSintetic = !sortAscNumeSintetic;
		textNumeSintetic.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortCodSintetic() {
		setHeaderFontStyle();

		if (sortAscCodSintetic) {
			textCodSintetic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_green, 0, 0, 0);
		} else {
			textCodSintetic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_green, 0, 0, 0);
		}

		populateListMateriale(necesarArticole.sortByCodSintetic(necesarArticole.getListaMateriale(), !sortAscCodSintetic));

		sortAscCodSintetic = !sortAscCodSintetic;
		textCodSintetic.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortCons30() {
		setHeaderFontStyle();
		if (sortAscCons30) {
			textCons30.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_green, 0);
		} else {
			textCons30.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_green, 0);
		}

		populateListMateriale(necesarArticole.sortByConsum30(necesarArticole.getListaMateriale(), !sortAscCons30));
		sortAscCons30 = !sortAscCons30;
		textCons30.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortStoc() {
		setHeaderFontStyle();
		if (sortAscStoc) {
			textStoc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_green, 0);
		} else {
			textStoc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_green, 0);
		}

		populateListMateriale(necesarArticole.sortByStoc(necesarArticole.getListaMateriale(), !sortAscStoc));
		sortAscStoc = !sortAscStoc;
		textStoc.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortPropunere() {
		setHeaderFontStyle();
		if (sortAscPropunere) {
			textPropunere.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_green, 0);
		} else {
			textPropunere.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_green, 0);
		}

		populateListMateriale(necesarArticole.sortByPropunere(necesarArticole.getListaMateriale(), !sortAscPropunere));
		sortAscPropunere = !sortAscPropunere;
		textPropunere.setTypeface(Typeface.DEFAULT_BOLD);
	}

	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(this, MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.textNumeMaterial) {
			sortNumeMaterial();
		} else if (id == R.id.textCons30) {
			sortCons30();
		} else if (id == R.id.textStoc) {
			sortStoc();
		} else if (id == R.id.textPropunere) {
			sortPropunere();
		} else if (id == R.id.textCodMaterial) {
			sortCodMaterial();
		} else if (id == R.id.textCodSintetic) {
			sortCodSintetic();
		} else if (id == R.id.textNumeSintetic) {
			sortNumeSintetic();
		}

	}

}
