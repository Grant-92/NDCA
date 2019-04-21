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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    QueryDocumentSnapshot doc;
    private EditText name, email, pass, confPass;
    private Button reg_btn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner spinner;
    private ArrayList<String> adapterArrayList;
    private String diet = "Error";

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //TODO redo diet select or hardcode cause i cannot get it working F the spinner that wont give a selected item even when everything looks too be good
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //TODO come baack and get spinner working corrrectly
        //spnSetUp();
        instantiateObjects();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {

        final String sName = name.getText().toString().trim();
        final String sEmail = email.getText().toString().trim();
        String sPass = pass.getText().toString().trim();
        String sConfPass = confPass.getText().toString().trim();


        if (!sName.isEmpty() && isEmailValid(sEmail) && sPass.equals(sConfPass)) {

            //Toast.makeText(RegisterActivity.this, "Creating user.",Toast.LENGTH_SHORT).show();

            //TODO validate user email and password 8chars 1upper 1lower 1number
            mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();

                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser firebaseUseruser = mAuth.getCurrentUser();

                                Map<String, Object> user = new HashMap<>();
                                user.put("Name", sName);
                                user.put("Email", sEmail);
                                user.put("diet", diet);
                                db.collection("Users").document(sEmail)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity.this, "Added to database",
                                                        Toast.LENGTH_SHORT).show();
                                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                                finish();
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


        //Toast.makeText(this, spinner.getCount(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selectedItem = parent.getItemAtPosition(position).toString();
        diet = selectedItem;
        Toast.makeText(parent.getContext(), selectedItem + "was selected", Toast.LENGTH_SHORT).show();
    }


    //TODO spinner is never set ????

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Nothiing was selected", Toast.LENGTH_SHORT).show();


    }

    private void spnSetUp() {
        adapterArrayList = new ArrayList<>();
        spinner = findViewById(R.id.diet_spinner_reg);
        //TODO may need to hardcode arraylist as adapter is working butt produces npe error look at doing spinner  dynamically
        db.collection("Plans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                adapterArrayList.add(document.getId());
                                Toast.makeText(getApplicationContext(), document.getId(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void instantiateObjects() {
        name = findViewById(R.id.name_editText_reg);
        email = findViewById(R.id.email_editText_reg);
        pass = findViewById(R.id.pass_editText_reg);
        confPass = findViewById(R.id.conf_pass_editText_reg);

        reg_btn = findViewById(R.id.reg_btn_reg);
        reg_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        spnSetUp();
        ArrayAdapter<?> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                adapterArrayList
        );
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }
}