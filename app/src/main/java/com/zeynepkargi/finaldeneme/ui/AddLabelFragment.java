package com.zeynepkargi.finaldeneme.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;
import com.zeynepkargi.finaldeneme.R;
import com.zeynepkargi.finaldeneme.databinding.FragmentAddlabelBinding;
import com.zeynepkargi.finaldeneme.databinding.FragmentAddphotoBinding;

public class AddLabelFragment extends Fragment {
    private FragmentAddlabelBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference labelreference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddlabelBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        LinearLayout ll = binding.linearLayout;

        firebaseFirestore = FirebaseFirestore.getInstance();
        labelreference = firebaseFirestore.collection("label");

        labelreference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> gorev) {
                if(gorev.isSuccessful()){
                    for(QueryDocumentSnapshot document : gorev.getResult()){
                        Label etiket = document.toObject(Label.class);
                        CheckBox checkBox = new CheckBox(getActivity());
                        checkBox.setText(etiket.getLabelText());
                        ll.addView(checkBox);

                    }

                }else {
                    Log.d("TAG", "DOSYA ALINIRKEN HATA OLUŞTU", gorev.getException());
                }
            }
        });

        Button eklenen = binding.added;
        eklenen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText labeltxt = binding.label;
                String labelet = labeltxt.getText().toString();
                labelreference.whereEqualTo("labelEditText",labelet).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> gorev) {

                            labelreference.add(new Label(labelet))
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(),"Label başarıyla eklendi!",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(),"Label ekleme başarısız!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                });
            }
        });

        return view;
    }







}