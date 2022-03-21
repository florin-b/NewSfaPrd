package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ClientAlocat;


public class ClientiAlocatiAdapter extends BaseAdapter {

	private Context context;
	private List<ClientAlocat> listClienti;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public ClientiAlocatiAdapter(List<ClientAlocat> listClienti, Context context) {
		this.context = context;
		this.listClienti = listClienti;

	}

	static class ViewHolder {
		public TextView textNumeClient, textNrCrt, textDep01, textDep02, textDep03, textDep04, textDep05, textDep06, textDep07, textDep08, textDep09;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.client_alocat_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textDep01 = (TextView) convertView.findViewById(R.id.textDep01);
			viewHolder.textDep02 = (TextView) convertView.findViewById(R.id.textDep02);
			viewHolder.textDep03 = (TextView) convertView.findViewById(R.id.textDep03);
			viewHolder.textDep04 = (TextView) convertView.findViewById(R.id.textDep04);
			viewHolder.textDep05 = (TextView) convertView.findViewById(R.id.textDep05);
			viewHolder.textDep06 = (TextView) convertView.findViewById(R.id.textDep06);
			viewHolder.textDep07 = (TextView) convertView.findViewById(R.id.textDep07);
			viewHolder.textDep08 = (TextView) convertView.findViewById(R.id.textDep08);
			viewHolder.textDep09 = (TextView) convertView.findViewById(R.id.textDep09);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ClientAlocat client = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1) + ".");
		viewHolder.textNumeClient.setText(client.getNumeClient());

		viewHolder.textDep01.setText(client.getTipClient01());
		viewHolder.textDep02.setText(client.getTipClient02());
		viewHolder.textDep03.setText(client.getTipClient03());
		viewHolder.textDep04.setText(client.getTipClient04());
		viewHolder.textDep05.setText(client.getTipClient05());
		viewHolder.textDep06.setText(client.getTipClient06());
		viewHolder.textDep07.setText(client.getTipClient07());
		viewHolder.textDep08.setText(client.getTipClient08());
		viewHolder.textDep09.setText(client.getTipClient09());

		convertView.setBackgroundColor(colors[colorPos % 2]);

		return convertView;
	}

	public int getCount() {
		return listClienti.size();
	}

	public ClientAlocat getItem(int position) {
		return listClienti.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
