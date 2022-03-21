package my.logon.screen.adapters;

import java.util.List;

import my.logon.screen.listeners.PierdTotalListener;
import my.logon.screen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import my.logon.screen.beans.PierdereTotal;

public class PierdereTotalAdapter extends BaseAdapter {

	private Context context;
	private List<PierdereTotal> listPierderi;

	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };
	private PierdTotalListener listener;

	public void setPierderiTotalListener(PierdTotalListener listener) {
		this.listener = listener;
	}

	public PierdereTotalAdapter(List<PierdereTotal> listPierderi, Context context) {
		this.context = context;
		this.listPierderi = listPierderi;

	}

	static class ViewHolder {
		public TextView textUl, txtClIstoric, textClientiPrezent, textClientiRest;
		public Button detaliiBtn;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.pierd_total_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textUl = (TextView) convertView.findViewById(R.id.textUl);
			viewHolder.txtClIstoric = (TextView) convertView.findViewById(R.id.txtClIstoric);
			viewHolder.textClientiPrezent = (TextView) convertView.findViewById(R.id.textClientiPrezent);
			viewHolder.textClientiRest = (TextView) convertView.findViewById(R.id.textClientiRest);
			viewHolder.detaliiBtn = (Button) convertView.findViewById(R.id.btnDetalii);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final PierdereTotal pierdere = getItem(position);

		viewHolder.textUl.setText(pierdere.getUl());
		viewHolder.txtClIstoric.setText(String.valueOf(pierdere.getNrClientiIstoric()));
		viewHolder.textClientiPrezent.setText(String.valueOf(pierdere.getNrClientiCurent()));
		viewHolder.textClientiRest.setText(String.valueOf(pierdere.getNrClientiRest()));

		viewHolder.detaliiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listener != null)
					listener.filialaSelected(pierdere.getUl());

			}
		});

		convertView.setBackgroundColor(colors[position % 2]);

		return convertView;
	}

	public int getCount() {
		return listPierderi.size();
	}

	public PierdereTotal getItem(int position) {
		return listPierderi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
