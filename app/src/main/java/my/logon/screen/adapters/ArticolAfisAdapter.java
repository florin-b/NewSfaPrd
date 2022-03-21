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
import my.logon.screen.beans.ValoriComanda;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.utils.UtilsGeneral;


public class ArticolAfisAdapter extends BaseAdapter {

	private Context context;
	private List<ArticolComanda> listArticole;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	private NumberFormat nf3;
	private NumberFormat nf2;
	private NumberFormat nf4;

	public ArticolAfisAdapter(Context context, List<ArticolComanda> listArticole) {
		this.context = context;
		this.listArticole = listArticole;

		nf3 = new DecimalFormat("#0.000");
		nf2 = new DecimalFormat("#0.00");
		nf4 = new DecimalFormat("#0.0000");

	}

	public static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textUmArt, textPretArt, textMonedaArt, textDepozit, textStatusArt, textProcRed;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % colors.length;
		String unitPret = "RON";

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_afis_comenzi, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textUmArt = (TextView) convertView.findViewById(R.id.textUmArt);
			viewHolder.textPretArt = (TextView) convertView.findViewById(R.id.textPretArt);
			viewHolder.textMonedaArt = (TextView) convertView.findViewById(R.id.textMonedaArt);
			viewHolder.textDepozit = (TextView) convertView.findViewById(R.id.textDepozit);
			viewHolder.textStatusArt = (TextView) convertView.findViewById(R.id.textStatusArt);
			viewHolder.textProcRed = (TextView) convertView.findViewById(R.id.textProcRed);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolComanda articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeArt.setText(articol.getNumeArticol());
		viewHolder.textCodArt.setText(articol.getCodArticol());
		viewHolder.textCantArt.setText(nf3.format(articol.getCantitate()));

		if (!articol.getUmb().equals(articol.getUm())) {
			unitPret = "RON/" + System.getProperty("line.separator") + articol.getUmb();
		}

		viewHolder.textUmArt.setText(articol.getUm());
		if (articol.getDepart().equals("02"))
			viewHolder.textPretArt.setText(nf4.format(articol.getPretUnit()));
		else
			viewHolder.textPretArt.setText(nf2.format(articol.getPretUnit()));

		viewHolder.textMonedaArt.setText(unitPret);
		viewHolder.textDepozit.setText(articol.getDepozit());
		viewHolder.textStatusArt.setText(UtilsGeneral.getDescStatusArt(articol.getStatus()));
		viewHolder.textProcRed.setText(nf2.format(articol.getProcent()));

		if (colorPos % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	public ValoriComanda getValoriComanda(List<ArticolComanda> articol) {

		double marja = 0;

		ValoriComanda valoriComanda = new ValoriComanda();

		for (ArticolComanda art : articol) {

			if (art.getTipArt().equals("B")) {
				valoriComanda.setPondereB(art.getPret() + valoriComanda.getPondereB());
			}

			valoriComanda.setTotal(art.getPret() + valoriComanda.getTotal());

			marja = (art.getPretUnit() / art.getMultiplu() - art.getCmp()) * art.getCantitate();
			valoriComanda.setMarja(marja + valoriComanda.getMarja());
		}

		return valoriComanda;
	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolComanda getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
