package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.R;
import my.logon.screen.listeners.CantCablu05Listener;



public class AdapterCabluri extends BaseAdapter {

	private List<BeanCablu05> listCabluri;
	private Context context;
	private double cantArticol;
	private CantCablu05Listener listener;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public AdapterCabluri(Context context, List<BeanCablu05> listCabluri, double cantitate) {
		this.context = context;
		this.listCabluri = listCabluri;
		this.cantArticol = cantitate;

	}

	static class ViewHolder {
		TextView textNumeBoxa, textStoc, textCantitate;
		CheckBox checkSelect;
		LinearLayout layoutUtilizat;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_cablu, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeBoxa = (TextView) convertView.findViewById(R.id.textNumeBoxa);
			viewHolder.textStoc = (TextView) convertView.findViewById(R.id.textStoc);
			viewHolder.textCantitate = (TextView) convertView.findViewById(R.id.textUtilizat);
			viewHolder.checkSelect = (CheckBox) convertView.findViewById(R.id.checkSelect);
			viewHolder.layoutUtilizat = (LinearLayout) convertView.findViewById(R.id.layoutUtilizat);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanCablu05 cablu = getItem(position);

		viewHolder.textNumeBoxa.setText(cablu.getNumeBoxa());
		viewHolder.textStoc.setText(cablu.getStoc());

		if (cablu.getCantitate() != null && !cablu.getCantitate().isEmpty() && Double.parseDouble(cablu.getCantitate()) > 0) {
			viewHolder.textCantitate.setText(cablu.getCantitate());
			viewHolder.layoutUtilizat.setVisibility(View.VISIBLE);
		} else {
			viewHolder.textCantitate.setText("");
			viewHolder.layoutUtilizat.setVisibility(View.INVISIBLE);
		}

		viewHolder.checkSelect.setChecked(cablu.isSelected());
		viewHolder.checkSelect.setTag(position);

		viewHolder.checkSelect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int currentPos = (Integer) v.getTag();
				boolean isChecked = false;
				if (!listCabluri.get(currentPos).isSelected()) {
					isChecked = true;
				}

				setCantitateBoxa(currentPos, isChecked);

				if (isChecked && Double.parseDouble(listCabluri.get(currentPos).getCantitate()) > 0)
					listCabluri.get(currentPos).setSelected(isChecked);

				if (!isChecked && Double.parseDouble(listCabluri.get(currentPos).getCantitate()) == 0)
					listCabluri.get(currentPos).setSelected(isChecked);

				notifyDataSetChanged();
			}
		});

		convertView.setBackgroundColor(colors[position % colors.length]);

		return convertView;

	}

	private void setCantitateBoxa(int currentPos, boolean itemCheck) {

		double stocBoxa = Double.parseDouble(listCabluri.get(currentPos).getStoc());

		if (itemCheck) {

			if (stocBoxa <= cantArticol) {
				listCabluri.get(currentPos).setCantitate(String.valueOf(stocBoxa));
				cantArticol -= stocBoxa;
			} else if (stocBoxa > cantArticol) {
				listCabluri.get(currentPos).setCantitate(String.valueOf(cantArticol));
				cantArticol = 0;
			}

		} else {
			cantArticol += Double.parseDouble(listCabluri.get(currentPos).getCantitate());
			listCabluri.get(currentPos).setCantitate("0");

		}

		if (listener != null)
			listener.cantitateCabluChanged(String.valueOf(cantArticol));

	}

	public void setCantCabluListener(CantCablu05Listener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return listCabluri.size();
	}

	@Override
	public BeanCablu05 getItem(int position) {
		return listCabluri.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
