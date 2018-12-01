package gr.android.uom.currencyconverter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {
    public static final String CODE_KEY = "Code";
    public static final String NAME_KEY = "Name";
    private double value;
    private ArrayList<Currencies> Curs;
    public JSONParser(){
        Curs = new ArrayList<>();
        value = 0;
    }

    public ArrayList<Currencies> getCurs(){
        return Curs;
    }
    public String getCurName(int i){
        return Curs.get(i).getName();
    }
    public double getValue(){
        return value;
    }

    public boolean parse(String jsonData,int CODE){
        switch(CODE) {
            case(1):
                try {
                    JSONArray jsonCodesArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonCodesArray.length(); i++) {
                        JSONObject jsonObject = jsonCodesArray.getJSONObject(i);
                        String cCode = jsonObject.getString(CODE_KEY);
                        String cName = jsonObject.getString(NAME_KEY);
                        Log.d("GEIASAS", "parse: " + cName);
                        Currencies c = new Currencies(cName, cCode);
                        Curs.add(c);
                        }
                }
                catch (JSONException e) {
                    Log.e("PRS", "parse: Error parsing json data", e);
                    e.printStackTrace();
                    return false;
            }
            break;
            case(2):
                try {
                    JSONObject jsonValue = new JSONObject(jsonData);
                    double value = jsonValue.getDouble("value");


                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
                break;

        }


        return true;
    }
}
