package gr.android.uom.currencyconverter;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import static gr.android.uom.currencyconverter.R.layout.saved_item;

public class Saves extends AppCompatActivity {
    ArrayList<String> ar;
    private ListView lv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);

        ar = new ArrayList<>();
        for(int i = 0; i<23; i++){
            ar.add(i+" Hello World");
        }
        lv = findViewById(R.id.savesList);
        tv = findViewById(R.id.savedItem);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.saved_item,R.id.savedItem,ar);
        lv.setAdapter(arrayAdapter);
        SavedCurrencies sc = new SavedCurrencies(1.23,"EUR","USD","23");
        sc.Save(sc);
        sc.addToList();

    }
}
