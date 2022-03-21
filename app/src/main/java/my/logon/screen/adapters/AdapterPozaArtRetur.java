package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.PozaArticol;


public class AdapterPozaArtRetur extends BaseAdapter {

	private List<PozaArticol> listPoze;
	private Context context;

	public AdapterPozaArtRetur(List<PozaArticol> listPoze, Context context) {
		this.listPoze = listPoze;
		this.context = context;

	}

	public static class ViewHolder {
		TextView textNumePoza;
		ImageButton btnStergePoza;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_poza_art_retur, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNumePoza = (TextView) convertView.findViewById(R.id.textNumePoza);
			viewHolder.btnStergePoza = (ImageButton) convertView.findViewById(R.id.btnStergePoza);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		PozaArticol pozaArticol = getItem(position);

		viewHolder.textNumePoza.setText(pozaArticol.getNume());
		viewHolder.btnStergePoza.setVisibility(View.VISIBLE);

		setStergeArticolListener(viewHolder, pozaArticol);

		return convertView;

	}

	private void setStergeArticolListener(ViewHolder viewHolder, final PozaArticol pozaArticol) {
		viewHolder.btnStergePoza.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				listPoze.remove(pozaArticol);
				notifyDataSetChanged();

			}

		});

	}

	public int getCount() {
		return listPoze.size();
	}

	public PozaArticol getItem(int position) {
		return listPoze.get(position);

	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

	}

	public void setPozeArticol(List<PozaArticol> listPoze) {

		this.listPoze = listPoze;
		notifyDataSetChanged();
	}

	public long getItemId(int itemId) {
		return itemId;
	}

}
