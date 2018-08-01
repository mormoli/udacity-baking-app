package com.example.udacity.udacity_baking_app.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.udacity_baking_app.R;
import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipesListViewHolder>{
    private ArrayList<TheRecipe> recipes;
    private OnItemClicked onItemClicked;
    private int selectedItem = RecyclerView.NO_POSITION;

    public RecipesListAdapter(ArrayList<TheRecipe> recipes){
        this.recipes = recipes;
    }
    @NonNull
    @Override
    public RecipesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new RecipesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipesListViewHolder holder, final int position) {
        /*if(recipes.get(position).getImageURL() != null){
            Picasso.get()
                    .load(recipes.get(position).getImageURL())
                    .into(holder.imageView);
        }*/
        holder.headerText.setText(recipes.get(position).getName());
        ArrayList<TheSteps> theSteps = recipes.get(position).getTheSteps();
        String shortDescription = theSteps.get(0).getShortDescription();
        holder.shortDescription.setText(shortDescription);
        String strServings = "Servings: " + recipes.get(position).getServings();
        holder.servings.setText(strServings);
        //highlighting list item background on select / on click.
        //@see "https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview/28838834#"
        if (selectedItem == RecyclerView.NO_POSITION) return;
        else { //color accent will be selected items background color
            //color primary is previously chosen items background color
            int selectedColor = holder.mView.getResources().getColor(R.color.colorAccent);
            int defaultColor = holder.mView.getResources().getColor(R.color.colorPrimary);
            holder.mView.setBackgroundColor(selectedItem == position ?
                    selectedColor : defaultColor);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.list_row_layout)
        View mView;
        @BindView(R.id.list_image_tv)
        ImageView imageView;
        @BindView(R.id.recipe_header_tv)
        TextView headerText;
        @BindView(R.id.recipe_sort_description_tv)
        TextView shortDescription;
        @BindView(R.id.servings_tv)
        TextView servings;
        public RecipesListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
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

    public interface OnItemClicked{
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onItemClicked = onClick;
    }
}
