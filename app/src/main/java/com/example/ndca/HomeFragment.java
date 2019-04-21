package com.example.ndca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lalongooo.Rings;


public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FrameLayout frag_frameLayout;
    private String extraEmail;
    private Rings rings;
    private int totalViewWidth = -1; //using this value to make sure Rings views are always square
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        frag_frameLayout = view.findViewById(R.id.frameLayout_home);
        rings = view.findViewById(R.id.rings);
        db = FirebaseFirestore.getInstance();
        extraEmail = getActivity().getIntent().getStringExtra("email"); //TODO Use to get user data because FBUser is broke

        getUsersPlanData(extraEmail);


        //TODO need to dynamically load diets based on user
        final DocumentReference docRef = db.collection("Plans").document("Keto");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        String sVal = document.get("value1").toString();
                        Toast.makeText(getContext(), "Got:" + sVal, Toast.LENGTH_LONG).show();
                        int val = Integer.parseInt(sVal);
                        rings.setRingInnerFirstProgress(val);

                    } else {
                        Toast.makeText(getContext(), "Error iintterenal", Toast.LENGTH_LONG).show();

                        Log.d("TAG", "No such document");
                    }
                } else {
                    Toast.makeText(getContext(), "Error EExternal", Toast.LENGTH_LONG).show();

                    Log.d("TA", "get failed with ", task.getException());
                }
            }
        });



        // Inflate the layout for this fragment
        return view;


    }

    private void getUsersPlanData(String email) {//TODO
        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getContext(), document.get("Name").toString(), Toast.LENGTH_LONG).show();
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }



}
