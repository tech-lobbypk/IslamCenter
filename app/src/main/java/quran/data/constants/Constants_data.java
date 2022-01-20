package quran.data.constants;

import android.graphics.Typeface;


import quran.example.reading.R;
import quran.islamCenter.adapters.ExpandedMenuModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Constants_data {

    public static Typeface custom_font_arabic = Typeface.createFromAsset(App.getContext().getAssets(), "QuranFont.ttf");
    public static Typeface custom_font_urdu = Typeface.createFromAsset(App.getContext().getAssets(),  "Jameel_Noori_Nastaleeq.ttf");
    public static Typeface custom_font_eng = Typeface.createFromAsset(App.getContext().getAssets(), "EnglishFont.ttf");



    public static Map<String, List<ExpandedMenuModel>> subtitles_headings = new LinkedHashMap<String, List<ExpandedMenuModel>>()
    {
        {
            put(App.getContext().getResources().getString(R.string.quran), new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("سورۂ الفاتحہ", "Surah Al-Fatiha", R.raw.surah_fatiha));
                    add(new ExpandedMenuModel("آیت الکرسی", "Ayat ul kursi",R.raw.ayat_ul_kursi));
                    add(new ExpandedMenuModel("سورة البَقَرَة (285-286)", "Surah Al-Baqara(285-286)",R.raw.surah_baraqah_last2aya));
                    add(new ExpandedMenuModel("سورة الکهف", "Surah Al-Kahf",R.raw.surah_kahf));
                    add(new ExpandedMenuModel("سورة مَریَم", "Surah Maryam",R.raw.surah_marryam));
                    add(new ExpandedMenuModel("سورة یسٓ", "Surah Ya-Seen",R.raw.surah_yasseen));
                    add(new ExpandedMenuModel("سورة الدّخان", "Surah Ad-Dukhan",R.raw.surah_dukhan));
                    add(new ExpandedMenuModel("سورة الرَّحمٰن", "Surah Ar-Rahman",R.raw.surah_rehhman));
                    add(new ExpandedMenuModel("سورة الواقِعَة", "Surah Al-Waqia",R.raw.surah_waqiyyah));
                    add(new ExpandedMenuModel("سورة المُلک","Surah Al-Mulk",R.raw.surah_mulk));
                    add(new ExpandedMenuModel("سورة المُزمّل","Surah Al-Muzzammil",R.raw.surah_muzzamil));
                    add(new ExpandedMenuModel("سورة الکافِرون","Surah Al-Kafiroon",R.raw.surah_kafiron));
                    add(new ExpandedMenuModel("سورة الإخلاص","Surah Al-Ikhlas",R.raw.surah_ikhlas));
                    add(new ExpandedMenuModel("سورة الفَلَق", "Surah Al-Falaq",R.raw.surah_falaq));
                    add(new ExpandedMenuModel("سورة النَّاس", "Surah An-Nas",R.raw.surah_naas));
                }
            });
            put("قرآن", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("سورۂ الفاتحہ", "Surah Al-Fatiha",R.raw.surah_fatiha));
                    add(new ExpandedMenuModel("آیت الکرسی", "Ayat ul kursi",R.raw.ayat_ul_kursi));
                    add(new ExpandedMenuModel("سورة البَقَرَة (285-286)", "Surah Al-Baqara(285-286)",R.raw.surah_baraqah_last2aya));
                    add(new ExpandedMenuModel("سورة الکهف", "Surah Al-Kahf",R.raw.surah_kahf));
                    add(new ExpandedMenuModel("سورة مَریَم", "Surah Maryam",R.raw.surah_marryam));
                    add(new ExpandedMenuModel("سورة یسٓ", "Surah Ya-Seen",R.raw.surah_yasseen));
                    add(new ExpandedMenuModel("سورة الدّخان", "Surah Ad-Dukhan",R.raw.surah_dukhan));
                    add(new ExpandedMenuModel("سورة الرَّحمٰن", "Surah Ar-Rahman",R.raw.surah_rehhman));
                    add(new ExpandedMenuModel("سورة الواقِعَة", "Surah Al-Waqia",R.raw.surah_waqiyyah));
                    add(new ExpandedMenuModel("سورة المُلک","Surah Al-Mulk",R.raw.surah_mulk));
                    add(new ExpandedMenuModel("سورة المُزمّل","Surah Al-Muzzammil",R.raw.surah_muzzamil));
                    add(new ExpandedMenuModel("سورة الکافِرون","Surah Al-Kafiroon",R.raw.surah_kafiron));
                    add(new ExpandedMenuModel("سورة الإخلاص","Surah Al-Ikhlas",R.raw.surah_ikhlas));
                    add(new ExpandedMenuModel("سورة الفَلَق", "Surah Al-Falaq",R.raw.surah_falaq));
                    add(new ExpandedMenuModel("سورة النَّاس", "Surah An-Nas",R.raw.surah_naas));
                }
            });

            put("تسبیح", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("تسبیح", "Repentance",R.raw.praising_lord));
                }
            });

            put("Praising Allah", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("تسبیح", "Repentance",R.raw.praising_lord));
                }
            });

            put("درود و سلام", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("درود و سلام", "Darood-o-Salam",R.raw.darood));
                }
            });

            put("Darood-O-Salam", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("درود و سلام", "Darood-o-Salam",R.raw.darood));
                }
            });


            put("استغفار", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("استغفار", "Repentance",R.raw.istaghfar));
                }
            });

            put("Repentance", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("استغفار", "Repentance",R.raw.istaghfar));
                }
            });

            put("صبح کے اذ کا ر", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("صبح کے اذ کا ر", "Evening Azkar",R.raw.morning_azkar));

                }
            });

            put("Morning Azkar", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("صبح کے اذ کا ر", "Evening Azkar",R.raw.morning_azkar));

                }
            });


            put("شا م کے اذ کا ر", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel(" شام کے اذکار", "Evening Azkar",R.raw.evening_azkar));
                }
            });

            put("Evening Azkar", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel(" شام کے اذکار", "Evening Azkar",R.raw.evening_azkar));
                }
            });


            put("دعائیں", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("رَبَّنَا ٓسے شروع ہونے والی دعائیں", "Prayers Starting with \"رَبَّنَآ\"",R.raw.rabbana_prayers));
                    add(new ExpandedMenuModel("روزانہ کی جانے والی دعائیں","General Prayers",R.raw.selected_duaein));
                    add(new ExpandedMenuModel("استخارہ کی دعا","Istikhara prayer",R.raw.istikhara));
                }
            });

            put("Prayers", new ArrayList<ExpandedMenuModel>(){
                {
                    add(new ExpandedMenuModel("رَبَّنَا سے شروع ہونے والی دعائیں", " Prayers \"رَبَّنَا\"",R.raw.rabbana_prayers));
                    add(new ExpandedMenuModel("روزانہ کی جانے والی دعائیں","General Prayers",R.raw.selected_duaein));
                    add(new ExpandedMenuModel("استخارہ کی دعا","Istikhara prayer",R.raw.istikhara));
                }
            });

        }
    };


    public static List<ExpandedMenuModel> getFileListing(String selected_value)
    {
        return subtitles_headings.get(selected_value);
    }


}