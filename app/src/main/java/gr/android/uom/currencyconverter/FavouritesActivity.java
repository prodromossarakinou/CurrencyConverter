package gr.android.uom.currencyconverter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ArrayList<String> ar;

    private ListView lv;
    TextView tv;

    private ArrayList<MyFavourites> lista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ar = new ArrayList<>();
        for(int i = 0; i<23; i++){
            ar.add(i+" Hello World");
        }
        lv = findViewById(R.id.savesList);
        tv = findViewById(R.id.savedItem);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Delete(lv.getItemAtPosition(position).toString());
                Log.d("onDEL", "onItemClick: "+ lv.getItemAtPosition(position).toString());
            }
        });


        this.addToList(0);
//        this.getList();
//



    }

    public void addToList(final int code) {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference cr1;
        ArrayList<MyFavourites> data = new ArrayList<>();

        final ArrayList<MyFavourites> dataToReturn = new ArrayList<>();
        cr1 = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Listener l = new Listener();
        cr1.addSnapshotListener(l);


    }
    public void Delete(String aText){



        FirebaseFirestore fs;
        for(MyFavourites f: lista){
            Log.d("TEXTS", "Delete: " + f.getText()+"="+aText);
            if(f.getText().equals(aText)){
                fs = FirebaseFirestore.getInstance();
                final DocumentReference dr = fs.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail()).document(f.getId());
                final AlertDialog askForDelete = new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("DELETED", "onSuccess: ");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    }
                                });
                            }
                        }).create();
                askForDelete.show();

            }
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(FavouritesActivity.this,MainMenu.class);
        startActivity(intent);

    }

    public class Listener implements EventListener<QuerySnapshot>{
        private ArrayList<MyFavourites> dataToReturn;
        private ArrayList<String> strs;
        public Listener(){


        }



        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            int k = 0;
            dataToReturn = new ArrayList<>();

            for(DocumentSnapshot s: queryDocumentSnapshots.getDocuments()){
                Log.d("Aloha2", "onEvent: " + s.getString("SavedCurrency")+ " "+k);
                MyFavourites f = new MyFavourites(s.getString("SavedCurrency")+" "+s.getString("timestamp"),s.getId());
                dataToReturn.add(f);
                Log.d("PARESAVE RE", "onEvent: "+f.toString());
                Log.d("prosas", "onEvent: "+ s.getId());
                Log.d("PAREPAREPARE ","onEvent "+ Build.BRAND);
                Log.d("PAREPAREPARE ","onEvent "+ Build.MODEL);
                k++;
            }

            Collections.sort(dataToReturn);
            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> dataId = new ArrayList<>();
            for(MyFavourites f: dataToReturn){
                data.add(f.toString());
                dataId.add(f.getId());

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.saved_item, R.id.savedItem, data);
            lv.setAdapter(adapter);


            for(MyFavourites d: dataToReturn){
                Log.d("kompleman1", "getList: "+d.getText());
            }
            setListt(dataToReturn);



        }
        public ArrayList<MyFavourites> getData(){
            Log.d("PARESIZE", "getData: "+dataToReturn.size());
            return dataToReturn;
        }



    }

    public void setListt(ArrayList<MyFavourites> a){
        lista = new ArrayList<>(a);
        Log.d("grigoris", "setListt: ."+lista.size());

    }
    public  ArrayList<MyFavourites> getListt(){
        return lista;
    }

}