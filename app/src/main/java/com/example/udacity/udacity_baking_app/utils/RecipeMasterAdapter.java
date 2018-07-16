package com.example.udacity.udacity_baking_app.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udacity.udacity_baking_app.R;
import com.example.udacity.udacity_baking_app.model.TheSteps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeMasterAdapter extends RecyclerView.Adapter<RecipeMasterAdapter.RecipeViewHolder>{

    private ArrayList<TheSteps> stepList;
    private OnItemClicked onItemClicked;

    public RecipeMasterAdapter(ArrayList<TheSteps> stepList){
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_row_item, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        String step = stepList.get(position).getId() + "\t" + stepList.get(position).getShortDescription();
        holder.textView.setText(step);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.steps_row_layout)
        View mView;
        @BindView(R.id.steps_text_tv)
        TextView textView;

        public RecipeViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClicked != null){
                onItemClicked.onItemClick(v, getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public interface OnItemClicked{
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onItemClicked = onClick;
    }
}
