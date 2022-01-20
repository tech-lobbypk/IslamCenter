package quran.islamCenter.adapters;

public class ExpandedMenuModel {
    String eng_title;
    String urdu_title;
    Integer file_id;

    public ExpandedMenuModel(String urdu_title, String eng_title, Integer raw_data_file_id)
    {
        this.urdu_title = urdu_title;
        this.eng_title = eng_title;
        this.file_id = raw_data_file_id;
    }


    public String getUrdu_title() {
        return urdu_title;
    }

    public void setUrdu_title(String urdu_title) {
        this.urdu_title = urdu_title;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }


    public String getEng_title() {
        return eng_title;
    }

    public void setEng_title(String eng_title) {
        eng_title = eng_title;
    }
}
