package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.enums.EnumStadiuObiectivKA;
import my.logon.screen.utils.UtilsFormatting;

public class AdapterObiectiveAfisare extends BaseAdapter {

	private List<BeanObiectivAfisare> listObiective;
	private Context context;

	public AdapterObiectiveAfisare(Context context, List<BeanObiectivAfisare> listObiective) {
		this.context = context;
		this.listObiective = listObiective;
	}

	public static class ViewHolder {
		TextView textNumeObiectiv, textBeneficiar, textStadiu, textDatacreare, textIdObiectiv;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.obiectiv_afis_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeObiectiv = (TextView) convertView.findViewById(R.id.textNumeObiectiv);
			viewHolder.textBeneficiar = (TextView) convertView.findViewById(R.id.textBeneficiar);
			viewHolder.textStadiu = (TextView) convertView.findViewById(R.id.textStadiu);
			viewHolder.textDatacreare = (TextView) convertView.findViewById(R.id.textDatacreare);
			viewHolder.textIdObiectiv = (TextView) convertView.findViewById(R.id.textIdObiectiv);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanObiectivAfisare obiectiv = getItem(position);

		String numeAgent = "";

		if (obiectiv.getNumeAgent() != null && obiectiv.getNumeAgent() != "null") {
			numeAgent = " [" + obiectiv.getNumeAgent() + "]";
		}
		viewHolder.textIdObiectiv.setText(obiectiv.getId() + numeAgent);
		viewHolder.textNumeObiectiv.setText(obiectiv.getNume());
		viewHolder.textBeneficiar.setText(obiectiv.getBeneficiar());
		viewHolder.textStadiu.setText(EnumStadiuObiectivKA.getNumeStadiu(Integer.parseInt(obiectiv.getCodStatus())));
		viewHolder.textDatacreare.setText(UtilsFormatting.formatDate(obiectiv.getData()));

		return convertView;
	}

	public int getCount() {
		return listObiective.size();
	}

	public BeanObiectivAfisare getItem(int position) {
		return listObiective.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}
