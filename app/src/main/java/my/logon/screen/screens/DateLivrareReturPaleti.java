package my.logon.screen.screens;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import android.widget.ListPopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanPersoanaContact;
import my.logon.screen.dialogs.MapAddressDialogF4;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.listeners.MapListener;
import my.logon.screen.model.ClientReturListener;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsGeneral;

public class DateLivrareReturPaleti extends Fragment implements OnItemClickListener, OnTouchListener, MapListener {

	ClientReturListener clientListener;
	Spinner spinnerTransport, spinnerDataRetur, spinnerAdresaRetur, spinnerJudet;
	String[] arrayTipTransport = { "Selectati tip transport", "TRAP - Transport Arabesque", "TCLI - Transport client" };
	LinearLayout layoutAdresaNoua;
	private List<BeanAdresaLivrare> listAdrese;
	private ListPopupWindow lpw;

	private EditText textOras, textStrada, textPersoana, textTelefon, textObservatii;
	public static String dataRetur, tipTransport, adresaCodJudet = "", adresaOras = "", adresaStrada = "", numePersContact = "",
			telPersContact = "", adresaCodAdresa = "", observatii = "";
	private String[] arrayPersoane, arrayTelefoane;

	private Button btnPozitieAdresa;

	String[] judete = { "ALBA", "ARAD", "ARGES", "BACAU", "BIHOR", "BISTRITA-NASAUD", "BOTOSANI", "BRAILA", "BRASOV", "BUCURESTI", "BUZAU",
			"CALARASI", "CARAS-SEVERIN", "CLUJ", "CONSTANTA", "COVASNA", "DAMBOVITA", "DOLJ", "GALATI", "GIURGIU", "GORJ", "HARGHITA", "HUNEDOARA",
			"IALOMITA", "IASI", "ILFOV", "MARAMURES", "MEHEDINTI", "MURES", "NEAMT", "OLT", "PRAHOVA", "SALAJ", "SATU-MARE", "SIBIU", "SUCEAVA",
			"TELEORMAN", "TIMIS", "TULCEA", "VALCEA", "VASLUI", "VRANCEA" };

	String[] codJudete = { "01", "02", "03", "04", "05", "06", "07", "09", "08", "40", "10", "51", "11", "12", "13", "14", "15", "16", "17", "52",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31", "30", "32", "33", "34", "35", "36", "38", "37", "39" };

	public static boolean isAltaAdresa = false;

	public static EnumTipComanda tipCmdRetur = EnumTipComanda.DISTRIBUTIE;
	public static EnumTipClientIP tipClientIP;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.date_livrare_retur_marfa, container, false);

		spinnerDataRetur = (Spinner) v.findViewById(R.id.spinnerDataRetur);
		populateSpinnerDataRetur();
		setListenerSpinnerData();

		spinnerTransport = (Spinner) v.findViewById(R.id.spinnerTranspRetur);
		populateSpinnerTransport();
		setListenerSpinnerTransport();



		spinnerAdresaRetur = (Spinner) v.findViewById(R.id.spinnerAdresaRetur);
		setSpinnerAdresaListener();
		layoutAdresaNoua = (LinearLayout) v.findViewById(R.id.layoutAdresa);
		layoutAdresaNoua.setVisibility(View.GONE);

		spinnerJudet = (Spinner) v.findViewById(R.id.spinnerJudet);
		setSpinnerJudetListener();
		textOras = (EditText) v.findViewById(R.id.textOras);
		setTextOrasListener();
		textStrada = (EditText) v.findViewById(R.id.textStrada);
		setTextStradaListener();

		textPersoana = (EditText) v.findViewById(R.id.textPersoana);
		textPersoana.setOnTouchListener(this);
		setTextPersoanaListener();
		lpw = new ListPopupWindow(getActivity());
		lpw.setOnItemClickListener(this);

		textTelefon = (EditText) v.findViewById(R.id.textTelefon);
		setTextTelefonListener();

		textObservatii = (EditText) v.findViewById(R.id.textObservatii);
		setTextObservatiiListener();

		btnPozitieAdresa = (Button) v.findViewById(R.id.btnPozitieAdresa);
		setListnerBtnPozitieAdresa();

		return v;

	}

	private void populateSpinnerTransport() {
		ArrayAdapter<String> adapterSpinnerTransp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayTipTransport);
		adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTransport.setAdapter(adapterSpinnerTransp);
	}

	private void setListenerSpinnerTransport() {
		spinnerTransport.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0)
					tipTransport = UtilsGeneral.getTipTransport(spinnerTransport.getSelectedItem().toString());
				else
					tipTransport = "";
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void setListnerBtnPozitieAdresa() {
		btnPozitieAdresa.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Address address = new Address();

				address.setCity(textOras.getText().toString().trim());
				address.setStreet(textStrada.getText().toString().trim());
				address.setSector(UtilsGeneral.getNumeJudet(adresaCodJudet));

				if (!isAdresaComplet())
					return;

				FragmentManager fm = getFragmentManager();

				MapAddressDialogF4 mapDialog = new MapAddressDialogF4(address, getActivity(), fm);

				mapDialog.setMapListener(DateLivrareReturPaleti.this);
				mapDialog.show();
			}
		});
	}

	private boolean isAdresaComplet() {
		if (spinnerJudet.getSelectedItemPosition() == 0) {
			Toast.makeText(getActivity(), "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (textOras.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "Completati localitatea", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void populateSpinnerDataRetur() {
		ArrayAdapter<String> adapterDataRetur = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
		adapterDataRetur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapterDataRetur.add("Selectati data retur");
		adapterDataRetur.add("Astazi");

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
		Calendar c1 = Calendar.getInstance();

		for (int i = 0; i < 4; i++) {
			c1.add(Calendar.DAY_OF_MONTH, 1);
			Date resultdate = new Date(c1.getTimeInMillis());
			adapterDataRetur.add(sdf.format(resultdate));
		}

		spinnerDataRetur.setAdapter(adapterDataRetur);
	}

	private void setListenerSpinnerData() {
		spinnerDataRetur.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0)
					dataRetur = UtilsGeneral.getDate(spinnerDataRetur.getSelectedItemPosition() - 1);
				else
					dataRetur = "";
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}



	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			clientListener = (ClientReturListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	private void setSpinnerAdresaListener() {
		spinnerAdresaRetur.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == spinnerAdresaRetur.getAdapter().getCount() - 1) {
					adresaCodJudet = "";
					adresaOras = "";
					adresaStrada = "";
					adresaCodAdresa = " ";
					textOras.setText("");
					textStrada.setText("");
					fillSpinnerJudete();
					layoutAdresaNoua.setVisibility(View.VISIBLE);
					isAltaAdresa = true;
				} else {
					setDateAdresa(position);
					layoutAdresaNoua.setVisibility(View.GONE);
					isAltaAdresa = false;
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void fillSpinnerJudete() {

		ArrayList<HashMap<String, String>> listJudete = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterJudete = new SimpleAdapter(getActivity(), listJudete, R.layout.rowlayoutjudete,
				new String[] { "numeJudet", "codJudet" }, new int[] { R.id.textNumeJudet, R.id.textCodJudet });

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("numeJudet", "Selectat judetul");
		map.put("codJudet", null);
		listJudete.add(map);

		for (int i = 0; i < codJudete.length; i++) {
			map = UtilsGeneral.newHashMapInstance();
			map.put("numeJudet", judete[i]);
			map.put("codJudet", codJudete[i]);
			listJudete.add(map);
		}

		spinnerJudet.setAdapter(adapterJudete);

	}

	private void setTextOrasListener() {
		textOras.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				adresaOras = textOras.getText().toString().trim();
			}
		});
	}

	private void setTextStradaListener() {
		textStrada.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				adresaStrada = textStrada.getText().toString().trim();
			}
		});
	}

	private void setTextPersoanaListener() {
		textPersoana.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				numePersContact = textPersoana.getText().toString().trim();
			}
		});
	}

	private void setTextTelefonListener() {
		textTelefon.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				telPersContact = textTelefon.getText().toString().trim();

			}
		});
	}

	private void setTextObservatiiListener() {
		textObservatii.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				observatii = textObservatii.getText().toString().trim();

			}
		});
	}

	private void setSpinnerJudetListener() {
		spinnerJudet.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) parent.getAdapter().getItem(position);
					adresaCodJudet = map.get("codJudet");
				} else
					adresaCodJudet = "";
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setDateAdresa(int position) {
		for (int i = 0; i < listAdrese.size(); i++) {
			if (i == position) {
				adresaCodJudet = listAdrese.get(i).getCodJudet();
				adresaOras = listAdrese.get(i).getOras();
				adresaStrada = listAdrese.get(i).getStrada() + " " + listAdrese.get(i).getNrStrada();
				adresaCodAdresa = listAdrese.get(i).getCodAdresa();
				break;
			}
		}
	}

	private void populateListAdrese() {
		List<String> strListAdrese = new ArrayList<String>();

		for (BeanAdresaLivrare adresa : listAdrese) {
			strListAdrese.add(UtilsGeneral.getNumeJudet(adresa.getCodJudet()) + ", " + adresa.getOras() + ", " + adresa.getStrada() + " "
					+ adresa.getNrStrada());
		}
		strListAdrese.add("Alta adresa");

		String[] adreseArray = strListAdrese.toArray(new String[strListAdrese.size()]);
		spinnerAdresaRetur.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adreseArray));
	}

	public void setListAdreseLivrare(List<BeanAdresaLivrare> listAdrese) {
		this.listAdrese = listAdrese;
		populateListAdrese();
	}

	public void setPersoaneContact(List<BeanPersoanaContact> listPersoane) {

		if (listPersoane.size() > 0) {
			arrayPersoane = new String[listPersoane.size()];
			for (int i = 0; i < listPersoane.size(); i++)
				arrayPersoane[i] = listPersoane.get(i).getNume();

			arrayTelefoane = new String[listPersoane.size()];
			for (int i = 0; i < listPersoane.size(); i++)
				arrayTelefoane[i] = listPersoane.get(i).getTelefon();

			lpw.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayPersoane));
			lpw.setAnchorView(textPersoana);
			lpw.setModal(true);

			textPersoana.setText(arrayPersoane[0]);

			if (arrayTelefoane.length > 0)
				textTelefon.setText(arrayTelefoane[0]);

		}
	}

	public static void setTipComandaRetur(EnumTipComanda tipComandaRetur) {
		tipCmdRetur = tipComandaRetur;
	}

	public static void setTipClientIP(EnumTipClientIP paramClientIP) {
		tipClientIP = paramClientIP;
	}

	private void setAdresaLivrare(Address address) {

		textOras.setText(address.getCity());

		String strStrada = address.getStreet().trim();

		if (address.getNumber() != null && address.getNumber().length() > 0)
			strStrada += " nr. " + address.getNumber();

		textStrada.setText(strStrada);

	}

	public static DateLivrareReturPaleti newInstance() {
		DateLivrareReturPaleti frg = new DateLivrareReturPaleti();
		Bundle bdl = new Bundle();
		frg.setArguments(bdl);
		return frg;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		textPersoana.setText(arrayPersoane[position]);
		textTelefon.setText(arrayTelefoane[position]);
		lpw.dismiss();

	}

	public boolean onTouch(View v, MotionEvent event) {
		final int DRAWABLE_RIGHT = 2;

		try {

			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (event.getX() >= (v.getWidth() - ((EditText) v).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
					lpw.show();
					return true;
				}
			}
		} catch (Exception ex) {

		}
		return false;
	}

	public void addressSelected(LatLng coord, android.location.Address address) {
		setAdresaLivrare(MapUtils.getAddress(address));

	}

}
