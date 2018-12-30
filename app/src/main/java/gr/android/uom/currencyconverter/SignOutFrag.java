package gr.android.uom.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class SignOutFrag extends Fragment {

    private Toolbar toolbar;

    public SignOutFrag() {

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sign_out, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_);
//        setHasOptionsMenu(true);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        toolbar.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);


                return false;
            }
        });



    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.menu_, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
