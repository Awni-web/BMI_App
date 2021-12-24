package com.example.bmiProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nmg.bmi_app.R;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FoodListAdapter FoodListAdapter;
    List<Food> foods;
    String[] strings;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        recyclerView = findViewById(R.id.rv_foods);
        strings = getResources().getStringArray(R.array.category_array);
        foods = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("food").whereEqualTo("uId",
                firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String foodName = document.getData().get("foodName").toString();
                                String foodCalorie = document.getData().get("foodCalorie").toString();
                                String fbUri = document.getData().get("fbUri").toString();
                                int foodCategoryId = Integer.parseInt(document.getData().get("categoryFoodsName").toString());
                                String category = strings[foodCategoryId];
                                String docId = document.getId();
                                foods.add(new Food(firebaseAuth.getUid(),
                                        foodCalorie,
                                        foodName,
                                        fbUri,
                                        category,
                                        docId,
                                        foodCategoryId));
                            }
                            FoodListAdapter = new FoodListAdapter(getApplicationContext(),
                                    foods);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(FoodListAdapter);
                            FoodListAdapter.OnItemClickListener(new FoodListAdapter.ClickListener() {
                                @Override
                                public void onClick(Food result) {
                                    Intent i = new Intent(getApplicationContext(), EditFoodActivity.class);
                                    i.putExtra("foodName",
                                            result.getFoodName());
                                    i.putExtra("foodCalorie",
                                            result.getFoodCalorie());
                                    i.putExtra("img",
                                            result.getFbUri());
                                    i.putExtra("docId",
                                            result.getFoodDocId());
                                    i.putExtra("category",
                                            result.getFoodNameCategory());
                                    i.putExtra("id",
                                            result.getFoodCategoryId());
                                    startActivity(i);
                                }
                            });
                        }
                    }
                });
    }
}