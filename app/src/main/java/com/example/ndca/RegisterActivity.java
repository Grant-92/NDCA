package com.example.ndca;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    QueryDocumentSnapshot doc;
    private EditText name, email, pass, confPass;
    private Button reg_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner plans;
    private ArrayList<String> adapterArrayList;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_editText_reg);
        email = findViewById(R.id.email_editText_reg);
        pass = findViewById(R.id.pass_editText_reg);
        confPass = findViewById(R.id.conf_pass_editText_reg);
        plans = findViewById(R.id.diet_spinner_reg);

        reg_btn = findViewById(R.id.reg_btn_reg);
        reg_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        adapterArrayList = new ArrayList<>();


        //TODO may need to hardcode arraylist as adapter is working butt produces noi error
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                adapterArrayList.add(document.getId());
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                adapterArrayList
        );

        plans.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        final String sName = name.getText().toString().trim();
        final String sEmail = email.getText().toString().trim();
        String sPass = pass.getText().toString().trim();
        String sConfPass = confPass.getText().toString().trim();


        if (!sName.isEmpty() && isEmailValid(sEmail) && sPass.equals(sConfPass)) {
            //TODO validate user email and password 8chars 1upper 1lower 1number
            mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser firebaseUseruser = mAuth.getCurrentUser();


                                // Create a new user with a first and last name
                                Map<String, Object> user = new HashMap<>();
                                user.put("Name", sName);
                                user.put("Email", sEmail);


                                db.collection("Users").document(sEmail)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                            }
                                        });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });


        } else {
            Toast.makeText(RegisterActivity.this, "Please ensure all fields are filled in and correct",
                    Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onStart() {
        super.onStart();


        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
