package com.example.bmiProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nmg.bmi_app.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView login;
    Button createBtn;
    EditText userName, password, rePassword, mail;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigester);
        login = findViewById(R.id.login);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.userName);
        rePassword = findViewById(R.id.rePassword);
        mail = findViewById(R.id.mail);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        createBtn = findViewById(R.id.createBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Username is Mandatory",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mail.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Email is Mandatory",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().equals("")) {
                    password.setError("Password is Mandatory");
                    return;
                }

                if (!password.getText().toString().equals(rePassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Make sure both passwords are matching!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = mail.getText().toString();
                String pass = password.getText().toString();
                 firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DocumentReference reference = firebaseFirestore.collection("User")
                                                .document(firebaseAuth.getUid());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("userName",
                                                userName.getText().toString());
                                        user.put("Email",
                                                mail.getText().toString());
                                        user.put("password",
                                                password.getText().toString());
                                        reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(getApplicationContext(),
                                                        CompleteRegisterActivity.class);
                                                intent.putExtra("userId",
                                                        firebaseAuth.getUid());
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });
    }
}