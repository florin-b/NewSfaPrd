package my.logon.screen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.model.ConstructoriDepart;


public class AdapterObiectiveConstructori extends BaseAdapter {

	private List<BeanObiectiveConstructori> listConstructori;
	private Context context;
	private String codDepart;
	private List<BeanObiectiveConstructori> listConstructoriDepart;

	public AdapterObiectiveConstructori(List<BeanObiectiveConstructori> listConstructori, Context context) {
		this.listConstructori = listConstructori;
		this.context = context;
		setListConstructoriDepart();

	}

	public static class ViewHolder {
		TextView textNrCrt, textNumeClient, textStareClient;
		ImageButton btnStergeClient;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_obiective_constructori, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textStareClient = (TextView) convertView.findViewById(R.id.textStareClient);
			viewHolder.btnStergeClient = (ImageButton) convertView.findViewById(R.id.btnStergeClient);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanObiectiveConstructori constructor = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeClient.setText(constructor.getNumeClient());
		viewHolder.textNumeClient.setTextColor(context.getResources().getColor(R.color.color_light_green));
		viewHolder.textStareClient.setText(constructor.getStare());
		viewHolder.btnStergeClient.setVisibility(View.VISIBLE);

		setStergeClientListener(viewHolder, constructor);

		if (position % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;

	}

	private void setStergeClientListener(ViewHolder viewHolder, final BeanObiectiveConstructori constructor) {
		viewHolder.btnStergeClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDeleteClientDialog(constructor);
			}

		});

	}

	private void showDeleteClientDialog(final BeanObiectiveConstructori constructor) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Confirmare");
		alertDialogBuilder.setMessage("Stergeti clientul " + constructor.getNumeClient() + "?").setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listConstructori.remove(constructor);
						notifyDataSetChanged();
					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public int getCount() {
		return listConstructoriDepart.size();
	}

	public BeanObiectiveConstructori getItem(int position) {
		return listConstructoriDepart.get(position);

	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		setListConstructoriDepart();
	}

	private void setListConstructoriDepart() {
		ConstructoriDepart constr = new ConstructoriDepart();
		listConstructoriDepart = constr.constructoriDepart(listConstructori, codDepart);
		BeanObiectiveGenerale.getInstance().setListConstructori(listConstructori);

	}

	public long getItemId(int itemId) {
		return itemId;
	}

}
