package com.example.ndca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
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

import static com.firebase.ui.auth.ui.email.CheckEmailFragment.TAG;


public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FrameLayout frag_frameLayout;
    private String extraEmail;
    private Rings rings;
    private TextView stats;
    private int val_1 = -2;
    private int val_2 = -2;
    private int val_3 = -2;

    private int totVal_1 = -1;
    private int totVal_2 = -1;
    private int totVal_3 = -1;


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
        stats = view.findViewById(R.id.diet_header_diet);
        extraEmail = getActivity().getIntent().getStringExtra("email"); //TODO Used to get user data because FBUser is broke

        getUsersPlanData(extraEmail);




        // Inflate the layout for this fragment
        return view;


    }

    private void getUsersPlanData(String email) {//TODO

        String userDiet = "";
        DocumentReference users = db.collection("Users").document(email);
        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userDiet = "";

                        //Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        stats.setText(document.get("Name").toString() + "'s " + "Statistics ");
                        userDiet = document.get("diet").toString();
                        val_1 = Integer.parseInt(document.get("firstval").toString());
                        val_2 = Integer.parseInt(document.get("secondval").toString());
                        val_3 = Integer.parseInt(document.get("thirdval").toString());

                        //TODO need to dynamically load diets based on user
                        final DocumentReference docRef = db.collection("Plans").document(userDiet);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                        //setting rings values


                                        totVal_1 = Integer.parseInt(document.get("value1").toString());
                                        double result = (float) val_1 / (float) totVal_1;
                                        rings.setRingInnerFirstProgress((float) result * 100);


                                        totVal_2 = Integer.parseInt(document.get("value2").toString());
                                        result = (float) val_2 / (float) totVal_2;
                                        rings.setRingInnerSecondProgress((float) result * 100);


                                        totVal_3 = Integer.parseInt(document.get("value3").toString());
                                        result = (float) val_3 / (float) totVal_3;
                                        rings.setRingInnerThirdProgress((float) result * 100);

                                        result = (float) val_1 + (float) val_2 + (float) val_3;
                                        rings.setRingOverallProgress((float) result / 3);


                                    } else {
                                        Toast.makeText(getContext(), "No such document", Toast.LENGTH_LONG).show();

                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Error get failed", Toast.LENGTH_LONG).show();

                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void setVal_1(int val_1) {
        this.val_1 = val_1;
    }

    public void setVal_2(int val_2) {
        this.val_2 = val_2;
    }

    public void setVal_3(int val_3) {
        this.val_3 = val_3;
    }

}
