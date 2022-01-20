package quran.islamCenter.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;
import quran.example.reading.R;


public class Recommended_readings_ViewHolder extends RecyclerView.ViewHolder {

    TextView txt_reading;
    Context con;

    public Recommended_readings_ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        txt_reading = itemView.findViewById(R.id.btn_rcv);
        con = context;
        txt_reading.setTextSize(25);

    }
    void setBtn_reading_text(final String txt, final Recommended_Readings_Adapter.onRCVClickListerner lstnr)
    {
        txt_reading.setText(txt);
        if(LocaleHelper.getLanguage(con).equals("en"))
        {
            txt_reading.setTextSize(20);
            txt_reading.setAllCaps(false);
            txt_reading.setTypeface(Constants_data.custom_font_eng);
        }
        else
        {
            txt_reading.setTypeface(Constants_data.custom_font_urdu);
            txt_reading.setTextSize(25);
        }
        txt_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstnr.onClick(itemView,getAdapterPosition());
            }
        });
    }
}
