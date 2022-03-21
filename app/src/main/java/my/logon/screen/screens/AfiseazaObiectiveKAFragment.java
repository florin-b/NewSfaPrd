package my.logon.screen.screens;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterObiectiveAfisare;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.beans.BeanStadiuObiectiv;
import my.logon.screen.beans.BeanUrmarireEveniment;
import my.logon.screen.dialogs.OptiuniObiectKaDialog;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumDepartFinisaje;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.enums.EnumTipUser;
import my.logon.screen.enums.EnumUrmarireObiective;
import my.logon.screen.listeners.DialogObiectiveKAListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.model.ViewPagerCustomDuration;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsUser;

public class AfiseazaObiectiveKAFragment extends Fragment implements DialogObiectiveKAListener, ObiectiveListener {

	ViewPagerCustomDuration pager;
	ViewPager viewPager;
	private OptiuniObiectKaDialog optiuniDialog;
	private Spinner spinnerObiective;
	private TextView detaliiText;
	private OperatiiObiective operatiiObiective;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View v = inflater.inflate(R.layout.activity_afis_ob_ka, container, false);
		super.onCreate(savedInstanceState);

		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Obiective");
		actionBar.setDisplayHomeAsUpEnabled(true);

		optiuniDialog = new OptiuniObiectKaDialog(getActivity());
		optiuniDialog.setDialogListener(this);

		operatiiObiective = new OperatiiObiective(getActivity());
		operatiiObiective.setObiectiveListener(this);

		setupLayout(v);

		if ((UserInfo.getInstance().getTipUser().equals(EnumTipUser.DV.getTipAcces())
				|| UserInfo.getInstance().getTipUser().equals(EnumTipUser.DK.getTipAcces()) || UserInfo.getInstance().getTipUser()
				.equals(EnumTipUser.SK.getTipAcces()))
				&& !UtilsUser.isDV_CONS())
			optiuniDialog.show();
		else
			getListObiective();

		return v;

	}

	private void setupLayout(View v) {
		spinnerObiective = (Spinner) v.findViewById(R.id.spinnerObiective);
		spinnerObiective.setVisibility(View.INVISIBLE);
		setSpinnerObiectiveListener();

		detaliiText = (TextView) v.findViewById(R.id.detaliiObiectiv);
		detaliiText.setText("");

	}

	private void getListObiective() {
		operatiiObiective = new OperatiiObiective(getActivity());

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipUser", UserInfo.getInstance().getTipUser());

		operatiiObiective.setObiectiveListener(this);
		operatiiObiective.getListObiectiveAV(params);

	}

	private void setSpinnerObiectiveListener() {
		spinnerObiective.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					BeanObiectivAfisare idObiectiv = (BeanObiectivAfisare) parent.getAdapter().getItem(position);
					getDetaliiObiectiv(idObiectiv);

				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void getDetaliiObiectiv(BeanObiectivAfisare idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv.getId());
		operatiiObiective.getDetaliiObiectiv(params);
	}

	private void displayListObiective(List<BeanObiectivAfisare> listObiective) {

		BeanObiectivAfisare dummyOb = new BeanObiectivAfisare();

		dummyOb.setNume("Selectati un obiectiv");

		dummyOb.setBeneficiar(" ");
		dummyOb.setCodStatus("-1");
		dummyOb.setData("");
		dummyOb.setId(" ");

		listObiective.add(0, dummyOb);

		AdapterObiectiveAfisare adapterOb = new AdapterObiectiveAfisare(getActivity(), listObiective);
		spinnerObiective.setAdapter(adapterOb);

		spinnerObiective.setVisibility(View.VISIBLE);
		detaliiText.setText("");

	}

	private void displayDetaliiOb(BeanObiectiveGenerale detaliiObiectiv) {

		StringBuilder strDetalii = new StringBuilder();
		String departFinisaje = "", client = "";

		String adresa = detaliiObiectiv.getAdresaObiectiv().getNumeJudet() + "/" + detaliiObiectiv.getAdresaObiectiv().getOras();
		String strada = detaliiObiectiv.getAdresaObiectiv().getStrada() != null ? "/" + detaliiObiectiv.getAdresaObiectiv().getStrada() : "";

		adresa += strada;

		strDetalii.append("Adresa :" + UtilsFormatting.addSpace("Adresa :", 30) + adresa + "\n");
		strDetalii.append("Beneficiar :" + UtilsFormatting.addSpace("Beneficiar :", 30) + detaliiObiectiv.getNumeBeneficiar() + "\n");
		strDetalii.append("Antreprenor general :" + UtilsFormatting.addSpace("Antreprenor general :", 30) + detaliiObiectiv.getNumeAntreprenorGeneral() + "\n");
		strDetalii.append("Categorie :" + UtilsFormatting.addSpace("Categorie :", 30) + detaliiObiectiv.getCategorieObiectiv().getNumeCategorie() + "\n");
		strDetalii.append("Valoare :" + UtilsFormatting.addSpace("Valoare :", 30)
				+ UtilsFormatting.format2Decimals(Double.parseDouble(detaliiObiectiv.getValoareObiectiv()), true) + " RON \n");

		strDetalii.append("\n\n");
		strDetalii.append("Stadii:" + "\n");

		List<BeanStadiuObiectiv> listStadii = detaliiObiectiv.getStadiiDepart();

		List<BeanObiectiveConstructori> listConstructori = detaliiObiectiv.getListConstructori();
		List<BeanUrmarireEveniment> listEvenimente = detaliiObiectiv.getListEvenimente();

		Iterator<BeanUrmarireEveniment> iteratorEv = null;

		for (BeanStadiuObiectiv stadiu : listStadii) {

			client = "";

			if (stadiu.getCodDepart().equals("04"))
				departFinisaje = "Fundatie si suprastructura";
			else
				departFinisaje = EnumDepartFinisaje.getNumeDepart(stadiu.getCodDepart());

			strDetalii.append("\n");
			strDetalii.append(departFinisaje + " :" + UtilsFormatting.addSpace(departFinisaje, 35) + stadiu.getNumeStadiu() + "\n");

			for (BeanObiectiveConstructori constructor : listConstructori) {

				if (stadiu.getCodDepart().equals("00")) {

					if (constructor.getCodDepart().equals("03") || constructor.getCodDepart().equals("06") || constructor.getCodDepart().equals("07")) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " ("
								+ EnumDepartExtra.getNumeDepart(constructor.getCodDepart()) + ")  \n";

					}

				} else if (stadiu.getCodDepart().equals("04")) {

					if (constructor.getCodDepart().equals("04")) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " \n";
					}

				} else {

					if (constructor.getCodDepart().equals(stadiu.getCodDepart())) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " \n";
					}

				}

				if (!client.equals("")) {
					strDetalii.append(client);
					client = "";
				}

				iteratorEv = listEvenimente.iterator();
				while (iteratorEv.hasNext()) {
					BeanUrmarireEveniment eveniment = iteratorEv.next();
					if (eveniment.getCodDepart().equals(stadiu.getCodDepart()) && eveniment.getCodClient().equals(constructor.getCodClient())) {
						strDetalii.append(UtilsFormatting.addSpace("", 43) + EnumUrmarireObiective.getNumeEveniment(eveniment.getIdEveniment()) + " :"
								+ UtilsFormatting.addSpace(EnumUrmarireObiective.getNumeEveniment(eveniment.getIdEveniment()), 10) + eveniment.getData()
								+ " Obs: " + eveniment.getObservatii() + " \n");

						iteratorEv.remove();

					}

				}

			}

		}

		detaliiText.setText(strDetalii.toString());

	}

	class ObiectivePagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public ObiectivePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		public int getCount() {
			return this.fragments.size();
		}
	}

	public void selectionComplete(List<BeanObiectivAfisare> listObiective) {
		displayListObiective(listObiective);

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_DETALII_OBIECTIV:
			displayDetaliiOb(operatiiObiective.deserializeDetaliiObiectiv((String) result));
			break;
		case GET_LIST_OBIECTIVE_AV:
			displayListObiective(operatiiObiective.deserializeListaObiective((String) result));
			break;

		default:
			break;
		}

	}

}
