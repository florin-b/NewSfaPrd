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
import my.logon.screen.beans.BeanComandaSimulata;
import my.logon.screen.model.Constants;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsUser;


public class ComandaSimulataAdapter extends BaseAdapter {

	private Context context;
	private List<BeanComandaSimulata> listComenzi;
	NumberFormat numberFormat = new DecimalFormat("#0.00");

	public ComandaSimulataAdapter(List<BeanComandaSimulata> listComenzi, Context context) {
		this.context = context;
		this.listComenzi = listComenzi;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_cmd_simulata, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textSuma = (TextView) convertView.findViewById(R.id.textSuma);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textMoneda = (TextView) convertView.findViewById(R.id.textMoneda);
			viewHolder.textSumaTVA = (TextView) convertView.findViewById(R.id.textSumaTVA);
			viewHolder.textMonedaTVA = (TextView) convertView.findViewById(R.id.textMonedaTVA);
			viewHolder.textCmdSap = (TextView) convertView.findViewById(R.id.textCmdSap);
			viewHolder.textTipClient = (TextView) convertView.findViewById(R.id.textTipClient);
			viewHolder.textAprobata = (TextView) convertView.findViewById(R.id.textAprobata);
			viewHolder.textValAvans = (TextView) convertView.findViewById(R.id.textValAvans);
			viewHolder.textMonedaAvans = (TextView) convertView.findViewById(R.id.textValAvans);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaSimulata comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getId());
		viewHolder.textClient.setText(comanda.getNumeClient());
		viewHolder.textData.setText(UtilsFormatting.formatDate(comanda.getData()));
		viewHolder.textSuma.setText(numberFormat.format(Double.valueOf(comanda.getSuma())));
		viewHolder.textStare.setText(comanda.getStare());
		viewHolder.textMoneda.setText(comanda.getMoneda());
		viewHolder.textSumaTVA.setText(numberFormat.format(Double.valueOf(comanda.getSumaTva())));
		viewHolder.textMonedaTVA.setText(comanda.getMonedaTva());
		viewHolder.textCmdSap.setText(comanda.getCmdSap().equals("-1") ? " " : comanda.getCmdSap());
		viewHolder.textTipClient.setText(comanda.getTipClient());
		comanda.setAprobata(isAprobata(comanda));

		if (!comanda.getCodStare().equals(Constants.CMD_SIM_CONDITII)) {
			if (comanda.isAprobata())
				viewHolder.textAprobata.setText("Aprobata");
			else
				viewHolder.textAprobata.setText("In curs de aprobare");
		}

		if (comanda.getCodStare().equals("21")) {
			viewHolder.textValAvans.setText("Avans: " + String.valueOf(comanda.getAvans()));
			viewHolder.textValAvans.setVisibility(View.VISIBLE);
			viewHolder.textMonedaAvans.setVisibility(View.VISIBLE);
		} else {
			viewHolder.textValAvans.setVisibility(View.INVISIBLE);
			viewHolder.textMonedaAvans.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	static class ViewHolder {
		public TextView textIdCmd, textClient, textData, textSuma, textStare, textMoneda, textSumaTVA, textMonedaTVA, textCmdSap, textTipClient, textAprobata,
				textValAvans, textMonedaAvans;
	}

	public int getCount() {
		return listComenzi.size();
	}

	public BeanComandaSimulata getItem(int position) {
		return listComenzi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setListComenzi(List<BeanComandaSimulata> listComenzi) {
		this.listComenzi = listComenzi;
	}

	private boolean isAprobata(BeanComandaSimulata comanda) {
		
		if (comanda.getCanalDistrib().equals("10") || UtilsUser.isConsWood())
			return comanda.isAprobDistrib();
		
		String[] aprobariNecesare = {};
		String[] aprobariPrimite = {};

		if (comanda.getAprobariNecesare().trim().length() > 0)
			aprobariNecesare = comanda.getAprobariNecesare().split(",");

		if (comanda.getAprobariPrimite().trim().length() > 0)
			aprobariPrimite = comanda.getAprobariPrimite().trim().split(";");

		boolean isAprobata = true;

		if (aprobariPrimite.length != aprobariNecesare.length)
			isAprobata = false;
		else {

			outer: for (String aprobNecesar : aprobariNecesare) {
				for (String aprobPrimite : aprobariPrimite) {
					String[] primita = aprobPrimite.split(":");
					if (aprobNecesar.equals(primita[0]))
						if (!Boolean.parseBoolean(primita[1])) {
							isAprobata = false;
							break outer;
						}

				}
			}
		}

		return isAprobata;

	}
}
