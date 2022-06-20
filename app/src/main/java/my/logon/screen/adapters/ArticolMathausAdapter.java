package my.logon.screen.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ArticolMathaus;
import my.logon.screen.listeners.AdapterMathausListener;
import my.logon.screen.model.DownloadImageTask;

public class ArticolMathausAdapter extends BaseAdapter {

	private Context context;
	private List<ArticolMathaus> listArticole;
	private AdapterMathausListener listener;

	public ArticolMathausAdapter(Context context, List<ArticolMathaus> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	@Override
	public int getCount() {
		return listArticole.size();
	}

	@Override
	public ArticolMathaus getItem(int position) {
		return listArticole.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ArticolMathaus articol = listArticole.get(position);

		if (convertView == null) {
			final LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.art_mathaus_layout, null);
		}

		final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_art);
		final TextView nameTextView = (TextView) convertView.findViewById(R.id.textview_nume);
		final Button butonAdauga = (Button) convertView.findViewById(R.id.buttonAdd);
		final TextView pretTextView = (TextView) convertView.findViewById(R.id.textview_pret);
		final TextView stocTextView = (TextView) convertView.findViewById(R.id.textview_stoc);

		setListenerAdauga(butonAdauga, articol);

		nameTextView.setText(articol.getNume());
		pretTextView.setText(articol.getCod().replaceFirst("^0*", ""));

		/*
		if (articol.getStoc() == null || articol.getStoc().equals("null"))
			stocTextView.setText(" ");
		else if (Double.valueOf(articol.getStoc()) < 0)
			stocTextView.setText("0 " + articol.getUmVanz());
		else
			stocTextView.setText(articol.getStoc() + " " + articol.getUmVanz());

		 */

		// if (imageView.getDrawable() == null)
		// if (!hasImage(imageView))
		new DownloadImageTask(imageView).execute(articol.getAdresaImg());

		convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	private boolean hasImage(ImageView view) {
		Drawable drawable = view.getDrawable();
		boolean hasImage = (drawable != null);

		if (hasImage && (drawable instanceof BitmapDrawable)) {
			hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
		}

		return hasImage;
	}

	private void setListenerAdauga(Button buttonAdd, final ArticolMathaus articol) {
		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showArticolDialog(articol);

			}
		});
	}

	public void setArticolMathausListener(AdapterMathausListener listener) {
		this.listener = listener;
	}

	private void showArticolDialog(ArticolMathaus articol) {

		if (listener != null) {
			listener.articolMathausSelected(articol);
		}

	}

}
