package me.kristinenguyen.bms3;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by intropella on 6/10/17.
 */

@IgnoreExtraProperties
public class CurrentBudget {

    private String currentBudget;

    public CurrentBudget(){};

    public CurrentBudget(String currentBudget){

        this.currentBudget = currentBudget;
    }
    public String getBudget(){ return currentBudget;}
}

