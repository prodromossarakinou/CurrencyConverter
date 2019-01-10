package gr.android.uom.currencyconverter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

public class FavouritesActivity extends AppCompatActivity {
    AdapterView.OnItemClickListener listener;
    AdapterView.OnItemLongClickListener longListener;
    //Αντικείμενο τύπου listView στο οποίο στην συνέχεια αναθέτεται το ListView της XML
    private ListView lv;
    //Λίστα στην οποία έρχονται τα δεδομένα απο το Firebase
    private ArrayList<MyFavourites> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        //ανάθεση τιμής στην λίστα
        lv = findViewById(R.id.savesList);
        //Δημιουργία ItemLongClickListener
        //για χειρισμό συμβάνων απο συνεχόμενο κλικ.
        longListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //Αυτό που συμβαίνει είναι να καλλείται η μέθοδος delete η οποία παίρνει ώς παράμετρο
                //το text Item της listView στο οποίο έγινε το click;
                Delete(lv.getItemAtPosition(position).toString());
                return true;
            }

        };
        //Δημιουργία OnItemClickLIstener
        //για χειρισμό συμβάντων σε απλό κλικ
        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //αυτό που συμβαίνει είναι να καλλείται η μέθοδος viewDetails η οποία παίρνει ώς παράμετρο
                //το text Item της listView στο οποίο έγινε το click;
                viewDetails(lv.getItemAtPosition(position).toString());
            }
        };
        //προσθήκη των listener στο ListView και ενεργοποίηση του LongClick για τα Items της λίστας
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(longListener);
        lv.setOnItemClickListener(listener);
        //Με την προσπέλαση της μεθόδου addToList εμφανίζονται τα δεδομένα στην λίστα
        addToList();
    }
    //Μέθοδος addToList
    public void addToList() {
        //Δημιουργία αντικειμένου FirebaseFirestore και παραλαβή της FireStore;
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference cr1;
        //Ανάθεση της Collection η οποία είναι ίδια με το e-mail του χρήστη το οποίο για κάθε χρήστη είναι αυθεντικό
        //σύμφωνα με τα user-authentication Rules της firebase
        cr1 = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //Δημιουργία αντικειμένου κλάσης listener(στο τέλος αυτου του αρχείου)
        Listener first;
        first = new Listener();
        //προσθήκη του Listener στο αντικέιμενο Τύπου CollectionListener
        cr1.addSnapshotListener(first);

    }
    //Μέθοδος View Details δεχεται το text του Item της λίστας που έγινε click
    public void viewDetails(String aText){
        //Δημιουργία Αντικειμένου FirebaseFirestore
        FirebaseFirestore fs;
        for(MyFavourites f: lista){
            //προσπέλαση της λιστας για να βρώ σε ποίο δεδομένο έγινε το CLICK
            if(f.getText().equals(aText)){

                //ανάθεση των δεδομέωων της Firestore
                fs = FirebaseFirestore.getInstance();

                //Δημιουργία DocumentReference με Collection αναφορά στο email του συνδεμένου χρήστη
                //και αναφορά του Document στο id του συγκεκριμένου δεδομένου ώστε να εμφανιστούν τα σωστά data
                final DocumentReference dr = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail()).document(f.getId());
                //Προσθήκη EventListener και δημιουργία αυτού
                dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    //επικάλυψη την onEvent για να πάρω τα δεδομενα
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        //σε περίπτωση που τα δεδομένα δεν ειναι άδεια τοτε
                        if(documentSnapshot.getString("timestamp")!=null) {
                            //Εμφανιση μηνυματος στον χρήστη με τα δεδομένα
                            final AlertDialog messageDialoge = new AlertDialog.Builder(FavouritesActivity.this)
                                    .setTitle("Saving Details")
                                    .setMessage("Saving Details: \n"
                                            +"Date&Time: "+documentSnapshot.getString("timestamp")+"\n"
                                            +"Device in which this favourite is saved: "+ documentSnapshot.getString("DeviceDetails"))
                                    //απο το DocumentSnapshot το οποιο έχει της αναφορές που αναφέρθηκαν στο σχόλιο
                                    //της DocumentRefernce
                                    //παίρνω τα πεδια "timestamp"
                                    //και "DeviceDetails τα οποία:
                                    //είναι δύο πεδία μέσα σε ένα Collection με όνομα το email του χρήστη
                                    //κάποιο αυτόματο DocumentID που μας δίνει η firestore και το κρατάμε στην δημιουργία των αντικειμένων MyFavourites
                                    //οπότε τα δεδομένα που παίρνουμε είναι για Item στο οποίο έγινε το Click
                                    //και είναι ορατό μόνο για τον συγκεκριμένο χρήστη

                                    //προσθήκη button για να συνεχίσει ο χρήστης.
                                    .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            //εμφάνιση μηνύματος.
                            messageDialoge.show();
                        }
                    }
                });
                break;
            }
        }
    }

    //Μέθοδος Delete δεχεται το text του Item της λίστας που έγινε click
    public void Delete(final String aText){
        //Δημιουργία Αντικειμένου FirebaseFirestore
        FirebaseFirestore fs;
        for(MyFavourites f: lista){
            //προσπέλαση της λιστας για να βρώ σε ποίο δεδομένο έγινε το CLICK
            Log.d("TEXTS", "Delete: " + f.getText()+"="+aText);
            if(f.getText().equals(aText)){
                //ανάθεση των δεδομέωων της Firestore
                fs = FirebaseFirestore.getInstance();
                //Δημιουργία DocumentReference με Collection αναφορά στο email του συνδεμένου χρήστη
                //και αναφορά του Document στο id του συγκεκριμένου δεδομένου ώστε να εμφανιστούν τα σωστά data
                final DocumentReference dr = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail()).document(f.getId());
                //Δημιουργία μηνύματος
                final AlertDialog askForDelete = new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //σε περίπτωση που χρήστης πατήσει το text με τίτλο Delete
                                //τότε διαγράφετε το αρχείο απο την FireStore
                                dr.delete();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            //σε περίπτωση που ο χρήστης πατήση το text με τίτλο το No
                            //τότε δεν γίνεται τίποτα και επιστρέφει ο χρήστης στην οθόνη
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    }

                                });
                            }
                        }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                            //Δεν γίνεται τίποτα και ο Χρήστης επιστέφει στην αρχική οθόνη επίσης εάν ο χρήστης πατήσει οποιοδήποτε κουμπί πχ το Back Key
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                return false;
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            //Δεν γίνεται τίποτα και ο Χρήστης επιστέφει στην αρχική οθόνη επίσης εάν ο χρήστης πατήσει κάπου εκτός του AlertDialog
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        })
                        .create();
                //εμφάνιση μηνύματος
                askForDelete.show();

                break;
            }

        }

    }
    //επικάλυψη κλάσης OnBackPressed
    @Override
    public void onBackPressed(){
        //o Χρήστης όταν πατάει το πίσω θα επιστρέφει στην MainMenu(Activity)
        Intent intent = new Intent(FavouritesActivity.this,MainMenu.class);
        startActivity(intent);

    }
    //μέθοδος setList η οποία χρησιμοποιείται για να πάρουμε στην λιστα:lista τα δεδομένα απο το Firestore
    public void setListt(ArrayList<MyFavourites> a){
        lista = new ArrayList<>(a);
        Log.d("grigoris", "setListt: ."+lista.size());

    }
    //Κλάση Listener η οποία κάνει implemetn την EventListener και φέρνει τα Documents απο την FireBase;
    public class Listener implements EventListener<QuerySnapshot>{
        private ArrayList<MyFavourites> dataToReturn;
        //Άδειος Κατασκευαστής
        public Listener(){


        }


        //Επικάλυψης της onEvent για να πάρουμε τα δεδομένα του χρήστη απο την Firebase
        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            dataToReturn = new ArrayList<>();
            //το CollectionReference στο οποίο ανατέθηκε ο Listener έχει αναφορα στο e-mail του χρήστη
            //επομένως αν παρουμε τα δεδομένα
            //έχουμε την λίστα
            for(DocumentSnapshot s: queryDocumentSnapshots.getDocuments()){
                //απο το Snapshot s παιρνω το SavedCurrency που είναι ένα Field μέσα σε ένα Document το οποίο είναι μέσα σε μια Collection
                //Δημιουργώ αντικείμενο τύπου MyFavoyrites
                //και παίρω το δεδομενο SavedCurrency απο την Firebase;
                MyFavourites f = new MyFavourites(s.getString("SavedCurrency")+" ",s.getId());
                //Το προσθέτω στην λίστα
                dataToReturn.add(f);


            }
            //Ταξινομώ την λίστα
            Collections.sort(dataToReturn);
            //Διασπάω τα δεδομένα της σε αυτο που εμφανίζεται και στο ID της.
            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> dataId = new ArrayList<>();
            for(MyFavourites f: dataToReturn){
                data.add(f.toString());
                dataId.add(f.getId());

            }
            //Δημιουργώ adapter με:
            //1η παράμετρο το Context
            //2η παράμετρο το Layout του XML του αρχείου που περιέχει τα γραφικά συστατικά της λίστας
            //3η παράμετρο το ID του γραφικού συστατικού στο οποίο θα φαίνονται τα αρχεία της λιστας.
            //4η παράμετρο την arrayList που θέλω να εμφανίσω
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.saved_item, R.id.savedItem, data);
            lv.setAdapter(adapter);

            //Αναθέτω την Λίστα της κλάσης FavouritesActivity
            setListt(dataToReturn);



        }

    }



}
