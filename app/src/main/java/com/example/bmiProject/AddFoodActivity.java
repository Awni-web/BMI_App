package com.example.bmiProject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nmg.bmi_app.R;

public class AddFoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Food foodLists;
    ImageView foodImg;
    Button uploadBtn, saveBtn;
    String uIds, category;
    ProgressDialog dial;
    EditText edtFoodName, edtCalorie;
    StorageReference references;
    Uri imgUri, uri;
    int selSpinner = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        edtCalorie = findViewById(R.id.edtCalorie);
        foodImg = findViewById(R.id.foodImg);
        saveBtn = findViewById(R.id.saveBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        edtFoodName = findViewById(R.id.edtFoodName);
        spinner = findViewById(R.id.spinner);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uIds = firebaseAuth.getUid();
        references = FirebaseStorage.getInstance().getReference();
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,
                        20);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        category = spinner.getSelectedItem().toString();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtFoodName.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Food name must be inserted",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCalorie.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Calorie quantity must be inserted",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please choose an image",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                foodLists = new Food();
                foodLists.setUId(uIds);

                foodLists.setFoodName(edtFoodName.getText().toString());
                foodLists.setFoodNameCategory(selSpinner + "");
                foodLists.setFoodCalorie(edtCalorie.getText().toString());
                foodLists.setFbUri(uri + "");
                foodLists.setUId(uIds);

                firebaseFirestore.collection("food").add(foodLists)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    edtCalorie.setText("");
                                    spinner.setSelection(0);
                                    edtFoodName.setText("");
                                    Glide.with(getApplicationContext()).load(R.drawable.ic_launcher_background).into(foodImg);
                                }
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
                imgUri = data.getData();
                Glide.with(getApplicationContext()).load(imgUri).into(foodImg);
                upload(imgUri);
            }
        }
    }

    private void upload(Uri image) {
        dial = ProgressDialog.show(this, "Wait",
                "Please wait for a moment...",
                true);
        final StorageReference reference = this.references.child(uIds);
        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        AddFoodActivity.this.uri = uri;
                        dial.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
        selSpinner = adapterView.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}