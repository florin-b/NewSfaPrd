package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.BeanComandaDeschisa;
import my.logon.screen.utils.UtilsComenzi;


public class ComandaDeschisaAdapter extends BaseAdapter {

	private Context context;
	private List<BeanComandaDeschisa> listComenzi;
	private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

	static class ViewHolder {
		public TextView textIdCmd, textClient, textSuma, textLocalitate, textStrada, textStare, textSofer;
	}

	public ComandaDeschisaAdapter(Context context, List<BeanComandaDeschisa> listComenzi) {
		this.context = context;
		this.listComenzi = listComenzi;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.comanda_deschisa, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textSuma = (TextView) convertView.findViewById(R.id.textSuma);
			viewHolder.textLocalitate = (TextView) convertView.findViewById(R.id.textLocalitate);
			viewHolder.textStrada = (TextView) convertView.findViewById(R.id.textStrada);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textSofer = (TextView) convertView.findViewById(R.id.textSofer);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaDeschisa comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getIdCmdSap());
		viewHolder.textClient.setText(comanda.getNumeClient());
		viewHolder.textSuma.setText(numberFormat.format(Double.valueOf(comanda.getValoare())));
		viewHolder.textLocalitate.setText(comanda.getLocalitate());
		viewHolder.textStrada.setText(comanda.getStrada());
		viewHolder.textStare.setText(UtilsComenzi.getStareComanda(comanda.getCodStareComanda()));

		String sofer = comanda.getNumeSofer();
		if (comanda.getTelSofer() != null && !comanda.getTelSofer().isEmpty())
			sofer += " / " + comanda.getTelSofer();

		viewHolder.textSofer.setText(sofer);

		return convertView;

	}

	@Override
	public int getCount() {

		return listComenzi.size();
	}

	@Override
	public BeanComandaDeschisa getItem(int position) {
		return listComenzi.get(position);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

}
