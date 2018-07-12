package com.example.udacity.udacity_baking_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TheIngredients implements Parcelable {
    public static final Parcelable.Creator<TheIngredients> CREATOR = new Parcelable.Creator<TheIngredients>(){
        public TheIngredients createFromParcel(Parcel in){
            return new TheIngredients(in);
        }

        public TheIngredients[] newArray(int size){
            return new TheIngredients[size];
        }
    };
    private float quantity;
    private String measure;
    private String ingredient;
    //Default constructor
    public TheIngredients(){}
    //Getters and Setters for the variables
    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    private TheIngredients(Parcel in){
        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    @Override
    public String toString() {
        return "TheIngredients{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
