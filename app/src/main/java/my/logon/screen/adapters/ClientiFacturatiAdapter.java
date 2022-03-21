package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanClientiFacturati;


public class ClientiFacturatiAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };
	private List<BeanClientiFacturati> listClientiFacturati;
	Context context;
	LayoutInflater inflater;
	String lunaSelectata;
	DecimalFormat formatter = new DecimalFormat("#,###.00");

	public interface showDetaliiFacturiInter {
		void showDetaliiFacturi(CharSequence codClient, CharSequence numeClient, String lunaSelectata);
	}

	public String afisareLunara(int lunaScadere) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, lunaScadere);
		String lunaAn = df.format(cal.getTime());
		return lunaAn;
	}

	public ClientiFacturatiAdapter(Context context, List<BeanClientiFacturati> myList) {
		this.context = context;
		this.listClientiFacturati = myList;

		inflater = LayoutInflater.from(this.context);

	}

	private class ViewHolder {
		public TextView textNrCrt, textCodClient, textNumeClient, textLuna1, textLuna2, textLuna3, textLuna4, textLuna5, textLuna6, textLuna7;

		public ImageButton btnDetalliFacturi;

		public ViewHolder(View item) {

			textNrCrt = (TextView) item.findViewById(R.id.txtNr);
			textCodClient = (TextView) item.findViewById(R.id.txtCodClient);
			textNumeClient = (TextView) item.findViewById(R.id.txtNumeClient);
			textLuna1 = (TextView) item.findViewById(R.id.txtValoareLuna1);
			textLuna2 = (TextView) item.findViewById(R.id.txtValoareLuna2);
			textLuna3 = (TextView) item.findViewById(R.id.txtValoareLuna3);
			textLuna4 = (TextView) item.findViewById(R.id.txtValoareLuna4);
			textLuna5 = (TextView) item.findViewById(R.id.txtValoareLuna5);
			textLuna6 = (TextView) item.findViewById(R.id.txtValoareLuna6);
			textLuna7 = (TextView) item.findViewById(R.id.txtValoareLuna7);

		}
	}

	
	public int getCount() {

		return listClientiFacturati.size();
	}

	
	public BeanClientiFacturati getItem(int position) {
		return listClientiFacturati.get(position);
	}

	
	public long getItemId(int position) {

		return 0;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {

		int colorPos = position % colors.length;

		final ViewHolder mviewHolder;

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.clienti_facturati_row, parent, false);

			mviewHolder = new ViewHolder(convertView);
			convertView.setTag(mviewHolder);
			convertView.setFocusableInTouchMode(false);

		} else {
			mviewHolder = (ViewHolder) convertView.getTag();
		}

		BeanClientiFacturati clientFacturat = getItem(position);

		mviewHolder.textNrCrt.setText(Integer.toString(position + 1));
		mviewHolder.textCodClient.setText(clientFacturat.getCodClient());
		mviewHolder.textNumeClient.setText(clientFacturat.getNumeClient());
		mviewHolder.textLuna1.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna1())));
		mviewHolder.textLuna2.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna2())));
		mviewHolder.textLuna3.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna3())));
		mviewHolder.textLuna4.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna4())));
		mviewHolder.textLuna5.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna5())));
		mviewHolder.textLuna6.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna6())));
		mviewHolder.textLuna7.setText(formatter.format(Double.parseDouble(clientFacturat.getLuna7())));

		mviewHolder.textLuna1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(0);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}
			}
		});

		mviewHolder.textLuna2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-1);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});

		mviewHolder.textLuna3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-2);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});

		mviewHolder.textLuna4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-3);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});

		mviewHolder.textLuna5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-4);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});

		mviewHolder.textLuna6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-5);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});

		mviewHolder.textLuna7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (context instanceof showDetaliiFacturiInter) {

					lunaSelectata = afisareLunara(-6);
					((showDetaliiFacturiInter) context).showDetaliiFacturi(mviewHolder.textCodClient.getText(), mviewHolder.textNumeClient.getText(),
							lunaSelectata);
				}

			}
		});
		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

}
