package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.MaterialNecesar;

public class ArticoleNecesarAdapter extends BaseAdapter {

	List<MaterialNecesar> listArticole;
	Context context;

	public ArticoleNecesarAdapter(List<MaterialNecesar> listArticole, Context context) {
		this.listArticole = listArticole;
		this.context = context;

	}

	static class ViewHolder {
		TextView textNrCrt, textNumeMaterial, textCodMaterial, textNumeSintetic, textCodSintetic, textCons30, textStoc,
				textPropunere, textCA, intrariI1, intrariI2, intrariI3;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_necesar, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeMaterial = (TextView) convertView.findViewById(R.id.textNumeMaterial);
			viewHolder.textCodMaterial = (TextView) convertView.findViewById(R.id.textCodMaterial);
			viewHolder.textNumeSintetic = (TextView) convertView.findViewById(R.id.textNumeSintetic);
			viewHolder.textCodSintetic = (TextView) convertView.findViewById(R.id.textCodSintetic);
			viewHolder.textCons30 = (TextView) convertView.findViewById(R.id.textCons30);
			viewHolder.textStoc = (TextView) convertView.findViewById(R.id.textStoc);
			viewHolder.textPropunere = (TextView) convertView.findViewById(R.id.textPropunere);
			viewHolder.textCA = (TextView) convertView.findViewById(R.id.textCA);
			viewHolder.intrariI1 = (TextView) convertView.findViewById(R.id.intrariI1);
			viewHolder.intrariI2 = (TextView) convertView.findViewById(R.id.intrariI2);
			viewHolder.intrariI3 = (TextView) convertView.findViewById(R.id.intrariI3);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MaterialNecesar articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeMaterial.setText(articol.getNumeArticol());
		viewHolder.textCodMaterial.setText(articol.getCodArticol());
		viewHolder.textCodSintetic.setText(articol.getCodSintetic());
		viewHolder.textNumeSintetic.setText(articol.getNumeSintetic());
		viewHolder.textCons30.setText(articol.getConsum30());
		viewHolder.textStoc.setText(articol.getStoc());
		viewHolder.textPropunere.setText(articol.getPropunereNecesar());
		viewHolder.textCA.setText(articol.getCA());
		viewHolder.intrariI1.setText(articol.getInterval1());
		viewHolder.intrariI2.setText(articol.getInterval2());
		viewHolder.intrariI3.setText(articol.getInterval3());

		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public MaterialNecesar getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
