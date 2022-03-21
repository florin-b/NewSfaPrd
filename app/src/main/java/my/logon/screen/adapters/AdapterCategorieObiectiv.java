package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanCategorieObiectiv;


public class AdapterCategorieObiectiv extends BaseAdapter {

	private List<BeanCategorieObiectiv> listCategorii;
	private Context context;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	static class ViewHolder {
		TextView textNumeCategorie, textCodCategorie;
	}

	public AdapterCategorieObiectiv(Context context, List<BeanCategorieObiectiv> listCategorii) {
		this.listCategorii = listCategorii;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.generic_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeCategorie = (TextView) convertView.findViewById(R.id.textNume);
			viewHolder.textCodCategorie = (TextView) convertView.findViewById(R.id.textCod);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanCategorieObiectiv judet = getItem(position);
		viewHolder.textNumeCategorie.setText(judet.getNumeCategorie());
		viewHolder.textCodCategorie.setText(judet.getCodCategorie());

		convertView.setBackgroundColor(colors[position % colors.length]);

		return convertView;
	}

	public int getCount() {
		return listCategorii.size();
	}

	public BeanCategorieObiectiv getItem(int position) {
		return listCategorii.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

}
