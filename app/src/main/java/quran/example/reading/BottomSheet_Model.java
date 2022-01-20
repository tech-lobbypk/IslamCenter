package quran.example.reading;

public class BottomSheet_Model {
    String text;
    int iconID;

    public BottomSheet_Model(String text, int imdId) {
        this.text = text;
        this.iconID = imdId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
}
