package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanDocumentRetur;
import my.logon.screen.enums.EnumTipOp;
import my.logon.screen.listeners.DocumentReturListener;

public class PaletiReturAdapter extends BaseAdapter {

	Context context;
	List<BeanDocumentRetur> listDocumente;
    DocumentReturListener documentListener;
	int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public PaletiReturAdapter(Context context, List<BeanDocumentRetur> listDocumente) {
		this.context = context;
		this.listDocumente = listDocumente;
        documentListener = (DocumentReturListener) context;
	}

	static class ViewHolder {
		TextView textNumarDocument, textDataDocument;
		CheckBox checkBoxSelect;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.palet_retur_item, parent, false);
			viewHolder.textNumarDocument = (TextView) convertView.findViewById(R.id.textNumarDocument);
			viewHolder.textDataDocument = (TextView) convertView.findViewById(R.id.textDataDocument);
			viewHolder.checkBoxSelect = (CheckBox) convertView.findViewById(R.id.checkBoxSelect);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanDocumentRetur docRetur = getItem(position);

		viewHolder.textNumarDocument.setText(docRetur.getNumar());
		viewHolder.textDataDocument.setText(docRetur.getData());
        viewHolder.checkBoxSelect.setOnCheckedChangeListener(null);
        viewHolder.checkBoxSelect.setChecked(docRetur.isSelectat());

		viewHolder.checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				docRetur.setSelectat(isChecked);

				if (isChecked)
                    documentListener.documentSelected(docRetur.getNumar(), EnumTipOp.ADAUGA);
				else
					documentListener.documentSelected(docRetur.getNumar(), EnumTipOp.ELIMINA);


			}
		});

		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}


	public int getCount() {
		return listDocumente.size();
	}


	public BeanDocumentRetur getItem(int position) {
		return listDocumente.get(position);
	}


	public long getItemId(int arg0) {
		return 0;
	}

}
