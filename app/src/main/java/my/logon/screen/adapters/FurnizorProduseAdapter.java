package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanFurnizorProduse;

public class FurnizorProduseAdapter extends BaseAdapter {

	private Context context;
	private List<BeanFurnizorProduse> listFurnizoriProduse;

	static class ViewHolder {
		TextView textNumeItem;
	}

	public FurnizorProduseAdapter(Context context) {
		this.context = context;
	}

	public void setListFurnizoriProduse(List<BeanFurnizorProduse> listFurnizoriProduse) {
		this.listFurnizoriProduse = listFurnizoriProduse;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.simple_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeItem = (TextView) convertView.findViewById(R.id.textNumeItem);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanFurnizorProduse furnizor = (BeanFurnizorProduse) getItem(position);
		viewHolder.textNumeItem.setText(furnizor.getNumeFurnizorProduse());

		return convertView;

	}

	public int getCount() {
		return listFurnizoriProduse.size();
	}

	public Object getItem(int position) {
		return listFurnizoriProduse.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
