package gr.android.uom.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class CurrencyView extends AppCompatActivity {
    private String myUrl = "http://demo5434819.mockable.io/CurrencyConverter123456";
    private ListView listView;
    ArrayList<Currencies> curs;
    private static final String TAG = "TeoMainActivity";
    JSONObject JO1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_view);
        DownloadData downloadData = new DownloadData();
        downloadData.execute(myUrl);
        listView = findViewById(R.id.listView);



    }



    public class DownloadData extends AsyncTask<String,String,String>{

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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(String jsonData) {
            String[] from = {"name_item"};

            int[] to = {R.id.codeTextView};
            super.onPostExecute(jsonData);
            Log.d(TAG, "onPostExecute parameter is " + jsonData );
            JSONParser parser = new JSONParser();
            parser.parse(jsonData);

             curs = parser.getCurs();
            HashMap<String,String> hm = new HashMap<>();
            for(int i =0; i<curs.size(); i++){
                Log.d(TAG, curs.get(i).getName());
                Log.d(TAG, "-------------------------------");
                hm.put(curs.get(i).getCode(),curs.get(i).getName());
            }
            ArrayList<HashMap<String, String>> arr =new ArrayList<>();
            arr.add(hm);

        }
    }


}
