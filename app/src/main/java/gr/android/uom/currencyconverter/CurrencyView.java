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

import static gr.android.uom.currencyconverter.R.id.textView;

public class CurrencyView extends AppCompatActivity {
    private String myUrl = "http://demo5434819.mockable.io/CurrencyConverter123456";
    private ListView listView;
    private ArrayList<Currencies> curs;
    private static final String TAG = "Prosa";
    JSONObject JO1;
    private ArrayList<Currencies> currencies;
    ArrayList<String> listOfStrings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_currency_view);
        listOfStrings = new ArrayList<>();
        listView = findViewById(R.id.listView);

        ArrayList strs = getIntent().getStringArrayListExtra("strs");




        Intent intent = getIntent();
        ArrayList<String> namesList = intent.getStringArrayListExtra("names_list");
        ArrayList<String> codesList = intent.getStringArrayListExtra("codes_list");
        namesList.size();
        for(int i = 0; i<codesList.size(); i++){
            Log.d(TAG, "onCreate: "+ codesList.get(i) +": "+namesList.get(i));
            listOfStrings.add(codesList.get(i) +": "+namesList.get(i));
        }
        ArrayAdapter<String> aA = new ArrayAdapter<>(CurrencyView.this,R.layout.row_layout,R.id.text1,listOfStrings);
        listView.setAdapter(aA);

    }






}
