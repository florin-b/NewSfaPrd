package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanArticolConcurenta;
import my.logon.screen.dialogs.AdaugaPretDialog;
import my.logon.screen.dialogs.TextDialog;
import my.logon.screen.listeners.InputTextDialogListener;
import my.logon.screen.listeners.TextDialogListener;


public class AdapterArticolConcurenta extends BaseAdapter implements InputTextDialogListener, TextDialogListener {

	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };
	Context context;
	List<BeanArticolConcurenta> listArticole;
	private int selectedPos;

	static class ViewHolder {
		public TextView textNumeArticol, textCodArticol, textTipArt, textData;
		public TextView textValoare;
		public Button adaugaPret, observatii;
	}

	public AdapterArticolConcurenta(Context context, List<BeanArticolConcurenta> listArticole) {
		this.context = context;
		this.listArticole = listArticole;

	}

	public void setListArticole(List<BeanArticolConcurenta> listArticole) {
		this.listArticole = listArticole;

		notifyDataSetChanged();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_concurenta, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
			viewHolder.textCodArticol = (TextView) convertView.findViewById(R.id.textCodArticol);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textValoare = (TextView) convertView.findViewById(R.id.textValoare);
			viewHolder.adaugaPret = (Button) convertView.findViewById(R.id.adaugaPret);
			viewHolder.observatii = (Button) convertView.findViewById(R.id.observatii);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final BeanArticolConcurenta articol = getItem(position);
		viewHolder.textNumeArticol.setText(articol.getNume());
		viewHolder.textCodArticol.setText(articol.getCod());
		viewHolder.textData.setText(articol.getDataValoare());

		viewHolder.textValoare.setText(articol.getValoare());

		viewHolder.adaugaPret.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AdaugaPretDialog dialog = new AdaugaPretDialog("Pret articol " + articol.getNume(), articol.getValoare(), context);
				dialog.setModificaObiectivListener(AdapterArticolConcurenta.this);
				dialog.show();
				selectedPos = position;

			}
		});

		viewHolder.observatii.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TextDialog inputDialog = new TextDialog("Observatii articol", articol.getObservatii(), context);
				inputDialog.setTextListener(AdapterArticolConcurenta.this);
				inputDialog.show();
				selectedPos = position;

			}
		});

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public BeanArticolConcurenta getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void textSaved(String textValue) {
		listArticole.get(selectedPos).setValoare(textValue);
		notifyDataSetChanged();

	}

	
	public void textDialogSaved(String textValue) {
		listArticole.get(selectedPos).setObservatii(textValue);
		notifyDataSetChanged();

	}

}
