package my.logon.screen.adapters;

import java.util.List;

import my.logon.screen.listeners.PierdDepartListener;
import my.logon.screen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import my.logon.screen.beans.PierdereDepart;

public class PierdereDepartAdapter extends BaseAdapter {

	private Context context;
	private List<PierdereDepart> listPierderi;

	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };
	private PierdDepartListener listener;

	public void setPierderiDepartListener(PierdDepartListener listener) {
		this.listener = listener;
	}

	public PierdereDepartAdapter(List<PierdereDepart> listPierderi, Context context) {
		this.context = context;
		this.listPierderi = listPierderi;

	}

	static class ViewHolder {
		public TextView textNumeAgent, txtClIstoric, textClientiPrezent, textClientiRest;
		public Button detaliiBtn;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.pierd_depart_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
			viewHolder.txtClIstoric = (TextView) convertView.findViewById(R.id.txtClIstoric);
			viewHolder.textClientiPrezent = (TextView) convertView.findViewById(R.id.textClientiPrezent);
			viewHolder.textClientiRest = (TextView) convertView.findViewById(R.id.textClientiRest);
			viewHolder.detaliiBtn = (Button) convertView.findViewById(R.id.btnDetalii);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final PierdereDepart pierdere = getItem(position);

		viewHolder.textNumeAgent.setText(pierdere.getNumeAgent());
		viewHolder.txtClIstoric.setText(String.valueOf(pierdere.getNrClientiIstoric()));
		viewHolder.textClientiPrezent.setText(String.valueOf(pierdere.getNrClientiCurent()));
		viewHolder.textClientiRest.setText(String.valueOf(pierdere.getNrClientiRest()));

		viewHolder.detaliiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listener != null)
					listener.agentDepartSelected(pierdere.getCodAgent(), pierdere.getNumeAgent());

			}
		});

		convertView.setBackgroundColor(colors[position % 2]);

		return convertView;
	}

	public int getCount() {
		return listPierderi.size();
	}

	public PierdereDepart getItem(int position) {
		return listPierderi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
