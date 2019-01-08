package gr.android.uom.currencyconverter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Users {
    private String userName;
    private String userID;
    private String email;
    private DatabaseReference dbRef;
    private FirebaseDatabase mRootRef;


   public Users(String aName, String anUserId, String anEmail){

       userName = aName;
       userID = anUserId;
       email = anEmail;


   }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
