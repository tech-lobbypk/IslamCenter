package quran.example.reading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;



import quran.islamCenter.adapters.Divider_RecyclerView_Decorator;
import quran.islamCenter.adapters.ExpandedMenuModel;
import quran.islamCenter.adapters.surah_Reading_Adapter;
import quran.islamCenter.adapters.surah_reading_ViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

public class ReadingActivity extends AppCompatActivity implements surah_Reading_Adapter.onRCVClickListerner, BottomSheetFragment.Listener {

    surah_Reading_Adapter adapter;
    RecyclerView rcvAyyat;

    int sub_reading_index = 0;
    int main_reading_index =0;
    int scroll_position = 0;

    String main_reading_section = null;

    ScaleGestureDetector mScaleDetector;
    float currentScaleFactor = 1.f;
    float previousScaleFactor = 0.f;
    List<List<ArabicViewModel>> ayyat = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        loadSettings(); //load font and translation preferences

        findViewById(R.id.btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPrevious_clicked(view);
            }
        });

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext_clicked(view);
            }
        });

        findViewById(R.id.btnfast_forward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFastForward_clicked(view);
            }
        });

        findViewById(R.id.btnfast_rewind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFastRewind_clicked(view);
            }
        });


        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        if(savedInstanceState == null) {
            main_reading_index = getIntent().getExtras().getInt(getString(R.string.ReadingToDisplay_index));
            sub_reading_index = getIntent().getIntExtra(getString(R.string.Sub_ReadingToDisplay), 0);
            scroll_position = getIntent().getIntExtra(getString(R.string.last_scroll), 0);
        }
        else
        {
            main_reading_index = savedInstanceState.getInt(getString(R.string.ReadingToDisplay_index));
            sub_reading_index = savedInstanceState.getInt(getString(R.string.Sub_ReadingToDisplay), 0);
            scroll_position = savedInstanceState.getInt(getString(R.string.last_scroll), 0);
        }

        LoadSection();

        adapter = new surah_Reading_Adapter(ayyat.get(sub_reading_index), this);
        rcvAyyat = findViewById(R.id.rcvSurah);
        rcvAyyat.setLayoutManager(new LinearLayoutManager(this));
        rcvAyyat.addItemDecoration(new Divider_RecyclerView_Decorator(ContextCompat.getDrawable(this, R.drawable.divider)));
        rcvAyyat.setAdapter(adapter);
        rcvAyyat.scrollToPosition(scroll_position);

        rcvAyyat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scroll_position = dy;

            }
        });

        rcvAyyat.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if(e.getPointerCount() == 2) mScaleDetector.onTouchEvent(e);
                return false;
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) { }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        RadioGroup rg = findViewById(R.id.radio_selection);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                SetLanguageWithMode(i);
                saveSettings();
                adapter.notifyDataSetChanged();
            }
        });

        boolean isOpening_Bookmark = getIntent().getBooleanExtra("openingBookmark",false);
        if(isOpening_Bookmark)
        {
            ((RadioButton)findViewById(R.id.radioArabic)).setChecked(true);
        }
        setBottomBarButtons();
        setLocale();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(getString(R.string.ReadingToDisplay_index),main_reading_index);
        outState.putInt(getString(R.string.Sub_ReadingToDisplay),sub_reading_index);
        outState.putInt(getString(R.string.last_scroll),scroll_position);
    }

    private void setLocale()
    {
        String languageCode = LocaleHelper.getLanguage(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.getChildAt(0);

        RadioButton rb1 = findViewById(R.id.radioEng);
        RadioButton rb2 = findViewById(R.id.radioArabic);
        RadioButton rb3 = findViewById(R.id.radioBoth);

        if(languageCode.equals("ur"))
        {
            toolbarTitle.setTypeface(Constants_data.custom_font_urdu);
            toolbarTitle.setTextSize(40);
            rb1.setTypeface(Constants_data.custom_font_urdu);
            rb2.setTypeface(Constants_data.custom_font_urdu);
            rb3.setTypeface(Constants_data.custom_font_urdu);
            rb1.setTextSize(25);
            rb2.setTextSize(25);
            rb3.setTextSize(25);
        }
        else
        {
            toolbarTitle.setTypeface(Constants_data.custom_font_eng);
            toolbarTitle.setTextSize(20);

            rb1.setTypeface(Constants_data.custom_font_eng);
            rb2.setTypeface(Constants_data.custom_font_eng);
            rb3.setTypeface(Constants_data.custom_font_eng);

            rb1.setTextSize(15);
            rb2.setTextSize(15);
            rb3.setTextSize(15);
        }

    }


    private void LoadSection() {
        if(ayyat.size() > 0) ayyat.clear();
        main_reading_section = getResources().getStringArray(R.array.titles)[main_reading_index];
        List<ExpandedMenuModel> listOfReadings = Constants_data.getFileListing(main_reading_section);
        for(int i = 0; i<= sub_reading_index; i++) {
            ayyat.add(ArabicViewModel.initArabicModelWithJSON(getResources(), listOfReadings.get(i).getFile_id()));
            SetLanguageWithMode(((RadioGroup)findViewById(R.id.radio_selection)).getCheckedRadioButtonId());
        }
      //  this.setTitle(main_reading_section);
    }

    private void SetLanguageWithMode(int i) {
        if(i == R.id.radioArabic)
        {
            SetLanguage(true,false);
            surah_reading_ViewHolder.mode = 1;

        }
        else if (i == R.id.radioEng )
        {
            SetLanguage(false,true);
            surah_reading_ViewHolder.mode = 2;

        }
        else
        {
            SetLanguage(true,true);
            surah_reading_ViewHolder.mode = 0;

        }
    }

    private void SetLanguage (boolean arabic, boolean translation) {
        Iterator<List<ArabicViewModel>> iterator = ayyat.iterator();
        while (iterator.hasNext())
        {
            ArabicViewModel.setLanguage(iterator.next(),arabic,translation);
        }
    }

    @Override
    public void onClick_RCV(View v) {
        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    public void setDrawable(int imageID,  ImageButton btn) {

        boolean enabled = btn.isEnabled();
        Drawable originalIcon = this.getResources().getDrawable(imageID);
        Drawable icon = enabled ? originalIcon : getGrayDrawable(originalIcon);
        btn.setImageDrawable(icon);
    }

    private Drawable getGrayDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(Color.GRAY, BlendMode.SRC_ATOP));
        } else {
            res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
        return res;
    }

    void setBottomBarButtons() {

        ImageButton btn_previous = findViewById(R.id.btnPrevious);
        if (sub_reading_index == 0) {
            btn_previous.setEnabled(false);
        } else {
            btn_previous.setEnabled(true);
        }
        setDrawable(R.drawable.previous_24px,btn_previous);

        ImageButton btn_next = findViewById(R.id.btnNext);


        List<ExpandedMenuModel> listOfReadings = Constants_data.getFileListing(main_reading_section);
        if (sub_reading_index+1 < listOfReadings.size()) {
            btn_next.setEnabled(true);
        } else {
            btn_next.setEnabled(false);
        }
        setDrawable(R.drawable.next_24px,btn_next);

        ImageButton btnFastForward = findViewById(R.id.btnfast_forward);
        if (main_reading_index+1 >= getResources().getStringArray(R.array.titles).length) {
            // no next section to fast forward to
            // Disable the fast forward button
            btnFastForward.setEnabled(false);
        } else {
            btnFastForward.setEnabled(true);
        }
        setDrawable(R.drawable.fast_forward_24px,btnFastForward);

        ImageButton btnFastRewind = findViewById(R.id.btnfast_rewind);
        if (main_reading_index == 0) {
            // no further backward navigation possible so disable rewind button
            btnFastRewind.setEnabled(false);
        } else {
            btnFastRewind.setEnabled(true);
        }
        setDrawable(R.drawable.fast_rewind_24px,btnFastRewind);

        String sub_title = null;
        if(LocaleHelper.getLanguage(this).equals("en"))
            sub_title = Constants_data.getFileListing(main_reading_section).get(sub_reading_index).getEng_title();
        else
            sub_title = Constants_data.getFileListing(main_reading_section).get(sub_reading_index).getUrdu_title();

        this.setTitle(sub_title);

    }

    public void btnPrevious_clicked(View v)
    {
        if(sub_reading_index -1 >=0)
        {
            sub_reading_index--;
            rcvAyyat.scrollToPosition(0);
            adapter.setAyyat(ayyat.get(sub_reading_index));
            adapter.notifyDataSetChanged();
            setBottomBarButtons();
        }
    }
    public void btnNext_clicked(View v)
    {
        List<ExpandedMenuModel> listOfReadings = Constants_data.getFileListing(main_reading_section);
        if(sub_reading_index +1 < listOfReadings.size() )
        {
            sub_reading_index++;
            if(sub_reading_index >= ayyat.size()) {
                ayyat.add(ArabicViewModel.initArabicModelWithJSON(getResources(), listOfReadings.get(sub_reading_index).getFile_id()));
                SetLanguageWithMode(((RadioGroup)findViewById(R.id.radio_selection)).getCheckedRadioButtonId());

            }
            rcvAyyat.scrollToPosition(0);
            adapter.setAyyat(ayyat.get(sub_reading_index));
            adapter.notifyDataSetChanged();
        }
        setBottomBarButtons();

    }

    private void btnFastForward_clicked(View view) {

        if(main_reading_index+1 < getResources().getStringArray(R.array.titles).length)
        {
            main_reading_index++;
            sub_reading_index=0;
            LoadSection();
            rcvAyyat.scrollToPosition(0);
            adapter.setAyyat(ayyat.get(sub_reading_index));
            adapter.notifyDataSetChanged();
        }
        setBottomBarButtons();

    }

    private void btnFastRewind_clicked(View view)
    {
        if(main_reading_index-1 >=0)
        {
            main_reading_index--;
            sub_reading_index=0;
            LoadSection();
            rcvAyyat.scrollToPosition(0);
            adapter.setAyyat(ayyat.get(sub_reading_index));
            adapter.notifyDataSetChanged();
            setBottomBarButtons();
        }
    }

    @Override
    public void onfragment_bottom_sheetClicked(int bottom_sheet_option, int selected_ayat_index)
    {
        if(bottom_sheet_option == 0 ) //copy option is selected
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", ayyat.get(sub_reading_index).get(selected_ayat_index).getAyat_arabic() + "\n" + ayyat.get(sub_reading_index).get(selected_ayat_index).getTrans_eng());
            clipboard.setPrimaryClip(clip);
        }
        else if (bottom_sheet_option == 1) // share option is selected
        {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, ayyat.get(sub_reading_index).get(selected_ayat_index).getAyat_arabic() + "\n" + ayyat.get(sub_reading_index).get(selected_ayat_index).getTrans_eng());
            startActivity(Intent.createChooser(sendIntent, null));
        }
        else if (bottom_sheet_option == 2)
        {
            //Bookmark selected

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.bookmark_sp), Context.MODE_PRIVATE);
            int count = sharedPref.getInt(getString(R.string.bookmarked_ayat_count),0);

            String str = String.valueOf(count);
            String ayat = ayyat.get(sub_reading_index).get(selected_ayat_index).getAyat_arabic();
            if(ayat.length() > 50)
                ayat = ayat.substring(0,50);//take only first 15 characters of the ayat_arabic

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt((getString(R.string.bookmarked_ayat_index)+ str),selected_ayat_index);
            editor.putString((getString(R.string.bookmarked_ayat)+str),ayat+"...");
            editor.putInt(getString(R.string.bm_last_main_reading_index)+str, main_reading_index);
            editor.putInt(getString(R.string.bm_last_sub_reading_index)+str, sub_reading_index);
            editor.putInt(getString(R.string.bm_last_scroll)+str,rcvAyyat.computeVerticalScrollOffset());
            editor.putInt("bookmarked_ayat_count",count+1);
            editor.commit();


        }
    }
    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScaleFactor = detector.getScaleFactor();
            currentScaleFactor = Math.max(0.1f, Math.min(currentScaleFactor, 5.0f));
            if(currentScaleFactor > 1.0) {
              surah_reading_ViewHolder.arabic_font_size += ((surah_reading_ViewHolder.arabic_font_size /50)*currentScaleFactor);
              surah_reading_ViewHolder.arabic_font_size = Math.min(surah_reading_ViewHolder.arabic_font_size,surah_reading_ViewHolder.max_font);
              surah_reading_ViewHolder.translation_font_size = Math.max(surah_reading_ViewHolder.arabic_font_size -20,surah_reading_ViewHolder.min_font);
            }
            else
            {
                surah_reading_ViewHolder.arabic_font_size -= ((surah_reading_ViewHolder.arabic_font_size /50)*currentScaleFactor);
                surah_reading_ViewHolder.arabic_font_size = Math.max(surah_reading_ViewHolder.arabic_font_size,surah_reading_ViewHolder.min_font);
                surah_reading_ViewHolder.translation_font_size = Math.max(surah_reading_ViewHolder.arabic_font_size -20,surah_reading_ViewHolder.min_font);
            }
            saveSettings();
            adapter.notifyDataSetChanged();
            return true;
        }
    }

    private void loadSettings() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if(sharedPref.contains(getString(R.string.language_mode))) {
            surah_reading_ViewHolder.mode = sharedPref.getInt(getString(R.string.language_mode), surah_reading_ViewHolder.mode);
            surah_reading_ViewHolder.arabic_font_size = sharedPref.getFloat(getString(R.string.arabic_font), surah_reading_ViewHolder.arabic_font_size);
            surah_reading_ViewHolder.translation_font_size = sharedPref.getFloat(getString(R.string.translation_font), surah_reading_ViewHolder.translation_font_size);

            switch (surah_reading_ViewHolder.mode)
            {
                case 0:
                    ((RadioGroup) findViewById(R.id.radio_selection)).check(R.id.radioBoth);
                    break;
                case 1:
                    ((RadioGroup) findViewById(R.id.radio_selection)).check(R.id.radioArabic);
                    break;
                case 2:
                    ((RadioGroup) findViewById(R.id.radio_selection)).check(R.id.radioEng);
                    break;
            }

        }
        else {
            saveSettings();
        }
    }

    private void saveSettings() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.language_mode), surah_reading_ViewHolder.mode);
        editor.putFloat(getString(R.string.arabic_font),surah_reading_ViewHolder.arabic_font_size);
        editor.putFloat(getString(R.string.translation_font),surah_reading_ViewHolder.translation_font_size);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.last_main_reading_index), main_reading_index);
        editor.putInt(getString(R.string.last_sub_reading_index), sub_reading_index);
        editor.putInt(getString(R.string.last_scroll),((LinearLayoutManager)rcvAyyat.getLayoutManager()).findFirstVisibleItemPosition());
        editor.commit();


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
