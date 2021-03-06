package com.example.bmiProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompleteRegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText length, weight;
    RadioButton gender;
    Button maxLength, minLength, minWeight, maxWeight, saveBtn;
    RadioGroup group;
    Intent intent;
    TextView birthday;
    String uId;
    FirebaseFirestore firebaseFirestore;
    int countWight = 50, countLength = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compliter_regester);
        saveBtn = findViewById(R.id.saveBtn);
        birthday = findViewById(R.id.birthday);
        length = findViewById(R.id.length);
        maxLength = findViewById(R.id.maxLength);
        minLength = findViewById(R.id.minLength);
        weight = findViewById(R.id.weight);
        minWeight = findViewById(R.id.minWeight);
        maxWeight = findViewById(R.id.maxWeight);
        group = findViewById(R.id.radioGroup);
        firebaseFirestore = FirebaseFirestore.getInstance();
        intent = getIntent();
        uId = intent.getStringExtra("userId");

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePick = new DatePick();
                datePick.show(getSupportFragmentManager(),
                        "date picker");
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                gender = (RadioButton) radioGroup.findViewById(i);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(c.getTime());
        birthday.setText(currentDate);
    }

    public void maxLength(View view) {
        countLength++;
        length.setText(countLength + "");
    }

    public void minLength(View view) {
        if (!length.getText().toString().equals("120")) {
            countLength--;
            length.setText(countLength + "");
        }
    }

    public void minWei(View view) {
        if (weight.getText().toString().equals("50")) {
            Toast.makeText(getApplicationContext(),
                    "Please insert a value more than 50Kg!",
                    Toast.LENGTH_SHORT).show();
        } else {
            countWight--;
            weight.setText(countWight + "");
        }
    }

    public void maxWei(View view) {
        countWight++;
        weight.setText(countWight + "");
    }

    public void save(View view) {

        if (birthday.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please insert your birth date!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put(
                "gender",
                gender.getText().toString());
        stringObjectHashMap.put(
                "length",
                length.getText().toString());
        stringObjectHashMap.put(
                "weight",
                weight.getText().toString());
        stringObjectHashMap.put(
                "birthday",
                birthday.getText().toString());

        firebaseFirestore.collection("User").document(uId).update(stringObjectHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intent);
                    }
                });
    }
}