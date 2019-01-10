package gr.android.uom.currencyconverter;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import java.util.ArrayList;

//Activity στην οποία εμφανίζονται μέσω API τα διαθέσιμα νομίσματα που περιέχονται στην εφαρμογή
public class CurrencyView extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Currencies> curs;
    private static final String TAG = "Prosa";
    private ArrayList<Currencies> currencies;
    ArrayList<String> listOfStrings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_currency_view);
        listOfStrings = new ArrayList<>();
        listView = findViewById(R.id.listView);
        //Παραλαβή λίστων μέσω του Intent οι οποίες έχουν δημιουργηθεί στην MainMenu(activity)
        Intent intent = getIntent();
        ArrayList<String> namesList = intent.getStringArrayListExtra("names_list");
        ArrayList<String> codesList = intent.getStringArrayListExtra("codes_list");
        for(int i = 0; i<codesList.size(); i++){
            Log.d(TAG, "onCreate: "+ codesList.get(i) +": "+namesList.get(i));
            //Δημιουργίας 3ης λίστας η οποία θα περιέχει String το οποίο θα προβάλετε
            //Σχολιο: Κακός σχεδιασμός, αλλα όταν το έφτιαξα δεν ήξερα ακριβως πώς να χρησιμοποιήσω έναν CustomAdapter;
            listOfStrings.add(codesList.get(i) +": "+namesList.get(i));
        }
        //Δημιουργία Adapter και προσθήκη αυτού στην λίστα για εμφάνιση δεδομένων
        ArrayAdapter<String> aA = new ArrayAdapter<>(CurrencyView.this,R.layout.row_layout,R.id.text1,listOfStrings);
        listView.setAdapter(aA);

    }






}
