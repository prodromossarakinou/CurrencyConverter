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


public class ToolBarFragment extends Fragment {

    private Toolbar toolbar;
    View rootView;
    public ToolBarFragment() {

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_tool_bar, container, false);

        MenuItem mItem = rootView.findViewById(R.id.favItem);

        toolbar = rootView.findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_);

//        setHasOptionsMenu(true);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();


        toolbar.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Log.d("PROSAPRSOA", "onStart: "+getActivity().toString());

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.signOut:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        Intent intent1 = (new Intent(getActivity(), LoginActivity.class));
                        startActivity(intent1);
                        break;
                    case R.id.menu_item_share:
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setQuote("This is CurrencyConverter App, Download it for free")
                                .setContentUrl(Uri.parse("https://drive.google.com/file/d/12GNgisvGEcldLJqp1UTnwKsYst0fuhyh/view?usp=sharing"))

                                .build();
                        ShareDialog shareDialog = new ShareDialog(getActivity());
                        shareDialog.show(content);
                        break;
                    case R.id.favItem:
                        Intent intent2 =(new Intent(getActivity(), Saves.class));
                        startActivity(intent2);
                        break;


                }




                return false;
            }
        });



    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();

        inflater.inflate(R.menu.menu_, menu);
//        String activityName = gr.android.uom.currencyconverter.MainMenu.class.toString();
//
//        String thisActivityName = getActivity().toString().substring(0,getActivity().toString().indexOf("@"));
//        Log.d("HELLO", "onStart: "+ activityName+ "  next class " + thisActivityName);
//        if(activityName.equals("class "+thisActivityName)){
//
//            MenuItem item = menu.getItem(R.id.favItem);
//            item.setVisible(false);
//        }
//        MenuItem item = menu.getItem(R.id.favItem);
//        item.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
