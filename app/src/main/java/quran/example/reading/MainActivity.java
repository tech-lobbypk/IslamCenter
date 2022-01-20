package quran.example.reading;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import quran.data.constants.Constants_data;
import quran.data.constants.LocaleHelper;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.tabs.TabLayout;
import quran.islamCenter.adapters.BookMarkAdapter;
import quran.islamCenter.adapters.BookMarkViewModel;
import quran.islamCenter.adapters.Divider_RecyclerView_Decorator;
import quran.islamCenter.adapters.ExpandableListAdapter;
import quran.islamCenter.adapters.ItemOffsetDecoration;
import quran.islamCenter.adapters.Recommended_Readings_Adapter;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    List<BookMarkViewModel> bookmarks = null;
    BookMarkAdapter bookmark_adapter = null;
    Recommended_Readings_Adapter recommended_readings_adapter = null;
    MaterialAlertDialogBuilder language_dialog;

    int which;
    int reminder_hour;

    ViewPager mViewPager;
    SlideShowAdapter adapter;
    int position = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if (position >= SlideShowAdapter.PAGES) {
                position = 0;
            } else {
                position = position + 1;
            }
            if (mViewPager != null) {
                mViewPager.setCurrentItem(position, true);
                handler.postDelayed(runnable, 5000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        AndroidThreeTen.init(this);

        SetNavigationDrawer();
        startSlideShow(savedInstanceState);
        populateRecommendedReadings();
        SetBottomSheet();
        setLeftOff_PickUp();
        setLocale();
        setInitialReminder();
    }




    private void setLocale()
    {
        String languageCode = LocaleHelper.getLanguage(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.getChildAt(0);

        Button btn_pickup = findViewById(R.id.txt_leftoff);

        if(languageCode.equals("ur"))
        {
            btn_pickup.setTypeface(Constants_data.custom_font_urdu);
            toolbarTitle.setTypeface(Constants_data.custom_font_urdu);
            toolbarTitle.setTextSize(25);

        }
        else
        {
            btn_pickup.setTypeface(Constants_data.custom_font_eng);
            toolbarTitle.setTypeface(Constants_data.custom_font_eng);
            toolbarTitle.setTextSize(20);
        }

        //updateViews(languageCode);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void setLeftOff_PickUp()
    {

        findViewById(R.id.txt_leftoff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLeftOff_PickUp(view);
            }
        });
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPref.contains(getString(R.string.last_main_reading_index))) {
            ((Button)findViewById(R.id.txt_leftoff)).setTextColor(getResources().getColor(R.color.url_blue));
            findViewById(R.id.txt_leftoff).setEnabled(true);
        }
        else {

            ((Button)findViewById(R.id.txt_leftoff)).setTextColor(getResources().getColor(android.R.color.darker_gray));
            findViewById(R.id.txt_leftoff).setEnabled(false);
        }
    }

    public void setLeftOff_PickUp(View v) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPref.contains(getString(R.string.last_main_reading_index))) {

            findViewById(R.id.txt_leftoff).setVisibility(View.VISIBLE);

            int main_index = sharedPref.getInt(getString(R.string.last_main_reading_index), 0);
            int sub_index = sharedPref.getInt(getString(R.string.last_sub_reading_index), 0);
            int scroll_position = sharedPref.getInt(getString(R.string.last_scroll),0);
            OpenReadingActivity(main_index,sub_index,scroll_position,false);
        }
    }

    private void SetBottomSheet()
    {
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        closeBottomSheet();
                    }
                    break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        MaterialButton btnBookmark = findViewById(R.id.btnBookmark);

        RecyclerView rcv = findViewById(R.id.rcv_bookmarks);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.addItemDecoration(new Divider_RecyclerView_Decorator(getResources().getDrawable(R.drawable.divider)));
        rcv.addItemDecoration(new ItemOffsetDecoration(this,R.dimen.rcv_row_padding));

        bookmarks = LoadBookMarks();

        setBookmarkAdapter();

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    btnBookmark.setIcon(getResources().getDrawable(R.drawable.expand_more_24px_white));
                    // reload bookmarks
                    bookmarks = LoadBookMarks();
                    if(bookmarks != null)
                    {
                        setBookmarkAdapter();
                        bookmark_adapter.notifyDataSetChanged();
                        rcv.setVisibility(View.VISIBLE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"You have not bookmarked any Aya yet",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    closeBottomSheet();
                }
            }
        });


    }

    private void setBookmarkAdapter() {
        if(bookmarks != null && bookmark_adapter==null)
        {
            bookmark_adapter = new BookMarkAdapter(this, bookmarks, new BookMarkAdapter.onRCVClickListerner() {
                @Override
                public void onClick(int main_reading_index, int sub_reading_index, int scroll_position) {
                    OpenReadingActivity(main_reading_index,sub_reading_index,scroll_position,true);
                }
            });
            RecyclerView rcv = findViewById(R.id.rcv_bookmarks);
            rcv.setAdapter(bookmark_adapter);
        }
    }

    private void closeBottomSheet() {
        MaterialButton btnBookmark = findViewById(R.id.btnBookmark);
        RecyclerView rcv = findViewById(R.id.rcv_bookmarks);

        btnBookmark.setIcon(getResources().getDrawable(R.drawable.expand_less_24px_white));
        rcv.setVisibility(View.GONE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private List<BookMarkViewModel> LoadBookMarks()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.bookmark_sp), Context.MODE_PRIVATE);
        int count = sharedPref.getInt(getString(R.string.bookmarked_ayat_count),0);
        if(count>0) {

            if(bookmarks == null)
             bookmarks = new ArrayList<BookMarkViewModel>();
            else bookmarks.clear();

            for(int i=0; i<count; i++)
            {
                String str = String.valueOf(i);
                String ayat = sharedPref.getString(getString(R.string.bookmarked_ayat)+str,"");
                int ayat_index = sharedPref.getInt(getString(R.string.bookmarked_ayat_index)+str,0);
                int ayat_main_reading_index = sharedPref.getInt(getString(R.string.bm_last_main_reading_index)+str,0);
                int ayat_sub_reading_index = sharedPref.getInt(getString(R.string.bm_last_sub_reading_index)+str,0);
                int scroll_position = sharedPref.getInt(getString(R.string.bm_last_scroll)+str,0);
                BookMarkViewModel bm = new BookMarkViewModel(ayat_main_reading_index,ayat_sub_reading_index,ayat_index,ayat,scroll_position);
                bookmarks.add(bm);
            }
        }

        return bookmarks;
    }

    private void SetNavigationDrawer() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ExpandableListView expandableList = findViewById(R.id.lv_navigationmenu);
        ExpandableListAdapter navDrawer_ListAdapter = new ExpandableListAdapter(this, getResources().getStringArray(R.array.titles), Constants_data.subtitles_headings, expandableList);
        expandableList.setAdapter(navDrawer_ListAdapter);

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l)
            {
                String[] titles = getResources().getStringArray(R.array.titles);
                if(Constants_data.getFileListing(titles[i]).size() == 1)
                {
                    OpenReadingActivity(i,0,0,false); // this group contains no children. Open relevant activity on group item click
                    return true;
                }
                return false;
            }
        });
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                expandableList.collapseGroup(i);
                OpenReadingActivity(i,i1,0,false);
                return false;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView txtNav_header = navigationView.getHeaderView(0).findViewById(R.id.txtNav_header);
        if(LocaleHelper.getLanguage(this).equals("en")) {
            txtNav_header.setTypeface(Constants_data.custom_font_eng);
            txtNav_header.setTextSize(25);
        }
        else {
            txtNav_header.setTypeface(Constants_data.custom_font_urdu);
            txtNav_header.setTextSize(35);
        }
    }

    @Override
    public void onBackPressed() {
       if(!CloseDrawer())
       {
           super.onBackPressed();
       }
    }

    private boolean CloseDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(LocaleHelper.getLanguage(this).equals("en"))
        {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
                return true;
            }
        }
        else {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
                return true;
            }
        }
        return false;
    }

    public boolean OpenReadingActivity(int group_index,int sub_heading_index,int scroll_position,boolean isBookmarked) {
        // Handle navigation view item clicks here.
        Intent i = new Intent(MainActivity.this,ReadingActivity.class);
        i.putExtra(getString(R.string.ReadingToDisplay_index),group_index);
        i.putExtra(getString(R.string.Sub_ReadingToDisplay),sub_heading_index);
        i.putExtra(getString(R.string.last_scroll),scroll_position);
        i.putExtra(getString(R.string.openingBookmark),isBookmarked);
        CloseDrawer();
        startActivityForResult(i,123);
        return true;
    }


    private void populateRecommendedReadings() {

        RecyclerView rcv = findViewById(R.id.rcv_recommended);
        rcv.setLayoutManager(new GridLayoutManager(this, 1));
        rcv.addItemDecoration(new ItemOffsetDecoration(this,R.dimen.rcv_row_padding));
        rcv.addItemDecoration(new Divider_RecyclerView_Decorator(ContextCompat.getDrawable(this, R.drawable.divider)));

        recommended_readings_adapter = new Recommended_Readings_Adapter(getResources().getStringArray(R.array.titles), new Recommended_Readings_Adapter.onRCVClickListerner() {
            @Override
            public void onClick(View v, int position) {
                OpenReadingActivity(position,0,0,false);
            }
        },this);
        rcv.setAdapter(recommended_readings_adapter);
    }


    private void setInitialReminder() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        reminder_hour = sharedPref.getInt(getString(R.string.reminder_hour),-1);
        if(reminder_hour == -1)
        {
            reminder_hour = 2;
        }
        setReminder();
    }

    private void setReminder() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.reminder_hour),reminder_hour);
        editor.commit();

        Intent i = new Intent(this, BroadcastReciver_timeup.class);
        i.putExtra(getString(R.string.reminder_hour),reminder_hour);
        PendingIntent pi = PendingIntent.getBroadcast(this, 234, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(reminder_hour != 0 && am != null) {

            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (reminder_hour *AlarmManager.INTERVAL_HOUR), pi);
            //am.set(AlarmManager.RTC_WAKEUP, 10000, pi);
            Toast.makeText(this,"Ziker Reminder set for every "+ String.valueOf(reminder_hour) +" hours",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(am != null)
            am.cancel(pi);
            Toast.makeText(this,"Zikr notifications are turned off by user",Toast.LENGTH_SHORT).show();
        }
    }

    private void startSlideShow(Bundle bundle) {
        mViewPager = findViewById(R.id.viewPager);
        // set the adapter
        adapter = new SlideShowAdapter(ArabicViewModel.initArabicModelWithJSON(getResources(),R.raw.dua_translation), MainActivity.this,bundle);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler!= null) {
            handler.removeCallbacks(runnable);
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        handler.postDelayed(runnable, 5000);
        setLeftOff_PickUp();
        closeBottomSheet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
            case R.id.language:
                showLanguageChoose();

                break;
            case R.id.reminders:
                showReminderChooserDialog();
                break;
        }
        return false;
    }

    private void showLanguageChoose() {

        AlertDialog ad;

        int itemSelected = 0;
        if(LocaleHelper.getLanguage(this).equals("ur"))
            itemSelected = 1;

        final int previous_locale = itemSelected;

        language_dialog = new MaterialAlertDialogBuilder(this,R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        language_dialog.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle_bg));



        ListAdapter adtr = new ArrayAdapter<String>(getApplicationContext(), R.layout.dialog_layout )
        {
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_layout, parent, false);
                }

                MaterialRadioButton radio_eng = ((MaterialRadioButton)convertView.findViewById(R.id.btn_radio_eng));
                radio_eng.setTypeface(Constants_data.custom_font_eng);
                MaterialRadioButton radio_urdu = ((MaterialRadioButton)convertView.findViewById(R.id.btn_radio_urdu));
                radio_urdu.setTypeface(Constants_data.custom_font_urdu);

                if(LocaleHelper.getLanguage(MainActivity.this).equals("en"))
                {
                    radio_eng.setChecked(true);
                    which = 0;
                }
                else
                {
                    radio_urdu.setChecked(true);
                    which = 1;
                }

                convertView.findViewById(R.id.btn_radio_urdu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        which = 1;
                    }
                });

                convertView.findViewById(R.id.btn_radio_eng).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        which = 0;
                    }
                });


                return convertView;
            }

            @Override
            public int getCount() {
                return 1;
            }

        };

        language_dialog.setAdapter(adtr, null);

        language_dialog.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (which)
                {
                    case 0:
                        if(previous_locale != 0) {
                            LocaleHelper.setLocale(MainActivity.this, "en");
                            dialogInterface.dismiss();
                            recreate();
                        }
                        break;
                    case 1:
                       if(previous_locale != 1) {
                           LocaleHelper.setLocale(MainActivity.this, "ur");
                           dialogInterface.dismiss();
                           recreate();
                       }
                        break;
                }
            }
        });


        TextView tv_title = new TextView(this);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setTextColor(ContextCompat.getColor(this,R.color.blue_dark));
        tv_title.setText(R.string.Select_Language);
        tv_title.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle_topedges_bg));
        tv_title.setTextColor(ContextCompat.getColor(this,R.color.white));
        if(LocaleHelper.getLanguage(this).equals("ur")) {
            tv_title.setTypeface(Constants_data.custom_font_urdu);
            tv_title.setTextSize(30);
        }
        else
        {
            tv_title.setTypeface(Constants_data.custom_font_eng);
            tv_title.setTextSize(25);
        }

        tv_title.setPadding(10,30,10,30);
        language_dialog.setCustomTitle(tv_title);

        ad = language_dialog.show();

        ListView lv = ad.getListView();
        lv.setBackground(ContextCompat.getDrawable(this,R.drawable.bottom_boundary));
        lv.setPadding(0,0,0,0);

        Button btn_positive = ad.getWindow().findViewById(android.R.id.button1);

        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) btn_positive.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        btn_positive.setLayoutParams(positiveButtonLL);

        btn_positive.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        if(LocaleHelper.getLanguage(this).equals("ur")) {
            btn_positive.setTypeface(Constants_data.custom_font_urdu);
            btn_positive.setTextSize(30);
            btn_positive.setPaddingRelative(0,2,0,2);
        }
        else
        {
            btn_positive.setTypeface(Constants_data.custom_font_eng);
            btn_positive.setTextSize(20);
            btn_positive.setPaddingRelative(0,5,0,6);
        }
    }

    private void showReminderChooserDialog() {

        AlertDialog ad ;
       //load from shared preference - settings for reminders
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        reminder_hour = sharedPref.getInt(getString(R.string.reminder_hour),0);

        language_dialog = new MaterialAlertDialogBuilder(this,R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        language_dialog.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle_bg));

        ListAdapter adtr = new ArrayAdapter<String>(getApplicationContext(), R.layout.dialog_layout_reminder )
        {
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_layout_reminder, parent, false);
                }
                ((RadioGroup)convertView.findViewById(R.id.rg_reminder)).check(reminder_hour);


                MaterialRadioButton radio_never = convertView.findViewById(R.id.btn_radio_never);
                MaterialRadioButton radio_1hour = convertView.findViewById(R.id.btn_radio_1hour);
                MaterialRadioButton radio_2hour = convertView.findViewById(R.id.btn_radio_2hour);

                if(LocaleHelper.getLanguage(MainActivity.this).equals("en"))
                {
                    radio_never.setTypeface(Constants_data.custom_font_eng);
                    radio_1hour.setTypeface(Constants_data.custom_font_eng);
                    radio_2hour.setTypeface(Constants_data.custom_font_eng);
                }
                else
                {
                    radio_never.setTypeface(Constants_data.custom_font_urdu);
                    radio_1hour.setTypeface(Constants_data.custom_font_urdu);
                    radio_2hour.setTypeface(Constants_data.custom_font_urdu);

                }

                if(reminder_hour == 0)
                    radio_never.setChecked(true);
                else if (reminder_hour == 1)
                    radio_1hour.setChecked(true);
                else radio_2hour.setChecked(true);

               radio_never.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reminder_hour = 0;
                    }
                });

                radio_1hour.findViewById(R.id.btn_radio_1hour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reminder_hour = 1;
                    }
                });

                radio_2hour.findViewById(R.id.btn_radio_2hour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reminder_hour = 2;
                    }
                });

                return convertView;
            }

            @Override
            public int getCount() {
                return 1;
            }

        };

        language_dialog.setAdapter(adtr, null);

        language_dialog.setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setReminder();
            }
        });


        TextView tv_title = new TextView(this);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setTextColor(ContextCompat.getColor(this,R.color.blue_dark));
        tv_title.setText(R.string.Select_Reminder_Setting);
        tv_title.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle_topedges_bg));
        tv_title.setTextColor(ContextCompat.getColor(this,R.color.white));
        if(LocaleHelper.getLanguage(this).equals("ur")) {
            tv_title.setTypeface(Constants_data.custom_font_urdu);
            tv_title.setTextSize(30);
        }
        else
        {
            tv_title.setTypeface(Constants_data.custom_font_eng);
            tv_title.setTextSize(25);
        }

        tv_title.setPadding(10,30,10,30);
        language_dialog.setCustomTitle(tv_title);

        ad = language_dialog.show();
        ListView lv = ad.getListView();
        lv.setBackground(ContextCompat.getDrawable(this,R.drawable.bottom_boundary));
        lv.setPadding(0,0,0,0);

        Button btn_positive = ad.getWindow().findViewById(android.R.id.button1);

        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) btn_positive.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        btn_positive.setLayoutParams(positiveButtonLL);

        btn_positive.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        if(LocaleHelper.getLanguage(this).equals("ur")) {
            btn_positive.setTypeface(Constants_data.custom_font_urdu);
            btn_positive.setTextSize(30);
            btn_positive.setPaddingRelative(0,2,0,2);
        }
        else
        {
            btn_positive.setTypeface(Constants_data.custom_font_eng);
            btn_positive.setTextSize(20);
            btn_positive.setPaddingRelative(0,5,0,6);
        }
    }
}

