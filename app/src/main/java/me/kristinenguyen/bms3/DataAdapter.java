package me.kristinenguyen.bms3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    //private ArrayList<String> names;
    List<Items> items;
    Context context;
    public DataAdapter(List<Items> items, Context context){
        //super(context,R.layout.row_layout, items);
        this.items = items;
        this.context=context;}

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Items mylist = items.get(i); // this is the position
        viewHolder.tv_name.setText(mylist.getName());
        viewHolder.tv_cost.setText("$"+mylist.getCost());
        //viewHolder.tv_cost.SetText("$"+currentbudget.getBudget());
       // viewHolder.tv_cost.setText(costs.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
        //return costs.size();
    } //the adapter names


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_cost;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_cost = (TextView)view.findViewById(R.id.tv_cost);
        }
    }
}