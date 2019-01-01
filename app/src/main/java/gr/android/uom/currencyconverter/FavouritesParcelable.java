package gr.android.uom.currencyconverter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class FavouritesParcelable extends Favourites implements Parcelable {
    public FavouritesParcelable(String text, String id) {
        super(text, id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
