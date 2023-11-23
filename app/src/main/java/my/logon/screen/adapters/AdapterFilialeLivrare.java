package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanFilialaLivrare;

public class AdapterFilialeLivrare extends BaseAdapter {

	private List<BeanFilialaLivrare> listFiliale;
	private Context context;

	public AdapterFilialeLivrare(Context context, List<BeanFilialaLivrare> listFiliale) {
		this.context = context;
		this.listFiliale = listFiliale;

	}

	static class ViewHolder {
		TextView textNumeFiliala;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_filiala_livrare, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.textNumeFiliala = (TextView) convertView.findViewById(R.id.textNumeFiliala);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanFilialaLivrare filialaLivrare = getItem(position);
		viewHolder.textNumeFiliala.setText(filialaLivrare.getNumeFiliala());

		return convertView;

	}

	@Override
	public int getCount() {
		return listFiliale.size();
	}

	@Override
	public BeanFilialaLivrare getItem(int position) {
		return listFiliale.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
