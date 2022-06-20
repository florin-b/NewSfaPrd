package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;

public class CategoriiMathausAdapter extends BaseExpandableListAdapter {

    private List<String> parentHeaders;
    private HashMap<String, List<String>> listDataChild1;
    private Context context;

    public CategoriiMathausAdapter(Context context, List<String> parentHeaders, HashMap<String, List<String>> listDataChild1) {
        this.context = context;
        this.parentHeaders = parentHeaders;
        this.listDataChild1 = listDataChild1;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textTitle);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_mathaus, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.textDesc);

        txtListChild.setText(childText);
        return convertView;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild1.get(this.parentHeaders.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild1.get(this.parentHeaders.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return parentHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parentHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
