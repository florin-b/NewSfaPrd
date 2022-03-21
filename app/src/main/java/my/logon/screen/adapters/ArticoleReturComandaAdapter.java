package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.enums.EnumMotivRespArticol;

public class ArticoleReturComandaAdapter extends BaseAdapter {

	private Context context;
	private List<BeanArticolRetur> listArticole;

	public static class ViewHolder {
		TextView textCrt, textNumeArticol, textCodArticol, textCantitate, textUm, textCantitateRetur, labelRetur, textMotivRetur, textInlocuire;
	}

	public ArticoleReturComandaAdapter(Context context, List<BeanArticolRetur> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int cPos = position % 2;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_retur_comanda_item, parent, false);
			viewHolder.textCrt = (TextView) convertView.findViewById(R.id.textCrt);
			viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
			viewHolder.textCodArticol = (TextView) convertView.findViewById(R.id.textCodArticol);
			viewHolder.textCantitate = (TextView) convertView.findViewById(R.id.textCantitate);
			viewHolder.textUm = (TextView) convertView.findViewById(R.id.textUm);
			viewHolder.textCantitateRetur = (TextView) convertView.findViewById(R.id.textCantitateRetur);
			viewHolder.labelRetur = (TextView) convertView.findViewById(R.id.labelRetur);
			viewHolder.textMotivRetur = (TextView) convertView.findViewById(R.id.textMotivRetur);
			viewHolder.textInlocuire = (TextView) convertView.findViewById(R.id.textInlocuire);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanArticolRetur articol = getItem(position);

		viewHolder.textCrt.setText(String.valueOf(++position) + ".");
		viewHolder.textNumeArticol.setText(articol.getNume());
		viewHolder.textCodArticol.setText(articol.getCod());
		viewHolder.textCantitate.setText(String.valueOf(articol.getCantitate()));
		viewHolder.textUm.setText(articol.getUm());
		viewHolder.textCantitateRetur.setText(String.valueOf(articol.getCantitateRetur()));

		if (articol.getCantitateRetur() > 0) {
			viewHolder.textCantitateRetur.setVisibility(View.VISIBLE);
			viewHolder.labelRetur.setVisibility(View.VISIBLE);
		} else {
			viewHolder.textCantitateRetur.setVisibility(View.INVISIBLE);
			viewHolder.labelRetur.setVisibility(View.INVISIBLE);
		}

		if (articol.getMotivRespingere() == null)
			viewHolder.textMotivRetur.setText("");
		else
			viewHolder.textMotivRetur.setText(EnumMotivRespArticol.getNumeRetur(articol.getMotivRespingere()));

		viewHolder.textInlocuire.setText(articol.isInlocuire() ? "Inlocuire" : "");

		if (cPos % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	public int getCount() {
		if (listArticole == null)
			return 0;
		return listArticole.size();
	}

	public BeanArticolRetur getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

}
