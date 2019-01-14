package gr.android.uom.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.service.autofill.SaveCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConvertActivity extends AppCompatActivity {

    private static final String TAG = "PROSA";
    private String from = "GBP";
    private String to2 = "EUR";
    private String API_KEY = "AcWTjGGE6fuTVxlZZP1bRyifBKVkuoGs";

    private String myUrl;
    private String endpoint = "convert";
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText ammountText;
    private TextView result;
    private Button saveButton;
    private SavedCurrencies sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        //Δήλωση των γραφικών συστατικών για επεξεργασία τους μετέπειτα.
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        saveButton = findViewById(R.id.saveButton);
        ammountText = findViewById(R.id.ammountText);
        result = findViewById(R.id.result);
        //ενσωμάτωση της λίστας με title codes_list απο την MainMenu(activity)
        Intent intent = getIntent();
        //αρχικοποίηση λίστας για αποφυγή null pointer exception
        ArrayList<String> codesList = new ArrayList<>();
        codesList = intent.getStringArrayListExtra("codes_list");
        //Δημιουργία adapter για να εμφανίζει τα δεδομένα της codes_list
        //1η παράμετρος context: ConvertActivity δηλαδη στην activity στην οποία είναι η λίστα
        //2η παράμετρος το layout το οποίο περιέχει το γραφικό συστατικό το οποίο θα περιέχει η λίστα
        //3η παράμετρος το id απο το γραφικό συστατικό το οποίο περιέχεται στο παραπάνω layout
        //4η παράμετρος η arrayList που θέλω να εμφανίζεται στο listView
        ArrayAdapter<String> Aa = new ArrayAdapter<>(this,R.layout.list_item,R.id.text,codesList);
        //Προσθήκη adapter στα δύο πανωμυότυπα listView
        fromSpinner.setAdapter(Aa);
        toSpinner.setAdapter(Aa);



    }


    //μέθοδος η οποία εκτελεί το Convert μέσω API
    public void Convert(View view){
        //Αρχικοποίηση των μεταβλητων με το Item που επέλεξε ο χρήστης απο το Spinner
        from = fromSpinner.getSelectedItem().toString();
        to2 = toSpinner.getSelectedItem().toString();
        //ΕΛΕΓΧΟΣ ΕΑΝ ΥΠΑΡΧΕΙ ΔΩΣΜENO amount
        if(!ammountText.getText().toString().isEmpty()) {
            if(!from.equals(to2)) {
                //δημιουργία URL για το Convert το οποίο μας επιστρέφει το μήνυμα.
                myUrl = "https://forex.1forge.com/1.0.3/" + endpoint + "?from=" + from + "&to=" + to2 + "&quantity=" + ammountText.getText().toString() + "&api_key=" + API_KEY;
                DownloadData dlData = new DownloadData();
                //κατέβασμα δεδομένων απο το API μέσω του AsyncTask.
                dlData.execute(myUrl);
            }
            //εμφάνιση μυνήματος σε περίπτωη που χρήστης δεν επιλέξει δυο διαφορετικά νομίσματα
            //διότι δεν επιστρέφει αποτελέσματα το API
            else{
                Toast ts = null;
                ts.makeText(getApplicationContext(),"Please select difference currencies to convert!",Toast.LENGTH_SHORT).show();

            }

        }
        //Εμφάνιση μηνύματος στον χρήστη σε περίπτωση που δεν έχει επιλέξει ammount
        //Υ.Γ. το text του ammount έχει την επιλογή μόνο για numbers οπότε γλιτώνουμε αρκετά σφάλματα
        else{
            Toast ts = null;
            ts.makeText(getApplicationContext(),"Please set an ammount",Toast.LENGTH_SHORT).show();
        }

    }
    //OnClick μέθοδος για το Button Save
    //Δεν υπαρχει extend στην κλάση ή οτιδήποτε σε κάποιον listener
    //επειδη είναι καθορισμένη η OnClick μέθοδος μέσω της XML
    public void saveCurr(View v){
        sc.Save(sc);
        //εμφάνιση Toast στον χρήστη οτι το Favourite του αποθηκεύτηκε επιτυχώς
        Toast.makeText(this,"Favourite saved",Toast.LENGTH_SHORT).show();
    }


    //Δημιουργία κλάσης που κληρονομεί το asyncTask και χρησιμοποιείται για να κατεβάσει τα δεδομένα.
    public class DownloadData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            //ανάθεση στο postData τα δεδομένα απο την JSON
            String postData = downloadJSON(strings[0]);

            return postData;
        }
        //η κλαση η οποία κατεβάζει τα δεδομένα
        //Παράμετρος: δέχεται το URLL του API
        private String downloadJSON(String urlPath) {
            //δημιουργεία StringBuilder για να προσθέσουμε στην συνέχεια τα δεδομένα του API

            StringBuilder sb = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                //Εκκίνηση σύνδεσης με το API
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int reponseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + reponseCode);
                //δημιουργία και εκκίνηση BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    //προσθήκη κάθε γραμμής JSON που διαβάζεται απο το URL στο
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }
                //Κλείσιμο σύνδεσης με το API
                connection.disconnect();
                //κλεισιμο του BufferedReader
                reader.close();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ",e);
            }
            //επιστροφή του StringBuilder σε String το οποίο είναι τα JSONData
            //Δηλαδη δεδομένα σε μορφή JSON
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            //επεξεργασία των δεδομένων της JSON



            try {
                JSONObject jsonObject= new JSONObject(jsonData);
                //το αποτέλεσμα του Convert με παραμέτρους τα δυο νομίσματα που εισαγε ο χρήστης
                //και το ammount
                Double text = jsonObject.getDouble("value");
                //Παίρνω το value και το κάνω να είναι με μόνο δύο δεκαδικά ψηφία
                DecimalFormat format = new DecimalFormat("#.00");
                //Σε περίπτωση που το formatαρισμένο double είναι .00 θα έχω θέμα με την διαίρεση μετέπειτα
                //οπότε παίρνω περιπτώσεις
                if(format.format(text).equals(".00")){
                    result.setText(ammountText.getText().toString()+" "+ fromSpinner.getSelectedItem().toString()+" doesn't worth any "+
                            toSpinner.getSelectedItem().toString()+".");
                }
                //σε περίπτωση που δεν έχω .00
                else{
                    //Εμφανίζω αποτέλεσμα
                    result.setText(ammountText.getText().toString()+" " +fromSpinner.getSelectedItem().toString()+
                            " is worth " +format.format(text)+" "+toSpinner.getSelectedItem().toString() +".");
                    //και εμφανίζω στον Χρήστη την επιλογή Save
                    //Κάνω VISIBLE το BUTTON
                    saveButton.setVisibility(View.VISIBLE);
                    double rate= text/(Double.parseDouble(ammountText.getText().toString()));
                    NumberFormat rateFormat = NumberFormat.getInstance();
                    String curr1 = fromSpinner.getSelectedItem().toString();
                    String curr2 = toSpinner.getSelectedItem().toString();
                    //Δημιουργώ τώρα το αντικείμενο sc το οποίο χρησιμοποιείται στην μέθοδο SaveCurr
                    //
                    sc = new SavedCurrencies(rate,curr1,curr2,Calendar.getInstance().getTime().toString());


                }

            } catch (JSONException e) {
                Log.d("MITSOS", "onPostExecute: FAIL");
                e.printStackTrace();
            }


        }
    }
}
