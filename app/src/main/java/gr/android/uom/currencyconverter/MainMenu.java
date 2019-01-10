package gr.android.uom.currencyconverter;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


//Activity στην οποία προβάλεται το Menu
public class MainMenu extends AppCompatActivity {
    private FirebaseUser user;

    //Αρχικοποίηση URL στο API? DEMO API CREATED BY ME
    //Λόγω αστοχίας στα API χρειάστηκε να κάτσω να γράψω εγώ ένα API
    //σχετικό μετα διαθέσιμα νομίσματα που υπάρχουν στην εφαρμογή
    private String myUrl = "http://demo5434819.mockable.io/CurrencyConverter123456";
    public ArrayList<Currencies> curs;
    ArrayList<String> listOfNames;
    ArrayList<String> listofCodes;
    private static final String TAG = "Prosa";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownloadData downloadData = new DownloadData();
        //Κατέβασμα δεδομένων απο το API
        downloadData.execute(myUrl);
        setContentView(R.layout.main_menu);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Αρχικοποίηση λιστών διότι σε περίπτωση που δεν έχουν προλάβει να κατέβουν
        //Πέφτουμε σε null pointer exception
        listOfNames = new ArrayList<>();
        listofCodes = new ArrayList<>();

    }

    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Button Favourites
    public void seeList(View v){
        //Χρησιμοποιέιτα το Intent για την εναλλαγή των Activity

        Intent intent;
        intent = new Intent(this,FavouritesActivity.class);
        //εκκίνηση άλλης activity.
        startActivity(intent);
    }
    //επικάληψη μεθόδου onBackPressed
    //για να μπορώ να ελέγχω τι κάνει ο χρήστης όταν πατάει το Key Back απο την συσκευή του
    @Override
    public void onBackPressed() {
        //Όταν πατάει το back Key, δεν κάνει απολύτων τίποτα η εφαρμογή.
    }

    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Button Currencies View
    public void openCurrenciesView (View view){
        //Χρησιμοποιέιτα το Intent για την εναλλαγή των Activity
        Intent intent;
        intent = new Intent(MainMenu.this,CurrencyView.class);
        if(listofCodes.isEmpty() || listOfNames.isEmpty()){
            //αρχικοποίηση DEMO Λιστών για να μην πέσουμε σε null pointer exception
            ArrayList<String> emptyNames = new ArrayList<>();
            ArrayList<String> emptyCodes = new ArrayList<>();
            emptyNames.add("Go back and come again in several minutes");
            emptyCodes.add("  -  ");
            //Αποστολή DEMO Λιστών
            intent.putStringArrayListExtra("names_list",emptyNames);
            intent.putStringArrayListExtra("codes_list",emptyCodes);
            //εκκίνηση άλλης activity.
            startActivity(intent);
        }
        else {
            //Αποστολή κανονικών λιστών
            intent.putStringArrayListExtra("names_list", listOfNames);
            intent.putStringArrayListExtra("codes_list", listofCodes);
            //εκκίνηση άλλης activity.
            startActivity(intent);
        }

    }
    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Button Converter
    public void openConverter (View view){
        //Χρησιμοποιέιτα το Intent για την εναλλαγή των Activity
        Intent intent;
        intent = new Intent(MainMenu.this,ConvertActivity.class);
        if(listofCodes.isEmpty()){
            //αρχικοποίηση DEMO Λιστών για να μην πέσουμε σε Null Pointer exception
            ArrayList<String> emptyCodes = new ArrayList<>();
            emptyCodes.add("   ");
            intent.putStringArrayListExtra("codes_list",emptyCodes);
            //εκκίνηση άλλης activity.
            startActivity(intent);
        }
        else {
            intent.putStringArrayListExtra("codes_list", listofCodes);
            //εκκίνηση άλλης activity.
            startActivity(intent);
        }

    }
    //Δημιουργία κλάσης DownloadData η οποία κατεβάζει ασύγχρονα τα δεδομένα της JSON
    public class DownloadData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            //ανάθεση στο postData τα δεδομένα απο την JSON
            String postData = downloadJSON(strings[0]);
            return postData;
        }
        //η μέθοδος η οποία κατεβάζει τα δεδομένα
        //Παράμετρος: δέχεται το URLL του API
        private String downloadJSON(String urlPath) {
            //δημιουργεία StringBuilder για να προσθέσουμε στην συνέχεια τα δεδομένα του API
            StringBuilder sb = new StringBuilder();
            try{
                URL url = new URL(urlPath);
                //εκκίνηση σύνδεσης με URL
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //δημιουργία και εκκίνηση του BufferedReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    //Προσθήκη κάθε γραμμής JSON που διαβάζεται απο το URL στο sb
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }
                //κλείσιμο σύνδεσης με το URL
                connection.disconnect();
                //κλείσιμο BufferedReader;
                reader.close();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ",e);
            }
            //επιστρέφουμε τα JSONData
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.d(TAG, "onPostExecute parameter is " + jsonData );
            //Δημιουργια JSON Parser για να διαχειριστεί τα δεδομένα
            JSONParser parser = new JSONParser();
            parser.parse(jsonData,1);
            //έπειτα παίρνω την λίστα η οποία είναι αντικείμενο τύπου Currencies
            curs = parser.getCurs();
            listofCodes = new ArrayList<>();
            listOfNames = new ArrayList<>();
            //τέλος διαχωρισμός των δεδεομένων σε δύο λίστες τύπου String
            //Θα μπορουσε να γίνει και implementation στο Parcelable στην κλάση Currencies
            //ώστε να μπορέσει να αποσταλθεί αυτούσια η λίστα.
            for(Currencies currency: curs){
                Log.d(TAG, "onPostExecute: "+ currency.getCode());
                listofCodes.add(currency.getCode());
                listOfNames.add(currency.getName());
           }

        }
    }
}