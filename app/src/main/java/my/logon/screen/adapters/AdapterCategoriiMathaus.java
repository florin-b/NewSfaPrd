package my.logon.screen.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.CategorieMathaus;

public class AdapterCategoriiMathaus extends BaseAdapter {

	private List<CategorieMathaus> listCategorii;
	private Context context;

	public AdapterCategoriiMathaus(Context context, List<CategorieMathaus> listCategorii) {
		this.context = context;
		this.listCategorii = listCategorii;
	}

	static class ViewHolder {
		TextView textNumeCategorie;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.categorie_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeCategorie = (TextView) convertView.findViewById(R.id.textNumeCategorie);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CategorieMathaus categorie = getItem(position);

		viewHolder.textNumeCategorie.setText(categorie.getNume());

		if (position == 0)
			viewHolder.textNumeCategorie.setTypeface(null, Typeface.NORMAL);
		else
			viewHolder.textNumeCategorie.setTypeface(viewHolder.textNumeCategorie.getTypeface(), Typeface.BOLD);

		return convertView;
	}

	public int getCount() {
		return listCategorii.size();
	}

	public CategorieMathaus getItem(int position) {
		return listCategorii.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}
