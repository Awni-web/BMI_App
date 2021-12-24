package com.example.bmiProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nmg.bmi_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    TextView logout, status, name, load;
    Button newRecord, viewFood, addFood;
    String userId, userStatus, userGender,userWeight, userLength, birthday;
    RecordListAdapter RecordListAdapter;
    List<Records> recordsList;
    RecyclerView recyclerView;
    Calendar calendar;
    double agePercent = 0, weight, length, bmiPercent, bmiChange, bmiLast, bmiSecondLast;
    int age, year;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        status = findViewById(R.id.status);
        newRecord = findViewById(R.id.newRecord);
        load = findViewById(R.id.load);
        addFood = findViewById(R.id.addFood);
        logout = findViewById(R.id.logout);
        viewFood = findViewById(R.id.viewFood);
        name = findViewById(R.id.name);
        recordsList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        recyclerView = findViewById(R.id.recyclerViews);
        calendar = Calendar.getInstance();

        showData();

        viewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        FoodListActivity.class);
                startActivity(intent);
            }
        });
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        AddFoodActivity.class);
                startActivity(intent);
            }
        });
        newRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        NewRecordActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        firebaseFirestore.collection("User").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().get("userName").toString();
                            userGender = task.getResult().get("userGender").toString();
                            userWeight = task.getResult().get("userWeight").toString();
                            userLength = task.getResult().get("userLength").toString();
                            birthday = task.getResult().get("birthday").toString();

                            HomeActivity.this.name.setText(name);
                        }
                    }
                });
    }


    private void showData() {
        firebaseFirestore.collection("Records").whereEqualTo("uId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dateRecords = document.getData().get("dateRecords").toString();
                                String lengthRecords = document.getData().get("lengthRecords").toString();
                                String weightRecords = document.getData().get("weightRecords").toString();

                                year = Integer.parseInt(dateRecords.substring(7));
                                age = calendar.get(Calendar.YEAR) - year;
                                weight = Double.parseDouble(weightRecords);
                                length = Double.parseDouble(lengthRecords) / 100 * Double.parseDouble(lengthRecords) / 100;

                                if (age >= 20) {
                                    agePercent = 1;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Male")) {
                                    agePercent = 0.90;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Female")) {
                                    agePercent = 0.80;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if (age >= 2 && age <= 10) {
                                    agePercent = 0.7;
                                    bmiPercent = (weight / length) * agePercent;
                                }

                                if (bmiPercent > 30) {
                                    userStatus = "Obesity";
                                } else if (bmiPercent <= 30 && bmiPercent > 25) {
                                    userStatus = "Overweight";
                                } else if (bmiPercent <= 25 && bmiPercent > 18.5) {
                                    userStatus = "Healthy Weight";
                                } else if (bmiPercent <= 18.5) {
                                    userStatus = "Underweight";
                                }
                                recordsList.add(new Records(dateRecords,
                                        "",
                                        lengthRecords,
                                        weightRecords,
                                        userId,
                                        userStatus,
                                        bmiPercent));
                            }
                            Collections.reverse(recordsList);
                            if (recordsList.size() < 1) {
                                load.setVisibility(View.VISIBLE);
                                year = Integer.parseInt(birthday.substring(7));
                                age = calendar.get(Calendar.YEAR) - year;
                                weight = Double.parseDouble(userWeight);
                                length = Double.parseDouble(userLength) / 100 * Double.parseDouble(userLength) / 100;
                                if (age >= 20) {
                                    agePercent = 1;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Male")) {
                                    agePercent = 0.90;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Female")) {
                                    agePercent = 0.80;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if (age >= 2 && age <= 10) {
                                    agePercent = 0.7;
                                    bmiPercent = (weight / length) * agePercent;
                                }

                                if (bmiPercent > 30) {
                                    userStatus = "Obesity";
                                } else if (bmiPercent <= 30 && bmiPercent > 25) {
                                    userStatus = "Overweight";
                                } else if (bmiPercent <= 25 && bmiPercent > 18.5) {
                                    userStatus = "Healthy Weight";
                                } else if (bmiPercent <= 18.5) {
                                    userStatus = "Underweight";
                                }

                                status.setText(userStatus);

                            } else if (recordsList.size() == 1) {
                                load.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);

                                bmiLast = recordsList.get(0).getBmiRecords();
                                year = Integer.parseInt(birthday.substring(7).toString());
                                age = calendar.get(Calendar.YEAR) - year;
                                weight = Double.parseDouble(userWeight);
                                length = Double.parseDouble(userLength) / 100 * Double.parseDouble(userLength) / 100;
                                if (age >= 20) {
                                    agePercent = 1;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Male")) {
                                    agePercent = 0.90;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if ((age >= 10 && age <= 19) && userGender.equals("Female")) {
                                    agePercent = 0.80;
                                    bmiPercent = (weight / length) * agePercent;
                                } else if (age >= 2 && age <= 10) {
                                    agePercent = 0.7;
                                    bmiPercent = (weight / length) * agePercent;
                                }

                                bmiChange = bmiLast - bmiPercent;
                                String userStatus = recordsList.get(0).getStatusRecords();

                                switch (userStatus) {
                                    case "Obesity":
                                        if ((bmiChange < -1) || (bmiChange >= -1 && bmiChange < -0.6)) {
                                            status.setText(userStatus + " Go Ahead");
                                        } else if ((bmiChange < -0.3 && bmiChange >= -0.6) || (bmiChange >= -0.3 && bmiChange > 0)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if (bmiChange >= 0 && bmiChange < 0.3) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= 0.3 && bmiChange < 0.6) || (bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " So Bad");
                                        }
                                        break;

                                    case "Overweight":
                                        if ((bmiChange < -1) || (bmiChange >= 0.3 && bmiChange < 0.6)) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= -1 && bmiChange < -0.6)) {
                                            status.setText(userStatus + " Go Ahead");
                                        } else if ((bmiChange < -0.3 && bmiChange >= -0.6)) {
                                            status.setText(userStatus + " still good");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if ((bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " So Bad");
                                        }
                                        break;

                                    case "Healthy Weight":
                                        if ((bmiChange < -1)) {
                                            status.setText(userStatus + " So Bad");
                                        } else if ((bmiChange >= -1 && bmiChange < -0.6) || (bmiChange < -0.3 && bmiChange >= -0.6) ||
                                                (bmiChange >= 0.3 && bmiChange < 0.6) || (bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        }
                                        break;

                                    case "Underweight":
                                        if ((bmiChange < -1) || (bmiChange >= -1 && bmiChange < -0.6) ||
                                                (bmiChange < -0.3 && bmiChange >= -0.6)) {
                                            status.setText(userStatus + " So Bad");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if ((bmiChange >= 0.3 && bmiChange < 0.6)) {
                                            status.setText(userStatus + " Still Good");
                                        } else if ((bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " Go Ahead");
                                        }
                                        break;
                                }

                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                bmiLast = recordsList.get(0).getBmiRecords();
                                bmiSecondLast = recordsList.get(1).getBmiRecords();


                                bmiChange = bmiLast - bmiSecondLast;
                                String userStatus = recordsList.get(0).getStatusRecords();

                                switch (userStatus) {
                                    case "Obesity":
                                        if ((bmiChange < -1) || (bmiChange >= -1 && bmiChange < -0.6)) {
                                            status.setText(userStatus + " Go Ahead");
                                        } else if ((bmiChange < -0.3 && bmiChange >= -0.6) || (bmiChange >= -0.3 && bmiChange > 0)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if (bmiChange >= 0 && bmiChange < 0.3) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= 0.3 && bmiChange < 0.6) || (bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " So Bad");
                                        }
                                        break;

                                    case "Overweight":
                                        if ((bmiChange < -1) || (bmiChange >= 0.3 && bmiChange < 0.6)) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= -1 && bmiChange < -0.6)) {
                                            status.setText(userStatus + " Go Ahead");
                                        } else if ((bmiChange < -0.3 && bmiChange >= -0.6)) {
                                            status.setText(userStatus + " still good");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if ((bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " So Bad");
                                        }
                                        break;

                                    case "Healthy Weight":
                                        if ((bmiChange < -1)) {
                                            status.setText(userStatus + " So Bad");
                                        } else if ((bmiChange >= -1 && bmiChange < -0.6) || (bmiChange < -0.3 && bmiChange >= -0.6) ||
                                                (bmiChange >= 0.3 && bmiChange < 0.6) || (bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " Be Careful");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        }
                                        break;

                                    case "Underweight":
                                        if ((bmiChange < -1) || (bmiChange >= -1 && bmiChange < -0.6) ||
                                                (bmiChange < -0.3 && bmiChange >= -0.6)) {
                                            status.setText(userStatus + " So Bad");
                                        } else if ((bmiChange >= -0.3 && bmiChange < 0) || (bmiChange >= 0 && bmiChange < 0.3)) {
                                            status.setText(userStatus + " Little Changes");
                                        } else if ((bmiChange >= 0.3 && bmiChange < 0.6)) {
                                            status.setText(userStatus + " Still Good");
                                        } else if ((bmiChange >= 0.6 && bmiChange < 1) || bmiChange >= 1) {
                                            status.setText(userStatus + " Go Ahead");
                                        }
                                        break;
                                }
                            }
                            RecordListAdapter = new RecordListAdapter(getApplicationContext(),recordsList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(RecordListAdapter);
                            Log.d("TAG", recordsList.size()+"");

                        } else {
                            Log.w("TAG",
                                    "Error getting documents.",
                                    task.getException());
                        }
                    }
                });
    }
}