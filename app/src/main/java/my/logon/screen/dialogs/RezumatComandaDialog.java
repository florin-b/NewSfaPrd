package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterRezumatComanda;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.listeners.ComandaMathausListener;
import my.logon.screen.listeners.RezumatListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;

public class RezumatComandaDialog extends Dialog implements RezumatListener {

	private Context context;
	private List<ArticolComanda> listArticole;
	private ListView listViewComenzi;
	private String canalDistrib;
	private Button btnCancelComanda;
	private Button btnOkComanda;
	private ComandaMathausListener listener;
	private List<CostTransportMathaus> costTransport;
	private String tipTransport;
	private String filialeArondate;

	public RezumatComandaDialog(Context context, List<ArticolComanda> listArticole, String canal, List<CostTransportMathaus> costTransport, String tipTransport, String filialeArondate) {
		super(context);
		this.context = context;
		this.listArticole = listArticole;
		this.canalDistrib = canal;
		this.costTransport = costTransport;
		this.tipTransport = tipTransport;
		this.filialeArondate = filialeArondate;

		setContentView(R.layout.rezumat_comanda_dialog);
		setTitle("Rezumat comanda");
		setCancelable(true);

		setupLayout();

	}

	private void setupLayout() {

		listViewComenzi = (ListView) findViewById(R.id.listComenzi);



		AdapterRezumatComanda adapterRezumat = new AdapterRezumatComanda(context, getRezumatComanda(), costTransport, tipTransport, filialeArondate);
		adapterRezumat.setRezumatListener(this);
		listViewComenzi.setAdapter(adapterRezumat);

		btnCancelComanda = (Button) findViewById(R.id.btnCancelComanda);
		setListenerBtnCancel();
		btnOkComanda = (Button) findViewById(R.id.btnOkComanda);
		setListenerComandaSalvata();

	}

	private void setListenerBtnCancel() {
		btnCancelComanda.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private void setListenerComandaSalvata() {
		btnOkComanda.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {

					if (listViewComenzi.getAdapter().getCount() > 0)
						listener.comandaSalvata();

					dismiss();
				}

			}
		});
	}

	private List<RezumatComanda> getRezumatComanda() {

		Set<String> filiale = getFilialeComanda();

		List<RezumatComanda> listComenzi = new ArrayList<RezumatComanda>();

		for (String filiala : filiale) {

			RezumatComanda rezumat = new RezumatComanda();
			rezumat.setFilialaLivrare(filiala);
			List<ArticolComanda> listArtComanda = new ArrayList<ArticolComanda>();

			for (ArticolComanda articol : listArticole) {
				if (articol.getFilialaSite().equals(filiala)) {
					listArtComanda.add(articol);
				}
			}

			rezumat.setListArticole(listArtComanda);
			listComenzi.add(rezumat);
		}

		return listComenzi;
	}

	private Set<String> getFilialeComanda() {

		Set<String> filiale = new HashSet<String>();
		for (final ArticolComanda articol : listArticole) {
			filiale.add(articol.getFilialaSite());
		}
		return filiale;

	}

	public void setRezumatListener(ComandaMathausListener listener) {
		this.listener = listener;
	}

	public void showDialog() {
		this.show();
	}

	@Override
	public void comandaEliminata(List<String> listArticoleEliminate, String filialaLivrare) {

		List<ArticolComanda> listArticoleComanda;
		if (canalDistrib.equals("10"))
			listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();
		else
			listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();

		Iterator<ArticolComanda> listIterator = listArticoleComanda.iterator();

		while (listIterator.hasNext()) {
			ArticolComanda articol = listIterator.next();

			for (String articolEliminat : listArticoleEliminate) {

				if (articol.getCodArticol().equals(articolEliminat) && articol.getFilialaSite().equals(filialaLivrare)) {
					listIterator.remove();
					break;
				}

			}

		}

		if (listener != null)
			listener.comandaEliminata();

	}

}
