package codepath.com.goingout.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codepath.com.goingout.EventListActivity;
import codepath.com.goingout.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> mListDataChild;
    ExpandableListView expandList;

    public ArrayList<String> categoryFilter;
    public String locationFilter;
    public int priceFilter;
    public int ratingFilter;
    private String dateFilter;



    public ExpandableListAdapter(Context context, List<String> listDataHeader,HashMap<String, List<String>> listChildData,ExpandableListView mView)
    {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList=mView;
    }

    @Override
    public int getGroupCount() {
        int i= mListDataHeader.size();
        Log.d("GROUPCOUNT",String.valueOf(i));
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount=0;
        if(groupPosition<6)
        {
            childCount=this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                    .size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("CHILD", mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition));
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View   convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.submenu);
        ImageView headerIcon=    (ImageView)convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        headerIcon.setImageResource(EventListActivity.images[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);

        }
        ToggleButton txtListChild = (ToggleButton) convertView
                .findViewById(R.id.filter_submenu);

        txtListChild.setText(childText);
        txtListChild.setTextOn(childText);
        txtListChild.setTextOff(childText);

        if (!txtListChild.isChecked()){
            txtListChild.setBackgroundResource(R.drawable.filter_selector);
        } else{
            txtListChild.setBackgroundResource(R.drawable.filter_selector);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
