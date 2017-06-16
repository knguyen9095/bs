package me.kristinenguyen.bms3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import android.widget.TextView;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;


import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //objects
    List<Items> items;
    List<CurrentBudget> budgets;
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private AlertDialog.Builder alertDialog;
    private EditText edit_name, edit_cost;
    private Button buttonLogout;
    private CheckBox optional, need;
    private TextView tv_budget;
    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "MainActivity";
    String userid;


    //database reference
    DatabaseReference databaseItems;
    FirebaseDatabase database;
    FirebaseUser userFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting the reference of items
        firebaseAuth = FirebaseAuth.getInstance();
        userFire = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userid = userFire.getUid();
        databaseItems = database.getReference("user").child(userid);
        final TextView tv_budget = (TextView) findViewById(R.id.budgetCurrent);
        initViews();
        initBudget(view);
        initDialog();
    }
 //

    // initViews display the function onto the app
    private void initViews(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        databaseItems.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items = new ArrayList<Items>();
                // this method is called once with the initial value and again
                // whenver data at this location is updated.
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Items value = dataSnapshot1.getValue(Items.class);
                    items.add(value); //before it was items
                }
                //create the adapter
                DataAdapter adapter = new DataAdapter(items, MainActivity.this);
                //
                // /attatch the adapter to the card
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        initSwipe();

    }

    //private view for the textedit
    private void initBudget(View v){
        final TextView tv_budget = (TextView) findViewById(R.id.budgetCurrent);
        databaseItems.child("budget").child("budget").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                    String newbudget = dataSnapshot.getValue(String.class);
                    tv_budget.setText("$" + newbudget);
                    Log.i(TAG, "Value is: " + newbudget);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //initSwipe is a funtion when you wripe left or write
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //onswipe is the adapter
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final String nid;
                int position = viewHolder.getAdapterPosition();
                Items item = items.get(position);
                nid = item.getId();

                if (direction == ItemTouchHelper.LEFT){
                    // delete the item & it will update to the database
                    deleteItem(nid);
                } else {
                    // edit and update the name instead
                    //removeView();
                    //edit_position = position;
                    alertDialog.setTitle("Edit Item");
                    String name = edit_name.getText().toString().trim();
                    String cost = edit_cost.getText().toString().trim();
                    //updateItem(nid,name,cost);
                    //edit_name.setText(items.get(position));
                    //edit_cost.setText(items.get(position));
                    alertDialog.show();
                }
            }


            //This part is when you swipe left or right
            // Swipe Left = edit
            // Swipe Right = delete
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    // This is when the pop up card shows up when you press the ADD
    //

    private void initDialog(){
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(add){
                    add =false;
                    addItem();
                    dialog.dismiss();
                } else { // edit the dialog needs work. // i'm working on it.
                    // final RecyclerView.ViewHolder viewHolder = ();
                    // final String nid;
                    //int position = viewHolder.getAdapterPosition();
                    //Items item = items.get(position);
                    //nid = item.getId();
                    String name = edit_name.getText().toString().trim();
                    String cost = edit_cost.getText().toString().trim();

                    //if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cost)){
                    //   updateItem(nid,name,cost);
                    //}
                    //edit_name.setText(Items.name);
                    //adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
        edit_name = (EditText)view.findViewById(R.id.edit_name);
        edit_cost = (EditText)view.findViewById(R.id.edit_cost);
    }

    //add the item + when the item is add it decrement from the currentbudget value
    private void addItem(){
        String name = edit_name.getText().toString().trim();
        final String cost = edit_cost.getText().toString().trim();
        optional = (CheckBox) findViewById(R.id.nec);
        need = (CheckBox) findViewById(R.id.opt);
        //decrement

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cost)){
            // get the userid
            userid = userFire.getUid();
            //get a unique id
            String id = databaseItems.push().getKey();
            //is it optional or necessary?
            //creating Items object (name & cost.. didn't do status yet)
            Items items = new Items(id,name,cost);
            //save it
            databaseItems.child("items").child(id).setValue(items);
            //displaying a success toast
            Toast.makeText(this, "Item added", Toast.LENGTH_LONG).show();


            //add the thing now. what
            databaseItems.child("budget").child("budget").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //get the value of currentbudget from firebase
                    String getthebudget = dataSnapshot.getValue(String.class);
                    //convert it into a double
                    double oldbudget = Double.parseDouble(getthebudget);
                    Log.i(TAG, "Value is: " + oldbudget);
                    //convert the cost into a double
                    double addcost = Double.parseDouble(cost);
                    Log.i(TAG, "Value is: " + cost);
                    // decrement it
                    double newCurrentBudget = oldbudget - addcost;
                    //convert the double back into a string
                    String newCB = String.valueOf(newCurrentBudget);
                    //pass newCB to the BudgetAdapter
                    CurrentBudget budgets = new CurrentBudget(newCB);
                    // save it to the current budget
                    databaseItems.child("budget").setValue(budgets);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }

            });


        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name or cost", Toast.LENGTH_LONG).show();
        }

    }

    //delete the item
    private void deleteItem(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("user").child(userid).child("items").child(id);

        // add the cost value back from the current budget
        dR.child("cost").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {
                databaseItems.child("budget").child("budget").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //get the value of currentbudget from firebase
                        String getbudget = dataSnapshot.getValue(String.class);
                        double thebudget = Double.parseDouble(getbudget);
                        String getcost = dataSnapshot1.getValue(String.class);
                        double thecost = Double.parseDouble(getcost);
                        Log.i(TAG, "thebudget Value is: " + thebudget);
                        Log.i(TAG, "thecost Value is: " + thecost);

                        // decrement it
                        double newCurrentBudget = thebudget + thecost;
                        //convert the double back into a string
                        String newCB = String.valueOf(newCurrentBudget);
                        //pass newCB to the BudgetAdapter
                        CurrentBudget budgets = new CurrentBudget(newCB);
                        // save it to the current budget
                        databaseItems.child("budget").setValue(budgets);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }

                });
            }
            @Override
                public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }});

        //remove the item id
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
        return;
    }


    //update the item
    private void updateItem(String id, String name, String cost){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("user").child(userid).child("items");
        //update name & cost
        Items items = new Items(id,name,cost);
        dR.setValue(items);
        Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_LONG).show();
        return;
    }

    //is it necessary or optional?
    // save it to the necessary or optional
    public void onClickNorO(View v){
        String m ="";
        if(need.isChecked()){
            m = "necessary";
        }
        if(optional.isChecked()){
            m = "optional";
        }
    }

    //add product and pass

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Product");
                edit_name.setText("");
                edit_cost.setText("");
                alertDialog.show();
                break;
        }
    }


    // FirebaseUser user = firebaseAuth.getCurrentUser();
    //Logout Button
    //buttonLogout = (Button) findViewById(R.id.LogoutButton);
    public void onLogOut(View v){
        firebaseAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
