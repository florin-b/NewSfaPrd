package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectivDepartament;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;


public class AdapterObiective extends BaseAdapter {

	private List<BeanObiectivDepartament> listObiective;
	private Context context;

	public AdapterObiective(Context context, List<BeanObiectivDepartament> listObiective) {
		this.context = context;
		this.listObiective = listObiective;
	}

	public static class ViewHolder {
		TextView textNumeObiectiv, textBeneficiar, textAdresa, textDatacreare;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.obiectiv_depart_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeObiectiv = (TextView) convertView.findViewById(R.id.textNumeObiectiv);
			viewHolder.textBeneficiar = (TextView) convertView.findViewById(R.id.textBeneficiar);
			viewHolder.textAdresa = (TextView) convertView.findViewById(R.id.textAdresa);
			viewHolder.textDatacreare = (TextView) convertView.findViewById(R.id.textDatacreare);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanObiectivDepartament obiectiv = getItem(position);

		viewHolder.textNumeObiectiv.setText(obiectiv.getNume());
		viewHolder.textBeneficiar.setText(obiectiv.getBeneficiar());
		viewHolder.textAdresa.setText(getAdresa(obiectiv.getAdresa()));
		viewHolder.textDatacreare.setText(UtilsFormatting.formatDate(obiectiv.getDataCreare()));

		return convertView;
	}

	public int getCount() {
		return listObiective.size();
	}

	public BeanObiectivDepartament getItem(int position) {
		return listObiective.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private String getAdresa(String strAdresa) {
		String retAdresa = " ";

		if (strAdresa.length() > 1) {
			String[] adresa = strAdresa.split("/");

			retAdresa = UtilsGeneral.getNumeJudet(adresa[0]) + "/" + adresa[1];

			if (adresa.length == 3 && adresa[2] != null)
				retAdresa += "/" + adresa[2];
		}
		return retAdresa;
	}

}
