package com.example.ndca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class DietFragment extends Fragment {

    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    private RecyclerView recyclerView;
    private ArrayList<DietModel> dietList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Declare an instance of View
        View v =  inflater.inflate(R.layout.fragment_diet, container, false);

        //FireBase Storage instance
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Firestore instance
        db = FirebaseFirestore.getInstance();
        //Getting RecyclerView
        recyclerView = v.findViewById(R.id.rv);

        final TextView g1 = v.findViewById(R.id.good1);
        final TextView g2 = v.findViewById(R.id.good2);
        final TextView g3 = v.findViewById(R.id.good3);
        final TextView g4 = v.findViewById(R.id.good4);
        final TextView g5 = v.findViewById(R.id.good5);

        final TextView b1 = v.findViewById(R.id.Bad1);
        final TextView b2 = v.findViewById(R.id.bad2);
        final TextView b3 = v.findViewById(R.id.bad3);
        final TextView b4 = v.findViewById(R.id.bad4);
        final TextView b5 = v.findViewById(R.id.bad5);


        //Get Activity so Context can be used
        FragmentActivity fragmentActivity = getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);



        dietList = new ArrayList<>();


        db.collection("Plans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                //Setting text
                                g1.setText(document.get("g1").toString());
                                g2.setText(document.get("g2").toString());
                                g3.setText(document.get("g3").toString());
                                g4.setText(document.get("g4").toString());
                                g5.setText(document.get("g5").toString());

                                b1.setText(document.get("b1").toString());
                                b2.setText(document.get("b2").toString());
                                b3.setText(document.get("b3").toString());
                                b4.setText(document.get("b4").toString());
                                b5.setText(document.get("b5").toString());



                                //TODO  retreive pics
                                DietModel fillerData = new DietModel(R.drawable.placeholder, document.get("Name").toString(), document.get("Des").toString());
                                dietList.add(fillerData);
                                DietAdapter dietAdapter = new DietAdapter(getActivity(), dietList);
                                recyclerView.setAdapter(dietAdapter);}
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        DietAdapter dietAdapter = new DietAdapter(getActivity(), dietList);
        recyclerView.setAdapter(dietAdapter);





        //recyclerView.setAdapter(dietAdapter);




        // Inflate the layout for this fragment
        return v;
    }






}
