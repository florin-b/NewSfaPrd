/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import my.logon.screen.R;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.listeners.ClientSelection;
import my.logon.screen.listeners.ClientSelectionListener;
import my.logon.screen.listeners.SearchClientListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;

public class SelectClientNeincasate extends Fragment implements Observer, ClientSelectionListener {

	private static EditText txtNumeClient;

	private Button cautaClientBtn, saveClntBtn;

	private static ArrayList<HashMap<String, String>> listClienti = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> tempSelClnt = new ArrayList<HashMap<String, String>>();

	public SimpleAdapter adapterClienti, adapterJudete, adapterFiliale, adapterSelectedClient;

	private ListView listViewClienti, listViewSelClient;
	private LinearLayout layoutClient;

	String codClient = "", numeClient = "", numeClientDel = "", codClientDel = "";

	private TextView codClientText;
	private TextView numeClientText;

	static final int DATE_DIALOG_ID = 1;

	LinearLayout selClientLayout;
	SlidingDrawer slidingDrawer;

	View selectedItemView;
	ImageButton deleteClientBtn, deleteAllClntBtn;

	SearchClientListener searchClientListener = new SearchClientListener();;
	ClientSelection clientSelected = new ClientSelection();

	public static final SelectClientNeincasate newInstance() {

		SelectClientNeincasate f = new SelectClientNeincasate();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_client_neincasate, container, false);

		try {

			txtNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
			txtNumeClient.setHint("Introduceti nume client");

			this.cautaClientBtn = (Button) v.findViewById(R.id.clientBtn);
			addListenerCautaClient();

			clientSelected.setClientSelectionListener(this);

			selectedItemView = new View(getActivity());

			listClienti = new ArrayList<HashMap<String, String>>();
			adapterClienti = new SimpleAdapter(getActivity(), listClienti, R.layout.customrownumeclient, new String[] {
					"numeClient", "codClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient });

			listViewClienti = (ListView) v.findViewById(R.id.listClienti);
			listViewClienti.setOnItemClickListener(clientSelected);
			listViewClienti.setVisibility(View.INVISIBLE);

			this.slidingDrawer = (SlidingDrawer) v.findViewById(R.id.clientSlidingDrawer);
			addDrawerListener();

			this.layoutClient = (LinearLayout) v.findViewById(R.id.layoutClient);
			layoutClient.setVisibility(View.INVISIBLE);

			codClientText = (TextView) v.findViewById(R.id.textCodClient);
			numeClientText = (TextView) v.findViewById(R.id.textNumeClient);

			this.saveClntBtn = (Button) v.findViewById(R.id.saveClntBtn);

			this.selClientLayout = (LinearLayout) v.findViewById(R.id.clientData);

			addListenerSaveClient();

			this.deleteClientBtn = (ImageButton) v.findViewById(R.id.deleteClientBtn);
			this.deleteClientBtn.setVisibility(View.INVISIBLE);
			addListenerDeleteClientBtn();

			this.deleteAllClntBtn = (ImageButton) v.findViewById(R.id.deleteAllClntBtn);
			this.deleteAllClntBtn.setVisibility(View.INVISIBLE);
			addListenerDeleteAllClientBtn();

			adapterSelectedClient = new SimpleAdapter(getActivity(), tempSelClnt, R.layout.customrownumeclient,
					new String[] { "numeClient", "codClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient });

			listViewSelClient = (ListView) v.findViewById(R.id.listViewSelClient);
			listViewSelClient.setVisibility(View.INVISIBLE);
			listViewSelClient.setOnItemClickListener(new MyOnSelectedClientListener());
			fillSelClientList();

			searchClientListener.addObserver(this);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	public void addDrawerListener() {

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

				selClientLayout.setVisibility(View.GONE);

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				selClientLayout.setVisibility(View.VISIBLE);
				fillSelClientList();
			}
		});

	}

	public void addListenerCautaClient() {
		cautaClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeClient.length() > 0) {
						performListClients();
					} else {
						Toast.makeText(SelectClientNeincasate.this.getActivity(), "Introduceti nume client!",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(SelectClientNeincasate.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	private void fillSelClientList() {

		if (Neincasate.clientListCode.size() == 0) {
			this.deleteClientBtn.setVisibility(View.INVISIBLE);
			this.deleteAllClntBtn.setVisibility(View.INVISIBLE);
			listViewSelClient.setVisibility(View.INVISIBLE);
		} else {
			this.deleteClientBtn.setVisibility(View.VISIBLE);
			this.deleteAllClntBtn.setVisibility(View.VISIBLE);
			listViewSelClient.setVisibility(View.VISIBLE);
		}

		tempSelClnt.clear();

		HashMap<String, String> tempCl;
		for (int i = 0; i < Neincasate.clientListName.size(); i++) {
			tempCl = new HashMap<String, String>();

			tempCl.put("numeClient", Neincasate.clientListName.get(i));
			tempCl.put("codClient", Neincasate.clientListCode.get(i));
			tempSelClnt.add(tempCl);

		}

		listViewSelClient.setAdapter(adapterSelectedClient);

	}

	private void performListClients() {
		try {

			String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

			searchClientListener.performSearchResults(getActivity(), numeClient, UserInfo.getInstance().getCodDepart(),
					UserInfo.getInstance().getCodDepart(), UserInfo.getInstance().getUnitLog());

		} catch (Exception e) {
			Toast.makeText(this.getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void populateListViewClient(String clientResponse) {

		HandleJSONData objClientList = new HandleJSONData(getActivity(), clientResponse);
		ArrayList<BeanClient> clientArray = objClientList.decodeJSONClientList();

		if (clientArray.size() > 0) {
			HashMap<String, String> temp;

			for (int i = 0; i < clientArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("numeClient", clientArray.get(i).getNumeClient());
				temp.put("codClient", clientArray.get(i).getCodClient());
				listClienti.add(temp);
			}

			listViewClienti.setVisibility(View.VISIBLE);
			listViewClienti.setAdapter(adapterClienti);

		} else {
			Toast.makeText(getActivity(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

		}

	}

	public class MyOnSelectedClientListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			selectedItemView.setBackgroundResource(0);
			v.setBackgroundResource(R.color.pressed_item_color);
			selectedItemView = v;

			try {

				HashMap<String, String> artMap = (HashMap<String, String>) adapterSelectedClient.getItem(pos);

				numeClientDel = artMap.get("numeClient");
				codClientDel = artMap.get("codClient");

			}

			catch (Exception ex) {
				Toast.makeText(SelectClientNeincasate.this.getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO
		}
	}

	public void addListenerSaveClient() {
		saveClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClient.length() == 0) {
					Toast.makeText(getActivity(), "Selectati un client!", Toast.LENGTH_SHORT).show();
				} else {

					// verificare existenta
					boolean isSelected = false;
					for (int i = 0; i < Neincasate.clientListName.size(); i++) {
						if (Neincasate.clientListCode.get(i).equals(codClient)) {
							isSelected = true;
							break;
						}
					}
					// sf. verificare

					if (!isSelected) {

						Neincasate.clientListName.add(numeClient);
						Neincasate.clientListCode.add(codClient);

						numeClientText.setText("");
						codClientText.setText("");

						saveClntBtn.setVisibility(View.GONE);
					} else {
						Toast.makeText(SelectClientNeincasate.this.getActivity(), "Client selectat deja!",
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		});

	}

	public void addListenerDeleteClientBtn() {
		deleteClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					if (!codClientDel.equals("")) {
						for (int i = 0; i < Neincasate.clientListCode.size(); i++) {
							if (Neincasate.clientListCode.get(i).equals(codClientDel)) {
								Neincasate.clientListCode.remove(i);
								break;
							}
						}

						for (int i = 0; i < Neincasate.clientListName.size(); i++) {
							if (Neincasate.clientListName.get(i).equals(numeClientDel)) {
								Neincasate.clientListName.remove(i);
								break;
							}
						}

						fillSelClientList();
					}

				} catch (Exception ex) {
					Toast.makeText(SelectClientNeincasate.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	public void addListenerDeleteAllClientBtn() {
		deleteAllClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					Neincasate.clientListCode.clear();
					Neincasate.clientListName.clear();

					tempSelClnt.clear();
					listViewSelClient.setAdapter(adapterSelectedClient);

					fillSelClientList();

				} catch (Exception ex) {
					Toast.makeText(SelectClientNeincasate.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	public void update(Observable observable, Object data) {
		if (searchClientListener != null) {
			if (searchClientListener.getSearchResult().length() > 0) {
				listClienti.clear();
				adapterClienti.notifyDataSetChanged();

				populateListViewClient(searchClientListener.getSearchResult());

				InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtNumeClient.getWindowToken(), 0);
				txtNumeClient.setText("");
			}
		}

	}

	public void selectedClient(String codClient, String numeClient, View listView, View parentView) {
		this.numeClient = numeClient;
		this.codClient = codClient;

		layoutClient.setVisibility(View.VISIBLE);
		saveClntBtn.setVisibility(View.VISIBLE);

		numeClientText.setText(numeClient);
		codClientText.setText(codClient);

	}

}
