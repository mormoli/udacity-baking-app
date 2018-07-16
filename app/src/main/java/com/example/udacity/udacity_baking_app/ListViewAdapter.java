package com.example.udacity.udacity_baking_app;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.udacity_baking_app.ListViewFragment.OnListFragmentInteractionListener;
import com.example.udacity.udacity_baking_app.dummy.DummyContent.DummyItem;
import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TheRecipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private final ArrayList<TheRecipe> mValues;
    //private final OnListFragmentInteractionListener mListener;

    public ListViewAdapter(ArrayList<TheRecipe> items) {
        mValues = items;
       // mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);
        //image url if not null set image
        if(mValues == null || mValues.size() == 0) return;
        if(mValues.get(position).getImageURL() != null){
            Picasso.get()
                    .load(mValues.get(position).getImageURL())
                    .into(holder.imageView);
        }
        //recipe name
        holder.headerText.setText(mValues.get(position).getName());
        //short description
        ArrayList<TheSteps> theSteps = mValues.get(position).getTheSteps();
        String shortDescription = theSteps.get(0).getShortDescription();
        holder.shortDescription.setText(shortDescription);
        //servings value
        String strServings = "Servings: " + mValues.get(position).getServings();
        holder.servings.setText(strServings);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //Set intent to launch details fragment to show ingredient list
                //send broadcast to widget show ingredients list on widget. Intent(Widget.UPDATE_ACTION);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
