package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.listeners.ArticolModificareListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.OperatiiArticolImpl;
import my.logon.screen.model.UserInfo;

public class ArticolModificareAdapter extends BaseAdapter implements OperatiiArticolListener {

	private Context context;
	private List<ArticolComanda> listArticole;
	private List<BeanConditiiArticole> listConditii;
	private OperatiiArticolImpl operatiiArticol;
	private int pozitieArticolSelectat;

	private ArticolModificareListener listener;
	private BeanComandaCreata comanda;

	private NumberFormat nf3;
	private NumberFormat nf2;
	private NumberFormat nf4;

	public ArticolModificareAdapter(Context context, List<ArticolComanda> listArticole, List<BeanConditiiArticole> listConditii, BeanComandaCreata comanda) {
		this.context = context;
		this.listArticole = listArticole;
		this.listConditii = listConditii;
		this.comanda = comanda;

		operatiiArticol = new OperatiiArticolImpl(context);
		operatiiArticol.setListener(this);

		nf3 = new DecimalFormat("#0.000");
		nf2 = new DecimalFormat("#0.00");
		nf4 = new DecimalFormat("#0.0000");

	}

	public void setListArticole(List<ArticolComanda> listArticole) {
		this.listArticole = listArticole;
	}

	public static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textUmArt, textPretArt, textMonedaArt, textDepozit, textStatusArt, textProcRed,
				textCantConditie, textUmConditie, textPretConditie, textMonedaConditie, textConditie, textObsArt;

		ImageButton stergeArticolBtn, acceptaConditiiBtn;
		public ImageView statusImage;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;
		String unitPret = "RON";

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_modificare_comanda, parent, false);

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
			viewHolder.textConditie = (TextView) convertView.findViewById(R.id.textConditie);
			viewHolder.textCantConditie = (TextView) convertView.findViewById(R.id.textCantConditie);
			viewHolder.textUmConditie = (TextView) convertView.findViewById(R.id.textUmConditie);
			viewHolder.textPretConditie = (TextView) convertView.findViewById(R.id.textPretConditie);
			viewHolder.textMonedaConditie = (TextView) convertView.findViewById(R.id.textMonedaConditie);
			viewHolder.textObsArt = (TextView) convertView.findViewById(R.id.textObsArt);
			viewHolder.stergeArticolBtn = (ImageButton) convertView.findViewById(R.id.stergeArticolBtn);
			viewHolder.acceptaConditiiBtn = (ImageButton) convertView.findViewById(R.id.acceptaConditiiBtn);
			viewHolder.statusImage = (ImageView) convertView.findViewById(R.id.imageAlertaPret);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolComanda articol = getItem(position);

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

		viewHolder.textObsArt.setText(getObsArt(articol.getStatus()));

		viewHolder.textStatusArt.setText(getStatusArticol(articol, comanda));
		viewHolder.textProcRed.setText(nf2.format(articol.getProcent()));

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
		}

		if (isUserGed()) {

			viewHolder.statusImage.setImageResource(0);
			if (articol.getTipAlertPret() != null) {
				if (articol.getTipAlertPret().equals("FM")) { // pret foarte mic
					viewHolder.statusImage.setImageResource(R.drawable.alert_pret_1);
				}

				if (articol.getTipAlertPret().equals("M")) { // pret mic
					viewHolder.statusImage.setImageResource(R.drawable.alert_pret_2);
				}
			}

		}

		setStergeArticolListener(viewHolder, position);
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

	private boolean isUserGed() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
	}

	private String getStatusArticol(ArticolComanda articol, BeanComandaCreata comanda) {

		String statusArticol = "";

		if (!comanda.getStare().equals(ClientiGenericiGedInfoStrings.statusAprobCmd(1)))
			statusArticol = "";

		if (comanda.getAprobariNecesare() == null || comanda.getAprobariNecesare().trim().isEmpty())
			statusArticol = "";

		if (articol.hasConditii())
			statusArticol = "";

		if (comanda.getAprobariNecesare().contains(articol.getDepartSintetic()))
			statusArticol = "Articolul necesita aprobare";

		if (comanda.getAprobariPrimite().contains(articol.getDepartSintetic()))
			statusArticol = getStareArticolComandaCV(articol, comanda);

		return statusArticol;


	}


	private String getStatusArticol_old(ArticolComanda articol, BeanComandaCreata comanda) {

		String statusArticol = "";

		if (articol.getDepart().equals(comanda.getDivizieComanda())) {

			if (articol.hasConditii()) {
				statusArticol = "";
			} else if (articol.getPonderare() == 1) {
				if (comanda.getAprobariPrimite().contains(articol.getDepartSintetic()))
					statusArticol = getStareArticolComandaCV(articol, comanda);
				else
					statusArticol = "Articolul necesita aprobare";
			}

		}

		return statusArticol;

	}

	private String getObsArt(String statusArt) {
		if (statusArt != null && statusArt.equals("9"))
			return "Stoc insuficient";
		return "";
	}

	private String getStareArticolComandaCV(ArticolComanda articol, BeanComandaCreata comanda) {
		String status = "";

		String[] aprobariCmd = comanda.getAprobariPrimite().split(";");
		String[] tipStare = null;

		for (int i = 0; i < aprobariCmd.length; i++) {

			if (!aprobariCmd[i].contains(":"))
				continue;

			tipStare = aprobariCmd[i].split(":");

			if (articol.getDepartSintetic().substring(0, 2).equals(tipStare[0].substring(0, 2))) {
				if (Boolean.valueOf(tipStare[1]))
					status = "Articol aprobat";
				else
					status = "Articol respins";
			}

		}

		return status;
	}

	private void setStergeArticolListener(ViewHolder viewHolder, final int position) {
		viewHolder.stergeArticolBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String codArticol = listArticole.get(position).getCodArticol();
				String filiala = listArticole.get(position).getFilialaSite();
				listArticole.remove(position);
				if (listener != null) {
					listener.articolSters(codArticol, filiala);
				}
				notifyDataSetChanged();
			}
		});
	}

	private void setAcceptaConditiiListener(ViewHolder viewHolder, final int position) {
		viewHolder.acceptaConditiiBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				ArticolComanda articolComanda = getItem(position);
				BeanConditiiArticole articolConditie = getConditieArticol(articolComanda.getCodArticol());
				pozitieArticolSelectat = position;

				if (articolComanda.getCantitate() != articolConditie.getCantitate()) {
					acceptaConditiiCantitative(articolComanda);
				}

				if (articolComanda.getPretUnit() != articolConditie.getValoare()) {
					acceptaConditiiValorice(articolComanda, articolConditie);

				}

			}
		});
	}

	private void valideazaCantitateCeruta(String strStocExistent) {
		String[] tokenStoc = strStocExistent.split("#");
		double stocExistent = Double.parseDouble(tokenStoc[0]);

		ArticolComanda articolComanda = getItem(pozitieArticolSelectat);
		BeanConditiiArticole articolConditie = getConditieArticol(articolComanda.getCodArticol());

		if (stocExistent < articolConditie.getCantitate()) {
			Toast.makeText(context, "Stoc insuficient.", Toast.LENGTH_SHORT).show();
		} else {
			articolComanda.setConditii(false);
			getPretArticol(articolComanda, articolConditie.getCantitate());
		}

	}

	private boolean areConditions(ArticolComanda articolComanda, BeanConditiiArticole articolConditie) {
		return articolConditie.getCantitate() != articolComanda.getCantitate() || articolConditie.getValoare() != articolComanda.getPretUnit();

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

		double finalPrice = articolComanda.getPretUnit();
		if (comanda.getCanalDistrib().equals("20"))
			finalPrice  = articolComanda.getPretUnitarClient();

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
		listArticole.get(pozitieArticolSelectat).setCantitateInit(cantUmB);
		listArticole.get(pozitieArticolSelectat).setCantitate50(cantUmB);
		listArticole.get(pozitieArticolSelectat).setInfoArticol(tokenPret[9].replace(',', '.'));
		listArticole.get(pozitieArticolSelectat).setProcentFact(procRedFact);

		notifyDataSetChanged();

	}

	private void acceptaConditiiCantitative(ArticolComanda articolComanda) {

		String codArticol = articolComanda.getCodArticol();
		articolComanda.setConditii(false);
		articolComanda.setPonderare(0);

		if (codArticol.length() == 8)
			codArticol = "0000000000" + codArticol;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codArt", codArticol);
		params.put("filiala", getUnitLogArtStoc(articolComanda));
		params.put("depozit", articolComanda.getDepozit());

		operatiiArticol.getStocDepozit(params);

	}

	private String getUnitLogArtStoc(ArticolComanda articolComanda) {

		if (articolComanda.getUnitLogAlt().toUpperCase().contains("BV9"))
			return articolComanda.getUnitLogAlt();
		else if (articolComanda.getDepozit().toUpperCase().equals("WOOD") || comanda.getCanalDistrib().equals("20"))
			return comanda.getFiliala();
		else
			return UserInfo.getInstance().getUnitLog();

	}

	private void acceptaConditiiValorice(ArticolComanda articolComanda, BeanConditiiArticole articolConditie) {

		articolComanda.setConditii(false);
		articolComanda.setPonderare(0);

		double pretInit = articolComanda.getPretUnit() / (1 - articolComanda.getProcent() / 100);

		listArticole.get(pozitieArticolSelectat).setPretUnit(articolConditie.getValoare());
		listArticole.get(pozitieArticolSelectat).setPretUnitarClient(articolConditie.getValoare());

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

	public ArticolComanda getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void notifyDataSetChanged() {
		triggerEventModificareArticol();
		super.notifyDataSetChanged();

	}

	private void triggerEventModificareArticol() {
		if (listener != null) {
			listener.articolModificat();
		}
	}

	public void setArticolModificareListener(ArticolModificareListener listener) {
		this.listener = listener;
	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {

		switch (methodName) {
		case GET_STOC_DEPOZIT:
			valideazaCantitateCeruta((String) result);
			break;

		case GET_PRET:
			actualizeazaDatePret((String) result);
			break;

		default:
			break;
		}

	}

}
