package com.example.udacity.udacity_baking_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
/**
 "id": 1,
 "name": "Nutella Pie",
 "ingredients": [
 {
 "quantity": 2,
 "measure": "CUP",
 "ingredient": "Graham Cracker crumbs"
 },"steps": [
 {
 "id": 0,
 "shortDescription": "Recipe Introduction",
 "description": "Recipe Introduction",
 "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
 "thumbnailURL": ""
 },
 "servings": 8,
 "image": ""
 */

public class TheRecipe implements Parcelable{
    public static final Parcelable.Creator<TheRecipe> CREATOR = new Parcelable.Creator<TheRecipe>(){
        public TheRecipe createFromParcel(Parcel in){
            return new TheRecipe(in);
        }

        public TheRecipe[] newArray(int size){
            return new TheRecipe[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<TheIngredients> theIngredients;
    @SerializedName("steps")
    private ArrayList<TheSteps> theSteps;
    @SerializedName("servings")
    private String servings;
    @SerializedName("image")
    private String imageURL;

    public TheRecipe(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TheIngredients> getTheIngredients() {
        return theIngredients;
    }

    public void setTheIngredients(ArrayList<TheIngredients> theIngredients) {
        this.theIngredients = theIngredients;
    }

    public ArrayList<TheSteps> getTheSteps() {
        return theSteps;
    }

    public void setTheSteps(ArrayList<TheSteps> theSteps) {
        this.theSteps = theSteps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private TheRecipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        theIngredients = new ArrayList<>();
        in.readTypedList(theIngredients, TheIngredients.CREATOR);
        theSteps = new ArrayList<>();
        in.readTypedList(theSteps, TheSteps.CREATOR);
        this.servings = in.readString();
        this.imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.theIngredients);
        dest.writeTypedList(this.theSteps);
        dest.writeString(this.servings);
        dest.writeString(this.imageURL);
    }

    @Override
    public String toString() {
        return "TheRecipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theIngredients=" + theIngredients +
                ", theSteps=" + theSteps +
                ", servings='" + servings + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
