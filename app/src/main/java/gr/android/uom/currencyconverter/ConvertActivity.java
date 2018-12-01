package gr.android.uom.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;

public class ConvertActivity extends AppCompatActivity {

    private static final String TAG = "PROSA";
    private String from = "GBP";
    private String to2 = "EUR";
    private String API_KEY = "AcWTjGGE6fuTVxlZZP1bRyifBKVkuoGs";
    private float ammount = 10;
    private String myUrl;
    private String endpoint = "convert";
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private TextView fromT;
    private TextView toT;
    private EditText ammountText;
    private TextView result;
    private Double value;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);

        Intent intent = getIntent();
        final ArrayList<String> codesList = intent.getStringArrayListExtra("codes_list");
        ArrayAdapter<String> Aa = new ArrayAdapter<>(this,R.layout.list_item,R.id.text,codesList);
        fromSpinner.setAdapter(Aa);
        toSpinner.setAdapter(Aa);
        Log.d(TAG, "onCreate: "+fromSpinner.getSelectedItem().toString());


        ammountText = findViewById(R.id.ammountText);
        result = findViewById(R.id.result);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }
        });
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void Convert(View view){
        from = fromSpinner.getSelectedItem().toString();
        to2 = toSpinner.getSelectedItem().toString();

        if(!ammountText.getText().toString().isEmpty()) {
            if(!from.equals(to2)) {
                myUrl = "https://forex.1forge.com/1.0.3/" + endpoint + "?from=" + from + "&to=" + to2 + "&quantity=" + ammountText.getText().toString() + "&api_key=" + API_KEY;
                DownloadData dlData = new DownloadData();
                dlData.execute(myUrl);
            }
            else{
                Toast ts = null;
                ts.makeText(getApplicationContext(),"Please select difference currencies to convert!",Toast.LENGTH_SHORT).show();

            }

        }
        else{
            Toast ts = null;
            ts.makeText(getApplicationContext(),"Please set an ammount",Toast.LENGTH_SHORT).show();
        }

    }



    public class DownloadData extends AsyncTask<String,String,String> {

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



            super.onPostExecute(jsonData);
            Log.d(TAG, "onPostExecute parameter is " + jsonData );

            try {
                JSONObject jsonObject= new JSONObject(jsonData);
                Double text = jsonObject.getDouble("value");
                DecimalFormat format = new DecimalFormat("#.00");
                result.setText(ammountText.getText().toString()+" " +fromSpinner.getSelectedItem().toString()+
                        " is worth " +format.format(text).toString()+" "+toSpinner.getSelectedItem().toString() +".");
            } catch (JSONException e) {
                Log.d("MITSOS", "onPostExecute: gamw to spiti");
                e.printStackTrace();
            }


        }
    }
}
