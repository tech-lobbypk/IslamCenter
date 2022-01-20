package quran.example.reading;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;



import java.util.List;

import static quran.data.constants.Constants_data.custom_font_arabic;

public class SlideShowAdapter extends PagerAdapter {

    static int PAGES = 5;

    private LayoutInflater mInflater;
    private Bundle bundle;
    private static List<ArabicViewModel> mLayouts;
    MainActivity parent;

    SlideShowAdapter(List<ArabicViewModel> ls, Context context, Bundle data_bundle) {
        mInflater = LayoutInflater.from(context);
        mLayouts = ls;
        bundle = data_bundle;
        parent = (MainActivity) context;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup pageView = (ViewGroup) mInflater.inflate(R.layout.sliding_fragment,
                container, false);

        TextView arabic = pageView.findViewById(R.id.txt_arabic);
    //    TextView eng =  pageView.findViewById(R.id.txt_eng);
    //    TextView ref = pageView.findViewById(R.id.txt_ref);


        arabic.setTypeface(custom_font_arabic);
        arabic.setText(mLayouts.get(position).getAyat_arabic());

    //    eng.setTypeface(custom_font_urdu);
    //    eng.setText(mLayouts.get(position).getTrans_eng());

    //    ref.setText(mLayouts.get(position).getRef());

        container.addView(pageView);
        //getItemPosition(pageView);
        return pageView;
    }

    @Override
    public int getCount() {
        PAGES = mLayouts.size();
        return PAGES;
    }
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
}

