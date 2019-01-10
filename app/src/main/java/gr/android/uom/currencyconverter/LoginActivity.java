package gr.android.uom.currencyconverter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String TAG = "reProsa";
    LoginButton loginButton;
    EditText editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //αρχικοποίηση γραφικών συστατικών για επεξεργασία στην συνέχεια
        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.login_button);
        //αρχικοποίηση mAuth
        //αν και σε πολλα σημεία χρησιμοποείται παλι ως FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        //Login Via Facebook
        //ο κώδικας είναι αντιγραμένος απο το Documentation της Firebase για Facebook User Authentication
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(LoginActivity.this,"Succesfully logged in",Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"fail",Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this,"ERROR ERROR ERROR",Toast.LENGTH_SHORT);
            }
        });
    }

    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Button Favourites
    public void goToSignUp(View v){
        //εκκίνηση activity signUp
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
    //Επικάληψη μεθόδου onBackPressed
    //Ελέγχουμε τί θα κάνει ο χρήστης όταν πατήσει το back Key

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        //Με την βοήθεια του Intent όταν ο χρήστης πατήσει το back key θα κλείσει η εφαρμογή όταν βρίσκεται σε αυτό το Activity
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    //μέθοδος User-Authentication via email&and password
    //αντιγραμμένος κώδικας απο το Documentation της Firebase
    //Ευδιάκριτος
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        //περίπτωση επιτυχής σύνδεσης
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //επικάλυψη της onStart
    @Override
    protected void onStart() {
        super.onStart();
        //σε περίπτωση που ο χρήστης είναι ήδη συνδεμένος τον πετάει κατευθέινα στην menuActivity
        if (mAuth.getCurrentUser() != null) {
            finish();
            //εκκίνηση MenuActivity
            startActivity(new Intent(this, MainMenu.class));
        }
    }
    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Connect και συνδέει τον User μέσω της UserLogin();
    public void Connecting(View v){
        userLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //μέθοδος η διαχειρίζεται την σύδενση μέσω Facebook
    //Κώδικα αντιγραμένος απο το Documentation της Firebase User Authentication via Facebook;
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        //o Χρήστης εμφανίζεται αυτόματα και στους χρήστες που είναι αποθηκευμένοι στην Firebase
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent = new Intent(LoginActivity.this,MainMenu.class);
                            startActivity(intent);

                        } else {

                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                    //Δεν μου χρειάστηκε να υλοποιήσω την updateUserInterface
                    private void updateUI(FirebaseUser user) {
                        
                    }
                });

    }


}
