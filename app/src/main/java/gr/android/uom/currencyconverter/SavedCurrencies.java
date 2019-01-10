package gr.android.uom.currencyconverter;


import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


//κλάση η οποία υποδηλώνει αντικείμενα τα οποία αποθηκεύονται στο Firestore
//ο χρήστης έχει την δυνατότητα να τα δεί μέσω του Favourites το οποίο εμφανίζεται στον χρήστη
//FavouritesActivity
public class SavedCurrencies extends AppCompatActivity {
    private double rate;
    private String curr1,curr2;
    private String date;
    FirebaseFirestore fs;
    DocumentReference dr;
    CollectionReference dr1;
//    ArrayList<String> data;
    //κατασκευαστής δέχεται ένα exchange Rate δύο Currencies_Code(Ex. BTC,EUR) και μια ημερομηνία
    public SavedCurrencies(double aRate,String aCurr1,String aCurr2,String aDate){
        rate = aRate;
        curr1 = aCurr1;
        curr2 = aCurr2;
        date = aDate;
    }
    //Μέθοδος Save, αυτήν εκτελείτε απο την την μέθοδο που χρησιμοποιεί ο listener του Button Save
    //Που βρίσκεται στην ConvertActivity
    public void Save(SavedCurrencies aSave){

        fs = FirebaseFirestore.getInstance();
        dr1 = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        dr1= fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        dr= fs.collection("FavouritesActivity").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        HashMap<String,String> hs = new HashMap<>();
        //αποθηκεύω τα δεδομένα σε ένα hashmap
        //ΠΡΩΤΑ ΤΟ ΤΑ ΝΟΜΙΣΜΑΤΑ ΚΑΙ ΤΟ RATE
        hs.put("SavedCurrency","  "+aSave.curr1 + " to " + aSave.curr2 +" " +"Rate: "+aSave.rate );
        //ΜΕΤΑ ΤΗΝ ΔΕΔΟΜΕΝΗ ΧΡΟΝΙΚΗ ΣΤΙΓΜΗ
        hs.put("timestamp",aSave.date);
        //ΜΕΤΑ ΤΟ ΚΙΝΗΤΟ ΜΕΣΩ ΤΟΥ ΟΠΟΙΟΥ ΑΠΟΘΗΚΕΥΤΗΚΕ
        hs.put("DeviceDetails", Build.BRAND +" "+ Build.MODEL);

        //αποστέλνω το hashMap στην Firestore στην Collection με όνομα το email του Χρήστη
        //και με ένα τυχαίο αυτόματο DocumentID που θα δημιουργηθεί
        dr1.add(hs);

    }



}
