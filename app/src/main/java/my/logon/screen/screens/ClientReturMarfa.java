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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class ClientReturMarfa extends Fragment implements OperatiiClientListener {

	private OperatiiClient opClient;
	private EditText textNumeClient;
	private ListView clientiList;
	private TextView selectIcon;
	private RadioGroup groupTipDistrib;
	private RadioButton radioDistrib, radioGed;
	private EnumTipComanda tipComanda;
	

	ClientReturListener clientListener;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.client_retur_marfa, container, false);

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

		radioDistrib = (RadioButton) v.findViewById(R.id.radioDistrib);
		radioGed = (RadioButton) v.findViewById(R.id.radioGed);

		tipComanda = EnumTipComanda.DISTRIBUTIE;
		addRadioDistribListener();
		addRadioGedListener();

		groupTipDistrib = (RadioGroup) v.findViewById(R.id.groupTipDistrib);

		if (!UtilsUser.isCV())
			groupTipDistrib.setVisibility(View.VISIBLE);
		else {
			groupTipDistrib.setVisibility(View.GONE);
		}

		return v;

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

	public static ClientReturMarfa newInstance() {
		ClientReturMarfa frg = new ClientReturMarfa();
		Bundle bdl = new Bundle();
		frg.setArguments(bdl);
		return frg;
	}

	private void addRadioDistribListener() {
		radioDistrib.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tipComanda = EnumTipComanda.DISTRIBUTIE;
					DateLivrareReturPaleti.setTipComandaRetur(tipComanda);
					clearSelection();
				}

			}
		});
	}

	private void addRadioGedListener() {
		radioGed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					tipComanda = EnumTipComanda.GED;
					DateLivrareReturPaleti.setTipComandaRetur(tipComanda);
					clearSelection();
				}

			}
		});
	}

	private void clearSelection() {
		textNumeClient.setText("");
		clientiList.setAdapter(new CautareClientiAdapter(getActivity(), new ArrayList<BeanClient>()));
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

			clearScreen();
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("numeClient", textNumeClient.getText().toString().trim());
			params.put("depart", "00");
			params.put("departAg", UserInfo.getInstance().getCodDepart());
			params.put("unitLog", UserInfo.getInstance().getUnitLog());
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
					DateLivrareReturPaleti.setTipClientIP(client.getTipClientIP());
					clientListener.clientSelected(client.getCodClient(), client.getNumeClient(), null, tipComanda);

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
