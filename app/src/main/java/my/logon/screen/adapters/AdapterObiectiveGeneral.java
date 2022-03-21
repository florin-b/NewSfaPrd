package my.logon.screen.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanAdresaGenerica;
import my.logon.screen.beans.BeanCategorieObiectiv;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanJudet;
import my.logon.screen.beans.BeanLinieObiectiv;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.dialogs.AdresaDialog;
import my.logon.screen.dialogs.CautaClientDialog;
import my.logon.screen.dialogs.InputTextDialog;
import my.logon.screen.dialogs.SelectCategorieObiectivDialog;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.dialogs.SelectStadiuObiectiv;
import my.logon.screen.dialogs.SelectStadiuSubantrepDialog;
import my.logon.screen.enums.EnumMotiveSuspendareObKA;
import my.logon.screen.enums.EnumNumeObiective;
import my.logon.screen.enums.EnumStadiuObiectivKA;
import my.logon.screen.enums.EnumStadiuSubantrep;
import my.logon.screen.listeners.AdresaDialogListener;
import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.InputTextDialogListener;
import my.logon.screen.listeners.SelectCategorieListener;
import my.logon.screen.listeners.SelectJudetListener;
import my.logon.screen.listeners.StadiuDialogListener;

public class AdapterObiectiveGeneral extends BaseAdapter implements InputTextDialogListener, CautaClientDialogListener, StadiuDialogListener,
		SelectJudetListener, SelectCategorieListener, AdresaDialogListener {

	private BeanObiectiveGenerale obiectiv;
	private Context context;
	private List<BeanLinieObiectiv> liniiObiective;
	private int position;

	public AdapterObiectiveGeneral(BeanObiectiveGenerale obiectiv, Context context, List<BeanLinieObiectiv> liniiObiective) {
		this.obiectiv = obiectiv;
		this.context = context;
		this.liniiObiective = liniiObiective;
	}

	public static class ViewHolder {
		TextView textNumeLinie, textValoareLinie;
		ImageButton btnModificaValoare;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_obiective_general, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeLinie = (TextView) convertView.findViewById(R.id.textNumeLinie);
			viewHolder.textValoareLinie = (TextView) convertView.findViewById(R.id.textValoareLinie);
			viewHolder.btnModificaValoare = (ImageButton) convertView.findViewById(R.id.btnModificaValoare);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanLinieObiectiv linieObiectiv = getItem(position);

		viewHolder.textNumeLinie.setText(linieObiectiv.getNumeObiectiv());
		viewHolder.textValoareLinie.setText(obiectiv.getValoareObiectiv(position));

		setModificaValoareListener(viewHolder, position);

		if (position % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	private void setModificaValoareListener(ViewHolder viewHolder, final int position) {
		viewHolder.btnModificaValoare.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(position);

			}
		});
	}

	private void showDialog(int position) {

		this.position = position;

		switch (position) {

		case 3:
			showSelectAdresaDialog();
			break;
		case 7:
			showSelectCategorieDialog();
			break;
		case 0:
		case 4:
		case 6:
		case 8:
		case 9:
		case 13:
			showModificaValoareDialog();
			break;
		case 2:
		case 10:
		case 11:
			showSelectDateDialog();
			break;
		case 12:
			showPrimariaEmitentaDialog();
			break;
		case 5:
			showCautaClientDialog();
			break;
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
			showSelectStadiuSubantrepDialog();
			break;
		case 1:
			showSelectStadiuObiectivDialog();
			break;

		}
	}

	private void showModificaValoareDialog() {
		InputTextDialog dialog = new InputTextDialog(EnumNumeObiective.getNumeObiectiv(position), obiectiv.getValoareObiectiv(position), context);
		dialog.setInputTextListener(AdapterObiectiveGeneral.this);
		dialog.show();
	}

	private void showSelectAdresaDialog() {
		AdresaDialog adresaDialog = new AdresaDialog(context, true);
		adresaDialog.setAdresaDialogListener(AdapterObiectiveGeneral.this);
		adresaDialog.show();
	}

	private void showPrimariaEmitentaDialog() {
		AdresaDialog primariaDialog = new AdresaDialog(context, false);
		primariaDialog.setAdresaDialogListener(AdapterObiectiveGeneral.this);
		primariaDialog.show();
	}

	private void showSelectDateDialog() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		SelectDateDialog dialogDate = new SelectDateDialog(context, datePickerListener, year, month, day);

		dialogDate.show();
	}

	private void showCautaClientDialog() {
		CautaClientDialog clientDialog = new CautaClientDialog(context);
		clientDialog.setClientSelectedListener(AdapterObiectiveGeneral.this);
		clientDialog.setClientObiectivKA(true);
		clientDialog.show();
	}

	private void showSelectStadiuSubantrepDialog() {
		SelectStadiuSubantrepDialog stadiuDialog = new SelectStadiuSubantrepDialog(EnumNumeObiective.getNumeObiectiv(position), context);
		stadiuDialog.setStadiuDialogListener(AdapterObiectiveGeneral.this);
		stadiuDialog.show();
	}

	private void showSelectStadiuObiectivDialog() {
		SelectStadiuObiectiv stadiuDialog = new SelectStadiuObiectiv(EnumNumeObiective.getNumeObiectiv(position), context);
		stadiuDialog.setStadiuDialogListener(AdapterObiectiveGeneral.this);
		stadiuDialog.show();
	}

	private void showSelectCategorieDialog() {
		SelectCategorieObiectivDialog categorieDialog = new SelectCategorieObiectivDialog(context);
		categorieDialog.setListCategoriiListener(AdapterObiectiveGeneral.this);
		categorieDialog.show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			int mYear = selectedYear;
			int mMonth = selectedMonth;
			int mDay = selectedDay;

			SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar calendar = new GregorianCalendar(mYear, mMonth, mDay);

			obiectiv.updateObiectiv(position, displayFormat.format(calendar.getTime()), null);
			notifyDataSetChanged();

		}
	};

	public int getCount() {
		return liniiObiective.size();
	}

	public BeanLinieObiectiv getItem(int position) {
		return liniiObiective.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void textSaved(String textValue) {
		obiectiv.updateObiectiv(position, textValue, null);
		notifyDataSetChanged();

	}

	public void clientSelected(BeanClient client) {
		obiectiv.updateObiectiv(position, client, null);
		notifyDataSetChanged();

	}

	public void stadiuSelected(EnumStadiuSubantrep stadiuObiectiv) {
		obiectiv.updateObiectiv(position, stadiuObiectiv, null);
		notifyDataSetChanged();

	}

	public void stadiuSelected(EnumStadiuObiectivKA stadiuObiectiv, EnumMotiveSuspendareObKA motivSuspendare) {
		obiectiv.updateObiectiv(position, stadiuObiectiv, motivSuspendare);
		notifyDataSetChanged();

	}

	public void judetSelected(BeanJudet judet) {
		obiectiv.updateObiectiv(position, judet, null);
		notifyDataSetChanged();

	}

	public void categorieSelected(BeanCategorieObiectiv categorie) {
		obiectiv.updateObiectiv(position, categorie, null);
		notifyDataSetChanged();

	}

	public void adresaSelected(BeanAdresaGenerica adresa) {
		obiectiv.updateObiectiv(position, adresa, null);
		notifyDataSetChanged();

	}

}
