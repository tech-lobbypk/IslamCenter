package quran.islamCenter.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;
import quran.example.reading.R;


import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[] titles; // header titles

    // child data in format of header etitle, child title(ExpandedMenuModel)
    private Map<String, List<ExpandedMenuModel>> mListDataChild;
    ExpandableListView expandList;


    public ExpandableListAdapter(Context context, String[] titles, Map<String, List<ExpandedMenuModel>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.titles = titles;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    @Override
    public int getGroupCount()
    {
        return titles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<ExpandedMenuModel> ls = this.mListDataChild.get(this.titles[groupPosition]);
        int childCount = ls.size();
        if(childCount > 1)
        return childCount;
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.titles[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(LocaleHelper.getLanguage(mContext).equals("en"))
        {
            return this.mListDataChild.get(this.titles[groupPosition])
                    .get(childPosition)
                    .getEng_title();
        }
        return this.mListDataChild.get(this.titles[groupPosition])
                .get(childPosition)
                .getUrdu_title();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_menu, parent,false);
        }
        TextView lblListHeader = convertView.findViewById(R.id.group_title);
        lblListHeader.setGravity(Gravity.START);
        lblListHeader.setText(title);

        int leftIcon = R.drawable.expand_more_24px_black;
        if (isExpanded) leftIcon = R.drawable.expand_less_24px_black;
        List<ExpandedMenuModel> ls = this.mListDataChild.get(this.titles[groupPosition]);
        int childCount = ls.size();
        if(childCount == 1) // no children
        {
            leftIcon = R.drawable.arrow_white_24px;
        }
        if (LocaleHelper.getLanguage(mContext).equals("en")) {
            lblListHeader.setTypeface(Constants_data.custom_font_eng);
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(mContext, leftIcon), null, null, null);
        } else {
            lblListHeader.setTypeface(Constants_data.custom_font_urdu);
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(mContext, leftIcon), null);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String title = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_submenu, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.subgroup_title);
        txtListChild.setText(title);

        if(LocaleHelper.getLanguage(mContext).equals("en"))
        {
            txtListChild.setTypeface(Constants_data.custom_font_eng);
            txtListChild.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(mContext,R.drawable.arrow_right_24px),null,null,null);
        }
        else
        {
            txtListChild.setTypeface(Constants_data.custom_font_urdu);
            txtListChild.setCompoundDrawablesWithIntrinsicBounds(
                    null,null,ContextCompat.getDrawable(mContext,R.drawable.arrow_left_24px),null);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
