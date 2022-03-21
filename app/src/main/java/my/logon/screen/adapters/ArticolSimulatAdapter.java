package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ArticolSimulat;
import my.logon.screen.beans.BeanComandaSimulata;
import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.beans.ValoriComanda;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.OperatiiArticolImpl;
import my.logon.screen.model.UserInfo;


public class ArticolSimulatAdapter extends BaseAdapter implements OperatiiArticolListener {

	private Context context;
	private List<ArticolSimulat> listArticole;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	private NumberFormat nf3;
	private NumberFormat nf2;
	private NumberFormat nf4;

	private List<BeanConditiiArticole> listConditii;
	private int pozitieArticolSelectat;
	private BeanComandaSimulata comanda;
	private OperatiiArticolImpl operatiiArticol;
	private List<Integer> listPozitiiConditii;

	public ArticolSimulatAdapter(Context context, List<ArticolSimulat> listArticole) {
		this.context = context;
		this.listArticole = listArticole;

		operatiiArticol = new OperatiiArticolImpl(context);
		operatiiArticol.setListener(this);
		
		nf3 = new DecimalFormat("#0.000");
		nf2 = new DecimalFormat("#0.00");
		nf4 = new DecimalFormat("#0.0000");
		listPozitiiConditii = new ArrayList<Integer>();

	}

	public void setListArticole(List<ArticolSimulat> listArticole) {
		this.listArticole = listArticole;
		notifyDataSetChanged();
	}

	public void setListConditii(List<BeanConditiiArticole> listConditii) {
		this.listConditii = listConditii;
	}

	public void setComandaCurenta(BeanComandaSimulata comanda) {
		this.comanda = comanda;
	}

	public static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textUmArt, textPretArt, textMonedaArt, textDepozit, textStatusArt, textProcRed,
				textStatusStoc, textCantConditie, textUmConditie, textPretConditie, textMonedaConditie, textConditie;

		ImageButton acceptaConditiiBtn;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % colors.length;
		String unitPret = "RON";

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_comenzi_sim, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textUmArt = (TextView) convertView.findViewById(R.id.textUmArt);
			viewHolder.textPretArt = (TextView) convertView.findViewById(R.id.textPretArt);
			viewHolder.textMonedaArt = (TextView) convertView.findViewById(R.id.textMonedaArt);
			viewHolder.textDepozit = (TextView) convertView.findViewById(R.id.textDepozit);
			viewHolder.textStatusArt = (TextView) convertView.findViewById(R.id.textStatusArt);
			viewHolder.textProcRed = (TextView) convertView.findViewById(R.id.textProcRed);
			viewHolder.textStatusStoc = (TextView) convertView.findViewById(R.id.textStatusStoc);
			viewHolder.textConditie = (TextView) convertView.findViewById(R.id.textConditie);
			viewHolder.textCantConditie = (TextView) convertView.findViewById(R.id.textCantConditie);
			viewHolder.textUmConditie = (TextView) convertView.findViewById(R.id.textUmConditie);
			viewHolder.textPretConditie = (TextView) convertView.findViewById(R.id.textPretConditie);
			viewHolder.textMonedaConditie = (TextView) convertView.findViewById(R.id.textMonedaConditie);
			viewHolder.acceptaConditiiBtn = (ImageButton) convertView.findViewById(R.id.acceptaConditiiBtn);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolSimulat articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeArt.setText(articol.getNumeArticol());
		viewHolder.textCodArt.setText(articol.getCodArticol());
		viewHolder.textCantArt.setText(nf3.format(articol.getCantitate()));

		if (!articol.getUmb().equals(articol.getUm())) {
			unitPret = "RON/" + System.getProperty("line.separator") + articol.getUmb();
		}

		viewHolder.textUmArt.setText(articol.getUm());
		if (articol.getDepart().equals("02"))
			viewHolder.textPretArt.setText(nf4.format(articol.getPretUnit()));
		else
			viewHolder.textPretArt.setText(nf2.format(articol.getPretUnit()));

		viewHolder.textMonedaArt.setText(unitPret);
		viewHolder.textDepozit.setText(articol.getDepozit());
		viewHolder.textProcRed.setText(nf2.format(articol.getProcent()));

		if (!articol.isExceptie()) {
			if (articol.isStocOK())
				viewHolder.textStatusStoc.setText("In stoc");
			else
				viewHolder.textStatusStoc.setText("Stoc insuficient");
		} else
			viewHolder.textStatusStoc.setText("");

		BeanConditiiArticole conditieArticol = getConditieArticol(articol.getCodArticol());

		if (conditieArticol != null) {

			if (conditieArticol.getCantitate() != articol.getCantitate()) {
				viewHolder.textCantConditie.setText(nf2.format(conditieArticol.getCantitate()));
				viewHolder.textCantConditie.setVisibility(View.VISIBLE);
				viewHolder.textUmConditie.setText(conditieArticol.getUm());
				viewHolder.textUmConditie.setVisibility(View.VISIBLE);
				viewHolder.textConditie.setVisibility(View.VISIBLE);
				viewHolder.acceptaConditiiBtn.setVisibility(View.VISIBLE);
			} else {
				viewHolder.textCantConditie.setVisibility(View.INVISIBLE);
				viewHolder.textUmConditie.setVisibility(View.INVISIBLE);
			}

			if (conditieArticol.getValoare() != articol.getPretUnit()) {
				if (articol.getDepart().equals("02"))
					viewHolder.textPretConditie.setText(nf4.format(conditieArticol.getValoare()));
				else
					viewHolder.textPretConditie.setText(nf2.format(conditieArticol.getValoare()));

				viewHolder.textPretConditie.setVisibility(View.VISIBLE);
				viewHolder.textConditie.setVisibility(View.VISIBLE);
				viewHolder.acceptaConditiiBtn.setVisibility(View.VISIBLE);
			} else {
				viewHolder.textPretConditie.setVisibility(View.INVISIBLE);
				viewHolder.textMonedaConditie.setVisibility(View.INVISIBLE);
			}

			if (!areConditions(articol, conditieArticol)) {
				viewHolder.textConditie.setVisibility(View.INVISIBLE);
				viewHolder.acceptaConditiiBtn.setVisibility(View.INVISIBLE);
				viewHolder.textStatusArt.setVisibility(View.INVISIBLE);
				articol.setConditii(false);
			} else {
				viewHolder.textStatusArt.setText("Articol supus unor conditii");
				articol.setConditii(true);
			}

		} else {
			viewHolder.textCantConditie.setVisibility(View.INVISIBLE);
			viewHolder.textUmConditie.setVisibility(View.INVISIBLE);
			viewHolder.textPretConditie.setVisibility(View.INVISIBLE);
			viewHolder.textMonedaConditie.setVisibility(View.INVISIBLE);
			viewHolder.acceptaConditiiBtn.setVisibility(View.INVISIBLE);
			viewHolder.textConditie.setVisibility(View.INVISIBLE);
			viewHolder.textStatusArt.setVisibility(View.INVISIBLE);
		}

		setAcceptaConditiiListener(viewHolder, position);

		if (colorPos % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	private BeanConditiiArticole getConditieArticol(String codArticol) {
		BeanConditiiArticole conditieArticol = null;

		for (BeanConditiiArticole cond : listConditii) {
			if (cond.getCod().equals(codArticol)) {
				conditieArticol = cond;
				break;
			}

		}

		return conditieArticol;
	}

	private boolean areConditions(ArticolComanda articolComanda, BeanConditiiArticole articolConditie) {
		return articolConditie.getCantitate() != articolComanda.getCantitate() || articolConditie.getValoare() != articolComanda.getPretUnit();

	}

	public ValoriComanda getValoriComanda(List<ArticolComanda> articol) {

		double marja = 0;

		ValoriComanda valoriComanda = new ValoriComanda();

		for (ArticolComanda art : articol) {

			if (art.getTipArt().equals("B")) {
				valoriComanda.setPondereB(art.getPret() + valoriComanda.getPondereB());
			}

			valoriComanda.setTotal(art.getPret() + valoriComanda.getTotal());

			marja = (art.getPretUnit() / art.getMultiplu() - art.getCmp()) * art.getCantitate();
			valoriComanda.setMarja(marja + valoriComanda.getMarja());
		}

		return valoriComanda;
	}

	private void setAcceptaConditiiListener(ViewHolder viewHolder, final int position) {
		viewHolder.acceptaConditiiBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				ArticolComanda articolComanda = getItem(position);
				BeanConditiiArticole articolConditie = getConditieArticol(articolComanda.getCodArticol());
				pozitieArticolSelectat = position;
				listPozitiiConditii.add(position);

				if (articolComanda.getCantitate() != articolConditie.getCantitate()) {
					acceptaConditiiCantitative();
				}

				if (articolComanda.getPretUnit() != articolConditie.getValoare()) {
					acceptaConditiiValorice(articolComanda, articolConditie);

				}

			}
		});
	}

	private void acceptaConditiiCantitative() {
		ArticolComanda articolComanda = getItem(pozitieArticolSelectat);
		BeanConditiiArticole articolConditie = getConditieArticol(articolComanda.getCodArticol());

		articolComanda.setConditii(false);
		getPretArticol(articolComanda, articolConditie.getCantitate());
	}

	private void getPretArticol(ArticolComanda articolComanda, double cantitate) {

		String codArticol = articolComanda.getCodArticol();

		String localDepart = articolComanda.getDepart();
		String localCanalDistrib = comanda.getCanalDistrib();

		if (articolComanda.getDepozit().equals("MAV1") && !articolComanda.getDepartAprob().equals("00")) {
			localDepart = "11";
			localCanalDistrib = "10";
		}

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", comanda.getCodClient());
		params.put("articol", codArticol);
		params.put("cantitate", String.valueOf(cantitate));
		params.put("depart", localDepart);
		params.put("um", articolComanda.getUm());
		params.put("ul", comanda.getFiliala());
		params.put("depoz", articolComanda.getDepozit());
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("canalDistrib", localCanalDistrib);
		params.put("filialaAlternativa", articolComanda.getUnitLogAlt());

		operatiiArticol.getPret(params);

	}

	private void actualizeazaDatePret(String datePret) {

		String[] tokenPret = datePret.split("#");

		ArticolComanda articolComanda = getItem(pozitieArticolSelectat);
		BeanConditiiArticole articolConditie = getConditieArticol(articolComanda.getCodArticol());

		double finalPrice = articolComanda.getPret();
		double initPrice = Double.parseDouble(tokenPret[1]);
		double listPrice = Double.parseDouble(tokenPret[8]);
		double valMultiplu = Double.parseDouble(tokenPret[13].toString().trim());

		double cantUmB = Double.parseDouble(tokenPret[14].toString().trim());

		if (listPrice == 0)
			listPrice = initPrice;

		if (finalPrice == initPrice)
			finalPrice = (finalPrice / articolConditie.getCantitate());

		double procRedFact = (initPrice / articolConditie.getCantitate() * valMultiplu - finalPrice) / (listPrice / articolConditie.getCantitate()) * 100;

		listArticole.get(pozitieArticolSelectat).setCantitate(articolConditie.getCantitate());
		listArticole.get(pozitieArticolSelectat).setCantUmb(cantUmB);
		listArticole.get(pozitieArticolSelectat).setInfoArticol(tokenPret[9].replace(',', '.'));
		listArticole.get(pozitieArticolSelectat).setProcentFact(procRedFact);

		notifyDataSetChanged();

	}

	private void acceptaConditiiValorice(ArticolComanda articolComanda, BeanConditiiArticole articolConditie) {

		articolComanda.setConditii(false);
		articolComanda.setPonderare(0);

		double pretInit = articolComanda.getPretUnit() / (1 - articolComanda.getProcent() / 100);

		listArticole.get(pozitieArticolSelectat).setPretUnit(articolConditie.getValoare());

		double valoareArticol = listArticole.get(pozitieArticolSelectat).getCantitate() * articolConditie.getValoare();

		listArticole.get(pozitieArticolSelectat).setPret(valoareArticol);

		double newProcRed = (1 - articolComanda.getPretUnit() / pretInit) * 100;
		listArticole.get(pozitieArticolSelectat).setProcent(newProcRed);

		double pretVanzareInit = pretInit / (1 - articolComanda.getDiscClient() / 100);
		double newProcAprob = (1 - articolComanda.getPretUnit() / pretVanzareInit) * 100;
		listArticole.get(pozitieArticolSelectat).setProcAprob(newProcAprob);

		double procRedFact = ((pretInit * (articolComanda.getCantitate() / articolComanda.getMultiplu())) / articolComanda.getCantitate()
				* articolComanda.getMultiplu() - articolComanda.getPretUnit())
				/ ((pretVanzareInit * (articolComanda.getCantitate() / articolComanda.getMultiplu())) / articolComanda.getCantitate() * articolComanda
						.getMultiplu()) * 100;

		listArticole.get(pozitieArticolSelectat).setProcentFact(procRedFact);

		notifyDataSetChanged();

	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolSimulat getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

	}

	public List<Integer> getListPozitiiConditii() {
		return listPozitiiConditii;
	}


	@Override
	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {
		case GET_PRET:
			actualizeazaDatePret((String) result);
			break;
		default:
			break;
		}

	}

}
