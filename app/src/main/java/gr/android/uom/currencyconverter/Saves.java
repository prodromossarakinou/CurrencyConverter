package gr.android.uom.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

public class Saves extends AppCompatActivity {
    ArrayList<String> ar;
    private ListView lv;
    TextView tv;
    ArrayAdapter<String> arrayAdapter;



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



        ar= this.addToList();

//



    }

    public ArrayList<String> addToList(){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference cr1;
        final ArrayList<String> data = new ArrayList<>();

        final ArrayList<String> dataToReturn = new ArrayList<>();
        cr1 = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        cr1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int k = 0;


                for(DocumentSnapshot s: queryDocumentSnapshots.getDocuments()){
                    Log.d("Aloha2", "onEvent: " + s.getString("SavedCurrency")+ " "+k);
                    dataToReturn.add(s.getString("SavedCurrency"));
                    k++;
                }

                Collections.sort(dataToReturn);

                ArrayAdapter<String> adapter= new ArrayAdapter<String>(Saves.this,R.layout.saved_item,R.id.savedItem,dataToReturn);
                lv.setAdapter(adapter);
            }

        });

        return dataToReturn;
    }
}
