package ir.alamdari.battleship.dataHolder;

import java.util.HashMap;

public class DataHolder {
    public  HashMap<String,String> data;

    private DataHolder() {
        data = new HashMap<>();
    }

    private final static DataHolder INSTANCE = new DataHolder();

    public static DataHolder getInstance() {
        return INSTANCE;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
