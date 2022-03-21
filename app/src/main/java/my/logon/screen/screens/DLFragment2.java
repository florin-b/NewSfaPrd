/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumDlDAO;
import my.logon.screen.listeners.DlDAOListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.DlDAO;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsGeneral;

public class DLFragment2 extends Fragment implements DlDAOListener, OperatiiArticolListener {

	Button articoleBtn, saveArtBtnDl, saveDlButton, slideButtonDl;
	ToggleButton tglButton, tglTipArtBtn;
	private EditText txtNumeArticol, textCantArt;

	String[] depozite = { "V1 - vanzare", "V2 - vanzare", "V3 - vanzare", "G1 - gratuite", "G2 - gratuite", "G3 - gratuite", "D1 - deteriorate",
			"D2 - deteriorate", "D3 - deteriorate", "DESC" };

	private static ArrayList<HashMap<String, String>> listArticole = null, listArtSelDl = null;
	public SimpleAdapter adapterListArtDl;
	public ListView listViewArticole;

	String codArticol = "", numeArticol = "", umArticol = "";

	public String globalDepozSel = "", globalCodDepartSelectetItem = "";
	private TextView textNumeArticol, textCodArticol, labelCantArt, textUmAprov;
	NumberFormat nf2;
	SlidingDrawer slidingDrawerArt, slidingDrawerSaveDl;
	LinearLayout layoutArtHeader, layoutArtDet;
	TableLayout layoutSaveDl;
	ListView listArtCmdDl;
	private HashMap<String, String> artMap = null;
	private int listViewSelPos = -1;
	private static ArticolComanda[] objArticol = new ArticolComanda[70];
	private ProgressBar mProgressDl;
	private Timer myTimerDl;
	private int progressVal = 0;
	private Handler dlHandler = new Handler();
	DlDAO operatiiComenzi;

	OperatiiArticol opArticol;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dl_fragment_2, container, false);

		try {

			checkStaticVars();

			opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", getActivity());
			opArticol.setListener(this);

			operatiiComenzi = new DlDAO(getActivity());
			operatiiComenzi.setDlDAOListener(this);

			initLocale();
			nf2 = NumberFormat.getInstance();

			layoutArtHeader = (LinearLayout) v.findViewById(R.id.layoutHeaderArtDl);
			layoutArtDet = (LinearLayout) v.findViewById(R.id.layoutDetArtDl);

			this.slidingDrawerArt = (SlidingDrawer) v.findViewById(R.id.articoleSlidingDrawer);
			addDrawerListener();

			layoutSaveDl = (TableLayout) v.findViewById(R.id.layoutSaveDl);
			layoutSaveDl.setVisibility(View.INVISIBLE);

			this.slidingDrawerSaveDl = (SlidingDrawer) v.findViewById(R.id.slidingDrawerDl);
			addDrawerDlListener();

			this.tglButton = (ToggleButton) v.findViewById(R.id.togglebuttonDl);
			addListenerToggle();
			this.tglButton.setChecked(true);

			this.tglTipArtBtn = (ToggleButton) v.findViewById(R.id.tglTipArtDl);
			addListenerTglTipArtBtn();

			txtNumeArticol = (EditText) v.findViewById(R.id.txtNumeArtDl);

			this.articoleBtn = (Button) v.findViewById(R.id.articoleBtnDl);
			addListenerBtnArticole();

			listArticole = new ArrayList<HashMap<String, String>>();

			this.listViewArticole = (ListView) v.findViewById(R.id.listArticole);
			listViewArticole.setOnItemClickListener(new MyOnItemSelectedListener());

			textNumeArticol = (TextView) v.findViewById(R.id.textNumeArticolDl);
			textNumeArticol.setVisibility(View.INVISIBLE);
			textCodArticol = (TextView) v.findViewById(R.id.textCodArticolDl);
			textCodArticol.setVisibility(View.INVISIBLE);

			labelCantArt = (TextView) v.findViewById(R.id.labelCantArtDl);
			labelCantArt.setVisibility(View.INVISIBLE);
			textCantArt = (EditText) v.findViewById(R.id.txtCantArtDl);
			textCantArt.setVisibility(View.INVISIBLE);

			textUmAprov = (TextView) v.findViewById(R.id.textUmAprov);
			textUmAprov.setText("");

			listArtCmdDl = (ListView) v.findViewById(R.id.listArtCmdDl);

			listArtSelDl = new ArrayList<HashMap<String, String>>();
			adapterListArtDl = new SimpleAdapter(getActivity(), listArtSelDl, R.layout.custom_art_row_clp, new String[] { "nrCrt", "numeArt",
					"codArt", "cantArt", "depozit", "Umb" }, new int[] { R.id.textNrCrt, R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt,
					R.id.textDepozit, R.id.textCantUmb }

			);

			listArtCmdDl.setAdapter(adapterListArtDl);
			listArtCmdDl.setClickable(true);
			addListenerListArtSel();
			registerForContextMenu(listArtCmdDl);

			this.saveArtBtnDl = (Button) v.findViewById(R.id.saveArtBtnDl);
			this.saveArtBtnDl.setVisibility(View.INVISIBLE);
			addListenerBtnSaveArticole();

			this.slideButtonDl = (Button) v.findViewById(R.id.slideButtonDl);

			this.saveDlButton = (Button) v.findViewById(R.id.saveDlBtn);
			addListenerSaveDlBtn();

			mProgressDl = (ProgressBar) v.findViewById(R.id.progress_bar_savedl);
			mProgressDl.setVisibility(View.INVISIBLE);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

		return v;
	}

	public static final DLFragment2 newInstance() {
		DLFragment2 f = new DLFragment2();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {
					if (tglTipArtBtn.isChecked()) {
						txtNumeArticol.setHint("Introduceti cod sintetic");
					} else {
						txtNumeArticol.setHint("Introduceti cod articol");
					}
				} else {
					if (tglTipArtBtn.isChecked()) {
						txtNumeArticol.setHint("Introduceti nume sintetic");
					} else {
						txtNumeArticol.setHint("Introduceti nume articol");
					}
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
					} else {
						txtNumeArticol.setHint("Introduceti cod sintetic");
					}
				} else {
					if (!tglButton.isChecked()) {
						txtNumeArticol.setHint("Introduceti nume articol");
					} else {
						txtNumeArticol.setHint("Introduceti cod articol");
					}

				}
			}
		});

	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {

						if (!CreareDispozitiiLivrare.codFurnizor.trim().equals(""))
							performGetArticole();
						else
							Toast.makeText(getActivity(), "Selectati furnizorul!", Toast.LENGTH_SHORT).show();

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
		saveArtBtnDl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					performSaveSelectedArt();
				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	// adaugare articol selectat in lista
	@SuppressWarnings("unchecked")
	private void performSaveSelectedArt() {

		try {

			// validari
			if (numeArticol.equals("")) {
				Toast.makeText(getActivity(), "Selectati un articol!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (textCantArt.getText().toString().trim().length() == 0) {
				Toast.makeText(getActivity(), "Cantitate invalida!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (Double.valueOf(textCantArt.getText().toString().trim()) == 0) {
				Toast.makeText(getActivity(), "Cantitate invalida!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (codArticol.length() == 18)
				codArticol = codArticol.substring(10, 18);

			// verificare existenta articol
			int nrArt = listArtSelDl.size(), ii = 0, selectedArtPos = -1;

			for (ii = 0; ii < nrArt; ii++) {
				artMap = (HashMap<String, String>) this.adapterListArtDl.getItem(ii);
				if (codArticol.equals(artMap.get("codArt"))) {
					selectedArtPos = ii;
					listArtSelDl.remove(ii);
					break;
				}

			}

			// sf. verificare

			HashMap<String, String> temp;
			temp = new HashMap<String, String>(30, 0.75f);

			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			Integer tokNrCrt = -1;

			if (selectedArtPos == -1) {
				selectedArtPos = listArtSelDl.size();
				tokNrCrt = listArtSelDl.size() + 1;
			} else {
				tokNrCrt = selectedArtPos + 1;
			}

			temp.put("nrCrt", String.valueOf(tokNrCrt) + ".");
			temp.put("numeArt", numeArticol);
			temp.put("codArt", codArticol);
			temp.put("cantArt", nf2.format(Double.valueOf(textCantArt.getText().toString().trim())));
			temp.put("Umb", umArticol);
			temp.put("depozit", " ");

			listArtSelDl.add(selectedArtPos, temp);
			listArtCmdDl.setAdapter(adapterListArtDl);
			adapterListArtDl.notifyDataSetChanged();

			InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

			this.saveArtBtnDl.setVisibility(View.INVISIBLE);
			clearVars();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}

	private void clearVars() {
		numeArticol = "";
		codArticol = "";
		umArticol = "";

		textNumeArticol.setText("");
		textCodArticol.setText("");
		textUmAprov.setText("");
		textCantArt.setText("");

		textCantArt.setVisibility(View.INVISIBLE);
		labelCantArt.setVisibility(View.INVISIBLE);

	}

	protected void performGetArticole() {

		try {

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
			params.put("furnizor", CreareDispozitiiLivrare.codFurnizor);
			params.put("codDepart", UserInfo.getInstance().getCodDepart());

			opArticol.getArticoleFurnizor(params);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateListViewArt(List<ArticolDB> listArticole) {

		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(getActivity(), listArticole);
		listViewArticole.setAdapter(adapterArticole);

	}

	public class MyOnItemSelectedListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			try {

				ArticolDB articol = (ArticolDB) parent.getAdapter().getItem(pos);

				numeArticol = articol.getNume();
				codArticol = articol.getCod();
				umArticol = articol.getUmVanz();

				textNumeArticol.setText(numeArticol);
				textCodArticol.setText(codArticol);
				textUmAprov.setText(umArticol);

				textNumeArticol.setVisibility(View.VISIBLE);
				textCodArticol.setVisibility(View.VISIBLE);

				labelCantArt.setVisibility(View.VISIBLE);
				textCantArt.setVisibility(View.VISIBLE);

				saveArtBtnDl.setVisibility(View.VISIBLE);

			}

			catch (Exception ex) {
				Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void addDrawerListener() {
		slidingDrawerArt.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

				layoutArtHeader.setVisibility(View.INVISIBLE);
				layoutArtDet.setVisibility(View.INVISIBLE);
				layoutSaveDl.setVisibility(View.INVISIBLE);

			}
		});

		slidingDrawerArt.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {

				layoutArtHeader.setVisibility(View.VISIBLE);
				layoutArtDet.setVisibility(View.VISIBLE);
				if (listArtSelDl.size() > 0) {
					layoutSaveDl.setVisibility(View.VISIBLE);
				}

				// hide keyb
				InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

			}
		});

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();

		if (menuItemIndex == 0) // stergere
		{
			listArtSelDl.remove(listViewSelPos);
			adapterListArtDl.notifyDataSetChanged();
			listViewSelPos = -1;

			refreshListViewItems();

			if (listArtSelDl.size() == 0)
				layoutSaveDl.setVisibility(View.INVISIBLE);

		}

		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listArtCmdDl) {

			try {

				artMap = (HashMap<String, String>) this.adapterListArtDl.getItem(listViewSelPos);
				String artSel = artMap.get("numeArt");

				menu.setHeaderTitle(artSel);
				menu.add(Menu.NONE, 0, 0, "Sterge");
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void addListenerListArtSel() {
		listArtCmdDl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelPos = position;
				return false;

			}
		});
	}

	@SuppressWarnings("unchecked")
	public void refreshListViewItems() {

		int nrArt = listArtSelDl.size(), ii = 0;
		String tokVal = "";

		nf2.setMinimumFractionDigits(3);
		nf2.setMaximumFractionDigits(3);

		// init obj
		for (int i = 0; i < objArticol.length; i++) {
			objArticol[i] = null;
		}

		for (ii = 0; ii < nrArt; ii++) {
			artMap = (HashMap<String, String>) this.adapterListArtDl.getItem(ii);

			objArticol[ii] = new ArticolComanda();

			tokVal = artMap.get("codArt");
			objArticol[ii].setCodArticol(tokVal);

			tokVal = artMap.get("numeArt");
			objArticol[ii].setNumeArticol(tokVal);

			tokVal = artMap.get("cantArt");
			objArticol[ii].setCantitate(Double.parseDouble(tokVal));

			tokVal = artMap.get("Umb");
			objArticol[ii].setUmb(tokVal);

			tokVal = artMap.get("depozit");
			objArticol[ii].setDepozit(tokVal);

		}// sf. for

		listArtSelDl.clear();
		adapterListArtDl.notifyDataSetChanged();
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

				listArtSelDl.add(temp);
			}

		}// sf. for

		adapterListArtDl.notifyDataSetChanged();

	}

	public void addDrawerDlListener() {
		slidingDrawerSaveDl.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButtonDl.setBackgroundResource(R.drawable.slideright32);
			}
		});

		slidingDrawerSaveDl.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slideButtonDl.setBackgroundResource(R.drawable.slideleft32);

			}
		});

	}

	public void addListenerSaveDlBtn() {

		saveDlButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						if (!checkDataToSave().equalsIgnoreCase("-1")) {
							Toast.makeText(getActivity(), checkDataToSave(), Toast.LENGTH_LONG).show();
						} else {

							mProgressDl.setVisibility(View.VISIBLE);
							mProgressDl.setProgress(0);
							progressVal = 0;
							myTimerDl = new Timer();
							myTimerDl.schedule(new UpdateProgress(), 40, 15);
						}
						return true;

					case MotionEvent.ACTION_UP:
						if (mProgressDl.getVisibility() == View.VISIBLE) {
							myTimerDl.cancel();
							mProgressDl.setVisibility(View.INVISIBLE);
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

	private String checkDataToSave() {
		String retVal = "-1";

		if (CreareDispozitiiLivrare.codClient.trim().toString().equalsIgnoreCase("")) {
			retVal = "Selectati clientul!";
			return retVal;
		}

		if (DLFragment1.radioClient.isChecked()) {
			if (CreareDispozitiiLivrare.codJudet.trim().toString().equalsIgnoreCase("")) {
				retVal = "Selectati judetul!";
				return retVal;
			}

			if (CreareDispozitiiLivrare.oras.trim().toString().equalsIgnoreCase("")) {
				retVal = "Completati orasul!";
				return retVal;
			}

			if (CreareDispozitiiLivrare.strada.trim().toString().equalsIgnoreCase("") && !hasCoordinates()) {
				retVal = "Completati strada sau pozitionati adresa pe harta!";
				return retVal;
			}

			if (CreareDispozitiiLivrare.persCont.trim().toString().equalsIgnoreCase("")) {
				retVal = "Completati persoana de contact!";
				return retVal;
			}

			if (CreareDispozitiiLivrare.telefon.trim().toString().equalsIgnoreCase("")) {
				retVal = "Completati nr. telefon!";
				return retVal;
			}

			if (DLFragment1.spinnerAgentiDl.getVisibility() == View.VISIBLE) {
				if (DLFragment1.spinnerAgentiDl.getSelectedItemId() == 0) {
					retVal = "Selectati agentul!";
					return retVal;
				}
			}

			if (!isAdresaCorecta()) {
				retVal = "Completati adresa corect sau pozitionati adresa pe harta.";
				return retVal;
			}
		}

		if (CreareDispozitiiLivrare.codFurnizor.trim().toString().equalsIgnoreCase("")) {
			retVal = "Selectati furnizorul!";
			return retVal;
		}

		if (CreareDispozitiiLivrare.codFurnizorProduse.trim().toString().equalsIgnoreCase("")) {
			retVal = "Selectati furnizorul de produse!";
			return retVal;
		}

		if (CreareDispozitiiLivrare.tipMarfa.trim().toString().equalsIgnoreCase("")) {
			retVal = "Completati tipul de marfa!";
			return retVal;
		}

		if (CreareDispozitiiLivrare.masaMarfa.trim().toString().equalsIgnoreCase("")) {
			retVal = "Completati masa !";
			return retVal;
		}

		if (DLFragment1.spinnerTipPlata.getSelectedItemId() == 4) {
			if (DLFragment1.txtValoareCLP.getText().toString().trim().equals("")) {
				retVal = "Completati valoarea!";
				return retVal;
			}
		}

		if (CreareDispozitiiLivrare.tipTransport.equals("TRAP") && DLFragment1.spinnerTonaj.getSelectedItemPosition() == 0) {
			retVal = "Selectati tonajul!";
			return retVal;
		}

		return retVal;
	}

	private boolean hasCoordinates() {
		if (DLFragment1.coordAdresa == null)
			return false;
		else if (DLFragment1.coordAdresa.latitude == 0)
			return false;

		return true;
	}

	private boolean isAdresaGoogleOk() {
		return MapUtils.geocodeAddress(getAddressFromForm(), getActivity()).isAdresaValida();

	}

	private boolean isAdresaCorecta() {
		if (CreareDispozitiiLivrare.tipTransport.toUpperCase().equals("TRAP") && !hasCoordinates())
			return isAdresaGoogleOk();
		else
			return true;

	}

	private Address getAddressFromForm() {
		Address address = new Address();

		address.setCity(CreareDispozitiiLivrare.oras);
		address.setStreet(CreareDispozitiiLivrare.strada);
		address.setSector(UtilsGeneral.getNumeJudet(CreareDispozitiiLivrare.codJudet));

		return address;
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (mProgressDl.getProgress() == 50) {
				dlHandler.post(new Runnable() {
					public void run() {

						performSaveNewDl();

					}
				});

				myTimerDl.cancel();
			} else {
				mProgressDl.setProgress(progressVal);
			}

		}
	}

	private void performSaveNewDl() {

		try {

			String articoleFinale = prepareArtForDelivery();

			String depozDest = "";
			String varSelectedDepoz = DLFragment1.spinnerDepozDl_Dest.getSelectedItem().toString().trim();

			if (varSelectedDepoz.contains("-")) {
				String[] tokenDep_Dest = varSelectedDepoz.split("-");
				depozDest = UserInfo.getInstance().getCodDepart() + tokenDep_Dest[0].trim();
			} else {
				depozDest = varSelectedDepoz;
			}

			String tipPlata = DLFragment1.spinnerTipPlata.getSelectedItem().toString().substring(0, 2).trim();
			String valoareClp = "0";
			if (tipPlata.equals("E1")) {
				valoareClp = DLFragment1.txtValoareCLP.getText().toString().trim();
			}

			String varObsClp = DLFragment1.txtObservatiiClp.getText().toString().trim().replace("#", " ").replace("@", " ");

			String observatiiClp = varObsClp.equals("") ? " " : varObsClp;

			// agenti, ka
			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {
				CreareDispozitiiLivrare.selectedAgent = UserInfo.getInstance().getCod();
			}

			String strTonaj = "-1";

			if (isConditiiTonaj(CreareDispozitiiLivrare.tipTransport, DLFragment1.spinnerTonaj)) {
				String[] tonaj = DLFragment1.spinnerTonaj.getSelectedItem().toString().split(" ");
				strTonaj = tonaj[0];
			}

			CreareDispozitiiLivrare.comandaFinala = CreareDispozitiiLivrare.codClient + "#" + CreareDispozitiiLivrare.codJudet + "#"
					+ CreareDispozitiiLivrare.oras + "#" + CreareDispozitiiLivrare.strada + "#" + CreareDispozitiiLivrare.persCont + "#"
					+ CreareDispozitiiLivrare.telefon + "#" + CreareDispozitiiLivrare.codFurnizor + "#" + CreareDispozitiiLivrare.dataLivrare + "#"
					+ CreareDispozitiiLivrare.tipMarfa + "#" + CreareDispozitiiLivrare.masaMarfa + "#"
					+ DLFragment1.spinnerTipCamion.getSelectedItem().toString().toUpperCase(Locale.getDefault()) + "#"
					+ DLFragment1.spinnerTipIncarcare.getSelectedItem().toString().toUpperCase(Locale.getDefault()) + "#" + depozDest + "#"
					+ tipPlata + "#" + valoareClp + "#" + observatiiClp + "#" + CreareDispozitiiLivrare.codFurnizorProduse + "#"
					+ CreareDispozitiiLivrare.selectedAgent + "#" + CreareDispozitiiLivrare.tipTransport + "#" + strTonaj + "@" + articoleFinale;

			performSaveDl();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private boolean isConditiiTonaj(String tipTransport, Spinner spinnerTonaj) {
		return tipTransport.equals("TRAP") && spinnerTonaj.getSelectedItem().toString().split(" ")[1].equals("T");

	}

	@SuppressWarnings("unchecked")
	private String prepareArtForDelivery() {

		String retVal = "";
		try {

			int nrArt = listArtSelDl.size(), ii = 0;

			for (ii = 0; ii < nrArt; ii++) {
				artMap = (HashMap<String, String>) this.adapterListArtDl.getItem(ii);

				retVal += artMap.get("codArt") + "#" + artMap.get("cantArt") + "#" + artMap.get("Umb") + "#" + artMap.get("depozit") + "@";

			}// sf. for

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

		return retVal;
	}

	private void performSaveDl() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String localAlertSD = "true";

			if (userIsSD()) {
				localAlertSD = "false";
			}

			params.put("comanda", CreareDispozitiiLivrare.comandaFinala);
			params.put("codAgent", UserInfo.getInstance().getCod());
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", UserInfo.getInstance().getCodDepart());
			params.put("alertSD", localAlertSD);

			operatiiComenzi.salveazaComanda(params);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	boolean userIsSD() {
		return UserInfo.getInstance().getTipAcces().equals("10");
	}

	private void clearAllData() {

		listArticole.clear();


		listArtSelDl.clear();
		adapterListArtDl.notifyDataSetChanged();

		globalDepozSel = "";
		globalCodDepartSelectetItem = "";
		CreareDispozitiiLivrare.codClient = "";
		CreareDispozitiiLivrare.codJudet = "";
		CreareDispozitiiLivrare.oras = "";
		CreareDispozitiiLivrare.strada = "";
		CreareDispozitiiLivrare.persCont = "";
		CreareDispozitiiLivrare.telefon = "";
		CreareDispozitiiLivrare.codFilialaDest = "";
		CreareDispozitiiLivrare.comandaFinala = "";
		CreareDispozitiiLivrare.dataLivrare = "";
		CreareDispozitiiLivrare.codFurnizor = "";
		CreareDispozitiiLivrare.tipMarfa = "";
		CreareDispozitiiLivrare.masaMarfa = "";
		CreareDispozitiiLivrare.codFurnizorProduse = "";
		CreareDispozitiiLivrare.selectedAgent = "";

		DLFragment1.clearClientData();

		for (int i = 0; i < objArticol.length; i++) {
			objArticol[i] = null;
		}

		slidingDrawerSaveDl.close();
		initLocale();
	}

	private void initLocale() {
		Locale locale = new Locale("en", "US");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
	}

	private void checkStaticVars() {
		// restart app la idle
		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}

	public void operationDlComplete(EnumDlDAO methodName, Object result) {
		switch (methodName) {
		case SALVEAZA_COMANDA:
			if (((String) result).equalsIgnoreCase("0")) {
				Toast toast = Toast.makeText(getActivity(), "Date salvate.", Toast.LENGTH_SHORT);
				toast.show();
				clearAllData();
			} else {
				Toast toast = Toast.makeText(getActivity(), "Datele nu au fost salvate.", Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		default:
			break;
		}

	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {
		case GET_ARTICOLE_FURNIZOR:
			populateListViewArt(opArticol.deserializeArticoleVanzare((String) result));
			break;
		default:
			break;

		}

	}
}
