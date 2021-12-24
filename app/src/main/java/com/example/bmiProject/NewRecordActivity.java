package com.example.bmiProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    Button maxLength, minLength, minWeight, maxWeight, saveDataBtn;
    EditText length, weight;

    int countWeight = 50, countLength = 120;

    Records records;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userId;
    TextView date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        saveDataBtn = findViewById(R.id.saveDataBtn);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        length = findViewById(R.id.length);
        minLength = findViewById(R.id.minLength);
        maxLength = findViewById(R.id.maxLength);
        weight = findViewById(R.id.weight);
        minWeight = findViewById(R.id.minWeight2);
        maxWeight = findViewById(R.id.maxWeight2);
        records = new Records();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getUid();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePick();
                datePicker.show(getSupportFragmentManager(),
                        "date picker");
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePick();
                timePicker.show(getSupportFragmentManager(),
                        "time picker");
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

        date.setText(currentDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time.setText(hourOfDay + ":" + minute);
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
            countWeight--;
            weight.setText(countWeight + "");
        }
    }

    public void maxWei(View view) {
        countWeight++;
        weight.setText(countWeight + "");
    }


    public void save(View view) {
        if (date.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please insert the Date!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please insert the Time!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        records.setUId(userId);
        records.setDateRecords(date.getText().toString());
        records.setLengthRecords(length.getText().toString());
        records.setWeightRecords(weight.getText().toString());
        records.setTimeRecords(time.getText().toString());
        records.setUId(userId);
        firebaseFirestore.collection("Records").add(records)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "done",
                                    Toast.LENGTH_SHORT).show();
                            date.setText("");
                            time.setText("");
                            length.setText("");
                            weight.setText("");
                            Intent intent =new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
