/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ClpDAOListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ClpDAO;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;

import my.logon.screen.R;
import my.logon.screen.patterns.ClpDepartComparator;
import my.logon.screen.utils.DepartamentAgent;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;
import my.logon.screen.adapters.CautareArticoleAdapter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.AntetComandaCLP;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.BeanGreutateArticol;
import my.logon.screen.beans.ComandaCreataCLP;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumClpDAO;
import my.logon.screen.enums.EnumDepartExtra;

public class CLPFragment2 extends Fragment implements AsyncTaskListener, ClpDAOListener, OperatiiArticolListener {

	Button articoleBtn, saveArtBtnClp, saveClpButton, slideButtonClp;
	ToggleButton tglButton, tglTipArtBtn;
	private EditText txtNumeArticol, textCantArt;

	String[] depozite = { "V1 - vanzare", "V2 - vanzare", "V3 - vanzare", "G1 - gratuite", "G2 - gratuite", "G3 - gratuite", "D1 - deteriorate",
			"D2 - deteriorate", "D3 - deteriorate", "DESC", "MAV1", "TAMP", "GAR1" };

	private static ArrayList<HashMap<String, String>> listArtSelClp = null, listUmVanz = null;

	public static SimpleAdapter adapterListArtClp;
	public SimpleAdapter adapterUmVanz;
	public ListView listViewArticole;
	private Spinner spinnerDepoz, spinnerUMClp;
	String codArticol = "", numeArticol = "", sintetic = "", selectedUnitMas = "", depArtSel = "";

	public static String globalDepozSel = "";
	public static String globalCodDepartSelectetItem = "";
	private TextView textNumeArticol, textCodArticol, labelStoc, textStoc, textUm, labelCantArt;
	NumberFormat nf2;
	SlidingDrawer slidingDrawerArt;
	static SlidingDrawer slidingDrawerSaveClp;
	LinearLayout layoutArtHeader, layoutArtDet;
	TableLayout layoutSaveClp;
	ListView listArtCmdClp;
	private HashMap<String, String> artMap = null;
	private int listViewSelPos = -1;
	private static ArticolComanda[] objArticol = new ArticolComanda[70];
	private ProgressBar mProgressClp;
	private Timer myTimerClp;
	private int progressVal = 0;
	private Handler clpHandler = new Handler();
	boolean cmdFasonate = true;
	ClpDAO operatiiClp;

	private BeanGreutateArticol greutateArticol;

	private static TextView textValoareGreutate, textTotalGreutate;
	private LinearLayout layoutGreutate, layoutTotalGreutate;
	private double factorConversie = 1;

	OperatiiArticol opArticol;
	private ComandaCreataCLP comandaCLP;
	private String selectedDepartamentAgent = UserInfo.getInstance().getCodDepart();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.clp_fragment_2, container, false);

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", getActivity());
		opArticol.setListener(this);

		initLocale();
		nf2 = NumberFormat.getInstance(new Locale("en_US"));

		this.tglButton = (ToggleButton) v.findViewById(R.id.togglebuttonClp);
		addListenerToggle();
		this.tglButton.setChecked(true);

		operatiiClp = new ClpDAO(getActivity());
		operatiiClp.setClpDAOListener(this);

		this.tglTipArtBtn = (ToggleButton) v.findViewById(R.id.tglTipArtClp);
		addListenerTglTipArtBtn();

		txtNumeArticol = (EditText) v.findViewById(R.id.txtNumeArtClp);

		this.articoleBtn = (Button) v.findViewById(R.id.articoleBtnClp);
		addListenerBtnArticole();

		this.listViewArticole = (ListView) v.findViewById(R.id.listArticole);

		textValoareGreutate = (TextView) v.findViewById(R.id.textValoareGreutate);

		textTotalGreutate = (TextView) v.findViewById(R.id.textTotalGreutate);
		layoutTotalGreutate = (LinearLayout) v.findViewById(R.id.layoutTotalGreutate);
		layoutTotalGreutate.setVisibility(View.INVISIBLE);

		layoutGreutate = (LinearLayout) v.findViewById(R.id.layoutGreutate);
		layoutGreutate.setVisibility(View.INVISIBLE);

		listViewArticole.setOnItemClickListener(new MyOnItemSelectedListener());

		spinnerDepoz = (Spinner) v.findViewById(R.id.spinnerDepozClp);
		ArrayAdapter<String> adapterSpinnerDepoz = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getDepozite());
		adapterSpinnerDepoz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDepoz.setAdapter(adapterSpinnerDepoz);
		spinnerDepoz.setOnItemSelectedListener(new OnSelectDepozit());

		textNumeArticol = (TextView) v.findViewById(R.id.textNumeArticolClp);
		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol = (TextView) v.findViewById(R.id.textCodArticolClp);
		textCodArticol.setVisibility(View.INVISIBLE);

		labelStoc = (TextView) v.findViewById(R.id.labelStocClp);
		labelStoc.setVisibility(View.INVISIBLE);

		textStoc = (TextView) v.findViewById(R.id.textStocClp);
		textStoc.setVisibility(View.INVISIBLE);

		textUm = (TextView) v.findViewById(R.id.textUmClp);
		textUm.setVisibility(View.INVISIBLE);

		labelCantArt = (TextView) v.findViewById(R.id.labelCantArtClp);
		labelCantArt.setVisibility(View.INVISIBLE);
		textCantArt = (EditText) v.findViewById(R.id.txtCantArtClp);
		textCantArt.setVisibility(View.INVISIBLE);

		layoutArtHeader = (LinearLayout) v.findViewById(R.id.layoutHeaderArtClp);
		layoutArtDet = (LinearLayout) v.findViewById(R.id.layoutDetArtClp);

		this.slidingDrawerArt = (SlidingDrawer) v.findViewById(R.id.articoleSlidingDrawer);
		addDrawerListener();

		listArtCmdClp = (ListView) v.findViewById(R.id.listArtCmdClp);

		listArtSelClp = new ArrayList<HashMap<String, String>>();
		adapterListArtClp = new SimpleAdapter(getActivity(), listArtSelClp, R.layout.articol_row_clp, new String[] { "nrCrt", "numeArt", "codArt",
				"cantArt", "depozit", "Umb", "sintetic", "greutate", "umgreutate", "depart" }, new int[] { R.id.textNrCrt, R.id.textNumeArt,
				R.id.textCodArt, R.id.textCantArt, R.id.textDepozit, R.id.textCantUmb, R.id.textSintetic, R.id.textGreutate, R.id.textUmGreutate,
				R.id.textDepart }

		);

		listArtCmdClp.setAdapter(adapterListArtClp);
		listArtCmdClp.setClickable(true);
		addListenerListArtSel();
		registerForContextMenu(listArtCmdClp);

		this.saveArtBtnClp = (Button) v.findViewById(R.id.saveArtBtnClp);
		this.saveArtBtnClp.setVisibility(View.INVISIBLE);
		addListenerBtnSaveArticole();

		layoutSaveClp = (TableLayout) v.findViewById(R.id.layoutSaveClp);
		layoutSaveClp.setVisibility(View.INVISIBLE);

		CLPFragment2.slidingDrawerSaveClp = (SlidingDrawer) v.findViewById(R.id.slidingDrawerClp);
		addDrawerClpListener();

		this.slideButtonClp = (Button) v.findViewById(R.id.slideButtonClp);

		this.saveClpButton = (Button) v.findViewById(R.id.saveClpBtn);
		addListenerSaveClpBtn();

		mProgressClp = (ProgressBar) v.findViewById(R.id.progress_bar_saveclp);
		mProgressClp.setVisibility(View.INVISIBLE);

		spinnerUMClp = (Spinner) v.findViewById(R.id.spinnerUMClp);

		listUmVanz = new ArrayList<HashMap<String, String>>();
		adapterUmVanz = new SimpleAdapter(getActivity(), listUmVanz, R.layout.simplerowlayout, new String[] { "rowText" },
				new int[] { R.id.textRowName });
		spinnerUMClp.setAdapter(adapterUmVanz);
		spinnerUMClp.setVisibility(View.GONE);
		spinnerUMClp.setOnItemSelectedListener(new OnSelectUnitMas());

		comandaCLP = new ComandaCreataCLP();

		return v;
	}

	public static final CLPFragment2 newInstance() {
		CLPFragment2 f = new CLPFragment2();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	private String[] getDepozite() {
		List<String> listDepozite = new ArrayList<String>(Arrays.asList(depozite));
		listDepozite.add(UserInfo.getInstance().getCodDepart() + "RZ");

		return listDepozite.toArray(new String[listDepozite.size()]);

	}

	private void addSpinnerDepartamente() {

		List<String> listDepart = DepartamentAgent.getDepartamenteAgentCLP(CLPFragment1.diviziiClient);

		if (!UserInfo.getInstance().getUnitLog().startsWith("BU"))
			listDepart.remove("Magazin");

		if (listDepart.size() == 1)
			return;

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listDepart);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View mCustomView = mInflater.inflate(R.layout.spinner_layout, null);
		final Spinner spinnerDepartament = (Spinner) mCustomView.findViewById(R.id.spinnerDep);

		spinnerDepartament.setOnItemSelectedListener(new OnItemSelectedListener() {
			@SuppressWarnings("unchecked")
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedDepartamentAgent = EnumDepartExtra.getCodDepart(spinnerDepartament.getSelectedItem().toString());
				listViewArticole.setAdapter(null);
				txtNumeArticol.setText("");
				saveArtBtnClp.setVisibility(View.INVISIBLE);

				if (selectedDepartamentAgent.equals("11"))
					spinnerDepoz.setSelection(((ArrayAdapter<String>) spinnerDepoz.getAdapter()).getPosition("DSCM"));
				else
					spinnerDepoz.setSelection(0);

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerDepartament.setAdapter(adapter);

		getActivity().getActionBar().setCustomView(mCustomView);
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);

	}

	private void removeSpinnerDepartamente() {
		getActivity().getActionBar().setCustomView(null);
		getActivity().getActionBar().setDisplayShowCustomEnabled(true);
	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {
					if (tglTipArtBtn.isChecked()) {
						txtNumeArticol.setHint("Introduceti cod sintetic");
					} else
						txtNumeArticol.setHint("Introduceti cod articol");
				} else {
					if (tglTipArtBtn.isChecked()) {
						txtNumeArticol.setHint("Introduceti nume sintetic");
					} else
						txtNumeArticol.setHint("Introduceti nume articol");
				}
			}
		});

	}

	public void addListenerTglTipArtBtn() {
		tglTipArtBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTipArtBtn.isChecked()) {
					if (!tglButton.isChecked()) {
						txtNumeArticol.setHint("Introduceti nume sintetic");
					} else
						txtNumeArticol.setHint("Introduceti cod sintetic");
				} else {
					if (!tglButton.isChecked()) {
						txtNumeArticol.setHint("Introduceti nume articol");
					} else
						txtNumeArticol.setHint("Introduceti cod articol");

				}
			}
		});

	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {

						performGetArticole();

					} else {
						Toast.makeText(getActivity(), "Introduceti nume articol!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerBtnSaveArticole() {
		saveArtBtnClp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					// validari
					if (numeArticol.equals("")) {
						Toast.makeText(getActivity(), "Selectati un articol!", Toast.LENGTH_LONG).show();
						return;
					}

					if (textCantArt.getText().toString().trim().length() == 0) {
						Toast.makeText(getActivity(), "Cantitate invalida!", Toast.LENGTH_LONG).show();
						return;
					}

					if (Double.valueOf(textCantArt.getText().toString().trim()) == 0) {
						Toast.makeText(getActivity(), "Cantitate invalida!", Toast.LENGTH_LONG).show();
						return;
					}

					if (Double.valueOf(textStoc.getText().toString().replace(",", "")) < Double.valueOf(textCantArt.getText().toString().trim())) {
						Toast.makeText(getActivity(), "Stoc insuficient!", Toast.LENGTH_LONG).show();
						return;
					}

					factorConversie = 1;

					if (!selectedUnitMas.equals(textUm.getText().toString().trim())) {
						performCheckCantUmAlt();
					} else {
						performSaveSelectedArt();
					}

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	private void performSaveSelectedArt() {

		try {

			if (codArticol.length() == 18)
				codArticol = codArticol.substring(10, 18);

			int nrArt = listArtSelClp.size(), ii = 0, selectedArtPos = -1;

			for (ii = 0; ii < nrArt; ii++) {
				artMap = (HashMap<String, String>) CLPFragment2.adapterListArtClp.getItem(ii);
				if (codArticol.equals(artMap.get("codArt"))) {
					selectedArtPos = ii;
					listArtSelClp.remove(ii);
					break;
				}

			}

			HashMap<String, String> temp;
			temp = new HashMap<String, String>(20, 0.75f);

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			double greutateCantitate = greutateArticol.getGreutate() * Double.valueOf(textCantArt.getText().toString().trim()) * factorConversie;

			Integer tokNrCrt = -1;

			if (selectedArtPos == -1) {
				selectedArtPos = listArtSelClp.size();
				tokNrCrt = listArtSelClp.size() + 1;
			} else {
				tokNrCrt = selectedArtPos + 1;
			}

			temp.put("nrCrt", String.valueOf(tokNrCrt) + ".");
			temp.put("numeArt", numeArticol);
			temp.put("codArt", codArticol);
			temp.put("cantArt", nf2.format(Double.valueOf(textCantArt.getText().toString().trim())));
			temp.put("Umb", selectedUnitMas);
			temp.put("depozit", globalDepozSel);
			temp.put("sintetic", sintetic);
			temp.put("greutate", nf2.format(greutateCantitate));
			temp.put("umgreutate", greutateArticol.getUnitMas().toString());
			temp.put("depart", depArtSel);

			listArtSelClp.add(selectedArtPos, temp);
			listArtCmdClp.setAdapter(adapterListArtClp);
			adapterListArtClp.notifyDataSetChanged();

			ScreenUtils.hideSoftKeyboard(getActivity(), txtNumeArticol);
			getTotalGreutate();

			clearVars();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private void performCheckCantUmAlt() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			params.put("codArt", codArticol);
			params.put("umAlt", selectedUnitMas);
			params.put("cantArt", textCantArt.getText().toString().trim());

			AsyncTaskListener contextListener = (AsyncTaskListener) CLPFragment2.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getCantUmAlt", params);
			call.getCallResultsFromFragment();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private void clearVars() {
		numeArticol = "";
		codArticol = "";
		sintetic = "";
		depArtSel = "";
		selectedUnitMas = "";

		textNumeArticol.setText("");
		textCodArticol.setText("");

		textUm.setText("");
		textStoc.setText("");
		labelStoc.setVisibility(View.INVISIBLE);

		textUm.setText("");
		textCantArt.setText("");

		textCantArt.setVisibility(View.INVISIBLE);
		labelCantArt.setVisibility(View.INVISIBLE);
		spinnerUMClp.setVisibility(View.GONE);
		layoutGreutate.setVisibility(View.INVISIBLE);

	}

	protected void performGetArticole() {

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

		String localDep = UserInfo.getInstance().getCodDepart();

		if (UtilsUser.isAgentOrSD()) {
			localDep = selectedDepartamentAgent;
		} else if (UtilsUser.isKA()) {
			localDep = "00";
		} else if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("44") || UserInfo.getInstance().getTipAcces().equals("39")) {
			localDep = "00";
		}

		if (localDep.length() > 0) {
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("searchString", numeArticol);
			params.put("tipArticol", tipArticol);
			params.put("tipCautare", tipCautare);
			params.put("departament", localDep);
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("codUser", UserInfo.getInstance().getCod());
			params.put("modulCautare", "CLP");

			opArticol.getArticoleDistributie(params);
		}

	}

	boolean isCV() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")
				|| UserInfo.getInstance().getTipUser().equals("SMR");
	}

	public void populateListViewArticol(List<ArticolDB> listArticole) {

		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(getActivity(), listArticole);
		listViewArticole.setAdapter(adapterArticole);
		ScreenUtils.hideSoftKeyboard(getActivity(), txtNumeArticol);

	}

	public class MyOnItemSelectedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			try {

				ArticolDB articol = (ArticolDB) parent.getAdapter().getItem(pos);

				numeArticol = articol.getNume();
				codArticol = articol.getCod();
				sintetic = articol.getSintetic();
				depArtSel = articol.getDepart();

				String umVanz = articol.getUmVanz10();

				globalCodDepartSelectetItem = articol.getDepart();

				String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

				if (tokenDep[0].trim().length() == 2) {
					globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
				} else
					globalDepozSel = tokenDep[0].trim();

				textNumeArticol.setText(numeArticol);
				textCodArticol.setText(codArticol);

				listUmVanz.clear();
				spinnerUMClp.setVisibility(View.GONE);
				HashMap<String, String> tempUmVanz;
				tempUmVanz = new HashMap<String, String>();
				tempUmVanz.put("rowText", umVanz);

				listUmVanz.add(tempUmVanz);
				spinnerUMClp.setAdapter(adapterUmVanz);

				performListArtStoc();

			}

			catch (Exception ex) {
				Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	// eveniment selectie depozit
	public class OnSelectDepozit implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (codArticol.length() > 0) {
				String[] tokenDep = spinnerDepoz.getSelectedItem().toString().split("-");

				if (tokenDep[0].trim().length() == 2) {
					globalDepozSel = globalCodDepartSelectetItem.substring(0, 2) + tokenDep[0].trim();
				} else
					globalDepozSel = tokenDep[0].trim();

				performListArtStoc();
			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	private void performListArtStoc() {
		try {

			if (CreareClp.codFilialaDest == null || CreareClp.codFilialaDest.isEmpty())
				return;

			HashMap<String, String> params = new HashMap<String, String>();

			if (codArticol.length() == 8)
				codArticol = "0000000000" + codArticol;

			String varLocalUnitLog = "";

			if (globalDepozSel.equals("MAV1")) {
				if (CreareClp.codFilialaDest.equals("BV90"))
					varLocalUnitLog = "BV92";
				else
					varLocalUnitLog = CreareClp.codFilialaDest.substring(0, 2) + "2" + CreareClp.codFilialaDest.substring(3, 4);
			} else {
				varLocalUnitLog = CreareClp.codFilialaDest;
			}

			params.put("codArt", codArticol);
			params.put("filiala", varLocalUnitLog);
			params.put("depozit", globalDepozSel);

			AsyncTaskListener contextListener = (AsyncTaskListener) CLPFragment2.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getStocWeight", params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("unchecked")
	private void listArtStoc(String stocResponse) {

		textCantArt.setText("");

		if (!stocResponse.equals("-1")) {

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			String[] tokenStoc = stocResponse.split("#");

			textNumeArticol.setVisibility(View.VISIBLE);
			textCodArticol.setVisibility(View.VISIBLE);
			layoutGreutate.setVisibility(View.VISIBLE);

			textUm.setVisibility(View.VISIBLE);
			textStoc.setVisibility(View.VISIBLE);
			labelStoc.setVisibility(View.VISIBLE);

			textUm.setText(tokenStoc[1]);
			selectedUnitMas = tokenStoc[1];

			String strGreutateArt = "";

			if (Double.valueOf(tokenStoc[0]) > 0) {
				greutateArticol = opArticol.deserializeGreutateArticol((String) tokenStoc[3]);

				strGreutateArt = String.valueOf(greutateArticol.getGreutate()) + " " + greutateArticol.getUnitMas() + "/"
						+ greutateArticol.getUnitMasCantiate();

			}

			textValoareGreutate.setText(strGreutateArt);

			artMap = (HashMap<String, String>) spinnerUMClp.getSelectedItem();
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
				spinnerUMClp.setAdapter(adapterUmVanz);
				spinnerUMClp.setVisibility(View.VISIBLE);
				selectedUnitMas = stocUM;
			}

			textStoc.setText(nf2.format(Double.valueOf(tokenStoc[0])));

			labelCantArt.setVisibility(View.VISIBLE);
			textCantArt.setVisibility(View.VISIBLE);

			this.saveArtBtnClp.setVisibility(View.VISIBLE);

		} else {

			Toast.makeText(getActivity(), "Nu exista informatii.", Toast.LENGTH_SHORT).show();

			textUm.setText("");
			textStoc.setText("");
		}

	}

	public void addDrawerListener() {
		slidingDrawerArt.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

				if (UtilsUser.isAgentOrSD())
					addSpinnerDepartamente();

				layoutArtHeader.setVisibility(View.INVISIBLE);
				layoutArtDet.setVisibility(View.INVISIBLE);
				layoutSaveClp.setVisibility(View.INVISIBLE);
				layoutTotalGreutate.setVisibility(View.INVISIBLE);

			}
		});

		slidingDrawerArt.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {

				if (UtilsUser.isAgentOrSD())
					removeSpinnerDepartamente();

				layoutArtHeader.setVisibility(View.VISIBLE);
				layoutArtDet.setVisibility(View.VISIBLE);
				if (listArtSelClp.size() > 0) {
					layoutSaveClp.setVisibility(View.VISIBLE);
					layoutTotalGreutate.setVisibility(View.VISIBLE);
				}

				ScreenUtils.hideSoftKeyboard(getActivity(), txtNumeArticol);

			}
		});

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();

		if (menuItemIndex == 0) // stergere
		{
			listArtSelClp.remove(listViewSelPos);
			adapterListArtClp.notifyDataSetChanged();
			listViewSelPos = -1;

			refreshListViewItems();

			if (listArtSelClp.size() == 0)
				layoutSaveClp.setVisibility(View.INVISIBLE);

		}

		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listArtCmdClp) {

			try {

				artMap = (HashMap<String, String>) CLPFragment2.adapterListArtClp.getItem(listViewSelPos);
				String artSel = artMap.get("numeArt");

				menu.setHeaderTitle(artSel);
				menu.add(Menu.NONE, 0, 0, "Sterge");
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void addListenerListArtSel() {
		listArtCmdClp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelPos = position;
				return false;

			}
		});
	}

	@SuppressWarnings("unchecked")
	private void getTotalGreutate() {

		int nrArticole = listArtSelClp.size();
		int ii = 0;

		NumberFormat nf3 = NumberFormat.getInstance();
		nf3.setMinimumFractionDigits(3);
		nf3.setMaximumFractionDigits(3);

		double totalGreutate = 0;
		for (ii = 0; ii < nrArticole; ii++) {
			artMap = (HashMap<String, String>) CLPFragment2.adapterListArtClp.getItem(ii);

			totalGreutate += Double.valueOf(UtilsFormatting.removeComma(artMap.get("greutate")));

		}

		textTotalGreutate.setText("Total greutate: " + nf3.format(totalGreutate) + " KG");
	}

	@SuppressWarnings("unchecked")
	public void refreshListViewItems() {

		int nrArt = listArtSelClp.size(), ii = 0;
		String tokVal = "";

		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);
		nf2.setGroupingUsed(false);

		NumberFormat nf3 = NumberFormat.getInstance();
		nf3.setMinimumFractionDigits(3);
		nf3.setMaximumFractionDigits(3);
		nf3.setGroupingUsed(false);

		// init obj
		for (int i = 0; i < objArticol.length; i++) {
			objArticol[i] = null;
		}

		for (ii = 0; ii < nrArt; ii++) {
			artMap = (HashMap<String, String>) CLPFragment2.adapterListArtClp.getItem(ii);

			objArticol[ii] = new ArticolComanda();

			tokVal = artMap.get("codArt");
			objArticol[ii].setCodArticol(tokVal);

			tokVal = artMap.get("numeArt");
			objArticol[ii].setNumeArticol(tokVal);

			tokVal = artMap.get("cantArt");
			objArticol[ii].setCantitate(Double.parseDouble(tokVal.replace(",", "")));

			tokVal = artMap.get("Umb");
			objArticol[ii].setUmb(tokVal);

			tokVal = artMap.get("depozit");
			objArticol[ii].setDepozit(tokVal);

			tokVal = artMap.get("sintetic");
			objArticol[ii].setObservatii(tokVal);

			tokVal = artMap.get("greutate");
			objArticol[ii].setCantUmb(Double.parseDouble(tokVal.replace(",", "")));

			tokVal = artMap.get("umgreutate");
			objArticol[ii].setUnitMasPretMediu(tokVal);

			tokVal = artMap.get("depart");
			objArticol[ii].setDepart(tokVal);

		}// sf. for

		listArtSelClp.clear();
		adapterListArtClp.notifyDataSetChanged();
		HashMap<String, String> temp;

		for (ii = 0; ii < objArticol.length; ii++) {
			if (objArticol[ii] != null) {

				temp = new HashMap<String, String>();
				temp.put("nrCrt", String.valueOf(ii + 1) + ".");
				temp.put("codArt", objArticol[ii].getCodArticol());
				temp.put("numeArt", objArticol[ii].getNumeArticol());
				temp.put("cantArt", nf2.format(objArticol[ii].getCantitate()));
				temp.put("Umb", objArticol[ii].getUmb());
				temp.put("depozit", objArticol[ii].getDepozit());
				temp.put("sintetic", objArticol[ii].getObservatii());
				temp.put("greutate", nf3.format(objArticol[ii].getCantUmb()));
				temp.put("umgreutate", objArticol[ii].getUnitMasPretMediu());
				temp.put("depart", objArticol[ii].getDepart());

				listArtSelClp.add(temp);
			}

		}// sf. for

		adapterListArtClp.notifyDataSetChanged();
		getTotalGreutate();

	}

	public void addDrawerClpListener() {
		slidingDrawerSaveClp.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButtonClp.setBackgroundResource(R.drawable.slideright32);
			}
		});

		slidingDrawerSaveClp.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slideButtonClp.setBackgroundResource(R.drawable.slideleft32);

			}
		});

	}

	public void addListenerSaveClpBtn() {

		saveClpButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						if (!validateDataToSave().equalsIgnoreCase("-1")) {
							Toast.makeText(getActivity(), validateDataToSave(), Toast.LENGTH_LONG).show();
						} else {

							mProgressClp.setVisibility(View.VISIBLE);
							mProgressClp.setProgress(0);
							progressVal = 0;
							myTimerClp = new Timer();
							myTimerClp.schedule(new UpdateProgress(), 40, 15);
						}
						return true;

					case MotionEvent.ACTION_UP:
						if (mProgressClp.getVisibility() == View.VISIBLE) {
							myTimerClp.cancel();
							mProgressClp.setVisibility(View.INVISIBLE);
							return true;
						}

					}
				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

				return false;
			}

		});

	}

	private String validateDataToSave() {
		String retVal = "-1";

		if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("44")) {

			if (CLPFragment1.textSelClient.getText().toString().trim().equals("")) {
				retVal = "Selectati clientul!";
				return retVal;
			}

		} else {
			if (CreareClp.codClient.trim().toString().equalsIgnoreCase("")) {
				retVal = "Selectati clientul!";
				return retVal;
			}
		}

		if (CLPFragment1.radioClient.isChecked()) {

			if (!CreareClp.tipTransport.equalsIgnoreCase("TCLI")) {

				if (CreareClp.codJudet.trim().equalsIgnoreCase("")) {
					retVal = "Selectati judetul!";
					return retVal;
				}

				if (CreareClp.oras.trim().equalsIgnoreCase("")) {
					retVal = "Completati orasul!";
					return retVal;
				}

				if (CreareClp.strada.trim().equalsIgnoreCase("") && !hasCoordinates()) {
					retVal = "Completati strada sau pozitionati adresa pe harta!";
					return retVal;
				}

				if (!isAdresaCorecta()) {
					retVal = "Completati adresa corect sau pozitionati adresa pe harta.";
					return retVal;
				}

				if (CreareClp.persCont.trim().equalsIgnoreCase("")) {
					retVal = "Completati persoana de contact!";
					return retVal;
				}

				if (CreareClp.telefon.trim().equalsIgnoreCase("")) {
					retVal = "Completati nr. telefon!";
					return retVal;
				}
			}

			if (CLPFragment1.spinnerAgentiCLP.getVisibility() == View.VISIBLE) {
				if (CLPFragment1.spinnerAgentiCLP.getSelectedItemId() == 0) {
					retVal = "Selectati agentul!";
					return retVal;
				}
			}

		}

		if (CreareClp.tipTransport.equalsIgnoreCase("TRAP")) {
			if (CreareClp.tipMarfa.trim().toString().equalsIgnoreCase("")) {
				retVal = "Completati tipul de marfa!";
				return retVal;
			}

			if (CreareClp.masaMarfa.trim().equalsIgnoreCase("")) {
				retVal = "Completati masa !";
				return retVal;
			}
		}

		if (CreareClp.tipTransport.toUpperCase().equals("TRAP") && CLPFragment1.spinnerTonaj.getSelectedItemPosition() == 0) {
			retVal = "Selectati tonajul!";
			return retVal;
		}

		if (CreareClp.codFilialaDest.trim().toString().equalsIgnoreCase("")) {
			retVal = "Selectati filiala!";
			return retVal;
		}

		return retVal;
	}

	private boolean hasCoordinates() {
		if (CLPFragment1.coordAdresa == null)
			return false;
		else if (CLPFragment1.coordAdresa.latitude == 0)
			return false;

		return true;
	}

	private boolean isAdresaGoogleOk() {
		return MapUtils.geocodeAddress(getAddressFromForm(), getActivity()).isAdresaValida();

	}

	private boolean isAdresaCorecta() {
		if (CreareClp.tipTransport.equalsIgnoreCase("TRAP") && !hasCoordinates())
			return isAdresaGoogleOk();
		else
			return true;

	}

	private Address getAddressFromForm() {
		Address address = new Address();

		address.setCity(CreareClp.oras);
		address.setStreet(CreareClp.strada);
		address.setSector(UtilsGeneral.getNumeJudet(CreareClp.codJudet));

		return address;
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (mProgressClp.getProgress() == 50) {
				clpHandler.post(new Runnable() {
					public void run() {

						performSaveNewClp();

					}
				});

				myTimerClp.cancel();
			} else {
				mProgressClp.setProgress(progressVal);
			}

		}
	}

	private void performSaveNewClp() {

		try {

			String articoleFinale = prepareArtForDelivery();

			String depozDest = "";
			String varSelectedDepoz = CLPFragment1.spinnerDepozClp_Dest.getSelectedItem().toString().trim();

			if (varSelectedDepoz.contains("-")) {
				String[] tokenDep_Dest = varSelectedDepoz.split("-");
				depozDest = UserInfo.getInstance().getCodDepart() + tokenDep_Dest[0].trim();
			} else {
				depozDest = varSelectedDepoz;
			}

			// agenti
			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {
				CreareClp.selectedAgent = UserInfo.getInstance().getCod();
			}

			String numeClientCV = CLPFragment1.textSelClient.getText().toString().trim();

			String observatiiCLP = CLPFragment1.txtObservatiiCLP.getText().toString().isEmpty() ? " " : CLPFragment1.txtObservatiiCLP.getText()
					.toString();

			String localCodClient = CreareClp.codClient;

			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
					|| UserInfo.getInstance().getTipAcces().equals("44")) {
				if (CLPFragment1.radioClientPF.isChecked()) {
					localCodClient = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PF");
				}

				if (CLPFragment1.radioClientPJ.isChecked()) {
					localCodClient = InfoStrings.getClientGenericGed(UserInfo.getInstance().getUnitLog(), "PJ");
				}

			}

			String strTonaj = "-1";

			if (isConditiiTonaj(CreareClp.tipTransport, CLPFragment1.spinnerTonaj)) {
				String[] tonaj = CLPFragment1.spinnerTonaj.getSelectedItem().toString().split(" ");
				strTonaj = tonaj[0];
			}

			String prelucrare04 = "-1";
			if (CLPFragment1.spinnerIndoire.getVisibility() == View.VISIBLE && CLPFragment1.spinnerIndoire.getSelectedItemPosition() > 0) {
				prelucrare04 = CLPFragment1.spinnerIndoire.getSelectedItem().toString();
			}

			AntetComandaCLP antetComandaCLP = new AntetComandaCLP();

			antetComandaCLP.setCodClient(localCodClient);
			antetComandaCLP.setCodJudet(CreareClp.codJudet);
			antetComandaCLP.setLocalitate(CreareClp.oras);
			antetComandaCLP.setStrada(CreareClp.strada.isEmpty() ? " " : CreareClp.strada);
			antetComandaCLP.setPersCont(CreareClp.persCont.isEmpty() ? " " : CreareClp.persCont);
			antetComandaCLP.setTelefon(CreareClp.telefon.isEmpty() ? " " : CreareClp.telefon);
			antetComandaCLP.setCodFilialaDest(CreareClp.codFilialaDest);
			antetComandaCLP.setDataLivrare(CreareClp.dataLivrare);
			antetComandaCLP.setTipPlata(CreareClp.tipPlata);
			antetComandaCLP.setTipTransport(CreareClp.tipTransport);
			antetComandaCLP.setDepozDest(depozDest);
			antetComandaCLP.setSelectedAgent(CreareClp.selectedAgent);
			antetComandaCLP.setCmdFasonate(cmdFasonate);
			antetComandaCLP.setNumeClientCV(numeClientCV);
			antetComandaCLP.setObservatiiCLP(observatiiCLP);
			antetComandaCLP.setTipMarfa(CreareClp.tipMarfa);
			antetComandaCLP.setMasaMarfa(CreareClp.masaMarfa);
			antetComandaCLP.setTipCamion(CLPFragment1.spinnerTipCamion.getSelectedItem().toString().toUpperCase(Locale.getDefault()));
			antetComandaCLP.setTipIncarcare(CLPFragment1.spinnerTipIncarcare.getSelectedItem().toString().toUpperCase(Locale.getDefault()));
			antetComandaCLP.setTonaj(strTonaj);
			antetComandaCLP.setPrelucrare(prelucrare04);

			comandaCLP.setAntetComandaCLP(antetComandaCLP);

			CreareClp.comandaFinala = localCodClient + "#" + CreareClp.codJudet + "#" + CreareClp.oras + "#" + CreareClp.strada + "#"
					+ CreareClp.persCont + "#" + CreareClp.telefon + "#" + CreareClp.codFilialaDest + "#" + CreareClp.dataLivrare + "#"
					+ CreareClp.tipPlata + "#" + CreareClp.tipTransport + "#" + depozDest + "#" + CreareClp.selectedAgent + "#" + cmdFasonate + "#"
					+ numeClientCV + "#" + observatiiCLP + "#" + CreareClp.tipMarfa + "#" + CreareClp.masaMarfa + "#"
					+ CLPFragment1.spinnerTipCamion.getSelectedItem().toString().toUpperCase(Locale.getDefault()) + "#"
					+ CLPFragment1.spinnerTipIncarcare.getSelectedItem().toString().toUpperCase(Locale.getDefault()) + "#" + strTonaj + "@"
					+ articoleFinale;

			performSaveClp();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private boolean isConditiiTonaj(String tipTransport, Spinner spinnerTonaj) {
		return tipTransport.equals("TRAP") && spinnerTonaj.getSelectedItem().toString().split(" ")[1].equals("T");

	}

	@SuppressWarnings("unchecked")
	private String prepareArtForDelivery() {

		List<ArticolCLP> listArticole = new ArrayList<ArticolCLP>();

		String retVal = "";
		try {

			cmdFasonate = true;
			int nrArt = listArtSelClp.size(), ii = 0;

			for (ii = 0; ii < nrArt; ii++) {
				artMap = (HashMap<String, String>) adapterListArtClp.getItem(ii);

				if (!isFasonat(artMap.get("sintetic"))) {
					cmdFasonate = false;
				}

				ArticolCLP articolCLP = new ArticolCLP();
				articolCLP.setCod(artMap.get("codArt"));
				articolCLP.setCantitate(artMap.get("cantArt"));
				articolCLP.setUmBaza(artMap.get("Umb"));
				articolCLP.setDepozit(artMap.get("depozit"));
				articolCLP.setDepart(artMap.get("depart"));
				listArticole.add(articolCLP);

				retVal += artMap.get("codArt") + "#" + artMap.get("cantArt") + "#" + artMap.get("Umb") + "#" + artMap.get("depozit") + "@";

			}

			Collections.sort(listArticole, new ClpDepartComparator());
			comandaCLP.setListaArticoleComanda(listArticole);

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

		return retVal;
	}

	private boolean isFasonat(String codSint) {
		boolean isFasonat = false;
		String[] sinteticeFasonate = { "437", "438", "439", "440" };

		for (int i = 0; i < sinteticeFasonate.length; i++) {

			if (sinteticeFasonate[i].equals(codSint)) {
				isFasonat = true;
			}

		}

		return isFasonat;

	}

	private void performSaveClp() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String localAlertSD = "true";

			// pentru KA nu se cere aprobarea SD-ului local
			if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("27")) {
				localAlertSD = "false";
			}

			String localDep = UserInfo.getInstance().getCodDepart();

			// consilieri
			if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
					|| UserInfo.getInstance().getTipAcces().equals("44")) {
				localDep = CLPFragment1.departamentConsilier;
			}

			params.put("comanda", CreareClp.comandaFinala);
			params.put("codAgent", UserInfo.getInstance().getCod());
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", localDep);
			params.put("alertSD", localAlertSD);
			params.put("serData", operatiiClp.serializeComandaClp(comandaCLP));
			params.put("codSuperAgent", UserInfo.getInstance().getCodSuperUser());

			operatiiClp.salveazaComanda(params);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	// eveniment selectie unit mas alternativa
	public class OnSelectUnitMas implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) spinnerUMClp.getSelectedItem();
			selectedUnitMas = artMap.get("rowText");

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	public static void clearArticoleList() {
		if (listArtSelClp != null) {
			listArtSelClp.clear();
			adapterListArtClp.notifyDataSetChanged();
		}
	}

	public void clearAllData() {

		listArtSelClp.clear();
		adapterListArtClp.notifyDataSetChanged();

		globalDepozSel = "";
		globalCodDepartSelectetItem = "";
		CreareClp.codClient = " ";
		CreareClp.codJudet = " ";
		CreareClp.oras = " ";
		CreareClp.strada = " ";
		CreareClp.persCont = " ";
		CreareClp.telefon = " ";
		CreareClp.codFilialaDest = " ";
		CreareClp.comandaFinala = " ";
		CreareClp.dataLivrare = " ";
		CreareClp.tipTransport = "TRAP";
		CreareClp.tipPlata = "B";

		CLPFragment1.clearClientData();

		for (int i = 0; i < objArticol.length; i++) {
			objArticol[i] = null;
		}

		slidingDrawerSaveClp.close();
		textValoareGreutate.setText("");
		textTotalGreutate.setText("");

		initLocale();
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getCantUmAlt")) {
			double cantUmBaza = Double.valueOf(textStoc.getText().toString().replace(",", ""));
			double cantUmAlt = Double.valueOf((String) result);

			factorConversie = cantUmAlt / Double.valueOf(textCantArt.getText().toString().trim());

			if (cantUmAlt > cantUmBaza) {
				Toast toast = Toast.makeText(getActivity(), "Stoc insuficient!", Toast.LENGTH_LONG);
				toast.show();
			} else {
				performSaveSelectedArt();
			}
		}

		if (methodName.equals("getStocWeight")) {
			listArtStoc((String) result);
		}

	}

	private void showSaveStatus(String status) {

		String strInfo = "";
		String[] info = { "" };

		if (status.contains(","))
			info = status.split(",");

		if (info.length > 1 && info[1].trim().length() > 0)
			strInfo = info[1];

		if (status.startsWith("0")) {

			if (info.length > 0 && info[1].trim().length() > 0)
				Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();

			Toast.makeText(getActivity(), "Comanda salvata.", Toast.LENGTH_SHORT).show();
			clearAllData();
		} else {
			Toast.makeText(getActivity(), "Comanda nu a fost salvata. " + strInfo, Toast.LENGTH_LONG).show();
		}
	}

	public void operationClpComplete(EnumClpDAO methodName, Object result) {
		switch (methodName) {
		case SALVEAZA_COMANDA:
			showSaveStatus((String) result);
			break;
		default:
			break;
		}

	}

	private void initLocale() {
		Locale locale = new Locale("en", "US");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {

		switch (methodName) {
		case GET_ARTICOLE_DISTRIBUTIE:
			populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
			break;
		default:
			break;
		}

	}
}
