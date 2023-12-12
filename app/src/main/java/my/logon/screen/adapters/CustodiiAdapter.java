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
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.utils.UtilsFormatting;

public class CustodiiAdapter extends BaseAdapter {

	private Context context;
	private List<BeanComandaCreata> listComenzi;
	private NumberFormat numberFormat = new DecimalFormat("#0.00");

	public CustodiiAdapter(List<BeanComandaCreata> listComenzi, Context context) {
		this.context = context;
		this.listComenzi = listComenzi;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.custodie_cmd, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textCmdSap = (TextView) convertView.findViewById(R.id.textCmdSap);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaCreata comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getId());
		viewHolder.textClient.setText(comanda.getNumeClient());
		viewHolder.textData.setText(UtilsFormatting.formatDate(comanda.getData().split("#")[0]));
		viewHolder.textStare.setText(ClientiGenericiGedInfoStrings.statusSAPMsg(Integer.valueOf(comanda.getCodStare())));
		viewHolder.textCmdSap.setText(comanda.getCmdSap().equals("-1") ? " " : comanda.getCmdSap());
		

		return convertView;
	}

	static class ViewHolder {
		public TextView textIdCmd, textClient, textData, textStare, textCmdSap;
	}

	public int getCount() {
		return listComenzi.size();
	}

	public BeanComandaCreata getItem(int position) {
		return listComenzi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setListComenzi(List<BeanComandaCreata> listComenzi) {
		this.listComenzi = listComenzi;
	}

}
