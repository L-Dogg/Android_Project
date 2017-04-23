package pl.kuc_industries.warsawnavihelper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import pl.kuc_industries.warsawnavihelper.R;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mExpandableListTitle;
    private Map<String, List<String>> mExpandableListDetail;
    private LayoutInflater mLayoutInflater;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       Map<String, List<String>> expandableListDetail) {
        mContext = context;
        mExpandableListTitle = expandableListTitle;
        mExpandableListDetail = expandableListDetail;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mExpandableListDetail.get(mExpandableListTitle.get(listPosition))
            .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition,
                             final int expandedListPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            //convertView = mLayoutInflater.inflate(R.layout.tram_list_item, null);
            int type = getChildType(listPosition, expandedListPosition);
            switch(type) {
                case ItemTypes.TRAMS_AND_BUSES:
                    convertView = mLayoutInflater.inflate(R.layout.tram_list_item, null);
                    break;
                case ItemTypes.ATMS:
                    convertView = mLayoutInflater.inflate(R.layout.atm_list_item, null);
                    break;
                //TODO: change after implementing new view
                default:
                    convertView = mLayoutInflater.inflate(R.layout.tram_list_item, null);
                    break;
            }
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;//TODO: might need change
    }

    @Override
    public Object getGroup(int listPosition) {
        return mExpandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mExpandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
            .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    // getChildTypeCount() is necessary to alternate expandable list's items' layout!
    @Override
    public int getChildTypeCount () { return ItemTypes.TYPE_COUNT; }

    // getChildTypeCount() is necessary to alternate expandable list's items' layout!
    @Override
    public int getChildType (int groupPosition, int childPosition) {
    /*
     * In case of group 1 this function returns 1,
     * in case of group 2 it returns 2, and so on.
     */
        return groupPosition;
    }


}
