package me.kristinenguyen.bms3;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by intropella on 6/10/17.
 */
@IgnoreExtraProperties
public class Items {
    private String name, cost, status,id;
    
    public Items(){}

    public Items (String id, String name,String cost){
        this.name = name;
        this.cost = cost;
        this.status = status;
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public String getCost(){return cost;}
    public String getStatus(){
        return status;
    }
    public String getId(){
        return id;
    }
}

