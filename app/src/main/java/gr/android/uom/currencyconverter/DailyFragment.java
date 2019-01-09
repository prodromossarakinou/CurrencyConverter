package gr.android.uom.currencyconverter;

import android.content.Intent;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;


public class DailyFragment extends Fragment {
    private View rootView;
    private String API_KEY = "1e086fbf519d7a8b97ac3035428977d9";
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
        rootView=inflater.inflate(R.layout.fragment_daily, container, false);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HELLOWORLD", "onStart: ");
        GifImageView gif= rootView.findViewById(R.id.usdGif);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);


        SimpleDateFormat dateYearFormat = new SimpleDateFormat("yy",Locale.getDefault());
        SimpleDateFormat dateMonthFormat = new SimpleDateFormat("MM",Locale.getDefault());
        SimpleDateFormat dateDayFormat = new SimpleDateFormat("dd",Locale.getDefault());
        String previousDay = "20"+dateYearFormat.format(cal.getTime())
                +"-"+dateMonthFormat.format(cal.getTime())
                +"-"+ dateDayFormat.format(cal.getTime());

        myUrlForToday = "http://data.fixer.io/api/"+latestDay+"?access_key="+API_KEY+"&symbols=USD,BTC";
        myUrlForPreviousDay = "http://data.fixer.io/api/"+previousDay+"?access_key="+API_KEY+"&symbols=USD,BTC";
        DownloadDataForToday latestDayData = new DownloadDataForToday();
        latestDayData.execute(myUrlForToday);
        DownloadDataForYesterday previousData = new DownloadDataForYesterday();
        previousData.execute(myUrlForPreviousDay);




    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void setArrows(){
        GifImageView usdGif = rootView.findViewById(R.id.usdGif);
        GifImageView eurGif = rootView.findViewById(R.id.eurGif);
        GifImageView btcGif = rootView.findViewById(R.id.btcGif);
        if(usdRateToday>usdRateYesterday){
            Log.d(TAG, "setArrows: bigger");

            usdGif.setImageResource(R.drawable.arrowup);
            eurGif.setImageResource(R.drawable.arrowdown);
        }
        else{
            Log.d(TAG, "setArrows: no");
            usdGif.setImageResource(R.drawable.arrowup);
            eurGif.setImageResource(R.drawable.arrowdown);
        }
        if(btcRateToday>btcRateYesterday){
            btcGif.setImageResource(R.drawable.arrowup);
        }
        else{
            btcGif.setImageResource(R.drawable.arrowdown);

        }
        btcGif.setVisibility(View.VISIBLE);
        usdGif.setVisibility(View.VISIBLE);
        eurGif.setVisibility(View.VISIBLE);
    }
    public class DownloadDataForToday extends AsyncTask<String,String,String> {

        
        
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground starts with: " + strings[0]);
            String postData = downloadJSON(strings[0]);
            if(postData == null){
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0] );
            }
            return postData;
        }
        private String downloadJSON(String urlPath) {
            StringBuilder sb = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int reponseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + reponseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }

                reader.close();

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

            Log.d(TAG, "onPostExecute: "+jsonData);
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String rate = jsonObject.getString("rates");
                JSONObject rates =new JSONObject(rate);

                setUSDrateToday(rates.getDouble("USD"));
                setBTCrateToday(rates.getDouble("BTC"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class DownloadDataForYesterday extends AsyncTask<String,String,String> {



        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground starts with: " + strings[0]);
            String postData = downloadJSON(strings[0]);
            if(postData == null){
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0] );
            }
            return postData;
        }
        private String downloadJSON(String urlPath) {
            StringBuilder sb = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int reponseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + reponseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }

                reader.close();

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

            Log.d(TAG, "onPostExecute: "+jsonData);
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String rate = jsonObject.getString("rates");
                JSONObject rates =new JSONObject(rate);

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
