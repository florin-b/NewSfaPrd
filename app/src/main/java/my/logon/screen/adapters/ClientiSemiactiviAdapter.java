/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanClientSemiactiv;


public class ClientiSemiactiviAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };
	private List<BeanClientSemiactiv> listClienti;
	Context context;

	static class ViewHolder {
		public TextView textNrCrt, textNumeClient, textCodClient, textJudet, textLocalitate, textStrada, textNumePersCont, textTelPersCont,
				textVanzMedie, textVanz03,textVanz06, textVanz07, textVanz09, textVanz040, textVanz041;

	}

	public ClientiSemiactiviAdapter(Context context, List<BeanClientSemiactiv> listClienti) {
		this.context = context;
		this.listClienti = listClienti;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.custom_art_row_clienti_semiactivi, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textCodClient = (TextView) convertView.findViewById(R.id.textCodClient);
			viewHolder.textJudet = (TextView) convertView.findViewById(R.id.textJudet);
			viewHolder.textLocalitate = (TextView) convertView.findViewById(R.id.textLocalitate);
			viewHolder.textStrada = (TextView) convertView.findViewById(R.id.textStrada);
			viewHolder.textNumePersCont = (TextView) convertView.findViewById(R.id.textNumePersCont);
			viewHolder.textTelPersCont = (TextView) convertView.findViewById(R.id.textTelPersCont);
			viewHolder.textVanzMedie = (TextView) convertView.findViewById(R.id.textVanzMedie);
			viewHolder.textVanz03 = (TextView) convertView.findViewById(R.id.textVanz03);
			viewHolder.textVanz06 = (TextView) convertView.findViewById(R.id.textVanz06);
			viewHolder.textVanz07 = (TextView) convertView.findViewById(R.id.textVanz07);
			viewHolder.textVanz09 = (TextView) convertView.findViewById(R.id.textVanz09);
			viewHolder.textVanz040 = (TextView) convertView.findViewById(R.id.textVanz040);
			viewHolder.textVanz041 = (TextView) convertView.findViewById(R.id.textVanz041);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanClientSemiactiv client = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1) + ".");
		viewHolder.textNumeClient.setText(client.getNumeClient());
		viewHolder.textCodClient.setText(client.getCodClient());
		viewHolder.textJudet.setText(client.getJudet());
		viewHolder.textLocalitate.setText(client.getLocalitate());
		viewHolder.textStrada.setText(client.getStrada());
		viewHolder.textNumePersCont.setText(client.getNumePersContact());
		viewHolder.textTelPersCont.setText(client.getTelPersContact());
		viewHolder.textVanzMedie.setText(client.getVanzMedie());
		viewHolder.textVanz03.setText(client.getVanz03());
		viewHolder.textVanz06.setText(client.getVanz06());
		viewHolder.textVanz07.setText(client.getVanz07());
		viewHolder.textVanz09.setText(client.getVanz09());
		viewHolder.textVanz040.setText(client.getVanz040());
		viewHolder.textVanz041.setText(client.getVanz041());
		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;

	}

	public int getCount() {
		return listClienti.size();
	}

	public BeanClientSemiactiv getItem(int position) {
		return listClienti.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}