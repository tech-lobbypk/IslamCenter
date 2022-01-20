package quran.example.reading;

import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArabicViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private static final String TAG = SlideShowAdapter.class.getSimpleName();

     String ayat_arabic;

     String trans_urdu;

     String trans_eng;

     Boolean trans_eng_visible = true;
     Boolean trans_Arabic_visible = true;

    public ArabicViewModel(
            String ayat_arabic, String trans_urdu,String trans_eng) {
        this.ayat_arabic = ayat_arabic;
        this.trans_eng = trans_eng;
        this.trans_urdu = trans_urdu;
    }

    public String getTrans_urdu() {
        return trans_urdu;
    }

    public void setTrans_urdu(String trans_urdu) {
        this.trans_urdu = trans_urdu;
    }

    public Boolean getTrans_Arabic_visible() {
        return trans_Arabic_visible;
    }

    public void setTrans_Arabic_visible(Boolean trans_Arabic_visible) {
        this.trans_Arabic_visible = trans_Arabic_visible;
    }

    public static void setLanguage(List<ArabicViewModel> ayyat, boolean arabic_preference, boolean eng_preference) {
        Iterator<ArabicViewModel> models = ayyat.iterator();
        while (models.hasNext())
        {
            ArabicViewModel arb = models.next();
            arb.setTrans_Arabic_visible(arabic_preference);
            arb.setTrans_eng_visible(eng_preference);
        }
    }

    public void toggleTrans_eng_visible()
     {
         trans_eng_visible = !trans_eng_visible;
     }
    public Boolean getTrans_eng_visible() {
        return trans_eng_visible;
    }

    public void setTrans_eng_visible(Boolean trans_eng_visible) {
        this.trans_eng_visible = trans_eng_visible;
    }
    public void toggleTrans_arabic_visible() {
            trans_Arabic_visible = !trans_Arabic_visible;
    }


    public String getAyat_arabic() {
        return ayat_arabic;
    }

    public String getTrans_eng() {
        return trans_eng;
    }

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<ArabicViewModel> initArabicModelWithJSON(Resources resources,int file_id) {

        InputStream inputStream = resources.openRawResource(file_id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonDuasString = writer.toString();
        Gson gson = new Gson();
        Type duaListType = new TypeToken<ArrayList<ArabicViewModel>>() {
        }.getType();
        return gson.fromJson(jsonDuasString, duaListType);
    }

}
