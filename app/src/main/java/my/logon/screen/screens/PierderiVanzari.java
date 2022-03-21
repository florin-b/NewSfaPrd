package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.PierdereDepartAdapter;
import my.logon.screen.adapters.PierdereNivel1Adapter;
import my.logon.screen.adapters.PierdereTipClientAdapter;
import my.logon.screen.adapters.PierdereTotalAdapter;
import my.logon.screen.adapters.PierdereVanzariAdapter;
import my.logon.screen.beans.PierdereDepart;
import my.logon.screen.beans.PierdereNivel1;
import my.logon.screen.beans.PierdereTipClient;
import my.logon.screen.beans.PierdereTotal;
import my.logon.screen.beans.PierdereVanz;
import my.logon.screen.beans.PierderiVanzariAV;
import my.logon.screen.beans.TotalPierderiClient;
import my.logon.screen.beans.TotalPierderiVanzariDepart;
import my.logon.screen.enums.EnumOperatiiPierdereVanz;
import my.logon.screen.filters.Nivel1PierderiFilter;
import my.logon.screen.filters.TipClientPierderiFilter;
import my.logon.screen.listeners.OperatiiClientPierderiListener;
import my.logon.screen.listeners.OperatiiPierdVanzListener;
import my.logon.screen.listeners.OperatiiPierderiListener;
import my.logon.screen.listeners.PierdDepartListener;
import my.logon.screen.listeners.PierdTotalListener;
import my.logon.screen.model.OperatiiPierdereVanz;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsUser;


public class PierderiVanzari extends Activity implements OperatiiPierdVanzListener, OperatiiPierderiListener, OperatiiClientPierderiListener,
		PierdDepartListener, PierdTotalListener, OnClickListener {

	private ActionBar actionBar;
	private NumberFormat nf;
	private OperatiiPierdereVanz opVanzari;
	private ListView listPierdereGen;
	private PierderiVanzariAV pierderiVanzariAV;
	private String ulDV;

	private TextView pierderiNumeClient, pierderiLC, pierderiLC1, pierderiLC2;
	private boolean sortAscNumeClient = true, sortAscLC = true, sortAscLC1 = true, sortAscLC2 = true;
	private ArrayList<PierdereTipClient> lPierderiTipClient;
	private ListView listTipClient;

	private TextView textNumeNivel1, textVenitLC, textVenitLC1, textVenitLC2;
	private boolean sortAscNumeNivel1 = true, sortAscVenitLC = true, sortAscVenitLC1 = true, sortAscVenitLC2 = true;
	private ListView listNivel1;
	private ArrayList<PierdereNivel1> lPierderiNivel1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.pierderi_vanzari);

		actionBar = getActionBar();
		actionBar.setTitle("Pierderi vanzari");
		actionBar.setDisplayHomeAsUpEnabled(true);

		listPierdereGen = (ListView) findViewById(R.id.listPierdereGen);

		nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setMaximumFractionDigits(2);

		listTipClient = (ListView) findViewById(R.id.listTipClient);
		listNivel1 = (ListView) findViewById(R.id.listNivel1);

		opVanzari = new OperatiiPierdereVanz(PierderiVanzari.this);
		opVanzari.setOperatiiPierdListener(PierderiVanzari.this);

		pierderiNumeClient = (TextView) findViewById(R.id.pierderiClient);
		pierderiNumeClient.setOnClickListener(this);

		pierderiLC = (TextView) findViewById(R.id.pierderiLC);
		pierderiLC.setOnClickListener(this);

		pierderiLC1 = (TextView) findViewById(R.id.pierderiLC1);
		pierderiLC1.setOnClickListener(this);

		pierderiLC2 = (TextView) findViewById(R.id.pierderiLC2);
		pierderiLC2.setOnClickListener(this);

		textNumeNivel1 = (TextView) findViewById(R.id.textNumeNivel1);
		textNumeNivel1.setOnClickListener(this);

		textVenitLC = (TextView) findViewById(R.id.textVenitLC);
		textVenitLC.setOnClickListener(this);

		textVenitLC1 = (TextView) findViewById(R.id.textVenitLC1);
		textVenitLC1.setOnClickListener(this);

		textVenitLC2 = (TextView) findViewById(R.id.textVenitLC2);
		textVenitLC2.setOnClickListener(this);

		if (UserInfo.getInstance().getTipUser().equals("AV"))
			getPierderiVanzariData(UserInfo.getInstance().getCod(), UserInfo.getInstance().getUnitLog(), UserInfo.getInstance().getCodDepart());
		else if (UserInfo.getInstance().getTipUser().equals("KA"))
			getPierderiVanzariData(UserInfo.getInstance().getCod(), UserInfo.getInstance().getUnitLog(), "10");
		else if (UserInfo.getInstance().getTipUser().equals("SD"))
			getPierderiVanzariDepart(UserInfo.getInstance().getUnitLog(), UserInfo.getInstance().getCodDepart());
		else if (UtilsUser.isUserSK() || UtilsUser.isUserSDKA())
			getPierderiVanzariDepart(UserInfo.getInstance().getUnitLog(), "10");
		else if (UtilsUser.isDV())
			getPierderiVanzariTotal();

	}

	private void getPierderiVanzariData(String codAgent, String ul, String codDepart) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		params.put("ul", ul);
		params.put("codDepart", codDepart);
		opVanzari.getPierdereVanzAg(params);
	}

	private void getPierderiVanzariDepart(String unitLog, String depart) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("ul", unitLog);
		params.put("codDepart", depart);
		opVanzari.getPierdereVanzDep(params);
	}

	private void getPierderiVanzariTotal() {
		String localCodDepart = UserInfo.getInstance().getCodDepart();

		if (localCodDepart.equals("00"))
			localCodDepart = UserInfo.getInstance().getInitDivizie();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codDepart", localCodDepart);
		opVanzari.getPierdereVanzTotal(params);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			returnToMainMenu();
			return true;

		}
		return false;
	}

	private TotalPierderiVanzariDepart getTotalClientiAv(List<PierdereVanz> listPierderiAv) {

		TotalPierderiVanzariDepart totalDepart = new TotalPierderiVanzariDepart();

		for (PierdereVanz p : listPierderiAv) {

			totalDepart.setTotalClienti(totalDepart.getTotalClienti() + p.getNrClientiIstoric());
			totalDepart.setTotalCurent(totalDepart.getTotalCurent() + p.getNrClientiCurent());
			totalDepart.setTotalRest(totalDepart.getTotalRest() + p.getNrClientiRest());

		}

		return totalDepart;
	}

	private void afisPierderiVanzari(String result) {

		((LinearLayout) findViewById(R.id.headerSalAgenti)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.headerTotalAgenti)).setVisibility(View.VISIBLE);
		pierderiVanzariAV = opVanzari.deserializePierdereVanz(result);

		PierdereVanzariAdapter adapter = new PierdereVanzariAdapter(pierderiVanzariAV.getPierderiHeader(), this);
		adapter.setPierderiVanzariListener(this);
		listPierdereGen.setVisibility(View.VISIBLE);
		listPierdereGen.setAdapter(adapter);

		listPierdereGen.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		TotalPierderiVanzariDepart totalDep = getTotalClientiAv(pierderiVanzariAV.getPierderiHeader());

		((TextView) findViewById(R.id.headerTotalClientiAv)).setText(totalDep.getTotalClienti() + "");
		((TextView) findViewById(R.id.headerTotalCurentAv)).setText(totalDep.getTotalCurent() + "");
		((TextView) findViewById(R.id.headerTotalRestAv)).setText(totalDep.getTotalRest() + "");

	}

	private TotalPierderiVanzariDepart getTotalClientiDepart(List<PierdereDepart> listPierderiDepart) {

		TotalPierderiVanzariDepart pDepart = new TotalPierderiVanzariDepart();

		for (PierdereDepart p : listPierderiDepart) {
			pDepart.setTotalClienti(pDepart.getTotalClienti() + p.getNrClientiIstoric());
			pDepart.setTotalCurent(pDepart.getTotalCurent() + p.getNrClientiCurent());
			pDepart.setTotalRest(pDepart.getTotalRest() + p.getNrClientiRest());

		}

		return pDepart;
	}

	private void afisPierderiVanzariDepart(String result) {

		((LinearLayout) findViewById(R.id.layoutPierdereDepart)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.layoutDateDepart)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.layoutTotalPierdereDepart)).setVisibility(View.VISIBLE);

		ListView listViewDepart = (ListView) findViewById(R.id.listPierdereDepart);

		List<PierdereDepart> listPierderiDepart = opVanzari.deserializePierdereDepart(result);

		PierdereDepartAdapter departAdapter = new PierdereDepartAdapter(listPierderiDepart, this);
		departAdapter.setPierderiDepartListener(this);
		listViewDepart.setAdapter(departAdapter);

		listViewDepart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		TotalPierderiVanzariDepart totalDep = getTotalClientiDepart(listPierderiDepart);

		((TextView) findViewById(R.id.textTotalClienti)).setText(totalDep.getTotalClienti() + "");
		((TextView) findViewById(R.id.textTotalCurent)).setText(totalDep.getTotalCurent() + "");
		((TextView) findViewById(R.id.textTotalRest)).setText(totalDep.getTotalRest() + "");

	}

	private TotalPierderiVanzariDepart getTotalClientiUL(List<PierdereTotal> listPierderiTotal) {

		TotalPierderiVanzariDepart pDepart = new TotalPierderiVanzariDepart();

		for (PierdereTotal p : listPierderiTotal) {
			pDepart.setTotalClienti(pDepart.getTotalClienti() + p.getNrClientiIstoric());
			pDepart.setTotalCurent(pDepart.getTotalCurent() + p.getNrClientiCurent());
			pDepart.setTotalRest(pDepart.getTotalRest() + p.getNrClientiRest());

		}

		return pDepart;
	}

	public void afisPierderiVanzariTotal(String result) {

		((LinearLayout) findViewById(R.id.layoutDateTotal)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.layoutPierdereTotal)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.layoutTotalUL)).setVisibility(View.VISIBLE);
		ListView listViewTotal = (ListView) findViewById(R.id.listPierdereTotal);

		List<PierdereTotal> listPierderiTotal = opVanzari.deserializePierdereTotal(result);
		PierdereTotalAdapter totalAdapter = new PierdereTotalAdapter(listPierderiTotal, this);
		totalAdapter.setPierderiTotalListener(this);
		listViewTotal.setAdapter(totalAdapter);

		listViewTotal.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		TotalPierderiVanzariDepart totalDep = getTotalClientiUL(listPierderiTotal);

		((TextView) findViewById(R.id.totalULClienti)).setText(totalDep.getTotalClienti() + "");
		((TextView) findViewById(R.id.totalULCurent)).setText(totalDep.getTotalCurent() + "");
		((TextView) findViewById(R.id.totalULRest)).setText(totalDep.getTotalRest() + "");

	}

	@Override
	public void onBackPressed() {
		returnToMainMenu();
		return;
	}

	private void returnToMainMenu() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	@Override
	public void operationPierduteComplete(EnumOperatiiPierdereVanz methodName, Object result) {

		switch (methodName) {
		case GET_VANZ_PIERD_AG:
			afisPierderiVanzari((String) result);
			break;
		case GET_VANZ_PIERD_DEP:
			afisPierderiVanzariDepart((String) result);
			break;
		case GET_VANZ_PIERD_TOTAL:
			afisPierderiVanzariTotal((String) result);
		default:
			break;
		}

	}

	private TotalPierderiClient getPierderiClient(List<PierdereTipClient> listPierderi) {
		TotalPierderiClient totalPierderi = new TotalPierderiClient();

		for (PierdereTipClient p : listPierderi) {
			totalPierderi.setVenitLC(totalPierderi.getVenitLC() + p.venitLC);
			totalPierderi.setVenitLC1(totalPierderi.getVenitLC1() + p.venitLC1);
			totalPierderi.setVenitLC2(totalPierderi.getVenitLC2() + p.venitLC2);
		}

		return totalPierderi;

	}

	@Override
	public void tipClientSelected(String codTipClient, String numeTipClient) {

		((TextView) findViewById(R.id.textTipClientSelectat)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.headerPierdTipClient)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.totalPierdTipClient)).setVisibility(View.VISIBLE);

		pierderiNumeClient.setTypeface(pierderiNumeClient.getTypeface(), Typeface.BOLD);

		((TextView) findViewById(R.id.pierderiLC)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		((TextView) findViewById(R.id.pierderiLC1)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		((TextView) findViewById(R.id.pierderiLC2)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		((TextView) findViewById(R.id.textNumeClientSelectat)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.layoutPierdNivel1)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.totalPierdNivel1)).setVisibility(View.GONE);
		((ListView) findViewById(R.id.listNivel1)).setVisibility(View.GONE);

		((TextView) findViewById(R.id.textTipClientSelectat)).setText("Detalii " + numeTipClient.toLowerCase());

		lPierderiTipClient = new TipClientPierderiFilter().getPierderiTipClient(pierderiVanzariAV.getListPierderiTipCl(), codTipClient);
		PierdereTipClientAdapter tipAdapter = new PierdereTipClientAdapter(lPierderiTipClient, this);
		tipAdapter.setPierderiVanzariListener(this);
		listTipClient.setAdapter(tipAdapter);

		listTipClient.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		listTipClient.setVisibility(View.VISIBLE);

		TotalPierderiClient totalPierderi = getPierderiClient(lPierderiTipClient);

		((TextView) findViewById(R.id.totalTipClientLC)).setText(nf.format(totalPierderi.getVenitLC()));
		((TextView) findViewById(R.id.totalTipClientLC1)).setText(nf.format(totalPierderi.getVenitLC1()));
		((TextView) findViewById(R.id.totalTipClientLC2)).setText(nf.format(totalPierderi.getVenitLC2()));

	}

	private void sortNumeClient() {

		pierderiNumeClient.setTypeface(pierderiNumeClient.getTypeface(), Typeface.BOLD);
		pierderiLC.setTypeface(null, Typeface.NORMAL);
		pierderiLC1.setTypeface(null, Typeface.NORMAL);
		pierderiLC2.setTypeface(null, Typeface.NORMAL);

		pierderiLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscNumeClient) {
			pierderiNumeClient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			pierderiNumeClient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByNumeClient(lPierderiTipClient, !sortAscNumeClient);

		loadPierderiTipClient(lPierderiTipClient);

		sortAscNumeClient = !sortAscNumeClient;

	}

	private void sortLC() {

		pierderiNumeClient.setTypeface(null, Typeface.NORMAL);
		pierderiLC.setTypeface(pierderiLC.getTypeface(), Typeface.BOLD);
		pierderiLC1.setTypeface(null, Typeface.NORMAL);
		pierderiLC2.setTypeface(null, Typeface.NORMAL);

		pierderiNumeClient.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscLC) {
			pierderiLC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			pierderiLC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByPierderiLC(lPierderiTipClient, !sortAscLC);

		loadPierderiTipClient(lPierderiTipClient);

		sortAscLC = !sortAscLC;

	}

	private void sortLC1() {

		pierderiNumeClient.setTypeface(null, Typeface.NORMAL);
		pierderiLC.setTypeface(null, Typeface.NORMAL);
		pierderiLC1.setTypeface(pierderiLC1.getTypeface(), Typeface.BOLD);
		pierderiLC2.setTypeface(null, Typeface.NORMAL);

		pierderiNumeClient.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscLC1) {
			pierderiLC1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			pierderiLC1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByPierderiLC1(lPierderiTipClient, !sortAscLC1);

		loadPierderiTipClient(lPierderiTipClient);

		sortAscLC1 = !sortAscLC1;

	}

	private void sortLC2() {

		pierderiNumeClient.setTypeface(null, Typeface.NORMAL);
		pierderiLC.setTypeface(null, Typeface.NORMAL);
		pierderiLC1.setTypeface(null, Typeface.NORMAL);
		pierderiLC2.setTypeface(pierderiLC2.getTypeface(), Typeface.BOLD);

		pierderiNumeClient.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		pierderiLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscLC2) {
			pierderiLC2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			pierderiLC2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByPierderiLC2(lPierderiTipClient, !sortAscLC2);

		loadPierderiTipClient(lPierderiTipClient);

		sortAscLC2 = !sortAscLC2;

	}

	private void sortNumeNivel1() {

		textNumeNivel1.setTypeface(pierderiNumeClient.getTypeface(), Typeface.BOLD);
		textVenitLC.setTypeface(null, Typeface.NORMAL);
		textVenitLC1.setTypeface(null, Typeface.NORMAL);
		textVenitLC2.setTypeface(null, Typeface.NORMAL);

		textVenitLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscNumeNivel1) {
			textNumeNivel1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			textNumeNivel1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByNumeNivel1(lPierderiNivel1, !sortAscNumeNivel1);
		loadPierderiNumeNivel1(lPierderiNivel1);

		sortAscNumeNivel1 = !sortAscNumeNivel1;

	}

	private void sortVenitLC() {

		textNumeNivel1.setTypeface(null, Typeface.NORMAL);
		textVenitLC.setTypeface(textVenitLC.getTypeface(), Typeface.BOLD);
		textVenitLC1.setTypeface(null, Typeface.NORMAL);
		textVenitLC2.setTypeface(null, Typeface.NORMAL);

		textNumeNivel1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscVenitLC) {
			textVenitLC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			textVenitLC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByVenitLC(lPierderiNivel1, !sortAscVenitLC);
		loadPierderiNumeNivel1(lPierderiNivel1);

		sortAscVenitLC = !sortAscVenitLC;

	}

	private void sortVenitLC1() {

		textNumeNivel1.setTypeface(null, Typeface.NORMAL);
		textVenitLC.setTypeface(null, Typeface.NORMAL);
		textVenitLC1.setTypeface(textVenitLC.getTypeface(), Typeface.BOLD);
		textVenitLC2.setTypeface(null, Typeface.NORMAL);

		textNumeNivel1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscVenitLC1) {
			textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByVenitLC1(lPierderiNivel1, !sortAscVenitLC1);
		loadPierderiNumeNivel1(lPierderiNivel1);

		sortAscVenitLC1 = !sortAscVenitLC1;

	}

	private void sortVenitLC2() {

		textNumeNivel1.setTypeface(null, Typeface.NORMAL);
		textVenitLC.setTypeface(null, Typeface.NORMAL);
		textVenitLC1.setTypeface(null, Typeface.NORMAL);
		textVenitLC2.setTypeface(textVenitLC2.getTypeface(), Typeface.BOLD);

		textNumeNivel1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (sortAscVenitLC2) {
			textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_btn, 0, 0, 0);
		} else {
			textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_btn, 0, 0, 0);
		}

		opVanzari.sortByVenitLC2(lPierderiNivel1, !sortAscVenitLC2);
		loadPierderiNumeNivel1(lPierderiNivel1);

		sortAscVenitLC2 = !sortAscVenitLC2;

	}

	private void loadPierderiTipClient(ArrayList<PierdereTipClient> lPierderiTipClient) {
		PierdereTipClientAdapter tipAdapter = new PierdereTipClientAdapter(lPierderiTipClient, this);
		tipAdapter.setPierderiVanzariListener(this);
		listTipClient.setAdapter(tipAdapter);
	}

	private void loadPierderiNumeNivel1(ArrayList<PierdereNivel1> lPierderiNivel1) {
		PierdereNivel1Adapter nivel1Adapter = new PierdereNivel1Adapter(lPierderiNivel1, this);
		listNivel1.setAdapter(nivel1Adapter);
	}

	private TotalPierderiClient getPierderiNivel1(List<PierdereNivel1> listPierderi) {
		TotalPierderiClient totalPierderi = new TotalPierderiClient();

		for (PierdereNivel1 p : listPierderi) {
			totalPierderi.setVenitLC(totalPierderi.getVenitLC() + p.getVenitLC());
			totalPierderi.setVenitLC1(totalPierderi.getVenitLC1() + p.getVenitLC1());
			totalPierderi.setVenitLC2(totalPierderi.getVenitLC2() + p.getVenitLC2());
		}

		return totalPierderi;

	}

	@Override
	public void clientSelected(String numeClient) {
		((TextView) findViewById(R.id.textNumeClientSelectat)).setText("Detalii " + numeClient);
		((TextView) findViewById(R.id.textNumeClientSelectat)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.layoutPierdNivel1)).setVisibility(View.VISIBLE);
		((LinearLayout) findViewById(R.id.totalPierdNivel1)).setVisibility(View.VISIBLE);

		textNumeNivel1.setTypeface(pierderiNumeClient.getTypeface(), Typeface.BOLD);
		textVenitLC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		textVenitLC2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		listNivel1 = (ListView) findViewById(R.id.listNivel1);
		listNivel1.setVisibility(View.VISIBLE);
		lPierderiNivel1 = new Nivel1PierderiFilter().getPierderiNivel1(pierderiVanzariAV.getListPierderiNivel1(), numeClient);
		PierdereNivel1Adapter nivel1Adapter = new PierdereNivel1Adapter(lPierderiNivel1, this);
		listNivel1.setAdapter(nivel1Adapter);

		sortAscNumeNivel1 = false;
		sortNumeNivel1();

		listNivel1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		TotalPierderiClient totalPierderi = getPierderiNivel1(lPierderiNivel1);

		((TextView) findViewById(R.id.totalVenitLC)).setText(nf.format(totalPierderi.getVenitLC()));
		((TextView) findViewById(R.id.totalVenitLC1)).setText(nf.format(totalPierderi.getVenitLC1()));
		((TextView) findViewById(R.id.totalVenitLC2)).setText(nf.format(totalPierderi.getVenitLC2()));

	}

	@Override
	public void agentDepartSelected(String codAgent, String numeAgent) {
		((TextView) findViewById(R.id.textAgentSelectat)).setText(numeAgent);
		((TextView) findViewById(R.id.textAgentSelectat)).setVisibility(View.VISIBLE);

		((TextView) findViewById(R.id.textNumeClientSelectat)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.layoutPierdNivel1)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.totalPierdNivel1)).setVisibility(View.GONE);
		((ListView) findViewById(R.id.listNivel1)).setVisibility(View.GONE);

		((TextView) findViewById(R.id.textTipClientSelectat)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.headerPierdTipClient)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.totalPierdTipClient)).setVisibility(View.GONE);
		((ListView) findViewById(R.id.listTipClient)).setVisibility(View.GONE);

		if (UtilsUser.isDV())
			getPierderiVanzariData(codAgent, ulDV, UserInfo.getInstance().getCodDepart());
		else if (UtilsUser.isUserSK() || UtilsUser.isUserSDKA())
			getPierderiVanzariData(codAgent, UserInfo.getInstance().getUnitLog(), "10");
		else
			getPierderiVanzariData(codAgent, UserInfo.getInstance().getUnitLog(), UserInfo.getInstance().getCodDepart());

	}

	@Override
	public void filialaSelected(String unitLog) {
		ulDV = unitLog;

		((TextView) findViewById(R.id.textUlSelect)).setText(unitLog);
		((TextView) findViewById(R.id.textUlSelect)).setVisibility(View.VISIBLE);

		((LinearLayout) findViewById(R.id.headerSalAgenti)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.headerTotalAgenti)).setVisibility(View.GONE);

		listPierdereGen.setVisibility(View.GONE);
		((TextView) findViewById(R.id.textAgentSelectat)).setVisibility(View.GONE);

		((TextView) findViewById(R.id.textTipClientSelectat)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.headerPierdTipClient)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.totalPierdTipClient)).setVisibility(View.GONE);
		((ListView) findViewById(R.id.listTipClient)).setVisibility(View.GONE);

		((TextView) findViewById(R.id.textNumeClientSelectat)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.layoutPierdNivel1)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.totalPierdNivel1)).setVisibility(View.GONE);
		((ListView) findViewById(R.id.listNivel1)).setVisibility(View.GONE);

		getPierderiVanzariDepart(unitLog, UserInfo.getInstance().getCodDepart());

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();

		if (id == R.id.pierderiClient)
			sortNumeClient();
		else if (id == R.id.pierderiLC)
			sortLC();
		else if (id == R.id.pierderiLC1)
			sortLC1();
		else if (id == R.id.pierderiLC2)
			sortLC2();
		else if (id == R.id.textNumeNivel1)
			sortNumeNivel1();
		else if (id == R.id.textVenitLC)
			sortVenitLC();
		else if (id == R.id.textVenitLC1)
			sortVenitLC1();
		else if (id == R.id.textVenitLC2)
			sortVenitLC2();
	}

}
