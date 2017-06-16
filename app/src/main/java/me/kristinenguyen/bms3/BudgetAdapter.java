package me.kristinenguyen.bms3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by intropella on 6/10/17.
 */

public class BudgetAdapter{

    List<CurrentBudget> budgets;
    Context context;

    public BudgetAdapter(List<CurrentBudget> budgets, Context context){
        //super(context,R.layout.row_layout, items);
        this.budgets = budgets;
        this.context=context;
    }
/*
    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new BudgetAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BudgetAdapter.ViewHolder viewHolder, int i) {
        TextView tv_budget;
        CurrentBudget thebudget = budgets.get(i); // this is the position
        viewHolder.tv_budget.SetText("$"+thebudget.getBudget());
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_budget;
        public ViewHolder(View view) {
            super(view);
            tv_budget = (TextView)view.findViewById(R.id.editBudgetV);
        }
    }

}
