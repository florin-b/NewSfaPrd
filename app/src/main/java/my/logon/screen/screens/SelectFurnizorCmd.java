/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareFurnizoriAdapter;
import my.logon.screen.adapters.FurnizorProduseAdapter;
import my.logon.screen.beans.BeanFurnizor;
import my.logon.screen.beans.BeanFurnizorProduse;
import my.logon.screen.beans.FurnizorComanda;
import my.logon.screen.enums.EnumOperatiiFurnizor;
import my.logon.screen.listeners.OperatiiFurnizorListener;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.OperatiiFurnizor;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;

public class SelectFurnizorCmd extends ListActivity implements OperatiiFurnizorListener {

	private Button furnizorBtn, saveFurnizorButton;
	private String codFurnizor = "";
	private String numeFurnizor = "";
	private EditText txtNumeFurnizor;
	private NumberFormat nf2;

	private LinearLayout resLayout;

	private OperatiiFurnizor operatiiFurnizor;
	private FurnizorComanda furnizorComanda;
	private Spinner spinnerFurnizorProduse;
	private RadioGroup radioCautare;
	private enum EnumTipCautareFurnizor {
		NUME_FURNIZOR, COD_ARTICOL;
	}

	private EnumTipCautareFurnizor tipCautareFurnizor = EnumTipCautareFurnizor.NUME_FURNIZOR;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectfurnizorcmdheader);

		operatiiFurnizor = new OperatiiFurnizor(this);
		operatiiFurnizor.setOperatiiFurnizorListener(this);

		this.furnizorBtn = (Button) findViewById(R.id.furnizorBtn);
		addListenerFurnizor();

		this.saveFurnizorButton = (Button) findViewById(R.id.saveClntBtn);
		addListenerSave();

		radioCautare = (RadioGroup) findViewById(R.id.radioCautare);
		setListenerRadioCautare();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Selectie furnizor");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		resLayout = (LinearLayout) findViewById(R.id.resLayout);
		resLayout.setVisibility(View.INVISIBLE);

		txtNumeFurnizor = (EditText) findViewById(R.id.txtNumeFurnizor);
		txtNumeFurnizor.setHint("Nume furnizor");

		saveFurnizorButton.setVisibility(View.INVISIBLE);

		nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		spinnerFurnizorProduse = (Spinner) findViewById(R.id.spinnerFurnizorProduse);
		setSpinnerFurnizorListener();

		furnizorComanda = new FurnizorComanda();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void setListenerRadioCautare(){
		radioCautare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {

					case R.id.radioFurnizor:
						tipCautareFurnizor = EnumTipCautareFurnizor.NUME_FURNIZOR;
						txtNumeFurnizor.setHint("Nume furnizor");
						break;
					case R.id.radioArticol:
						tipCautareFurnizor = EnumTipCautareFurnizor.COD_ARTICOL;
						txtNumeFurnizor.setHint("Cod articol");
						break;

				}

				txtNumeFurnizor.setText("");
				setListAdapter(null);
				resLayout.setVisibility(View.INVISIBLE);

			}
		});
	}

	private void setSpinnerFurnizorListener() {
		spinnerFurnizorProduse.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				BeanFurnizorProduse furnizor = (BeanFurnizorProduse) parent.getAdapter().getItem(position);
				if (furnizor.getCodFurnizorProduse().equals("-1")) {
					saveFurnizorButton.setVisibility(View.INVISIBLE);
				} else {
					furnizorComanda.setCodFurnizorProduse(furnizor.getCodFurnizorProduse());
					furnizorComanda.setNumeFurnizorProduse(furnizor.getNumeFurnizorProduse());

					saveFurnizorButton.setVisibility(View.VISIBLE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		resLayout.setVisibility(View.VISIBLE);

		BeanFurnizor furnizor = (BeanFurnizor) l.getAdapter().getItem(position);
		numeFurnizor = furnizor.getNumeFurnizor();
		codFurnizor = furnizor.getCodFurnizor();

		((TextView) findViewById(R.id.textFurnizorMarfa)).setText(numeFurnizor);
		furnizorComanda.setCodFurnizorMarfa(codFurnizor);
		furnizorComanda.setNumeFurnizorMarfa(numeFurnizor);

		getFurnizorProduse();

	}

	private void addListenerSave() {
		saveFurnizorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (furnizorComanda.getCodFurnizorMarfa() == null) {
					Toast.makeText(getApplicationContext(), "Selectati furnizorul de marfa.", Toast.LENGTH_LONG).show();
					return;
				}

				DateLivrare.getInstance().setFurnizorComanda(furnizorComanda);

				finish();

			}
		});

	}

	private void getFurnizorProduse() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codFurnizor", furnizorComanda.getCodFurnizorMarfa());
		operatiiFurnizor.getFurnizoriProduse(params);
	}

	private void populateListViewFurnizori(List<BeanFurnizor> listFurnizori) {
		CautareFurnizoriAdapter adapterFurnizori = new CautareFurnizoriAdapter(this);
		adapterFurnizori.setListFurnizori(listFurnizori);
		setListAdapter(adapterFurnizori);

	}

	private void populateSpinnerFurnizori(List<BeanFurnizorProduse> listFurnizori) {
		FurnizorProduseAdapter produseAdapter = new FurnizorProduseAdapter(getApplicationContext());

		if (listFurnizori.size() > 1)
			listFurnizori.add(0, new BeanFurnizorProduse("Selectati un furnizor", "-1"));

		produseAdapter.setListFurnizoriProduse(listFurnizori);
		spinnerFurnizorProduse.setAdapter(produseAdapter);

	}

	private void addListenerFurnizor() {
		furnizorBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (txtNumeFurnizor.length() > 0)
					performListFurnizori();

			}
		});

	}

	private void performListFurnizori() {

		String numeFurn = txtNumeFurnizor.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeFurn);
		params.put("depart", getExceptiiDepartament());
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", UserInfo.getInstance().getUnitLog());
		params.put("tipCautare", tipCautareFurnizor.toString());

		operatiiFurnizor.getFurnizoriMarfa(params);

		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

	}

	private String getExceptiiDepartament() {
		String depSel = UserInfo.getInstance().getCodDepart();
		if (CreareComanda.canalDistrib.equals("20"))
			depSel = "11";

		if (!CreareComanda.canalDistrib.equals("20") && UserInfo.getInstance().getTipUser().equals("KA")) {
			depSel = "00";
		}

		return depSel;
	}

	private void checkStaticVars() {

		String locLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

		if (!locLang.toLowerCase(Locale.getDefault()).equals("en")) {

			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}

		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}

	@Override
	public void onBackPressed() {

		finish();
		return;
	}

	@Override
	public void operationComplete(EnumOperatiiFurnizor methodName, Object result) {
		switch (methodName) {
		case GET_FURNIZORI_MARFA:
			populateListViewFurnizori(operatiiFurnizor.deserializeListFurnizori((String) result));
			break;
		case GET_FURNIZORI_PRODUSE:
			populateSpinnerFurnizori(operatiiFurnizor.deserializeListFurnizoriProduse((String) result));
			break;
		default:
			break;
		}

	}

}