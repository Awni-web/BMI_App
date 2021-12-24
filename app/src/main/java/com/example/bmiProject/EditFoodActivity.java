package com.example.bmiProject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nmg.bmi_app.R;

import java.util.HashMap;
import java.util.Map;

public class EditFoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    ProgressDialog dial;
    ImageView foodImg;
    Button uploadBtn, saveBtn;
    Uri uri;
    Intent intent;
    int id;
    String firebaseUr, foodName, foodCalorie, documentId,uId, category;
    EditText edtFoodName, edtCalorie;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_activty);
        edtCalorie = findViewById(R.id.edtCalorie);

        uploadBtn = findViewById(R.id.uploadBtn);
        foodImg = findViewById(R.id.foodImg2);
        edtFoodName = findViewById(R.id.edtFoodName);
        saveBtn = findViewById(R.id.saveEditBtn);
        uId = firebaseAuth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.spinner);
        intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        foodCalorie = intent.getStringExtra("foodCalorie");
        category = intent.getStringExtra("category");
        documentId = intent.getStringExtra("docId");
        firebaseUr = intent.getStringExtra("img");
        id = intent.getIntExtra("id",1);

        edtFoodName.setText(foodName);
        edtCalorie.setText(foodCalorie);
        Glide.with(getApplicationContext()).load(firebaseUr).into(foodImg);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(id);

        spinner.setOnItemSelectedListener(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 20);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtFoodName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Food name is Mandatory",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtCalorie.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Calories are Mandatory",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String foodName= edtFoodName.getText().toString();
                String foodCalorie= edtCalorie.getText().toString();

                Map<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("foodName", foodName);
                stringObjectHashMap.put("foodsNameCategories", id);
                stringObjectHashMap.put("foodCalories", foodCalorie);
                stringObjectHashMap.put("fbUri", firebaseUr);
                firebaseFirestore.collection("food").document(documentId)
                        .update(stringObjectHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                           startActivity(new Intent(getApplicationContext(),
                                   FoodListActivity.class));
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getData();
                upload(uri);
            }
        }
    }

    private void upload(Uri uri1) {
        dial = ProgressDialog.show(this, "Wait","Wait a second... ", true);
        final StorageReference reference = storageReference.child(uId);
        reference.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(foodImg);
                        firebaseUr = uri.toString();
                        dial.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
        id = adapterView.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}