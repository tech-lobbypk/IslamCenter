package quran.example.reading;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import quran.islamCenter.adapters.Divider_RecyclerView_Decorator;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     BottomSheetFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link BottomSheetFragment.Listener}.</p>
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private Listener mListener;
    private List<BottomSheet_Model> bottomsheet_items;
    private int row_selected; // this variable contains the row number for which bottom sheet has been initiated

    public BottomSheetFragment(List<BottomSheet_Model> ls, int row_selected)
    {
        bottomsheet_items = ls;
        this.row_selected = row_selected;
    }

    // TODO: Customize parameters
    public static BottomSheetFragment newInstance(List<BottomSheet_Model> ls,int row_selected) {
        final BottomSheetFragment fragment = new BottomSheetFragment(ls,row_selected);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragmentbottomsheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new Divider_RecyclerView_Decorator(getContext().getResources().getDrawable(R.drawable.divider)));
        fragment_bottom_sheetAdapter adapter = new fragment_bottom_sheetAdapter(bottomsheet_items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onfragment_bottom_sheetClicked(int bottom_sheet_option, int row_position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        //final ImageView icon;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_fragmentbottomsheet_content, parent, false));
            text = (TextView) itemView.findViewById(R.id.text);
          //  icon = (ImageView) itemView.findViewById(R.id.imgIcon);

        }

        public void setData(BottomSheet_Model item)
        {
            text.setText(item.getText());
            Drawable icon = getContext().getResources().getDrawable(item.getIconID());
            if(LocaleHelper.getLanguage(getContext()).equals("en")) {
                text.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                text.setTypeface(Constants_data.custom_font_eng);
                text.setTextSize(25);
            }
            else
            {
                text.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                text.setTypeface(Constants_data.custom_font_urdu);
                text.setTextSize(30);
            }
            text.setCompoundDrawablePadding(20);
            text.setPadding(20,20,20,20);
            if(getAdapterPosition()%2 == 0)
            {
                text.setTextColor(getContext().getResources().getColor(R.color.blue_dark));
            }
            else
            {
                text.setTextColor(getContext().getResources().getColor(R.color.red_dark));
            }

        //icon.setImageResource(item.getIconID());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onfragment_bottom_sheetClicked(getAdapterPosition(),row_selected);
                        dismiss();

                    }
                }
            });
        }
    }

    private class fragment_bottom_sheetAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<BottomSheet_Model> mItems;

        fragment_bottom_sheetAdapter(List<BottomSheet_Model> items) {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

    }

}
