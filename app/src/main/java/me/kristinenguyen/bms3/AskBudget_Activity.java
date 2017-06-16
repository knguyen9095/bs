package me.kristinenguyen.bms3;

/**
 * Created by intropella on 6/10/17.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.ArrayList;

public class AskBudget_Activity extends AppCompatActivity{
    List<CurrentBudget> budgets;
    private Button buttonContinue;
    private BudgetAdapter adapters;
    private EditText edit_budget;
    //database reference
    DatabaseReference databaseyo;
    FirebaseDatabase databaseapp;
    FirebaseUser userFire;
    private FirebaseAuth firebaseAuth;
    private View view;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_budget);
        Intent intent = getIntent();
        //getting the reference of items
        firebaseAuth = FirebaseAuth.getInstance();
        userFire = firebaseAuth.getCurrentUser();
        databaseapp = FirebaseDatabase.getInstance();
        userid = userFire.getUid();
        databaseyo = databaseapp.getReference("user").child(userid);
        addBudget();
        //
    }
    //save
    public void OnsaveBudget(View v){
        addBudget();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //add the item

    private void addBudget(){
        EditText edit_budget = (EditText) findViewById(R.id.editBudgetV);
        String thebudget = edit_budget.getText().toString().trim();

        //int finalbudget=Integer.parseInt(budget);

        if (!TextUtils.isEmpty(thebudget)){
            // get the userid
            userid = userFire.getUid();
            //get a unique id
            String id = databaseyo.push().getKey();
            //save it to the budgetAdapter ( this is when we want to mess with + - )
            CurrentBudget budgets = new CurrentBudget(thebudget);
            //save it
            //databaseyo.child("user").child(userid).child("budget").push().setValue(budgets);
            databaseyo.child("budget").setValue(budgets);
            databaseyo.child("start budget").setValue(budgets);
            //displaying a success toast
            Toast.makeText(this, "Budget added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter the budget", Toast.LENGTH_LONG).show();
        }

    }


}
