package me.kristinenguyen.bms3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by intropella on 6/10/17.
 */

public class Login extends AppCompatActivity {
    //defining the values
    private Button buttonSignIn;
    private EditText editEmail, editPassword;
    private TextView SignUp;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase auth object
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        Auth = FirebaseAuth.getInstance();


        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(Auth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //initializing views
        editEmail = (EditText) findViewById(R.id.login_emailid);
        editPassword = (EditText) findViewById(R.id.login_password);
        //buttonSignIn = (Button) findViewById(R.id.loginBtn);
        SignUp  = (TextView) findViewById(R.id.new_signup);
    }

    //Login
    private void LogIn(){
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //check if the email and or passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //login for user from Firebase
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            // Log.d(TAG, "signInWithEmail:success");
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void onClickSignIn(View view){
        LogIn();
    }

    public void OnClickSignUp(View view){

        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}

