package gr.android.uom.currencyconverter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {
    public static final String CODE_KEY = "Code";
    public static final String NAME_KEY = "Name";
    private ArrayList<Currencies> Curs;
    public JSONParser(){
        Curs = new ArrayList<>();
    }

    public ArrayList<Currencies> getCurs(){
        return Curs;
    }
    public String getCurName(int i){
        return Curs.get(i).getName();
    }

    public boolean parse(String jsonData){
        try {
            JSONArray jsonCodesArray = new JSONArray(jsonData);
            for(int i =0; i<jsonCodesArray.length(); i++){
                JSONObject jsonObject = jsonCodesArray.getJSONObject(i);
                String cCode = jsonObject.getString(CODE_KEY);
                String cName = jsonObject.getString(NAME_KEY);
                Log.d("GEIASAS", "parse: "+cName);
                Currencies c = new Currencies(cName,cCode);
                Curs.add(c);
            }
        } catch (JSONException e) {
            Log.e("PRS", "parse: Error parsing json data", e);
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
