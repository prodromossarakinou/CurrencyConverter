package gr.android.uom.currencyconverter;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class SignInActivity extends AppCompatActivity {
    private FirebaseUser user;

    private TextView nameView;
    private String myUrl = "http://demo5434819.mockable.io/CurrencyConverter123456";
    public ArrayList<Currencies> curs;
    ArrayList<String> listOfNames;
    ArrayList<String> listofCodes;
    private static final String TAG = "Prosa";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //download data first of all
        DownloadData downloadData = new DownloadData();
        downloadData.execute(myUrl);
        setContentView(R.layout.activity_sign_in);
        user = FirebaseAuth.getInstance().getCurrentUser();
        listOfNames = new ArrayList<>();
        listofCodes = new ArrayList<>();






    }


    public static String activityTitle(){
        return "SignIn";
    }





    public void seeList(View v){
        Intent intent = new Intent(this,Saves.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() { }


    public void openCurrenciesView (View view){
        Intent intent;
        intent = new Intent(SignInActivity.this,CurrencyView.class);
        if(listofCodes.isEmpty() || listOfNames.isEmpty()){
            ArrayList<String> emptyNames = new ArrayList<>();
            ArrayList<String> emptyCodes = new ArrayList<>();
            emptyNames.add("Go back and come again in several minutes");
            emptyCodes.add("  -  ");
            intent.putStringArrayListExtra("names_list",emptyNames);
            intent.putStringArrayListExtra("codes_list",emptyCodes);
            startActivity(intent);
        }
        else {
            intent.putStringArrayListExtra("names_list", listOfNames);
            intent.putStringArrayListExtra("codes_list", listofCodes);
            startActivity(intent);
        }

    }
    public void openConverter (View view){
        Intent intent;
        intent = new Intent(SignInActivity.this,ConvertActivity.class);
        if(listofCodes.isEmpty()){
            ArrayList<String> emptyCodes = new ArrayList<>();
            emptyCodes.add("   ");
            intent.putStringArrayListExtra("codes_list",emptyCodes);
            startActivity(intent);
        }
        else {
            intent.putStringArrayListExtra("codes_list", listofCodes);
            startActivity(intent);
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
                Log.d(TAG, "downloadJSON: "+sb.toString());

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
            JSONParser parser = new JSONParser();
            parser.parse(jsonData,1);

            curs = parser.getCurs();
            listofCodes = new ArrayList<>();
            listOfNames = new ArrayList<>();
            for(Currencies currency: curs){
                Log.d(TAG, "onPostExecute: "+ currency.getCode());
                listofCodes.add(currency.getCode());
                listOfNames.add(currency.getName());

            }



        }
    }
}