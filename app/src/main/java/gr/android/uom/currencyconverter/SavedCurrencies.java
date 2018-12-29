package gr.android.uom.currencyconverter;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

import javax.annotation.Nullable;

public class SavedCurrencies {
    private double rate;
    private String curr1,curr2;
    private String date;
    FirebaseFirestore fs;
    DocumentReference dr;
    public SavedCurrencies(double aRate,String aCurr1,String aCurr2,String aDate){
        rate = aRate;
        curr1 = aCurr1;
        curr2 = aCurr2;
        date = aDate;
    }
    public void Save(SavedCurrencies aSave){
        fs = FirebaseFirestore.getInstance();

        dr= fs.collection("Saves").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        HashMap<String,String> hs = new HashMap<>();

        hs.put("SAVE","HEY");

        dr.set(hs).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
    public void addToList(){
        dr= fs.collection("Saves").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Log.d("olakomple", "onEvent: "+documentSnapshot.getData().toString());
                Log.d("olakala", "onEvent: "+documentSnapshot.getString("SAVE"));
            }
        });
    }
}
