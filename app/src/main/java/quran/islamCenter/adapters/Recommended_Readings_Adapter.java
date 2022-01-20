package quran.islamCenter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quran.example.reading.R;


public class Recommended_Readings_Adapter extends RecyclerView.Adapter<Recommended_readings_ViewHolder> {

    String[] titles;
    onRCVClickListerner listerner;
    Context context;
    public static int custom_height=0;
    public interface onRCVClickListerner
    {
        public void onClick(View v, int position);
    }
    public Recommended_Readings_Adapter(String[] ls, onRCVClickListerner lstnr,Context context)
    {
        titles = ls;
        listerner = lstnr;
        this.context = context;
    }

    @NonNull
    @Override
    public Recommended_readings_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_recommended_readings,parent,false);
        int row_height = parent.getMeasuredHeight();
        int w = parent.getMeasuredWidth();
        v.findViewById(R.id.Cl_recommended_readings).setMinimumHeight(row_height);
        return  new Recommended_readings_ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull Recommended_readings_ViewHolder holder, int position) {
        holder.setBtn_reading_text(titles[position],listerner);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
