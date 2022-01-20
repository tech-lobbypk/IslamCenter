package quran.islamCenter.adapters;

public class BookMarkViewModel {
    int main_reading_index;
    int sub_reading_index;
    int ayat_no;
    int scroll_position;
    String ayat_text;

    public int getScroll_position() {
        return scroll_position;
    }

    public void setScroll_position(int scroll_position) {
        this.scroll_position = scroll_position;
    }

    public String getAyat_text() {
        return ayat_text;
    }

    public void setAyat_text(String ayat_text) {
        this.ayat_text = ayat_text;
    }

    public BookMarkViewModel(int main_reading_index, int sub_reading_index, int ayat_no,String ayat_text,int scroll_position) {
        this.main_reading_index = main_reading_index;
        this.sub_reading_index = sub_reading_index;
        this.ayat_no = ayat_no;
        this.ayat_text = ayat_text;
        this.scroll_position = scroll_position;
    }

    public int getMain_reading_index() {
        return main_reading_index;
    }

    public void setMain_reading_index(int main_reading_index) {
        this.main_reading_index = main_reading_index;
    }

    public int getSub_reading_index() {
        return sub_reading_index;
    }

    public void setSub_reading_index(int sub_reading_index) {
        this.sub_reading_index = sub_reading_index;
    }

    public int getAyat_no() {
        return ayat_no;
    }

    public void setAyat_no(int ayat_no) {
        this.ayat_no = ayat_no;
    }
}
