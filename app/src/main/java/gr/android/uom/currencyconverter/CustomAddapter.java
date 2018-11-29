package gr.android.uom.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAddapter extends ArrayAdapter<Currencies> {
    private Context mContext;
    private int mResource;

    public CustomAddapter( Context context, int resource) {
        super(context, resource);
    }

    private static class ViewHandler{

        TextView codeTextView;
        TextView nameTextView;

    }
    CustomAddapter(Context context, int Resource, ArrayList<Currencies> object){
        super(context,Resource,object);
        mContext = context;
        mResource = Resource;

    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        ViewHandler holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            
        }
        return super.getView(position, convertView, parent);
    }
}
