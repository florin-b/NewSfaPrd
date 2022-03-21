package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.model.Constants;
import my.logon.screen.utils.UtilsFormatting;


public class ComandaAprobAdapter extends BaseAdapter {

	private List<BeanComandaCreata> listComenzi;
	private Context context;
	private NumberFormat numberFormat = new DecimalFormat("#0.00");
	private String tipReducere, tipAlert, tipUser;

	public ComandaAprobAdapter(List<BeanComandaCreata> listComenzi, Context context) {
		this.context = context;
		this.listComenzi = listComenzi;
	}

	static class ViewHolder {
		public TextView textIdCmd, textClient, textData, textSuma, textStare, textFact, textAprob, textTipClient, textNumeAgent, textFiliala, textSumaTVA,
				textTermenPlata, textCursValutar, textAdrLivrNoua, textPondereB_30, textClientRaft, textMoneda, textMonedaTva;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.customrowcmdaprob, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textSuma = (TextView) convertView.findViewById(R.id.textSuma);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textFact = (TextView) convertView.findViewById(R.id.textFact);
			viewHolder.textAprob = (TextView) convertView.findViewById(R.id.textAprob);
			viewHolder.textTipClient = (TextView) convertView.findViewById(R.id.textTipClient);
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
			viewHolder.textFiliala = (TextView) convertView.findViewById(R.id.textFiliala);
			viewHolder.textSumaTVA = (TextView) convertView.findViewById(R.id.textSumaTVA);
			viewHolder.textTermenPlata = (TextView) convertView.findViewById(R.id.textTermenPlata);
			viewHolder.textCursValutar = (TextView) convertView.findViewById(R.id.textCursValutar);
			viewHolder.textAdrLivrNoua = (TextView) convertView.findViewById(R.id.textAdrLivrNoua);
			viewHolder.textPondereB_30 = (TextView) convertView.findViewById(R.id.textPondereB_30);
			viewHolder.textClientRaft = (TextView) convertView.findViewById(R.id.textClientRaft);
			viewHolder.textMoneda = (TextView) convertView.findViewById(R.id.textMoneda);
			viewHolder.textMonedaTva = (TextView) convertView.findViewById(R.id.textMonedaTva);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaCreata comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getId());
		viewHolder.textClient.setText(comanda.getNumeClient());

		viewHolder.textData.setText(UtilsFormatting.formatDate(comanda.getData()));
		viewHolder.textSuma.setText(numberFormat.format(Double.parseDouble(comanda.getSuma())));

		if (comanda.getCanalDistrib().equals("10")) {
			viewHolder.textSumaTVA.setText(numberFormat.format(Double.parseDouble(comanda.getSuma()) * Constants.TVA));
		} else {
			viewHolder.textSumaTVA.setText(numberFormat.format(Double.parseDouble(comanda.getSuma())));
		}

		viewHolder.textTipClient.setText(comanda.getTipClient());
		viewHolder.textStare.setText(comanda.getStare());

		tipReducere = "";
		if (comanda.getFactRed().equals("X")) {
			tipReducere = "2 facturi";
		}
		if (comanda.getFactRed().equals("R")) {
			tipReducere = "1 factura (red. separat)";
		}
		if (comanda.getFactRed().equals(" ")) {
			tipReducere = "1 factura (red. in pret)";
		}
		viewHolder.textFact.setText(tipReducere);

		tipAlert = "";
		if (comanda.getAccept1().equals("X")) {
			tipAlert = "SD";
		}

		if (comanda.getAccept2().equals("X")) {
			tipAlert += ";DV";
		}
		viewHolder.textAprob.setText(tipAlert);

		viewHolder.textFiliala.setText(comanda.getFiliala().substring(0, 2));

		tipUser = "  (AV) ";
		if (comanda.getDivizieAgent().equals("10")) {
			tipUser = "  (KA) ";
		}
		if (comanda.getDivizieAgent().equals("11")) {
			tipUser = "  (CV) ";
		}

		if (comanda.getTelAgent() != null && !comanda.getTelAgent().equals("-1"))
			tipUser += comanda.getTelAgent();

		viewHolder.textNumeAgent.setText(comanda.getNumeAgent() + tipUser);
		viewHolder.textTermenPlata.setText(comanda.getTermenPlata());

		if (!comanda.getCursValutar().equals("0"))
			viewHolder.textCursValutar.setText("\u20ac: " + comanda.getCursValutar());

		viewHolder.textAdrLivrNoua.setText(comanda.getAdresaNoua());
		viewHolder.textPondereB_30.setText(comanda.getPondere30());

		if (comanda.isClientRaft())
			viewHolder.textClientRaft.setVisibility(View.VISIBLE);
		else
			viewHolder.textClientRaft.setVisibility(View.GONE);

		viewHolder.textMoneda.setText(comanda.getMoneda());
		viewHolder.textMonedaTva.setText(comanda.getMonedaTva());
		
		return convertView;
	}

	public int getCount() {
		return listComenzi.size();
	}

	public BeanComandaCreata getItem(int pos) {
		return listComenzi.get(pos);
	}

	public long getItemId(int position) {
		return position;
	}

}
