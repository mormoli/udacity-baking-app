package com.example.udacity.udacity_baking_app.utils;

import android.content.Context;
import android.support.annotation.NonNull;
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
    private int selectedItem = RecyclerView.NO_POSITION;

    public RecipeMasterAdapter(ArrayList<TheSteps> stepList){
        this.stepList = stepList;
        //this.onItemClicked = (OnItemClicked) context;
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
        //highlighting list item background on select / click.
        //@see "https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview/28838834#"
        if (selectedItem == RecyclerView.NO_POSITION) return;
        else {//color accent will be selected items background color
            //color primary is previously chosen items background color
            int selectedColor = holder.mView.getResources().getColor(R.color.colorAccent);
            int defaultColor = holder.mView.getResources().getColor(R.color.colorPrimary);
            holder.mView.setBackgroundColor(selectedItem == position ?
                    selectedColor : defaultColor);
        }
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
                notifyItemChanged(selectedItem);
                selectedItem = getAdapterPosition();
                notifyItemChanged(selectedItem);
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
