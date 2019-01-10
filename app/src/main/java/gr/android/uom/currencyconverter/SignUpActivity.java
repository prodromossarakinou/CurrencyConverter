package gr.android.uom.currencyconverter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


//Activity SignUp στην οποία ο χρήστης κάνει εγγραφη
public class SignUpActivity extends AppCompatActivity {
    private Button signUp;
    private EditText userNameText,emailText,passwordText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //αρχικοποίηση γραφικών συστατικών και του mAuth
        signUp = findViewById(R.id.signUp);
        userNameText = findViewById(R.id.userName);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();


    }
    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το Button signUp
    public void signUp(View v){
            registerUser();

    }

    //Μέθοδος registerUser
    //Κώδικας αντιγραμμένος απο το User-Authentication Documentation της Firebase
    //Ευδιάκριτος
    private void registerUser(){

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        if (email.isEmpty()) {
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please enter a valid email");
            emailText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Minimum lenght of password should be 6");
            passwordText.requestFocus();
            return;
        }
        //εαν όλα πάνε καλά δημιουργεί έναν χρήση
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(userNameText.getText()!=null){
                        String name = userNameText.getText().toString();

                        //προσθέτει αν όλα πάνε κάλα στον χρήστη το Username που αυτός εισάγει
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                        user.updateProfile(profileUpdates);
                        finish();
                        //και ξεκινάει την activity MainMenu
                        startActivity(new Intent(SignUpActivity.this, MainMenu.class));
                    }
                    else{
                        //σε περιπτωση που χρήστης δεν εισαγει username του εμφανιζει σχετικο Toast
                        Toast.makeText(SignUpActivity.this, "We need a name to continue!", Toast.LENGTH_SHORT).show();
                        TextView errorView = findViewById(R.id.errorView);
                        errorView.setVisibility(View.VISIBLE);
                    }

                } else {
                    //σε περίπτωση που κάτι δεν πάει καλά
                    //εαν το mail είναι ήδη εγγεγραμένο στην εφαρμογή
                    //Εμφανίζει σχετικό Toast
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        //σε κάθε άλλη περίπτωση εμφανίζει σχετικό Toast με το exception που βρέθηκε
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }
    //Μέθοδος τύπου OnClick συνδεμένη μέσω XML με το textView Already User
    public void goBack(View view){
        //πατώντας στο textView ο χρήστης επιστρέφει στην LoginActivity
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }
    //Επικάλυψη της μεθόδου onBackPressed
    @Override
    public void onBackPressed() {
        //ο Χρήστης όταν πατάει το πίσω επιστρεφει στην LoginActivity
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

}
