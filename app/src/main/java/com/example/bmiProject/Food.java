package com.example.bmiProject;

public class Food {
    int foodCategoryId;
    String foodDocId;
    String uId;
    String foodNameCategory;
    String foodCalorie;
    String foodName;
    String fbUri;


    public Food() {
    }

    public Food(String uId, String foodCalories, String foodName, String fbUri, String foodNameCategory, String foodDocId, int foodCategoryId) {
        this.uId = uId;
        this.fbUri = fbUri;
        this.foodName = foodName;
        this.foodNameCategory = foodNameCategory;
        this.foodCalorie = foodCalories;
        this.foodCategoryId = foodCategoryId;
        this.foodDocId = foodDocId;
    }

    public int getFoodCategoryId() {
        return foodCategoryId;
    }



    public String getFoodDocId() {
        return foodDocId;
    }



    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUId() {
        return uId;
    }

    public String getFoodNameCategory() {
        return foodNameCategory;
    }

    public void setFoodNameCategory(String foodNameCategory) {
        this.foodNameCategory = foodNameCategory;
    }

    public String getFoodCalorie() {
        return foodCalorie;
    }

    public void setFoodCalorie(String foodCalorie) {
        this.foodCalorie = foodCalorie;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFbUri() {
        return fbUri;
    }

    public void setFbUri(String fbUri) {
        this.fbUri = fbUri;
    }
}

