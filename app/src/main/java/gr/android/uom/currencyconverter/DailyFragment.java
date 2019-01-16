package gr.android.uom.currencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

//Fragment στο οποίο εμφανίζονται Daily Results
//Δηλαδει πώς συμπεριφέρονται τρια(3) βασικά νομίσματα (USD,EUR,BTC)
public class DailyFragment extends Fragment {
    private View rootView;
    private String API_KEY = "e44dbe1727d627545c4b5f9a86541bea"; //2ndAPI_KEY: 5c397e6543685b082726be1e22ba69ee 3rd API_KEY: e44dbe1727d627545c4b5f9a86541bea
    private String latestDay = "latest";
    private String myUrlForToday;
    private Double usdRateToday;
    private Double usdRateYesterday;
    private Double btcRateToday;
    private Double btcRateYesterday;
    private String myUrlForPreviousDay;

    final String TAG = "HELLOWORLD";
    public DailyFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout
        rootView=inflater.inflate(R.layout.fragment_daily, container, false);
        return rootView;
    }
    //επικάλυψη της onStart
    @Override
    public void onStart() {
        super.onStart();
        //Εύρεση προηγουμενη μέρας για σχετική σύγκριση
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);

        //Συγκεκριμένα φορματς για να επιτύχουμε αυτό που ζηταεί το Api
        SimpleDateFormat dateYearFormat = new SimpleDateFormat("yy",Locale.getDefault());
        SimpleDateFormat dateMonthFormat = new SimpleDateFormat("MM",Locale.getDefault());
        SimpleDateFormat dateDayFormat = new SimpleDateFormat("dd",Locale.getDefault());
        //Προσθήκη και το 20 μπροστάαπο την χρονολογία διότι εμφάνιζει σκέτο 19 και το API ήθελε 2019
        String previousDay = "20"+dateYearFormat.format(cal.getTime())
                +"-"+dateMonthFormat.format(cal.getTime())
                +"-"+ dateDayFormat.format(cal.getTime());
        //Δημιουργία των δύο URL για κατέβασμα των δεδομένων
        myUrlForToday = "http://data.fixer.io/api/"+latestDay+"?access_key="+API_KEY+"&symbols=USD,BTC";
        myUrlForPreviousDay = "http://data.fixer.io/api/"+previousDay+"?access_key="+API_KEY+"&symbols=USD,BTC";
        //Δημιουργησα δύο κλάσεις γιατι δεν ήξερα πως να επικαλύψω την Execute για να εκτελεί ξεχωριστές περιπτώσεις.
        DownloadDataForToday latestDayData = new DownloadDataForToday();
        //Κατέβασμα τελευταίων δεδομένων νομίσμάτων
        latestDayData.execute(myUrlForToday);
        DownloadDataForYesterday previousData = new DownloadDataForYesterday();
        //Κατέβασμα χθεσινών δεδομένων νομισμάτων
        previousData.execute(myUrlForPreviousDay);




    }


    //Μέθοδος στην οποία αποφασίζεται τι βελάκι θα μπεί σε κάθε νόμισμα
    public void setArrows(){
        //Δημιουργία των τριών GifImageView
        GifImageView usdGif = rootView.findViewById(R.id.usdGif);
        GifImageView eurGif = rootView.findViewById(R.id.eurGif);
        GifImageView btcGif = rootView.findViewById(R.id.btcGif);
        //Διότι το API επιστρέφει Rates τα οποία είναι ανάλογα με το EUR
        //Το rate του EUR είναι μόνιμα 1
        //Όμως όταν ανεβαίνει κάποιο άλλο νόμισμα πχ το USD
        //σημαίνει οτι το EUR πέφτει
        //Με αυτόν τον συλλογισμό προχωρησα σε
        if(usdRateToday>usdRateYesterday){

            //εαν το σημερινό Rate του USD είναι μεγαλύτερο απο το χθεσινό τοτε:
            //Βελακι πρασινο προς τα πάνω για το USD
            usdGif.setImageResource(R.drawable.arrowup);
            //Βελάκι κόκκινο προς τα κάτω για το EUR
            eurGif.setImageResource(R.drawable.arrowdown);
        }
        else{
            //εαν το σημερινό Rate του USD είναι μικρότερο απο το χθεσινό τοτε:
            //Βελάκι κόκκινο προς τα κάτω για το USD
            usdGif.setImageResource(R.drawable.arrowdown);
            //Βελάκι πράσινο προς τα πάνω για το EUR
            eurGif.setImageResource(R.drawable.arrowup);
        }
        if(btcRateToday>btcRateYesterday){
            //εαν το σημερινό Rate του BTC είναι μεγαλύτερο απο το χθεσινό τοτε:
            //βελάκι πράσινο προς τα πάνω για το BTC
            btcGif.setImageResource(R.drawable.arrowup);
        }
        else{
            //εαν το σημερινό Rate του BTC είναι μικρότερο απο το χθεσινό τοτε:
            //βελάκι κόκκινο προς τα κάτω για το BTC
            btcGif.setImageResource(R.drawable.arrowdown);
        }
    }

    //Πρώτη κλάση DownloadData η οποία κληρονομεί AsyncTask
    public class DownloadDataForToday extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            //ανάθεση στο postData τα δεδομένα απο την JSON
            String postData = downloadJSON(strings[0]);
            return postData;
        }
        //η μέθοδος η οποία κατεβάζει τα δεδομένα
        //Παράμετρος: δέχεται το URL του API
        private String downloadJSON(String urlPath) {
            //δημιουργεία StringBuilder για να προσθέσουμε στην συνέχεια τα δεδομένα του API
            StringBuilder sb = new StringBuilder();
            try{
                //εκκίνηση σύνδεσης με URL
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int reponseCode = connection.getResponseCode();
                //δημιουργία και εκκίνηση του BufferedReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    //Προσθήκη κάθε γραμμής JSON που διαβάζεται απο το URL στο sb
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }
                //κλείσιμο του bufferedReader
                reader.close();
                //κλείσιμο σύνδεσης με το URL
                connection.disconnect();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ",e);
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String jsonData) {



            super.onPostExecute(jsonData);
            //Διαχείριση των JSONData αναλόγως με το πως τα στέλνει το API
            //οπώς είδα τα δεδομένα έιναι { JSON DATA: sth
                                            // rates:{
                                            //         "USD":aDOUBLE;
            //Οπότε είναι ένα JSONΟbject μέσα σ'ενα άλλο
            Log.d(TAG, "onPostExecute: "+jsonData);
            try {
                //Δημιουργία εξωτερικού JSONObject
                JSONObject jsonObject = new JSONObject(jsonData);
                //Και μετα Δημιουργια εσωτερικού JSONObject
                String rate = jsonObject.getString("rates");
                JSONObject rates =new JSONObject(rate);
                //Και απο το εσωτερικό παίρνω τα rates
                //τα ενσωματώνω με setter μέσα στα rates της κλάσης
                setUSDrateToday(rates.getDouble("USD"));
                setBTCrateToday(rates.getDouble("BTC"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //δεύτερη κλάση DownloadData η οποία κληρονομεί AsyncTask
    public class DownloadDataForYesterday extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            //ανάθεση στο postData τα δεδομένα απο την JSON
            String postData = downloadJSON(strings[0]);
            return postData;
        }
        //η μέθοδος η οποία κατεβάζει τα δεδομένα
        //Παράμετρος: δέχεται το URL του API
        private String downloadJSON(String urlPath) {
            //δημιουργεία StringBuilder για να προσθέσουμε στην συνέχεια τα δεδομένα του API
            StringBuilder sb = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                //εκκίνηση σύνδεσης με URL
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int reponseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + reponseCode);
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
            //Διαχείριση των JSONData αναλόγως με το πως τα στέλνει το API
            //οπώς είδα τα δεδομένα έιναι { JSON DATA: sth
                                 // rates:{
                                //         "USD":aDOUBLE;
            //Οπότε είναι ένα JSONΟbject μέσα σ'ενα άλλο

            Log.d(TAG, "onPostExecute: "+jsonData);
            try {
                //Δημιουργία εξωτερικού JSONObject
                JSONObject jsonObject = new JSONObject(jsonData);
                //Και μετα Δημιουργια εσωτερικού JSONObject
                String rate = jsonObject.getString("rates");
                JSONObject rates =new JSONObject(rate);
                //Και απο το εσωτερικό παίρνω τα rates
                //τα ενσωματώνω με setter μέσα στα rates της κλάσης
                setUSDrateYesterday(rates.getDouble("USD"));
                setBTCrateYesterday(rates.getDouble("BTC"));
                setArrows();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void setUSDrateToday(Double aRate){
        Log.d("123321", "setUSDrate: "+aRate);
        usdRateToday = aRate;
    }


    public void setUSDrateYesterday(Double aRate) {
        Log.d("123321", "setUSDrateYesterday: "+aRate);
        usdRateYesterday = aRate;
    }



    public void setBTCrateToday(Double aRate) {
        btcRateToday = aRate;
    }


    public void setBTCrateYesterday(Double aRate) {
        btcRateYesterday = aRate;
    }
}
