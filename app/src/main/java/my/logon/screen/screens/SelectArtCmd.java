/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.ArticolCant;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.dialogs.ArticoleCantDialog;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumFiliale;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.listeners.ArticolCantListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.UtilsArticole;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectArtCmd extends ListActivity implements OperatiiArticolListener, ArticolCantListener {

	Button articoleBtn, saveArtBtn, pretBtn;
	String filiala = "", nume = "", cod = "", umStoc = "";
	String articolResponse = "";
	String pretResponse = "";
	String codArticol = "";
	String numeArticol = "", tipArticol = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "";
	LinearLayout redBtnTable, layoutStocKA, layoutPretMaxKA, layoutPretMediuKA;
	EditText valRedIntText, valRedDecText;

	public String globalDepozSel = "", artPromoText = "", globalCodDepartSelectetItem = "";

	ToggleButton tglButton, tglTipArtBtn;

	private EditText txtNumeArticol, textProcRed;
	private TextView textCodArticol, txtPretArt;
	private TextView textNumeArticol, textStocKA, textUmKA;

	ToggleButton tglProc;

	private TextView textStoc;
	private TextView textCant, procDisc, textPretTVA, textMultipluArt;

	private TextView textUM, textPretMinKA, textPretMediuKA;
	private TextView labelCant, labelStoc;
	private Spinner spinnerDepoz, spinnerUnitMas;

	private TextView textPromo, textCondPret;

	private boolean pretMod = false;

	private double initPrice = 0, cmpArt = 0;
	private double finalPrice = 0, minimKAPrice = 0;
	private double listPrice = 0, procDiscClient = 0;
	private double discMaxAV = 0, discMaxSD = 0;
	private double pretVanzare = 0, procentAprob = 0, selectedCant = 0;
	private String istoricPret;
	private double procReducereCmp = 0;
	private double valoareUmrez = 1, valoareUmren = 1;

	NumberFormat nf2;

	private static ArrayList<HashMap<String, String>> listUmVanz = null;

	SimpleAdapter adapterUmVanz;
	private double varProc = 0, valMultiplu = 0;

	String tipAlert = "", codPromo = "", infoArticol = "", Umb = "", cantUmb = "", selectedUnitMas = "", selectedUnitMasPret = "";

	private HashMap<String, String> artMap = null;
	double procR = 0, globalCantArt = 0;
	private String depozUnic = "", unitLogUnic = "";
	Dialog dialogSelFilArt;

	LinearLayout resultLayout;
	OperatiiArticol opArticol;

	private String selectedDepartamentAgent;
	private ArrayAdapter<String> adapterSpinnerDepozite;
	private ArticolDB articolDBSelected;
	private TextView txtImpachetare;

	private enum EnumTipCautare {
		NOMINAL, STATISTIC;
	}

	private EnumTipCautare tipCautareArticol = EnumTipCautare.NOMINAL;

	private ArrayList<ArticolDB> listArticoleStatistic;
	private ArrayList<ArticolDB> listArticoleCustodie;
	private double discountASDL;
	
	private Spinner spinnerFilialeCustodie;
	private List<ArticolCant> listArticoleCant;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectartcmdheader);

		initSelectionDepartament();

		if (!isCV())
			addSpinnerDepartamente();

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
		opArticol.setListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		resultLayout = (LinearLayout) findViewById(R.id.resLayout);
		resultLayout.setVisibility(View.INVISIBLE);

		nf2 = NumberFormat.getInstance(new Locale("en", "US"));

		textStocKA = (TextView) findViewById(R.id.textStocKA);
		textUmKA = (TextView) findViewById(R.id.textUmKA);

		layoutStocKA = (LinearLayout) findViewById(R.id.layoutStocKA);
		layoutStocKA.setVisibility(View.INVISIBLE);

		textPretMinKA = (TextView) findViewById(R.id.textPretMaxKA);

		textPretMediuKA = (TextView) findViewById(R.id.textPretMediuKA);
		layoutPretMediuKA = (LinearLayout) findViewById(R.id.layoutPretMediuKA);
		layoutPretMediuKA.setVisibility(View.INVISIBLE);

		layoutPretMaxKA = (LinearLayout) findViewById(R.id.layoutPretMaxKA);
		layoutPretMaxKA.setVisibility(View.INVISIBLE);

		this.articoleBtn = (Button) findViewById(R.id.articoleBtn);
		addListenerBtnArticole();

		this.saveArtBtn = (Button) findViewById(R.id.saveArtBtn);
		addListenerBtnSaveArt();

		this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
		addListenerToggle();
		this.tglButton.setChecked(true);

		this.tglTipArtBtn = (ToggleButton) findViewById(R.id.tglTipArt);
		addListenerTglTipArtBtn();

		this.redBtnTable = (LinearLayout) findViewById(R.id.RedBtnTable);

		txtPretArt = (TextView) findViewById(R.id.txtPretArt);

		this.tglProc = (ToggleButton) findViewById(R.id.tglProc);
		addListenerTglProc();

		this.pretBtn = (Button) findViewById(R.id.pretBtn);
		addListenerPretBtn();

		textProcRed = (EditText) findViewById(R.id.textProcRed);
		textProcRed.setFocusableInTouchMode(true);
		addListenerProcArt();

		procDisc = (TextView) findViewById(R.id.procDisc);

		textMultipluArt = (TextView) findViewById(R.id.txtMultipluArt);

		txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
		textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
		textCodArticol = (TextView) findViewById(R.id.textCodArticol);
		textUM = (TextView) findViewById(R.id.textUm);
		textStoc = (TextView) findViewById(R.id.textStoc);
		textCant = (EditText) findViewById(R.id.txtCantArt);
		labelCant = (TextView) findViewById(R.id.labelCant);
		labelStoc = (TextView) findViewById(R.id.labelStoc);
		textCondPret = (TextView) findViewById(R.id.textCondPret);
		textPretTVA = (TextView) findViewById(R.id.textPretTVA);
		txtImpachetare = (TextView) findViewById(R.id.txtImpachetare);

		textPromo = (TextView) findViewById(R.id.textPromo);
		addListenerTextPromo();

		txtNumeArticol.setHint("Introduceti cod articol");

		CreareComanda.articoleComanda = "";

		spinnerDepoz = (Spinner) findViewById(R.id.spinnerDepoz);

		ArrayList<String> arrayListDepozite = new ArrayList<String>();
		arrayListDepozite.addAll(Arrays.asList(UtilsGeneral.getDepoziteDistributie()));
		adapterSpinnerDepozite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListDepozite);

		adapterSpinnerDepozite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDepoz.setAdapter(adapterSpinnerDepozite);
		spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

		if (isLivrareCustodie()) {
			spinnerDepoz.setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutHeaderArt)).setVisibility(View.INVISIBLE);
			addSpinnerFilialeCustodie();
			getArticoleCustodie();

		} else {
			spinnerDepoz.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutHeaderArt)).setVisibility(View.VISIBLE);
		}

		spinnerUnitMas = (Spinner) findViewById(R.id.spinnerUnitMas);

		listUmVanz = new ArrayList<HashMap<String, String>>();
		adapterUmVanz = new SimpleAdapter(this, listUmVanz, R.layout.simplerowlayout, new String[] { "rowText" }, new int[] { R.id.textRowName });
		spinnerUnitMas.setVisibility(View.GONE);
		spinnerUnitMas.setOnItemSelectedListener(new OnSelectUnitMas());

		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol.setVisibility(View.INVISIBLE);
		textUM.setVisibility(View.INVISIBLE);

		textStoc.setVisibility(View.INVISIBLE);
		textCant.setVisibility(View.INVISIBLE);

		labelCant.setVisibility(View.INVISIBLE);

		txtPretArt.setVisibility(View.INVISIBLE);
		labelStoc.setVisibility(View.INVISIBLE);
		saveArtBtn.setVisibility(View.INVISIBLE);

		redBtnTable.setVisibility(View.INVISIBLE);
		textProcRed.setVisibility(View.INVISIBLE);
		pretBtn.setVisibility(View.INVISIBLE);
		textPromo.setVisibility(View.INVISIBLE);
		procDisc.setVisibility(View.INVISIBLE);
		textCondPret.setVisibility(View.INVISIBLE);
		textPretTVA.setVisibility(View.INVISIBLE);
		textMultipluArt.setVisibility(View.INVISIBLE);

	}

	private void addSpinnerFilialeCustodie() {

		ArrayAdapter<EnumFiliale> adapterFil = new ArrayAdapter<EnumFiliale>(getBaseContext(), android.R.layout.simple_list_item_1, EnumFiliale.values());
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
		spinnerFilialeCustodie = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

		spinnerFilialeCustodie.setAdapter(adapterFil);

		for (int i = 0; i < spinnerFilialeCustodie.getAdapter().getCount(); i++) {
			if (EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(i).toString()).equals(DateLivrare.getInstance().getUnitLog())) {
				spinnerFilialeCustodie.setSelection(i);
				break;
			}

		}

		spinnerFilialeCustodie.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				CreareComanda.filialaCustodie = EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getAdapter().getItem(position).toString());
				getArticoleCustodie();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}	
	
	private void addSpinnerDepartamente() {

		if (isLivrareCustodie())
			return;

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,
				DepartamentAgent.getDepartamenteAgent());

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
		final Spinner spinnerDepartament = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

		spinnerDepartament.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerDepartament.getSelectedItem().toString());

				if (isDepartExtra() && selectedDepartamentAgent.equals("02"))
					CreareComanda.filialaAlternativa = "BV90";

				populateListViewArticol(new ArrayList<ArticolDB>());

				if (selectedDepartamentAgent.equals("11") || selectedDepartamentAgent.equals("05")) {
					adapterSpinnerDepozite.clear();
					adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteGed());
					
					UtilsGeneral.trateazaExceptieMAV_BU(adapterSpinnerDepozite);

					if (selectedDepartamentAgent.equals("11"))
						spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
					else
						spinnerDepoz.setSelection(0);
				} else {

					if (UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED()) {
						spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("MAV1"));
						spinnerDepoz.setEnabled(false);
					} else {
						adapterSpinnerDepozite.clear();
						adapterSpinnerDepozite.addAll(UtilsGeneral.getDepoziteDistributie());
						spinnerDepoz.setSelection(0);
					}
				}

				saveArtBtn.setVisibility(View.INVISIBLE);

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerDepartament.setAdapter(adapter);
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	private void initSelectionDepartament() {

		selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

		if (isCV())
			selectedDepartamentAgent = "";

		if (isKA() || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED()
				|| UtilsUser.isOIVPD())
			selectedDepartamentAgent = "00";
	}

	private void CreateMenu(Menu menu) {

		if (isUserExceptie() && DateLivrare.getInstance().getTipComandaDistrib() != TipCmdDistrib.COMANDA_LIVRARE && !isLivrareCustodie()) {
			MenuItem mnu1 = menu.add(0, 0, 0, "Filiala");
			{
				mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
				populateListViewArticol(new ArrayList<ArticolDB>());

			}
		}

		if ((UtilsUser.isAgentOrSD() || UtilsUser.isOIVPD()) && DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_VANZARE) {
			MenuItem mnu2 = menu.add(0, 1, 1, "Tip cautare");
			{
				mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			}
		}

	}

	private boolean isUserExceptie() {

		if (UserInfo.getInstance().getTipUserSap().equals(Constants.tipInfoAv) || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM()
				|| UtilsUser.isCGED() || UtilsUser.isOIVPD()) {
			return false;
		} else if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")) {
			if (UserInfo.getInstance().getCodDepart().equals("02") || UserInfo.getInstance().getCodDepart().equals("05"))
				return true;
			else if (UserInfo.getInstance().getCodDepart().equals("01") && UserInfo.getInstance().getDepartExtra().contains("02"))
				return true;
		} else if (UserInfo.getInstance().getTipUser().equals("KA") || UtilsUser.isUserSDKA() || UtilsUser.isUserSK())
			return true;

		return false;
	}

	private boolean isDepartExtra() {
		return !UserInfo.getInstance().getDepartExtra().equals("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	// eveniment selectie unitate masura alternativa
	public class OnSelectUnitMas implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (codArticol.length() > 0) {

				artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
				selectedUnitMas = artMap.get("rowText");

				getFactoriConversie();

			}

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	private void getFactoriConversie() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			params.put("codArt", codArticol);
			params.put("unitMas", selectedUnitMas);

			opArticol.getFactorConversie(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	boolean isCV() {
		return (UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")) && !UtilsUser.isSMR()
				&& !UtilsUser.isCVR() && !UtilsUser.isSSCM() && !UtilsUser.isCGED() && !UtilsUser.isOIVPD();
	}

	// eveniment selectie depozit
	public class OnSelectDepozit implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (codArticol.length() > 0) {
				String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

				if (tokenDep[0].trim().length() == 2)
					globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
				else
					globalDepozSel = tokenDep[0].trim();

				performListArtStoc();
			}

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			showSelectFilArtDialogBox();
			return true;

		case 1:
			showCautareStatisticDialog();
			return true;

		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void showCautareStatisticDialog() {

		final Dialog dialogStatistic = new Dialog(SelectArtCmd.this);
		dialogStatistic.setContentView(R.layout.cautare_statistic_dialog);
		dialogStatistic.setTitle("Cautare articole");
		dialogStatistic.setCancelable(true);
		dialogStatistic.show();

		final RadioButton radioNominal = (RadioButton) dialogStatistic.findViewById(R.id.radio1);
		final RadioButton radioStatistic = (RadioButton) dialogStatistic.findViewById(R.id.radio2);

		if (tipCautareArticol == EnumTipCautare.NOMINAL)
			radioNominal.setChecked(true);
		else if (tipCautareArticol == EnumTipCautare.STATISTIC)
			radioStatistic.setChecked(true);

		Button btnStatistic = (Button) dialogStatistic.findViewById(R.id.btnStatistic);

		btnStatistic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (radioNominal.isChecked())
					tipCautareArticol = EnumTipCautare.NOMINAL;
				else if (radioStatistic.isChecked())
					tipCautareArticol = EnumTipCautare.STATISTIC;

				dialogStatistic.dismiss();

				trateazaTipCautareArticole(tipCautareArticol);

			}
		});

	}

	private void trateazaTipCautareArticole(EnumTipCautare tipCautare) {
		if (tipCautare == EnumTipCautare.NOMINAL) {
			setCautaLayoutVisibility(true);
			populateListViewArticol(new ArrayList<ArticolDB>());

		} else if (tipCautare == EnumTipCautare.STATISTIC) {
			setCautaLayoutVisibility(false);
			populateListViewArticol(new ArrayList<ArticolDB>());
			getArticoleStatistic();
		}
	}

	private void setCautaLayoutVisibility(boolean isVisible) {
		if (isVisible) {
			articoleBtn.setVisibility(View.VISIBLE);
			tglButton.setVisibility(View.VISIBLE);
			tglTipArtBtn.setVisibility(View.VISIBLE);
			txtNumeArticol.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.GONE);

		} else {
			articoleBtn.setVisibility(View.INVISIBLE);
			tglButton.setVisibility(View.INVISIBLE);
			tglTipArtBtn.setVisibility(View.INVISIBLE);
			txtNumeArticol.setVisibility(View.INVISIBLE);

		}

	}

	private void getArticoleStatistic() {

		if (listArticoleStatistic != null && !listArticoleStatistic.isEmpty()) {
			((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);
			populateListViewArticol(listArticoleStatistic);
			return;
		}

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codClient", CreareComanda.codClientVar);
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("departament", selectedDepartamentAgent);

		opArticol.getArticoleStatistic(params);
	}

	private void getArticoleCustodie() {

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codClient", CreareComanda.codClientVar);
		params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));
		params.put("departament", selectedDepartamentAgent);

		opArticol.getArticoleCustodie(params);
	}

	public void showSelectFilArtDialogBox() {
		dialogSelFilArt = new Dialog(SelectArtCmd.this);
		dialogSelFilArt.setContentView(R.layout.selectfilartdialogbox);
		dialogSelFilArt.setTitle("Selectati filiala");
		dialogSelFilArt.setCancelable(false);
		dialogSelFilArt.show();

		final RadioButton radioFilAg = (RadioButton) dialogSelFilArt.findViewById(R.id.radio1);
		radioFilAg.setText(UserInfo.getInstance().getUnitLog());

		radioFilAg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

				}

			}
		});

		final RadioButton radioFilBV90 = (RadioButton) dialogSelFilArt.findViewById(R.id.radio2);

		radioFilBV90.setText("BV90");

		radioFilBV90.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

				}

			}
		});

		if (CreareComanda.filialaAlternativa.equals(UserInfo.getInstance().getUnitLog()))
			radioFilAg.setChecked(true);
		else
			radioFilBV90.setChecked(true);

		Button btnOkFilArt = (Button) dialogSelFilArt.findViewById(R.id.btnOkSelFilArt);
		btnOkFilArt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (radioFilAg.isChecked()) {
					CreareComanda.filialaAlternativa = UserInfo.getInstance().getUnitLog();

				} else {
					CreareComanda.filialaAlternativa = "BV90";
				}

				if (!numeArticol.equals("")) {
					performListArtStoc();
				}

				dialogSelFilArt.dismiss();

			}
		});

	}

	public void addListenerTextPromo() {
		textPromo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!artPromoText.equals(""))
					showPromoWindow(artPromoText);

			}
		});
	}

	public void addListenerTglProc() {
		tglProc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (globalCantArt > 0) {

					if (tglProc.isChecked()) {

						nf2.setMinimumFractionDigits(3);
						nf2.setMaximumFractionDigits(3);

						varProc = -1;

						textProcRed.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
						textProcRed.requestFocus();
						textProcRed.selectAll();

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

						txtPretArt.setText("0");

						pretMod = true;
						finalPrice = initPrice;

						if (CreareComanda.canalDistrib.equals("10"))
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
						else
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

					} else {

						nf2.setMinimumFractionDigits(3);
						nf2.setMaximumFractionDigits(3);

						varProc = 0;
						textProcRed.setText("");

						textProcRed.setSelection(textProcRed.getText().length());
						textProcRed.requestFocus();
						textProcRed.selectAll();
						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

						txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
						pretMod = false;
						finalPrice = initPrice;

						if (CreareComanda.canalDistrib.equals("10"))
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
						else
							textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

					}
				}
			}
		});

	}

	public void addListenerProcArt() {

		textProcRed.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (textProcRed.hasFocus()) {
					InputMethodManager inputStatus = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputStatus.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}

			}
		});

		textProcRed.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					nf2.setMinimumFractionDigits(3);
					nf2.setMaximumFractionDigits(3);

					// verif. cantitate

					if (globalCantArt > 0) {

						if (!pretMod) // modificare valoare
						{
							if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

								if (varProc != -1) {
									varProc = Double.parseDouble(textProcRed.getText().toString());

									if (varProc >= 0) {

										double newPr = (((initPrice / globalCantArt) * valMultiplu - (initPrice / globalCantArt) * valMultiplu
												* (varProc / 100)));

										txtPretArt.setText(nf2.format(newPr));
										finalPrice = newPr;

										if (CreareComanda.canalDistrib.equals("10"))
											textPretTVA.setText(String.valueOf(nf2.format(finalPrice * Constants.TVA)));
										else
											textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));
									}
								}

							} else {

								txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
								if (CreareComanda.canalDistrib.equals("10"))
									textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
								else
									textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));
							}

						}// modificare procent
						else {

							if (isNumeric(textProcRed.getText().toString()) && isNumeric(textCant.getText().toString())) {

								double val1 = Double.parseDouble(textProcRed.getText().toString());

								procR = (((initPrice / globalCantArt * valMultiplu) - val1) / ((initPrice / globalCantArt * valMultiplu))) * 100;

								txtPretArt.setText(nf2.format(procR));
								finalPrice = Double.parseDouble(textProcRed.getText().toString());
							} else {
								txtPretArt.setText("0");
								finalPrice = 0;
							}

							if (CreareComanda.canalDistrib.equals("10"))
								textPretTVA.setText(String.valueOf(nf2.format(finalPrice * Constants.TVA)));
							else
								textPretTVA.setText(String.valueOf(nf2.format(finalPrice)));

						}

						afisPretUmAlternativa();
						
					}// sf. verif. cantitate

				} catch (Exception ex) {
					finalPrice = 0;
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});
	}

	public boolean isNumeric(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public void addListenerPretBtn() {
		pretBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				InputMethodManager mgr;

				try {

					if (textCant.getText().toString().trim().equals("")) {
						Toast.makeText(getApplicationContext(), "Cantitate invalida!", Toast.LENGTH_SHORT).show();
						return;
					}

					mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

					performGetPret();

				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	protected void performGetPret() {

		try {

			selectedCant = Double.parseDouble(textCant.getText().toString().trim());

			selectedUnitMas = "";
			selectedUnitMasPret = "";
			if (listUmVanz.size() > 1) {
				artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
				selectedUnitMas = artMap.get("rowText");
				selectedUnitMasPret = artMap.get("rowText");
			}

			if (selectedCant > 0) {

				actionGetPret();
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("unchecked")
	private void actionGetPret() {

		HashMap<String, String> params = new HashMap<String, String>();

		String depSel = "";
		String uLog = UserInfo.getInstance().getUnitLog();
		String tipUser = "";

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		depSel = globalCodDepartSelectetItem.substring(0, 2);

		if (CreareComanda.canalDistrib.equals("20") || globalDepozSel.equals("MAV1") || globalDepozSel.equals("MAV2")
				|| globalDepozSel.equals("DSCM")) {
			depSel = "11";
			uLog = UserInfo.getInstance().getUnitLog().substring(0, 2) + "2" + UserInfo.getInstance().getUnitLog().substring(3, 4);
		}

		if (UserInfo.getInstance().getTipAcces().equals("9") || UtilsUser.isOIVPD()) {
			tipUser = "AV";
		} else if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipUser = "SD";
		} else if (UserInfo.getInstance().getTipAcces().equals("14") || UserInfo.getInstance().getTipAcces().equals("12")) {
			tipUser = "DV";
		} else if (UserInfo.getInstance().getTipAcces().equals("27"))
			tipUser = "KA";
		else if (UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED())
			tipUser = "CV";

		String paramUnitMas = textUM.getText().toString();

		if (listUmVanz.size() > 1) {
			artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
			paramUnitMas = artMap.get("rowText");

		}

		params.put("client", CreareComanda.codClientVar);
		params.put("articol", codArticol);
		params.put("cantitate", textCant.getText().toString().trim());
		params.put("depart", depSel);
		params.put("um", paramUnitMas);
		params.put("ul", uLog);
		params.put("tipUser", tipUser);
		params.put("depoz", globalDepozSel);
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("canalDistrib", CreareComanda.canalDistrib);
		params.put("filialaAlternativa", CreareComanda.filialaAlternativa);
		params.put("filialaClp", DateLivrare.getInstance().getCodFilialaCLP());

		opArticol.getPret(params);

	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {
					if (tglTipArtBtn.isChecked())
						txtNumeArticol.setHint("Introduceti cod sintetic");
					else
						txtNumeArticol.setHint("Introduceti cod articol");
				} else {
					if (tglTipArtBtn.isChecked())
						txtNumeArticol.setHint("Introduceti nume sintetic");
					else
						txtNumeArticol.setHint("Introduceti nume articol");
				}
			}
		});

	}

	public void addListenerTglTipArtBtn() {
		tglTipArtBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (tglTipArtBtn.isChecked()) {
					if (!tglButton.isChecked())
						txtNumeArticol.setHint("Introduceti nume sintetic");
					else
						txtNumeArticol.setHint("Introduceti cod sintetic");
				} else {
					if (!tglButton.isChecked())
						txtNumeArticol.setHint("Introduceti nume articol");
					else
						txtNumeArticol.setHint("Introduceti cod articol");

				}
			}
		});

	}

	public void populateListViewArticol(List<ArticolDB> resultsList) {
		txtNumeArticol.setText("");
		resultLayout.setVisibility(View.INVISIBLE);
		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
		setListAdapter(adapterArticole);

	}

	
	private void showArticoleCantDialog(String listArticoleSer) {
		listArticoleCant = opArticol.deserializeArticoleCant(listArticoleSer);

		if (!listArticoleCant.isEmpty()) {

			int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
			int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

			ArticoleCantDialog articoleCant = new ArticoleCantDialog(this, listArticoleCant);
			articoleCant.setArticoleCantListener(this);
			articoleCant.getWindow().setLayout(width, height);
			articoleCant.show();
		}

	}
	
	private void showArticoleCantDialog() {
		if (listArticoleCant!= null && !listArticoleCant.isEmpty()) {

			int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
			int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

			ArticoleCantDialog articoleCant = new ArticoleCantDialog(this, listArticoleCant);
			articoleCant.setArticoleCantListener(this);
			articoleCant.getWindow().setLayout(width, height);
			articoleCant.show();
		}
	}	
	
	boolean isNotDepartRestricted(String codDepart) {
		if (UserInfo.getInstance().getTipAcces().equals("27"))
			return true;
		else
			return UserInfo.getInstance().getCodDepart().equals(codDepart);
	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {

						textNumeArticol.setVisibility(View.GONE);
						textCodArticol.setVisibility(View.GONE);
						textUM.setVisibility(View.GONE);
						textStoc.setVisibility(View.GONE);
						textCant.setVisibility(View.GONE);
						labelCant.setVisibility(View.GONE);
						labelStoc.setVisibility(View.GONE);
						saveArtBtn.setVisibility(View.GONE);

						pretBtn.setVisibility(View.GONE);

						redBtnTable.setVisibility(View.GONE);
						spinnerUnitMas.setVisibility(View.GONE);
						layoutStocKA.setVisibility(View.GONE);

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

						performGetArticole();

					} else {
						Toast.makeText(getApplicationContext(), "Introduceti nume articol!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void performGetArticole() {
		if (DateLivrare.getInstance().getFurnizorComanda() != null && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa() != null) {
			performGetArticoleFurnizor();
		} else {
			performGetArticoleDistributie();
		}

	}

	protected void performGetArticoleDistributie() {

		String numeArticol = txtNumeArticol.getText().toString().trim();
		String tipCautare = "", tipArticol = "";

		if (tglButton.isChecked())
			tipCautare = "C";
		else
			tipCautare = "N";

		if (tglTipArtBtn.isChecked())
			tipArticol = "S";
		else
			tipArticol = "A";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("searchString", numeArticol);
		params.put("tipArticol", tipArticol);
		params.put("tipCautare", tipCautare);
		params.put("departament", selectedDepartamentAgent);
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("codUser", UserInfo.getInstance().getCod());

		opArticol.getArticoleDistributie(params);

	}

	private void performGetArticoleFurnizor() {
		HashMap<String, String> params = new HashMap<String, String>();

		String tipArticol1 = "", tipArticol2 = "";

		if (tglButton.isChecked()) {
			tipArticol1 = "1";
			if (tglTipArtBtn.isChecked()) {
				tipArticol2 = "2";
			} else {
				tipArticol2 = "1";
			}

		} else {
			tipArticol1 = "2";
			if (tglTipArtBtn.isChecked()) {
				tipArticol2 = "2";
			} else {
				tipArticol2 = "1";
			}

		}

		String numeArticol = txtNumeArticol.getText().toString().trim();

		params.put("codArticol", numeArticol);
		params.put("tip1", tipArticol1);
		params.put("tip2", tipArticol2);
		params.put("furnizor", DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa());
		params.put("codDepart", selectedDepartamentAgent);
		params.put("codUser", UserInfo.getInstance().getCod());

		opArticol.getArticoleFurnizor(params);
	}

	@SuppressWarnings("unchecked")
	public void addListenerBtnSaveArt() {
		saveArtBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				try {

					if (isLivrareCustodie()) {
						saveArticolCustodie();
						return;
					}

					String localUnitMas = "";
					String alteValori = "", subCmp = "0";
					boolean altDepozit = false;

					if (textCant.getVisibility() != View.VISIBLE) {
						return;
					}

					if (textProcRed.getText().toString().trim().length() == 0) {
						if (tglProc.getText().equals(("%")))
							textProcRed.setText("0");

					}

					if (CreareComanda.articoleComanda.equals("")) {
						depozUnic = globalDepozSel.substring(2, 3);
						unitLogUnic = CreareComanda.filialaAlternativa;
					}

					if (!depozUnic.equals(globalDepozSel.substring(2, 3))) {
						altDepozit = true;
					}

					if (!CreareComanda.depozitUnic.equals("") && !CreareComanda.depozitUnic.equals(globalDepozSel.substring(2, 3))) {
						altDepozit = true;
					}

					if (!unitLogUnic.equals(CreareComanda.filialaAlternativa) && !isUserExceptieFiliale()) {
						Toast.makeText(getApplicationContext(), "Selectati articole dintr-o singura filiala!", Toast.LENGTH_LONG).show();

						return;
					}

					if (listUmVanz.size() > 1) {

						artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
						String currentUnitMas = artMap.get("rowText");

						if (!currentUnitMas.equals(selectedUnitMasPret)) {
							Toast.makeText(getApplicationContext(), "Unitatea de masura nu corespunde celei pentru care a fost solicitat pretul.",
									Toast.LENGTH_LONG).show();
							return;

						}

					}
					
					String cantArticol = textCant.getText().toString().trim();

					if (selectedCant != Double.parseDouble(cantArticol)) {

						Toast.makeText(getApplicationContext(), "Pretul nu corespunde cantitatii solicitate!", Toast.LENGTH_LONG).show();
						return;
					}

					if (!isComandaDL()
							&& Double.parseDouble(textCant.getText().toString().trim()) * (valoareUmrez / valoareUmren) > Double.parseDouble(textStoc
									.getText().toString().replaceAll(",", ""))) {
						Toast.makeText(getApplicationContext(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
						return;
					}

					// verificare umvanz.

					localUnitMas = textUM.getText().toString().trim();

					if (listUmVanz.size() > 1) {

						artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
						localUnitMas = artMap.get("rowText");

						if (!selectedUnitMas.equals(localUnitMas)) {
							Toast.makeText(getApplicationContext(), "U.m. vanzare eronata!", Toast.LENGTH_LONG).show();

							return;
						}

					}

					// verificare procent discount
					double procRedFin = 0, valArticol = 0;
					procentAprob = 0;

					// exceptie material transport
					if (InfoStrings.isMatTransport(codArticol)) {
						initPrice = pretVanzare = finalPrice;
					}

					if (finalPrice == initPrice) // pretul din sap e pe
													// cantitate, daca se
													// modifica devine pe
													// unitate
						finalPrice = (finalPrice / globalCantArt) * valMultiplu;

					valArticol = (finalPrice / valMultiplu) * globalCantArt;

					if (initPrice != 0) {
						if (!tglProc.isChecked()) {
							if (textProcRed.getText().length() > 0)
								procRedFin = Double.parseDouble(textProcRed.getText().toString());
							else
								procRedFin = 0;
						} else
							procRedFin = (1 - finalPrice / (initPrice / globalCantArt * valMultiplu)) * 100;
					}

					if (procRedFin > 0)// procent_aprob se calculeaza doar daca
										// exista proc. de reducere dat de ag.
					{
						procentAprob = (1 - finalPrice / (pretVanzare / globalCantArt * valMultiplu)) * 100;
					}

					// sf. verificare

					if (finalPrice != 0) {

						tipAlert = " ";

						if (!UserInfo.getInstance().getTipAcces().equals("27")) {
							if (procentAprob > discMaxAV) {
								tipAlert = "SD";
							}
						}

						// pentru KA este nevoie de aprobarea SD-ului in cazul
						// in care
						// cantitatea comandata este mai mare decat jumatate din
						// stocul disponibil
						if (UserInfo.getInstance().getTipAcces().equals("27")) {
							if (Double.parseDouble(cantArticol) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", "")) / 2) {

								if (!globalCodDepartSelectetItem.equals("11"))
									tipAlert = "SD";
							}
						}

						if (UserInfo.getInstance().getTipAcces().equals("27")) {// KA
							if (procentAprob > 0 && finalPrice < minimKAPrice) {

								if (!tipAlert.equals(""))
									tipAlert += ";" + "DV";
								else
									tipAlert = "DV";
							}

						} else {// agenti
							if (procentAprob > discMaxSD) {
								tipAlert = "DV";
							}
						}

						if ((finalPrice / valMultiplu) < cmpArt) {

							Toast.makeText(getApplicationContext(), "Procentul de reducere este mai mare decat cel acceptat.", Toast.LENGTH_LONG)
									.show();

							subCmp = "1";
							// restrictie eliminata pentru a se putea vinde
							// articole cu promotii de la dep 03. 12 martie 2019
							// return;

						}

						if ((UtilsUser.isASDL() || UtilsUser.isOIVPD())
								&& Math.round(procentAprob * 1000.0) / 1000.0 <= Math.round(discountASDL * 1000.0) / 1000.0) {
							tipAlert = " ";
						}

						double procRedFact = 0; // factura de reducere
						if (listPrice != 0)
							procRedFact = (initPrice / globalCantArt * valMultiplu - finalPrice) / (listPrice / globalCantArt * valMultiplu) * 100;

						alteValori = String.valueOf(valArticol) + "!" + String.valueOf(listPrice) + "!" + String.valueOf(initPrice) + "!"
								+ String.valueOf(discMaxAV) + "!" + String.valueOf(discMaxSD) + "!" + codPromo + "!" + subCmp;

						NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
						nf.setMaximumFractionDigits(2);

						if (codArticol.length() == 18)
							codArticol = codArticol.substring(10, 18);

						ArticolComanda unArticol = new ArticolComanda();
						unArticol.setNumeArticol(numeArticol);
						unArticol.setCodArticol(codArticol);
						unArticol.setCantitate(Double.valueOf(cantArticol));
						unArticol.setDepozit(globalDepozSel);
						unArticol.setPretUnit(finalPrice);
						unArticol.setProcent(Double.valueOf(procRedFin));
						unArticol.setUm(localUnitMas);
						unArticol.setProcentFact(Double.valueOf(procRedFact));
						unArticol.setConditie(false);
						unArticol.setDiscClient(procDiscClient);
						unArticol.setProcAprob(procentAprob);
						unArticol.setMultiplu(valMultiplu);
						unArticol.setPret(valArticol);
						unArticol.setMoneda("RON");
						unArticol.setInfoArticol(infoArticol);
						unArticol.setCantUmb(Double.valueOf(cantUmb));
						unArticol.setUmb(Umb);
						unArticol.setAlteValori(alteValori);
						unArticol.setDepart(globalCodDepartSelectetItem);
						unArticol.setTipArt(tipArticol);
						unArticol.setPromotie(Integer.parseInt(codPromo));
						unArticol.setObservatii(tipAlert);
						unArticol.setDepartAprob(articolDBSelected.getDepartAprob());
						unArticol.setUmPalet(articolDBSelected.isUmPalet());
						unArticol.setCategorie(articolDBSelected.getCategorie());
						unArticol.setLungime(articolDBSelected.getLungime());
						unArticol.setCmp(cmpArt);
						unArticol.setFilialaSite(CreareComanda.filialaAlternativa);

						if (procRedFin > 0)
							unArticol.setIstoricPret(istoricPret);

						ListaArticoleComanda listaComanda = ListaArticoleComanda.getInstance();
						listaComanda.addArticolComanda(unArticol);

						if (!altDepozit) {
							if (CreareComanda.articoleComanda.indexOf(codArticol) == -1) // articolul
																							// nu
																							// e
																							// adaugat
																							// deja
								CreareComanda.articoleComanda += numeArticol + "#" + codArticol + "#" + cantArticol + "#"
										+ String.valueOf(finalPrice) + "#" + localUnitMas + "#" + globalDepozSel + "#" + nf.format(procRedFin) + "#"
										+ tipAlert + "#" + codPromo + "#" + nf.format(procRedFact) + "#" + nf.format(procDiscClient) + "#"
										+ nf.format(procentAprob) + "#" + valMultiplu + "#" + String.valueOf(valArticol) + "#" + infoArticol + "#"
										+ Umb + "#" + cantUmb + "#" + alteValori + "#" + globalCodDepartSelectetItem + "#" + tipArticol + "@@";

						} else {
							Toast.makeText(getApplicationContext(), "Comanda contine depozite diferite, articolul nu a fost adaugat! ",
									Toast.LENGTH_LONG).show();

						}
						
						showArticoleCantDialog();
						
						if (UtilsArticole.isArticolPal(articolDBSelected.getSintetic()))
							afiseazaArticoleCant(codArticol, CreareComanda.filialaAlternativa);

						textNumeArticol.setText("");
						textCodArticol.setText("");
						textUM.setText("");
						textStoc.setText("");
						textCant.setText("");
						textPromo.setText("");
						txtNumeArticol.setText("");

						numeArticol = "";
						codArticol = "";
						tipArticol = "";
						umStoc = "";
						globalCodDepartSelectetItem = "";

						localUnitMas = "";
						procDiscClient = 0;
						initPrice = 0;
						finalPrice = 0;
						valArticol = 0;
						globalCantArt = 0;
						minimKAPrice = 0;
						cmpArt = 0;
						subCmp = "0";

						valoareUmrez = 1;
						valoareUmren = 1;

						redBtnTable.setVisibility(View.GONE);
						labelStoc.setVisibility(View.GONE);
						labelCant.setVisibility(View.GONE);
						textCant.setVisibility(View.GONE);
						pretBtn.setVisibility(View.GONE);
						spinnerUnitMas.setVisibility(View.GONE);
						layoutStocKA.setVisibility(View.GONE);
						resultLayout.setVisibility(View.INVISIBLE);

						if (!tglProc.isChecked())
							tglProc.performClick();

						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(textCant.getWindowToken(), 0);

					} else {

						Toast toast = Toast.makeText(getApplicationContext(), "Articolul nu are pret definit!", Toast.LENGTH_SHORT);
						toast.show();
					}

					textProcRed.setText("");

				} catch (Exception e) {

					Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});

	}

	private void afiseazaArticoleCant(String codArticol, String filiala) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("unitLog", filiala);
		params.put("codArtPal", codArticol);

		opArticol.getArticoleCant(params);
	}	
	
	
	private boolean isUserExceptieFiliale() {
		return globalCodDepartSelectetItem.equals("01") || globalCodDepartSelectetItem.equals("02");
	}	
	
	
	private void saveArticolCustodie() {

		if (textCant.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Cantitate invalida", Toast.LENGTH_LONG).show();
			return;
		}

		if (Double.parseDouble(textCant.getText().toString()) <= 0) {
			Toast.makeText(getApplicationContext(), "Cantitate invalida", Toast.LENGTH_LONG).show();
			return;
		}

		if (Double.parseDouble(textCant.getText().toString().trim()) > Double.parseDouble(textStoc.getText().toString().replaceAll(",", ""))) {
			Toast.makeText(getApplicationContext(), "Stoc insuficient.", Toast.LENGTH_LONG).show();
			return;
		}

		String cantArticol = textCant.getText().toString().trim();
		String localUnitMas = textUM.getText().toString().trim();

		if (codArticol.length() == 18)
			codArticol = codArticol.substring(10, 18);

		ArticolComanda unArticol = new ArticolComanda();
		unArticol.setNumeArticol(numeArticol);
		unArticol.setCodArticol(codArticol);
		unArticol.setCantitate(Double.valueOf(cantArticol));
		unArticol.setDepozit("");
		unArticol.setPretUnit(0);
		unArticol.setProcent(0);
		unArticol.setUm(localUnitMas);
		unArticol.setProcentFact(0);
		unArticol.setConditie(false);
		unArticol.setDiscClient(0);
		unArticol.setProcAprob(0);
		unArticol.setMultiplu(1);
		unArticol.setPret(0);
		unArticol.setMoneda("RON");
		unArticol.setInfoArticol("");
		unArticol.setCantUmb(Double.valueOf(cantArticol));
		unArticol.setUmb(localUnitMas);
		unArticol.setAlteValori("");
		unArticol.setDepart(globalCodDepartSelectetItem);
		unArticol.setTipArt("");
		unArticol.setPromotie(0);
		unArticol.setObservatii("");
		unArticol.setDepartAprob(articolDBSelected.getDepartAprob());
		unArticol.setUmPalet(false);
		unArticol.setCategorie("");
		unArticol.setLungime(0);
		unArticol.setCmp(0);

		ListaArticoleComanda listaComanda = ListaArticoleComanda.getInstance();
		listaComanda.addArticolComanda(unArticol);

		textNumeArticol.setText("");
		textCodArticol.setText("");
		textUM.setText("");
		textStoc.setText("");
		textCant.setText("");
		txtNumeArticol.setText("");
		resultLayout.setVisibility(View.INVISIBLE);

	}

	private boolean isLivrareCustodie() {
		return DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE;
	}

	private boolean isComandaDL() {
		return DateLivrare.getInstance().getFurnizorComanda() != null && DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa() != null;
	}

	@SuppressWarnings("unchecked")
	private void listArtStoc(String pretResponse) {

		resultLayout.setVisibility(View.VISIBLE);

		if (!pretResponse.equals("-1")) {

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			String[] tokenStoc = pretResponse.split("#");

			textNumeArticol.setVisibility(View.VISIBLE);
			textCodArticol.setVisibility(View.VISIBLE);
			textUM.setVisibility(View.VISIBLE);
			textStoc.setVisibility(View.VISIBLE);
			textCant.setVisibility(View.VISIBLE);
			labelCant.setVisibility(View.VISIBLE);
			labelStoc.setVisibility(View.VISIBLE);
			pretBtn.setVisibility(View.VISIBLE);

			textUM.setText(tokenStoc[1]);

			// verificare daca se afiseaza valoarea stocului (exceptia de la
			// BV90 pentr ag/sd)
			if (tokenStoc[2].equals("0")) {
				textStoc.setVisibility(View.INVISIBLE);
			} else {
				textStoc.setVisibility(View.VISIBLE);
			}

			textStoc.setText(nf2.format(Double.valueOf(tokenStoc[0])));

			// pentru KA se afiseaza stocul disponibil = stoc real / 2
			if (UserInfo.getInstance().getTipAcces().equals("27")) {
				layoutStocKA.setVisibility(View.VISIBLE);
				textUmKA.setText(tokenStoc[1]);
				textStocKA.setText(nf2.format(Double.valueOf(tokenStoc[0]) / 2));
			}

			umStoc = tokenStoc[1];

			artMap = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();

			String stocUM = artMap.get("rowText");

			if (!stocUM.equals(tokenStoc[1]) && !tokenStoc[1].trim().equals("")) // um
																					// vanz
																					// si
																					// um
																					// vanz
																					// difera
			{
				HashMap<String, String> tempUmVanz;
				tempUmVanz = new HashMap<String, String>();
				tempUmVanz.put("rowText", tokenStoc[1]);

				listUmVanz.add(tempUmVanz);
				spinnerUnitMas.setAdapter(adapterUmVanz);
				spinnerUnitMas.setVisibility(View.VISIBLE);
			}

		} else {

			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

			textUM.setText("");
			textStoc.setText("");
		}

	}

	private void listArtStocCustodie(String stocResponse) {
		if (!stocResponse.equals("-1")) {

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			resultLayout.setVisibility(View.VISIBLE);
			String[] tokenStoc = stocResponse.split("#");

			textNumeArticol.setVisibility(View.VISIBLE);
			textCodArticol.setVisibility(View.VISIBLE);
			textUM.setVisibility(View.VISIBLE);
			textStoc.setVisibility(View.VISIBLE);
			textCant.setVisibility(View.VISIBLE);
			labelCant.setVisibility(View.VISIBLE);
			labelStoc.setVisibility(View.VISIBLE);

			textUM.setText(tokenStoc[1]);
			textStoc.setText(nf2.format(Double.valueOf(tokenStoc[0])));

			saveArtBtn.setVisibility(View.VISIBLE);

		} else {
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();
		}
	}

	private void listArtPret(String pretResponse) {

		try {
			if (!pretResponse.equals("-1") && pretResponse.contains("#")) {

				String[] tokenPret = pretResponse.split("#");

				valMultiplu = Double.parseDouble(tokenPret[13].trim());

				globalCantArt = Double.parseDouble(tokenPret[14]);

				cantUmb = tokenPret[14].toString();
				Umb = tokenPret[15].toString();

				cmpArt = Double.parseDouble(tokenPret[17]);

				saveArtBtn.setVisibility(View.VISIBLE);

				textPromo.setText("");

				nf2.setMinimumFractionDigits(3);
				nf2.setMaximumFractionDigits(3);

				codPromo = "-1";

				txtPretArt.setVisibility(View.VISIBLE);

				initPrice = Double.parseDouble(tokenPret[1]); // pret cu
																// discount pe
																// client
				listPrice = Double.parseDouble(tokenPret[8]); // pret de lista

				txtImpachetare.setText(tokenPret[19]);

				afisIstoricPret(tokenPret[20]);

				istoricPret = UtilsFormatting.getIstoricPret(tokenPret[20], EnumTipComanda.DISTRIBUTIE);

				procReducereCmp = Double.parseDouble(tokenPret[21]);
				((TextView) findViewById(R.id.textPretGed)).setText(tokenPret[22]);

				procDiscClient = 0;
				minimKAPrice = 0;
				if (UserInfo.getInstance().getTipAcces().equals("27")) {

					minimKAPrice = listPrice / globalCantArt * valMultiplu - (listPrice / globalCantArt * valMultiplu)
							* Double.valueOf(tokenPret[16]) / 100;

					if (listPrice > 0)
						procDiscClient = 100 - (initPrice / listPrice) * 100;

					layoutPretMaxKA.setVisibility(View.VISIBLE);
					textPretMinKA.setText(String.valueOf(nf2.format(minimKAPrice)));

					layoutPretMediuKA.setVisibility(View.VISIBLE);
					textPretMediuKA.setText(nf2.format(Double.valueOf(tokenPret[18])));

				}

				if (globalDepozSel.substring(2, 3).equals("V")) {
					// pentru depozitele de vanzari se verifica cmp-ul

					if (initPrice / globalCantArt * valMultiplu < cmpArt) {

						Toast.makeText(getApplicationContext(), "Pret sub cmp!", Toast.LENGTH_LONG).show();

						if (layoutPretMaxKA.getVisibility() == View.VISIBLE)
							layoutPretMaxKA.setVisibility(View.INVISIBLE);

						// eliminat pentru vanzare promotii 03
						// return;
					}

				}

				finalPrice = initPrice;

				textProcRed.setText("");

				redBtnTable.setVisibility(View.VISIBLE);
				textProcRed.setVisibility(View.VISIBLE);
				procDisc.setVisibility(View.VISIBLE);
				textPretTVA.setVisibility(View.VISIBLE);
				textMultipluArt.setVisibility(View.VISIBLE);

				if (InfoStrings.isMatTransport(codArticol)) {
					txtPretArt.setVisibility(View.INVISIBLE);
				} else {
					txtPretArt.setVisibility(View.VISIBLE);
				}

				textMultipluArt.setText("Unit.pret: " + String.valueOf(valMultiplu) + " " + umStoc);

				txtPretArt.setText(nf2.format(initPrice / globalCantArt * valMultiplu));
				txtPretArt.setHint(nf2.format(initPrice / globalCantArt * valMultiplu));

				if (CreareComanda.canalDistrib.equals("10"))
					textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu * Constants.TVA)));
				else
					textPretTVA.setText(String.valueOf(nf2.format(initPrice / globalCantArt * valMultiplu)));

				discMaxAV = Double.valueOf(tokenPret[10]);
				discMaxSD = Double.valueOf(tokenPret[11]);

				String[] condPret = tokenPret[9].toString().split(";");
				infoArticol = tokenPret[9].replace(',', '.');

				int ii = 0;
				String[] tokPret;
				String stringCondPret = "";
				Double valCondPret = 0.0;

				for (ii = 0; ii < condPret.length; ii++) {
					tokPret = condPret[ii].split(":");
					valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
					if (valCondPret != 0) {
						stringCondPret += tokPret[0] + addSpace(20 - tokPret[0].length()) + ":"
								+ addSpace(10 - String.valueOf(nf2.format(valCondPret)).length()) + nf2.format(valCondPret)
								+ System.getProperty("line.separator");

					}

				}

				pretVanzare = listPrice; // calcul procent aprobare

				textCondPret.setVisibility(View.VISIBLE);

				textCondPret.setText(stringCondPret);

				// calcul doar pentru AG
				if (!UserInfo.getInstance().getTipAcces().equals("27")) {
					if (listPrice > 0)
						procDiscClient = 100 - (initPrice / listPrice) * 100;
				}

				afisPretUmAlternativa();
				
				procDisc.setText(nf2.format(procDiscClient));
				txtPretArt.setEnabled(true);
				textProcRed.setFocusableInTouchMode(true);
				tglProc.setEnabled(true);

				// pentru totaluri negociate nu se modifica preturi
				if (CreareComanda.isTotalNegociat) {
					textProcRed.setFocusable(false);
					tglProc.setEnabled(false);
				}

				// se afiseaza direct pretul si nu procentul
				tglProc.setChecked(false);
				tglProc.performClick();

				if (UtilsUser.isASDL() || UtilsUser.isOIVPD()) {

					String rawIstoricPret = getPretIstoric(tokenPret[20]);

					double istoricPretAsdl = Double.parseDouble(rawIstoricPret.split("#")[0]);
					double valPret = listPrice / globalCantArt * valMultiplu;
					String umIstoric = rawIstoricPret.split("#")[1];
					String umVanzASDL = umIstoric;

					if (spinnerUnitMas.getVisibility() == View.VISIBLE) {
						HashMap<String, String> unitMasVanz = (HashMap<String, String>) spinnerUnitMas.getSelectedItem();
						umVanzASDL = unitMasVanz.get("rowText");
					}

					if (istoricPretAsdl > 0 && umIstoric.equals(umVanzASDL)) {
						valPret = istoricPretAsdl;
					} else {
						valPret = valPret - valPret * (discMaxSD / 100);
					}

					textProcRed.setText(nf2.format(valPret));

					discountASDL = Double.parseDouble(txtPretArt.getText().toString());
				}

				if (noDiscount(tokenPret[3]) || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM()
						|| UtilsUser.isCGED()) {
					txtPretArt.setEnabled(false);
					textProcRed.setFocusable(false);
					tglProc.setEnabled(false);
					codPromo = "1";

					if (Double.parseDouble(tokenPret[5]) != 0) {

						artPromoText = "";
						textPromo.setVisibility(View.VISIBLE);
						textPromo.setText("Articol cu promotie!");

						double pret1 = (Double.parseDouble(tokenPret[1]) / Double.parseDouble(tokenPret[0])) * valMultiplu;
						double pret2 = (Double.parseDouble(tokenPret[6]) / Double.parseDouble(tokenPret[5])) * valMultiplu;

						artPromoText = "Din cantitatea comandata " + tokenPret[0] + " " + tokenPret[2] + " au pretul de " + nf2.format(pret1)
								+ " RON/" + tokenPret[2] + " si " + tokenPret[5] + " " + tokenPret[7] + " au pretul de " + nf2.format(pret2)
								+ " RON/" + tokenPret[7] + ".";
					}

				} else {

					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.showSoftInput(textProcRed, InputMethodManager.SHOW_IMPLICIT);

					// verificare articole promotii
					if (Double.parseDouble(tokenPret[5]) != 0) {
						artPromoText = "";
						textPromo.setVisibility(View.VISIBLE);

						// articolul din promotie are alt pret
						if (Double.parseDouble(tokenPret[6]) != 0) {

						} else // articolul din promotie este gratuit
						{
							codPromo = "2";

							// verificare cantitati articole gratuite
							// cant. art promotie se adauga la cant. ceruta
							if (Double.parseDouble(textCant.getText().toString().trim()) == Double.parseDouble(tokenPret[0])) {

								// verificare cod articol promotie
								// art. promo = art. din comanda
								if (codArticol.equals(tokenPret[4])) {
									artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + numeArticol + " gratuit. ";
								} else// art. promo diferit de art. din cmd.
								{
									artPromoText = tokenPret[5] + " " + tokenPret[7] + " x " + tokenPret[4] + " gratuit. ";

								}

							} else // cant art. promotie se scade din cant.
									// ceruta
							{

								artPromoText = "Din cantitatea comandata " + tokenPret[5] + " " + tokenPret[7] + " sunt gratis.";

							}

							textPromo.setText("Articol cu promotie");

						}

					}

				}

				// **la preturi zero se blocheaza modificarea
				if (Double.parseDouble(tokenPret[1].toString()) == 0) {
					txtPretArt.setEnabled(false);
				}

				if (!codPromo.equals("1")) {
					textProcRed.requestFocus();
				}

			} else {

				Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

			}

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void afisPretUmAlternativa() {

		if (valoareUmrez / valoareUmren != 1) {

			double pretUmAlt;

			if (pretMod) {

				if (textProcRed.getText().toString().trim().isEmpty())
					return;

				pretUmAlt = Double.parseDouble(textProcRed.getText().toString()) * valoareUmrez / valoareUmren;
			} else {

				if (txtPretArt.getText().toString().trim().isEmpty())
					return;

				pretUmAlt = Double.parseDouble(txtPretArt.getText().toString()) * valoareUmrez / valoareUmren;
			}

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			((TextView) findViewById(R.id.txtPretUmAlt)).setText("1 " + selectedUnitMas + " = " + nf.format(pretUmAlt) + " RON");
		} else
			((TextView) findViewById(R.id.txtPretUmAlt)).setText("");

	}
	
	private String getPretIstoric(String infoIstoric) {

		String pretIstoric = "0";
		String umIstoric = "-1";

		DecimalFormat df = new DecimalFormat("#0.00");

		if (infoIstoric.contains(":")) {
			String[] arrayIstoric = infoIstoric.split(":");

			String[] arrayPret = arrayIstoric[0].split("@");

			pretIstoric = df.format(Double.valueOf(arrayPret[0]));
			umIstoric = arrayPret[2].split(" ")[1].trim();
		}

		return pretIstoric + "#" + umIstoric;

	}

	private void afisIstoricPret(String infoIstoric) {
		LinearLayout layoutIstoric1 = (LinearLayout) findViewById(R.id.layoutIstoricPret1);
		LinearLayout layoutIstoric2 = (LinearLayout) findViewById(R.id.layoutIstoricPret2);
		LinearLayout layoutIstoric3 = (LinearLayout) findViewById(R.id.layoutIstoricPret3);

		layoutIstoric1.setVisibility(View.GONE);
		layoutIstoric2.setVisibility(View.GONE);
		layoutIstoric3.setVisibility(View.GONE);

		DecimalFormat df = new DecimalFormat("#0.00");

		if (infoIstoric.contains(":")) {
			String[] arrayIstoric = infoIstoric.split(":");

			if (arrayIstoric.length > 0 && arrayIstoric[0].contains("@")) {

				layoutIstoric1.setVisibility(View.VISIBLE);

				String[] arrayPret = arrayIstoric[0].split("@");

				TextView textIstoric1 = (TextView) findViewById(R.id.txtIstoricPret1);
				textIstoric1.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1]
						+ " " + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

			}

			if (arrayIstoric.length > 1 && arrayIstoric[1].contains("@")) {

				layoutIstoric2.setVisibility(View.VISIBLE);

				String[] arrayPret = arrayIstoric[1].split("@");

				TextView textIstoric2 = (TextView) findViewById(R.id.txtIstoricPret2);
				textIstoric2.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1]
						+ " " + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

			}

			if (arrayIstoric.length > 2 && arrayIstoric[2].contains("@")) {

				layoutIstoric3.setVisibility(View.VISIBLE);

				String[] arrayPret = arrayIstoric[2].split("@");

				TextView textIstoric3 = (TextView) findViewById(R.id.txtIstoricPret3);
				textIstoric3.setText(df.format(Double.valueOf(arrayPret[0])) + UtilsFormatting.addSpace(arrayPret[0].trim(), 6) + " /" + arrayPret[1]
						+ " " + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2));

			}

		}

	}

	private boolean noDiscount(String artPromo) {

		if (artPromo.equalsIgnoreCase("X"))
			return true;
		else if ((CreareComanda.canalDistrib.equals("20") || globalCodDepartSelectetItem.equals("11"))
				&& !(globalDepozSel.equals("MAV1") && articolDBSelected.getDepart().equals("11") && !articolDBSelected.getDepartAprob().equals("00")))
			return true;

		return false;

	}

	public void showPromoWindow(String promoString) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(promoString).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).setTitle("Promotie!").setIcon(R.drawable.promotie96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

		articolDBSelected = articol;

		numeArticol = articol.getNume();
		codArticol = articol.getCod();
		tipArticol = articol.getTipAB();

		if (!tipArticol.trim().equals(""))
			numeArticol += " (" + tipArticol + ")";
		else
			tipArticol = " ";

		globalCodDepartSelectetItem = articol.getDepart();

		textNumeArticol.setText(numeArticol);
		textCodArticol.setText(codArticol);

		textUM.setText("");
		textStoc.setText("");
		textCant.setText("");
		
		valoareUmrez = 1;
		valoareUmren = 1;

		String umVanz = articol.getUmVanz();
		listUmVanz.clear();
		spinnerUnitMas.setVisibility(View.GONE);
		HashMap<String, String> tempUmVanz;
		tempUmVanz = new HashMap<String, String>();
		tempUmVanz.put("rowText", umVanz);

		listUmVanz.add(tempUmVanz);
		spinnerUnitMas.setAdapter(adapterUmVanz);

		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol.setVisibility(View.INVISIBLE);
		saveArtBtn.setVisibility(View.GONE);

		redBtnTable.setVisibility(View.GONE);

		try {
			String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

			if (tokenDep[0].trim().length() == 2)
				globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
			else
				globalDepozSel = tokenDep[0].trim();

			if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE) {
				performListArtStocCustodie();
			} else {
				performListArtStoc();
			}

		} catch (Exception ex) {
			Log.e("Error", ex.toString());

		}

	}

	private void performListArtStocCustodie() {
		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codArticol", codArticol);
		params.put("codClient", CreareComanda.codClientVar);
		params.put("filiala", EnumFiliale.getCodFiliala(spinnerFilialeCustodie.getSelectedItem().toString()));

		opArticol.getStocCustodie(params);
	}

	private void performListArtStoc() {
		try {

			if (this.getListView().getAdapter().getCount() == 0)
				return;

			HashMap<String, String> params = new HashMap<String, String>();

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			String varLocalUnitLog = "";

			if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.COMANDA_LIVRARE)
			{
				if (globalDepozSel.equals("MAV1"))
					varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP().substring(0, 2) + "2" + DateLivrare.getInstance().getCodFilialaCLP().substring(3, 4);
				else
					varLocalUnitLog = DateLivrare.getInstance().getCodFilialaCLP();
			}
			else {
				if (globalDepozSel.equals("MAV1") || globalDepozSel.equals("DSCM")) {
					if (CreareComanda.filialaAlternativa.equals("BV90"))
						varLocalUnitLog = "BV92";
					else
						varLocalUnitLog = CreareComanda.filialaAlternativa.substring(0, 2) + "2" + CreareComanda.filialaAlternativa.substring(3, 4);
				} else {
					varLocalUnitLog = CreareComanda.filialaAlternativa;
				}
			}

			params.put("codArt", codArticol);
			params.put("filiala", varLocalUnitLog);

			params.put("depozit", globalDepozSel);
			params.put("depart", UserInfo.getInstance().getCodDepart());
			opArticol.getStocDepozit(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private String addSpace(int nrCars) {
		String retVal = "";

		for (int i = 0; i < nrCars; i++)
			retVal += " ";

		return retVal;
	}

	private void loadFactorConversie(String result) {
		String[] convResult = result.split("#");

		valoareUmrez = Integer.parseInt(convResult[0]);
		valoareUmren = Integer.parseInt(convResult[1]);

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {

		switch (methodName) {

		case GET_STOC_DEPOZIT:
			listArtStoc((String) result);
			break;

		case GET_PRET:
			listArtPret((String) result);
			break;


		case GET_ARTICOLE_STATISTIC:
			((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);
			listArticoleStatistic = opArticol.deserializeArticoleVanzare((String) result);
			populateListViewArticol(listArticoleStatistic);
			break;
		case GET_ARTICOLE_CUSTODIE:
			((TextView) findViewById(R.id.textAfisStatistic)).setVisibility(View.VISIBLE);

			TextView labelCustodie = ((TextView) findViewById(R.id.textAfisStatistic));
			labelCustodie.setText("Articole custodie");
			labelCustodie.setVisibility(View.VISIBLE);

			listArticoleCustodie = opArticol.deserializeArticoleVanzare((String) result);
			populateListViewArticol(listArticoleCustodie);
			break;
		case GET_ARTICOLE_DISTRIBUTIE:
		case GET_ARTICOLE_FURNIZOR:
			populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
			break;
		case GET_STOC_CUSTODIE:
			listArtStocCustodie((String) result);
			break;
		case GET_FACTOR_CONVERSIE:
			loadFactorConversie((String) result);
			break;
		case GET_ARTICOLE_CANT:
			showArticoleCantDialog((String) result);
			break;
		default:
			break;

		}

	}
	
	
	@Override
	public void articolCantSelected(ArticolCant articolCant) {

		List<ArticolDB> listArticole = new ArrayList<ArticolDB>();
		articolCant.setCod(articolCant.getCod().replaceFirst("^0*", ""));
		listArticole.add(articolCant);

		CreareComanda.filialaAlternativa = articolCant.getUlStoc();

		if (articolCant.getDepozit().equals("92V1"))
			spinnerDepoz.setSelection(adapterSpinnerDepozite.getPosition("92V1"));
		else
			spinnerDepoz.setSelection(0);

		txtNumeArticol.setText("");
		resultLayout.setVisibility(View.INVISIBLE);
		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, listArticole);
		setListAdapter(adapterArticole);

		this.getListView().performItemClick(this.getListView().getAdapter().getView(0, null, null), 0, this.getListView().getItemIdAtPosition(0));

	}

	@Override
	public void articolCantClosed() {
		listArticoleCant.clear();
		
	}

}
