package quran.islamCenter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quran.example.reading.ArabicViewModel;
import quran.example.reading.BottomSheet_Model;
import quran.example.reading.R;


import java.util.ArrayList;
import java.util.List;

public class surah_Reading_Adapter extends RecyclerView.Adapter<surah_reading_ViewHolder> {

    public interface onRCVClickListerner
    {
        public void onClick_RCV(View v);
    }

    List<ArabicViewModel> ayyat;
    Context context;
    onRCVClickListerner listerner;
    public surah_Reading_Adapter(List<ArabicViewModel> ls, Context con)
    {
        ayyat = ls;
        context = con;
        this.listerner = (surah_Reading_Adapter.onRCVClickListerner) con;
        surah_reading_ViewHolder.items = new ArrayList<>();
        surah_reading_ViewHolder.items.add(new BottomSheet_Model(con.getString(R.string.copy), R.drawable.icon_copy));
        surah_reading_ViewHolder.items.add(new BottomSheet_Model(con.getString(R.string.share),R.drawable.icon_share));
        surah_reading_ViewHolder.items.add(new BottomSheet_Model(con.getString(R.string.bookmark_bs),R.drawable.icon_bookmark));

    }

    @NonNull
    @Override
    public surah_reading_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_surah_reading,parent,false);
        return  new surah_reading_ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull surah_reading_ViewHolder holder, int position) {
        holder.set_reading_text(ayyat.get(position),listerner);
    }


    public void setAyyat(List<ArabicViewModel> ls)
    {
        ayyat = ls;
    }
    @Override
    public int getItemCount() {
        return ayyat.size();
    }
}
