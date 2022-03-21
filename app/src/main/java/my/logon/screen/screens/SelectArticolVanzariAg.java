/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;

import my.logon.screen.listeners.ArticolSelection;
import my.logon.screen.listeners.ArticolSelectionListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.model.VanzariAgenti;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.ToggleButton;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.enums.EnumArticoleDAO;

public class SelectArticolVanzariAg extends Fragment implements ArticolSelectionListener, OperatiiArticolListener {

	SlidingDrawer slidingDrawerArt;
	private EditText txtNumeArticol;
	LinearLayout layoutArtHeader, layoutArtDet;
	Button articoleBtn;
	ToggleButton tglButton, tglTipArtBtn;
	private static ArrayList<HashMap<String, String>> listArticole = null;
	private ArrayList<HashMap<String, String>> tempSelArt = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterArticole, adapterSelectedArt;
	ListView listViewArticole;
	String numeArticol = "", codArticol = "", codArtDel = "", numeArtDel = "";
	private TextView textNumeArticol, textCodArticol;
	Button saveArtBtnVanz;
	ImageButton deleteArtBtn, deleteAllArtBtn;
	ListView listViewSelArt;
	LinearLayout selArtLayout;
	View selectedItemView;

	private ArticolSelection articolSelected = new ArticolSelection();
	OperatiiArticol opArticol;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_articol_vanzari_ag, container, false);

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", getActivity());
		opArticol.setListener(this);

		try {

			selectedItemView = new View(getActivity());

			txtNumeArticol = (EditText) v.findViewById(R.id.txtNumeArtVanz);

			this.selArtLayout = (LinearLayout) v.findViewById(R.id.articoleData);

			this.slidingDrawerArt = (SlidingDrawer) v.findViewById(R.id.articoleSlidingDrawer);

			addDrawerListener();

			articolSelected.setArticolSelectionListener(this);

			this.articoleBtn = (Button) v.findViewById(R.id.articoleBtnVanz);
			addListenerBtnArticole();

			this.deleteArtBtn = (ImageButton) v.findViewById(R.id.deleteArtBtn);
			this.deleteArtBtn.setVisibility(View.INVISIBLE);
			addListenerDeleteArtBtn();

			this.deleteAllArtBtn = (ImageButton) v.findViewById(R.id.deleteAllArtBtn);
			this.deleteAllArtBtn.setVisibility(View.INVISIBLE);
			addListenerDeleteAllArtBtn();

			this.tglButton = (ToggleButton) v.findViewById(R.id.togglebuttonVanz);
			addListenerToggle();
			this.tglButton.setChecked(true);

			this.tglTipArtBtn = (ToggleButton) v.findViewById(R.id.tglTipArtVanz);
			addListenerTglTipArtBtn();

			listArticole = new ArrayList<HashMap<String, String>>();
			adapterArticole = new SimpleAdapter(getActivity(), listArticole, R.layout.customrownumearticol,
					new String[] { "numeArticol", "codArticol", "umVanz", "codDepart", "tipArt" }, new int[] {
							R.id.textNumeArticol, R.id.textCodArticol, R.id.textUmVanz, R.id.textCodDepart,
							R.id.textTipArt });

			this.listViewArticole = (ListView) v.findViewById(R.id.listArticoleVanz);
			listViewArticole.setAdapter(adapterArticole);
			listViewArticole.setOnItemClickListener(articolSelected);

			textNumeArticol = (TextView) v.findViewById(R.id.textNumeArticolVanz);
			textNumeArticol.setVisibility(View.INVISIBLE);
			textCodArticol = (TextView) v.findViewById(R.id.textCodArticolVanz);
			textCodArticol.setVisibility(View.INVISIBLE);

			this.saveArtBtnVanz = (Button) v.findViewById(R.id.saveArtBtnVanz);
			this.saveArtBtnVanz.setVisibility(View.INVISIBLE);
			addListenerBtnSaveArticole();

			listViewSelArt = (ListView) v.findViewById(R.id.listViewSelArt);
			listViewSelArt.setVisibility(View.INVISIBLE);

			adapterSelectedArt = new SimpleAdapter(getActivity(), tempSelArt, R.layout.customrownumearticol,
					new String[] { "numeArticol", "codArticol", "umVanz", "codDepart", "tipArt" }, new int[] {
							R.id.textNumeArticol, R.id.textCodArticol, R.id.textUmVanz, R.id.textCodDepart,
							R.id.textTipArt });

			listViewSelArt.setOnItemClickListener(articolSelected);

			fillSelArtList();

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
		}

		return v;
	}

	public void addDrawerListener() {
		slidingDrawerArt.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

				selArtLayout.setVisibility(View.GONE);

			}
		});

		slidingDrawerArt.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {

				selArtLayout.setVisibility(View.VISIBLE);
				fillSelArtList();

				// hide keyb
				InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

			}
		});

	}

	private void fillSelArtList() {

		try {

			if (VanzariAgenti.getInstance().articolListCode.size() == 0) {
				this.deleteArtBtn.setVisibility(View.INVISIBLE);
				this.deleteAllArtBtn.setVisibility(View.INVISIBLE);
				listViewSelArt.setVisibility(View.INVISIBLE);
			} else {
				this.deleteArtBtn.setVisibility(View.VISIBLE);
				this.deleteAllArtBtn.setVisibility(View.VISIBLE);
				listViewSelArt.setVisibility(View.VISIBLE);
			}

			tempSelArt.clear();

			HashMap<String, String> tempArt;
			for (int i = 0; i < VanzariAgenti.getInstance().articolListCode.size(); i++) {
				tempArt = new HashMap<String, String>();

				tempArt.put("numeArticol", VanzariAgenti.getInstance().articolListName.get(i));
				tempArt.put("codArticol", VanzariAgenti.getInstance().articolListCode.get(i));
				tempSelArt.add(tempArt);

			}

			listViewSelArt.setAdapter(adapterSelectedArt);

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
		}

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

	protected void performGetArticole() {

		try {

			String numeArticol = txtNumeArticol.getText().toString().trim().replace('*', '%');

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
			params.put("departament", UserInfo.getInstance().getCodDepart());

			opArticol.getArticoleDistributie(params);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void populateListViewArticol(List<ArticolDB> listArt) {

		listArticole.clear();
		txtNumeArticol.setText("");

		if (listArt.size() > 0) {
			HashMap<String, String> temp;

			Iterator<ArticolDB> iterator = listArt.iterator();
			ArticolDB articol;

			while (iterator.hasNext()) {

				articol = iterator.next();

				temp = new HashMap<String, String>();
				temp.put("numeArticol", articol.getNume());
				temp.put("codArticol", articol.getCod());
				temp.put("umVanz", articol.getUmVanz());
				temp.put("codDepart", articol.getDepart());
				temp.put("tipArt", " ");
				listArticole.add(temp);

			}

			adapterArticole.notifyDataSetChanged();

		} else {
			Toast.makeText(getActivity(), "Nu exista articole!", Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
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
		tglTipArtBtn.setOnClickListener(new View.OnClickListener() {
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

	public void addListenerBtnSaveArticole() {
		saveArtBtnVanz.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

				try {

					if (!numeArticol.equals("")) {

						// doar articole sau sintetice, nu impreuna
						if (tglTipArtBtn.isChecked()) {
							if (vanzariInstance.tipArticol.equals("A")) {
								vanzariInstance.articolListCode.clear();
								vanzariInstance.articolListName.clear();

							}
							vanzariInstance.tipArticol = "S";
						} else {
							if (vanzariInstance.tipArticol.equals("S")) {
								vanzariInstance.articolListCode.clear();
								vanzariInstance.articolListName.clear();

							}
							vanzariInstance.tipArticol = "A";
						}

						// verificare existenta
						boolean isSelected = false;
						for (int i = 0; i < vanzariInstance.articolListCode.size(); i++) {
							if (vanzariInstance.articolListCode.get(i).equals(codArticol)) {
								isSelected = true;
								break;
							}
						}
						// sf. verificare

						if (!isSelected) {

							vanzariInstance.articolListName.add(numeArticol);
							vanzariInstance.articolListCode.add(codArticol);

							textNumeArticol.setText("");
							textCodArticol.setText("");

							saveArtBtnVanz.setVisibility(View.INVISIBLE);
						} else {
							Toast.makeText(SelectArticolVanzariAg.this.getActivity(), "Articol selectat deja!",
									Toast.LENGTH_SHORT).show();
						}

					}

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerDeleteArtBtn() {
		deleteArtBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

					if (!codArtDel.equals("")) {
						for (int i = 0; i < vanzariInstance.articolListCode.size(); i++) {
							if (vanzariInstance.articolListCode.get(i).equals(codArtDel)) {
								vanzariInstance.articolListCode.remove(i);
								break;
							}
						}

						for (int i = 0; i < vanzariInstance.articolListName.size(); i++) {
							if (vanzariInstance.articolListName.get(i).equals(numeArtDel)) {
								vanzariInstance.articolListName.remove(i);
								break;
							}
						}

						fillSelArtList();
					}

				} catch (Exception ex) {
					Toast.makeText(SelectArticolVanzariAg.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	public void addListenerDeleteAllArtBtn() {
		deleteAllArtBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

					vanzariInstance.articolListCode.clear();
					vanzariInstance.articolListName.clear();

					tempSelArt.clear();
					listViewSelArt.setAdapter(adapterSelectedArt);

					fillSelArtList();

				} catch (Exception ex) {
					Toast.makeText(SelectArticolVanzariAg.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	public void selectedArticol(View parentView, View view, String codArticol, String numeArticol, String umVanz10) {

		// adaugare
		if (parentView.getId() == R.id.listArticoleVanz) {
			this.numeArticol = numeArticol;
			this.codArticol = codArticol;

			textNumeArticol.setVisibility(View.VISIBLE);
			textCodArticol.setVisibility(View.VISIBLE);

			textNumeArticol.setText(this.numeArticol);
			textCodArticol.setText(this.codArticol);

			saveArtBtnVanz.setVisibility(View.VISIBLE);
		}

		// stergere
		if (parentView.getId() == R.id.listViewSelArt) {
			selectedItemView.setBackgroundResource(0);
			view.setBackgroundResource(R.color.pressed_item_color);
			selectedItemView = view;

			numeArtDel = numeArticol;
			codArtDel = codArticol;

		}

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
