package my.logon.screen.screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareClientiAdapter;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.ClientReturListener;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class ClientReturComanda extends Fragment implements OperatiiClientListener {

	private OperatiiClient opClient;
	private EditText textNumeClient;
	private ListView clientiList;
	private TextView selectIcon;
	private Spinner spinnerCautare;
	private EnumTipComanda tipComanda;
	private RadioGroup radioTipCmd;


	private ClientReturListener clientListener;

	private enum EnumTipCautare {
		NUME, TELEFON
	};

	private EnumTipCautare enumTipCautare = EnumTipCautare.NUME;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.client_retur_comanda, container, false);

		opClient = new OperatiiClient(getActivity());
		opClient.setOperatiiClientListener(this);

		textNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
		textNumeClient.setHint("Introduceti nume client");

		Button searchClient = (Button) v.findViewById(R.id.clientBtn);
		addListenerClient(searchClient);

		clientiList = (ListView) v.findViewById(R.id.listClienti);
		addListenerClientiList();

		selectIcon = (TextView) v.findViewById(R.id.selectIcon);
		selectIcon.setVisibility(View.INVISIBLE);

		tipComanda = EnumTipComanda.DISTRIBUTIE;
		spinnerCautare = (Spinner) v.findViewById(R.id.spinnerCautare);
		spinnerCautare.setVisibility(View.GONE);
		setSpinnerCautareItems();
		setSpinnerCautareListener();

		radioTipCmd = (RadioGroup) v.findViewById(R.id.radioTipCmd);
		setTipCmdRadioListener();

		if (!UtilsUser.isCV() && !UtilsUser.isUserIP()) {
			tipComanda = EnumTipComanda.DISTRIBUTIE;
			radioTipCmd.setVisibility(View.VISIBLE);

		} else {
			tipComanda = EnumTipComanda.GED;
			spinnerCautare.setVisibility(View.VISIBLE);
		}

		return v;

	}

	private void setTipCmdRadioListener(){
		radioTipCmd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				selectIcon.setVisibility(View.INVISIBLE);
				switch (checkedId) {
					case R.id.radioDistrib:
						textNumeClient.setText("");
						clientiList.setAdapter(null);
						tipComanda = EnumTipComanda.DISTRIBUTIE;
						spinnerCautare.setVisibility(View.GONE);
						break;
					case R.id.radioGed:
						textNumeClient.setText("");
						clientiList.setAdapter(null);
						tipComanda = EnumTipComanda.GED;
						spinnerCautare.setVisibility(View.VISIBLE);
						break;
				}

			}

		});
	}

	private void setSpinnerCautareItems() {

		String[] criteriuCautare = { "Nume", "Telefon" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, criteriuCautare);
		spinnerCautare.setAdapter(adapter);

	}

	private void setSpinnerCautareListener() {
		spinnerCautare.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String criteriu = spinnerCautare.getSelectedItem().toString().toLowerCase();
				textNumeClient.setHint("Introduceti " + criteriu + " client");
				textNumeClient.setText("");
				clientiList.setAdapter(new CautareClientiAdapter(getActivity(), new ArrayList<BeanClient>()));
				selectIcon.setVisibility(View.INVISIBLE);

				if (criteriu.equalsIgnoreCase("nume"))
					enumTipCautare = EnumTipCautare.NUME;
				else if (criteriu.equalsIgnoreCase("telefon"))
					enumTipCautare = EnumTipCautare.TELEFON;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			clientListener = (ClientReturListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString());
		}
	}

	public static ClientReturComanda newInstance() {
		ClientReturComanda frg = new ClientReturComanda();
		Bundle bdl = new Bundle();
		frg.setArguments(bdl);
		return frg;
	}

	private void addListenerClient(Button clientButton) {
		clientButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getListClienti();
			}
		});
	}

	void getListClienti() {

		if (hasText(textNumeClient)) {

			String numeClientCautare = textNumeClient.getText().toString().trim();

			if (enumTipCautare == EnumTipCautare.TELEFON)
				numeClientCautare = "tel:" + numeClientCautare;

			clearScreen();
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("numeClient", numeClientCautare);
			params.put("depart", "00");
			params.put("departAg", UserInfo.getInstance().getCodDepart());
			params.put("unitLog", UserInfo.getInstance().getUnitLog());
			params.put("tipCmd","CMD");
			params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

			if (UtilsUser.isUserGed() || tipComanda == EnumTipComanda.GED)
				opClient.getListClientiCV(params);
			else
				opClient.getListClienti(params);

			hideSoftKeyboard();

		}

	}

	private void clearScreen() {
		clientiList.setAdapter(new CautareClientiAdapter(getActivity(), new ArrayList<BeanClient>()));
		selectIcon.setVisibility(View.INVISIBLE);
	}

	private void addListenerClientiList() {
		clientiList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				BeanClient client = (BeanClient) arg0.getAdapter().getItem(arg2);
				if (client != null) {
					clientListener.clientSelected(client.getCodClient(), client.getNumeClient(), "", tipComanda);

				}
			}
		});
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private boolean hasText(EditText editText) {
		return editText.getText().toString().trim().length() > 0 ? true : false;
	}

	private void populateListViewClient(List<BeanClient> listClienti) {

		if (listClienti.size() > 0) {
			selectIcon.setVisibility(View.VISIBLE);
			CautareClientiAdapter adapterClienti = new CautareClientiAdapter(getActivity(), listClienti);
			clientiList.setAdapter(adapterClienti);
		}

	}

	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_LISTA_CLIENTI:
		case GET_LISTA_CLIENTI_CV:
			populateListViewClient(opClient.deserializeListClienti((String) result));
			break;
		default:
			break;
		}

	}

}
