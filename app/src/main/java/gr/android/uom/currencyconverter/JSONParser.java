package gr.android.uom.currencyconverter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//Κλάση JSONPARSER
public class JSONParser {
    public static final String CODE_KEY = "Code";
    public static final String NAME_KEY = "Name";
    private double value;
    private ArrayList<Currencies> Curs;
    public JSONParser(){
        //Αρχικοποίηση της λίστας.
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
            //περίπτωση η παράμετρος Code να είναι 1
            //ανοιχτή switch case για προσθήκη μελλοντικά άλλων κωδικών
            //και αλλαγή του Parse;
            case(1):
                try {
                    //διαχείριση των jsonData σύμφνωνα με την μορφή που έχουν απο το API
                    //[
                    JSONArray jsonCodesArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonCodesArray.length(); i++) {
                        JSONObject jsonObject = jsonCodesArray.getJSONObject(i);
                        String cCode = jsonObject.getString(CODE_KEY);
                        String cName = jsonObject.getString(NAME_KEY);
                        Log.d("GEIASAS", "parse: " + cName);
                        //δημιουργία Currencies με κωδικό τον κωδικό που βρίσκεται στον jsonArray στην θέση i
                        //και name το όνομα που βρίσκεται στην θέση i αυτού του πίνακα.
                        Currencies c = new Currencies(cName, cCode);
                        //προσθήκη του Currencies στην λίστα
                        Curs.add(c);
                    }
                }
                catch (JSONException e) {
                    Log.e("PRS", "parse: Error parsing json data", e);
                    e.printStackTrace();
                    return false;
            }
            break;

        }


        return true;
    }
}
