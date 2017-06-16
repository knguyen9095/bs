package me.kristinenguyen.bms3;

/**
 * Created by intropella on 6/10/17.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
        private EditText inputEmail, inputPassword;
        private TextView already;
        private Button Button_SignUp, Button_SignIn, btnResetPassword;
        private FirebaseAuth Auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_register);

            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.password);
            already = (TextView) findViewById(R.id.user_already);
            //Get Firebase auth instance
            Auth = FirebaseAuth.getInstance();
        }
        private void Register(){

            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            //checking if email and passwords are empty
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }
            //creating a new user from the firebase thing
            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Registration Error",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, AskBudget_Activity.class));
                                finish();
                            }

                        }
                    });
        }



        public void onClickRegister(View view){
            // call the register method on click and prompt to the mainactivity
            Register();
        }

        //TextView already_sign = (TextView)addView.findViewById(R.id.user_already);
        public void onClickAlreadyUser(View view){
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
        }
}
