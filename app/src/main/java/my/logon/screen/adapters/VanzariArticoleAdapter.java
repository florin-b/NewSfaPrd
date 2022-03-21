/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import my.logon.screen.beans.VanzariArticol;

public class VanzariArticoleAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };

	private Context context;
	private List<VanzariArticol> listArticole;
	NumberFormat nf;

	static class ViewHolder {
		public TextView textNrCrt, textNumeAgent, textNumeArt, textCodArt, textCantArt, textValArt;
	}

	public VanzariArticoleAdapter(Context context, List<VanzariArticol> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
		nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		int colorPos = position % colors.length;

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.custom_art_row_vanzari_ag_2, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textValArt = (TextView) convertView.findViewById(R.id.textValArt);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		VanzariArticol articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeAgent.setText(articol.getNumeAgent());
		viewHolder.textNumeArt.setText(articol.getNumeArt());
		viewHolder.textCodArt.setText(articol.getCodArt());
		viewHolder.textValArt.setText(nf.format(Double.valueOf(articol.getValArt())));

		if (articol.getCantArt().toLowerCase().contains("total")) {
			viewHolder.textCantArt.setText("Total: ");
			viewHolder.textNrCrt.setText("");
		} else {
			viewHolder.textNrCrt.setText(String.valueOf(position + 1));
			viewHolder.textCantArt.setText(nf.format(Double.valueOf(articol.getCantArt())));
		}

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;

	}

	public int getCount() {
		return listArticole.size();
	}

	public VanzariArticol getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setListArticole(List<VanzariArticol> listArticole) {
		this.listArticole = listArticole;
	}

}