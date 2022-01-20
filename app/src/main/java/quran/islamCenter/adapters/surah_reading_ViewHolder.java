package quran.islamCenter.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import quran.data.constants.LocaleHelper;
import quran.example.reading.ArabicViewModel;
import quran.example.reading.BottomSheetFragment;
import quran.example.reading.BottomSheet_Model;

import quran.example.reading.R;
import quran.example.reading.ReadingActivity;

import java.util.ArrayList;

import quran.data.constants.Constants_data;

public class surah_reading_ViewHolder extends RecyclerView.ViewHolder {

    TextView txt_ayat;
    TextView txt_trans;
    //TextView txt_ayatNo;
    Context context;
    public static ArrayList<BottomSheet_Model> items;

    public static float arabic_font_size = 50;
    public static float translation_font_size = 20;
    public static float min_font = 20;
    public static float max_font = 70;

    ConstraintSet cset = new ConstraintSet();
    // for both, mode =0; for arabic only, mode = 1; for urdu only, mode = 2;
    public static int mode = 0;

    public surah_reading_ViewHolder(@NonNull View itemView, Context con) {
        super(itemView);
        context = con;
        txt_ayat = itemView.findViewById(R.id.txt_surah_arabic);
        txt_trans = itemView.findViewById(R.id.txt_surah_trans);


        txt_ayat.setTypeface(Constants_data.custom_font_arabic);
        txt_trans.setTypeface(Constants_data.custom_font_urdu);
      //  txt_ayatNo = itemView.findViewById(R.id.txt_ayatNo);
        //txt_ayatNo.setTypeface(custom_font_arabic);

        ConstraintLayout constraintLayout = itemView.findViewById(R.id.CL_Arabic);
        cset.clone(constraintLayout);

    }
    void set_reading_text(final ArabicViewModel ayat, final surah_Reading_Adapter.onRCVClickListerner lstner)
    {
        txt_ayat.setText(ayat.getAyat_arabic());

        //txt_ayatNo.setText(ayat.getRef());

        txt_ayat.setTextSize(arabic_font_size);
        //txt_ayatNo.setTextSize(20);
        //txt_ayatNo.setPadding(40,40,40,40);
        txt_trans.setTextSize(translation_font_size);

        ConstraintLayout CL_Trans = itemView.findViewById(R.id.CL_trans);

        if(LocaleHelper.getLanguage(context).equals("en")) {
            CL_Trans.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            txt_trans.setText(ayat.getTrans_eng());
        }
        else
        {
            CL_Trans.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            txt_trans.setText(ayat.getTrans_urdu());
        }

        ConstraintLayout constraint_layout = itemView.findViewById(R.id.CL_Arabic);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cset);

      if(getAdapterPosition() < 1)
        {
            constraintSet.connect(txt_ayat.getId(), ConstraintSet.END, constraint_layout.getId(), ConstraintSet.END, 0);
            constraintSet.connect(txt_ayat.getId(), ConstraintSet.START, constraint_layout.getId(), ConstraintSet.START, 0);

            constraintSet.connect(txt_trans.getId(), ConstraintSet.START, constraint_layout.getId(), ConstraintSet.START, 0);
            constraintSet.connect(txt_trans.getId(), ConstraintSet.END, constraint_layout.getId(), ConstraintSet.END, 0);
            txt_trans.setGravity(Gravity.CENTER);
        }
   /*   else
        {
            if(LocaleHelper.getLanguage(context).equals("en")) {
                constraintSet.connect(txt_ayat.getId(), ConstraintSet.END, constraint_layout.getId(), ConstraintSet.END, 0);
                constraintSet.connect(txt_trans.getId(), ConstraintSet.START, constraint_layout.getId(), ConstraintSet.START, 0);
            }
            else {
            //    constraintSet.connect(txt_ayat.getId(), ConstraintSet.START, constraint_layout.getId(), ConstraintSet.START, 0);

                constraintSet.connect(txt_trans.getId(), ConstraintSet.START, constraint_layout.getId(), ConstraintSet.START, 0);
            }

        }*/

        constraintSet.applyTo(constraint_layout);

        if(ayat.getTrans_eng_visible()) {
            if(ayat.getTrans_Arabic_visible()) {
                //Urdu + Arabic mode
                txt_trans.setTextColor(context.getResources().getColor(R.color.green_dark));
                txt_trans.setTextSize(25);

            }
            else
            {
                // Urdu Only mode
                if(getAdapterPosition()%2 == 0) txt_trans.setTextColor(context.getResources().getColor(R.color.red_dark));
                else txt_trans.setTextColor(context.getResources().getColor(R.color.blue_dark));
                float font = Math.max(arabic_font_size,20);
                txt_trans.setTextSize(font); // if Urdu only mode, set the font size equivalent to current arabic font size
            }
            txt_trans.setVisibility(View.VISIBLE);
        }
        else {
            txt_trans.setVisibility(View.GONE);
        }

        if(ayat.getTrans_Arabic_visible())
        {
            if(getAdapterPosition()%2 == 0) txt_ayat.setTextColor(context.getResources().getColor(R.color.red_dark));
            else txt_ayat.setTextColor(context.getResources().getColor(R.color.blue_dark));

            txt_ayat.setVisibility(View.VISIBLE);
            //txt_ayatNo.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_ayat.setVisibility(View.GONE);
            //txt_ayatNo.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == 0 || mode == 1) {
                    //arabic mode + urdu mode
                    ayat.toggleTrans_eng_visible();
                }
                else
                {
                    //mode 2 means urdu only mode
                    ayat.toggleTrans_arabic_visible();
                }
                lstner.onClick_RCV(view);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                BottomSheetFragment.newInstance(items,getAdapterPosition()).show(((ReadingActivity)context).getSupportFragmentManager(),"hhhh");
                return true;
            }
        });

    }
}
