package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanUrmarireEveniment;


public class AdapterObiectiveUrmarire extends BaseAdapter {

	
	private Context context;
	private List<BeanUrmarireEveniment> liniiUrmarire;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public AdapterObiectiveUrmarire( Context context,
			List<BeanUrmarireEveniment> liniiUrmarire) {
		
		this.context = context;
		this.liniiUrmarire = liniiUrmarire;
	}

	public static class ViewHolder {
		TextView textNumeLinie, textStare, textObservatii;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_obiective_urmarire, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeLinie = (TextView) convertView.findViewById(R.id.textNumeLinie);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textObservatii = (TextView) convertView.findViewById(R.id.textObservatii);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanUrmarireEveniment linieObiectiv = getItem(position);

		viewHolder.textNumeLinie.setText(linieObiectiv.getNumeEveniment());
		viewHolder.textStare.setText(linieObiectiv.getData());
		viewHolder.textObservatii.setText(linieObiectiv.getObservatii());

		convertView.setBackgroundColor(colors[position % 2]);

		return convertView;
	}

	public int getCount() {
		return liniiUrmarire.size();
	}

	public BeanUrmarireEveniment getItem(int position) {
		return liniiUrmarire.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void setListEvenimente(List<BeanUrmarireEveniment> liniiUrmarire) {
		this.liniiUrmarire = liniiUrmarire;
	}

}
