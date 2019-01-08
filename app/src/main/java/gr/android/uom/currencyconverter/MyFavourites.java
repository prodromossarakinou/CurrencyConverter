package gr.android.uom.currencyconverter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class MyFavourites implements Comparable<MyFavourites> {

    private String text;
    private String id;
    public MyFavourites(String text, String id) {
        this.text = text;
        this.id = id;
    }




    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(MyFavourites o) {
        return o.getText().compareTo(this.text);

    }
    @Override
    public String toString(){
        return text;
    }


}
