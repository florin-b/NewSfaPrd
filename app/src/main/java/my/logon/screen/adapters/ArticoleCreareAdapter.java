package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.utils.UtilsComenzi;

public class ArticoleCreareAdapter extends BaseAdapter {

	Context context;
	List<ArticolComanda> listArticole;
	NumberFormat numberFormat = new DecimalFormat("#0.00");

	public ArticoleCreareAdapter(List<ArticolComanda> listArticole, Context context) {
		this.context = context;
		this.listArticole = listArticole;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.customrowview, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textPretUnit = (TextView) convertView.findViewById(R.id.textPretUnit);
			viewHolder.textUmArt = (TextView) convertView.findViewById(R.id.textUmArt);
			viewHolder.textPretArt = (TextView) convertView.findViewById(R.id.textPretArt);
			viewHolder.textProcRed = (TextView) convertView.findViewById(R.id.textProcRed);
			viewHolder.textUmBaza = (TextView) convertView.findViewById(R.id.textUmBaza);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolComanda articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeArt.setText(articol.getNumeArticol());
		viewHolder.textCodArt.setText(articol.getCodArticol());
		viewHolder.textCantArt.setText(numberFormat.format(articol.getCantitate()));

		if (UtilsComenzi.isArticolCustodieDistrib(articol))
			viewHolder.textPretUnit.setText("0.00");
		else
			viewHolder.textPretUnit.setText(numberFormat.format(articol.getPretUnit()));

		viewHolder.textUmArt.setText(articol.getUm());

		if (UtilsComenzi.isArticolCustodieDistrib(articol))
			viewHolder.textPretArt.setText("0.00");
		else
			viewHolder.textPretArt.setText(numberFormat.format(articol.getPret()));

		viewHolder.textProcRed.setText(numberFormat.format(articol.getProcent()));
		viewHolder.textUmBaza.setText(articol.getUmb());

		return convertView;
	}

	static class ViewHolder {
		public TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textPretUnit, textUmArt, textPretArt,
				textProcRed, textUmBaza;

	}

	public int getCount() {
		return listArticole.size();
	}

	
	public ArticolComanda getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setListArticole(List<ArticolComanda> listArticole) {
		this.listArticole = listArticole;
	}

}
