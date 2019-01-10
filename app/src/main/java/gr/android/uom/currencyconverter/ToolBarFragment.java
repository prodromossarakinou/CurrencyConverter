package gr.android.uom.currencyconverter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;

//Fragment Το οποίο περιέχει το Menu:
public class ToolBarFragment extends Fragment {
    //Επειδή έχω διαγράψει το ActionBar το menu θα είναι πανω στο toolbar;
    private Toolbar toolbar;
    View rootView;
    public ToolBarFragment() {

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate to layout file tou fragment
        rootView = inflater.inflate(R.layout.fragment_tool_bar, container, false);
        toolbar = rootView.findViewById(R.id.toolbar);
        //inflate το layout file του Menu
        toolbar.inflateMenu(R.menu.menu_);
        return rootView;
    }
    //επικάλυψη της onStart
    @Override
    public void onStart() {
        super.onStart();

        //Προσθήκη τίτλου στο Toolbar το οποίο θα είναι το username του χρήστη
        //το οποίο το παίρνω απο το userAuthentication της Firebase
        toolbar.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        //Προσθήκη Click listener στο Menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.signOut: //Περίπτωση που θα πατηθεί το Sign Out
                        //log out και σε περιπτωση που ο χρήστης είναι συνδεμένος μέσω e-mail/password
                        //και σε περιπτωση που είναι συνδεμένος μέσω Facebook Authentication
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        //εκκίνηση activity LoginActivity
                        Intent intent1 = (new Intent(getActivity(), LoginActivity.class));
                        startActivity(intent1);
                        break;
                    case R.id.menu_item_share: //Περίπτωση που θα πατηθεί SHARE
                        //ο χρήστης έχει την επιλογή να κάνει Share την εφαρμογή στο Facebook
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setQuote("This is CurrencyConverter App, Download it for free") //και προσθέτετε και αυτό το σχόλιο
                                .setContentUrl(Uri.parse("https://drive.google.com/file/d/12GNgisvGEcldLJqp1UTnwKsYst0fuhyh/view?usp=sharing"))
                                .build();
                        //δημιουργία μηνυματος χρήστη
                        ShareDialog shareDialog = new ShareDialog(getActivity());
                        //εμφάνιση επιλογής για SHARE
                        shareDialog.show(content);
                        break;
                    case R.id.favItem: //Περίπτωση που θα πατηθεί το Favourites
                        //Εκκίνηση της Activity FavouritesActivity
                        Intent intent2 =(new Intent(getActivity(), FavouritesActivity.class));
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });



    }

}
