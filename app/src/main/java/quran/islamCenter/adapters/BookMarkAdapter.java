package quran.islamCenter.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quran.data.constants.App;
import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;
import quran.example.reading.R;


import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.Bookmark_ViewHolder> {

    Context con;
    onRCVClickListerner listner;
    List<BookMarkViewModel> array;

    public interface onRCVClickListerner
    {
        public void onClick(int main_reading_index, int sub_reading_index, int scroll_position);
    }
    public BookMarkAdapter(Context c,List<BookMarkViewModel> ls, onRCVClickListerner lstnr) {
        array = ls;
        con = c;
        listner = lstnr;
    }

    @NonNull
    @Override
    public Bookmark_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_bottomsheet,parent,false);
        int screen_width = parent.getMeasuredWidth();

        TextView txt_bookmark_ayat = v.findViewById(R.id.txt_bookmark_ayat);
        txt_bookmark_ayat.setTypeface(Constants_data.custom_font_arabic);
        return  new Bookmark_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Bookmark_ViewHolder holder, int position) {
        holder.SetValues(position);

    }

    @Override
    public int getItemCount() {
        if(array != null)
            return array.size();
        else return 0;

    }

    class Bookmark_ViewHolder extends RecyclerView.ViewHolder {

        public Bookmark_ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void SetValues(int position) {
            TextView txt_ayat = itemView.findViewById(R.id.txt_bookmark_ayat);
            txt_ayat.setText(array.get(position).getAyat_text());

            //Set reference
            String main_reading_section = con.getResources().getStringArray(R.array.titles)[array.get(position).getMain_reading_index()];
            List<ExpandedMenuModel> listOfReadings = Constants_data.getFileListing(main_reading_section);

            TextView txt_ref = itemView.findViewById(R.id.txt_bookmark_reference);
            String ref = null;
            if(LocaleHelper.getLanguage(con).equals("en"))
            {
                ref = listOfReadings.get(array.get(position).getSub_reading_index()).getEng_title();
                txt_ref.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else {
                ref = listOfReadings.get(array.get(position).getSub_reading_index()).getUrdu_title();
                txt_ref.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            txt_ref.setText(ref);

            if(position%2 == 0)
            {
                txt_ayat.setTextColor(App.getContext().getResources().getColor(R.color.blue_dark));
                txt_ref.setTextColor(App.getContext().getResources().getColor(R.color.red_dark));
            }
            else
            {
                txt_ayat.setTextColor(App.getContext().getResources().getColor(R.color.red_dark));
                txt_ref.setTextColor(App.getContext().getResources().getColor(R.color.blue_dark));

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onClick(array.get(position).getMain_reading_index(),array.get(position).getSub_reading_index(),array.get(position).getAyat_no());
                }
            });
        }
    }
}
