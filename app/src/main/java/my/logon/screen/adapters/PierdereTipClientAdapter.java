package my.logon.screen.adapters;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.listeners.OperatiiClientPierderiListener;
import my.logon.screen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import my.logon.screen.beans.PierdereTipClient;

public class PierdereTipClientAdapter extends BaseAdapter {

	private Context context;
	private List<PierdereTipClient> listPierderi;

	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };
	private OperatiiClientPierderiListener listener;
	private NumberFormat nf;

	public void setPierderiVanzariListener(OperatiiClientPierderiListener listener) {
		this.listener = listener;
	}

	public PierdereTipClientAdapter(List<PierdereTipClient> listPierderi, Context context) {
		this.context = context;
		this.listPierderi = listPierderi;

		nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setMaximumFractionDigits(2);		
		
	}

	static class ViewHolder {
		public TextView textNumeClient, textVenitLC, textVenitLC_1, textVenitLC_2;
		public Button detaliiBtn;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.pierd_tip_cl_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textVenitLC = (TextView) convertView.findViewById(R.id.textVenitLC);
			viewHolder.textVenitLC_1 = (TextView) convertView.findViewById(R.id.textVenitLC_1);
			viewHolder.textVenitLC_2 = (TextView) convertView.findViewById(R.id.textVenitLC_2);
			viewHolder.detaliiBtn = (Button) convertView.findViewById(R.id.btnDetalii);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final PierdereTipClient pierdere = getItem(position);

		viewHolder.textNumeClient.setText(pierdere.getNumeClient());
		viewHolder.textVenitLC.setText(nf.format(pierdere.getVenitLC()));
		viewHolder.textVenitLC_1.setText(nf.format(pierdere.getVenitLC1()));
		viewHolder.textVenitLC_2.setText(nf.format(pierdere.getVenitLC2()));

		viewHolder.detaliiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listener != null)
					listener.clientSelected(pierdere.getNumeClient());

			}
		});

		convertView.setBackgroundColor(colors[position % 2]);

		return convertView;
	}

	public int getCount() {
		return listPierderi.size();
	}

	public PierdereTipClient getItem(int position) {
		return listPierderi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
