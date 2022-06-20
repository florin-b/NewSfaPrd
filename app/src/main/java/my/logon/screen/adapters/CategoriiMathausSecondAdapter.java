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

public class CategoriiMathausSecondAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> parents;
    private HashMap<String, List<String>> childs;

    public CategoriiMathausSecondAdapter(Context context, List<String> parents, HashMap<String, List<String>> childs) {
        this.context = context;
        this.parents = parents;
        this.childs = childs;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childs.get(this.parents.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_second, null);
            TextView text = (TextView) convertView.findViewById(R.id.eventsListEventRowText);
            text.setText(childText);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childs.get(this.parents.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_third, null);
            TextView text = (TextView) convertView.findViewById(R.id.eventsListEventRowText);
            text.setText(headerTitle);
        }
        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
