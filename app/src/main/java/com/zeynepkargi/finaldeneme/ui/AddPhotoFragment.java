package com.zeynepkargi.finaldeneme.ui;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.graphics.ImageDecoder;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeynepkargi.finaldeneme.DrawerActivity;
import com.zeynepkargi.finaldeneme.MainActivity;
import com.zeynepkargi.finaldeneme.R;
import com.zeynepkargi.finaldeneme.databinding.ActivityMainBinding;
import com.zeynepkargi.finaldeneme.databinding.FragmentAboutBinding;
import com.zeynepkargi.finaldeneme.databinding.FragmentAddlabelBinding;
import com.zeynepkargi.finaldeneme.databinding.FragmentAddphotoBinding;

public class AddPhotoFragment extends Fragment {
    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;/*intent  olarak galeriye gitmemizi sağlıyor */
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FragmentAddphotoBinding binding;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private int  Foto = 1;
    List<String> firebaseLabelList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddphotoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Button btnSec = binding.btnFotosec;
        CollectionReference labelref = firebaseFirestore.collection("label");
        labelref.addSnapshotListener((snapshots,e) ->{
            if (e != null){
                return;
            }
            firebaseLabelList.clear();
            RadioGroup radioGroup = binding.radioGroup;
            for(QueryDocumentSnapshot document : snapshots){
                Label label = document.toObject(Label.class);
                CheckBox checkBox = new CheckBox(getActivity());
                checkBox.setText(label.getLabelText());
                radioGroup.addView(checkBox);
                firebaseLabelList.add(label.getLabelText());
            }
        });

        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,Foto);
            }
        });
        return view;
    }
    private List<String> getSelectedLabels(){
        RadioGroup radioGroup = binding.radioGroup;
        List<String> secililabel = new ArrayList<>();
        for(int i= 0; i< radioGroup.getChildCount(); i ++){
            View view = radioGroup.getChildAt(i);

            if(view instanceof CheckBox){
                CheckBox checkBox = (CheckBox) view;
                if(checkBox.isChecked()){
                    secililabel.add(checkBox.getText().toString());
                }
            }
        }
        return secililabel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent veri){
        super.onActivityResult(requestCode, resultCode, veri);
        if(requestCode == Foto && resultCode == RESULT_OK && veri != null){
            Uri seciliFoto = veri.getData();
            String [] dosyayolu = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(seciliFoto,
                    dosyayolu, null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(dosyayolu[0]);
            String fotoyolu = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = binding.imgview;
            imageView.setImageBitmap(BitmapFactory.decodeFile(fotoyolu));
            Button paylas = binding.btnYukle;
            paylas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference photoRef = storageReference.child("gönderiler/" + UUID.randomUUID().toString());

                    UploadTask uploadTask = photoRef.putFile(seciliFoto);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> taskuri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!taskuri.isSuccessful());
                            Uri download_Url = taskuri.getResult();
                            DocumentReference userReference = firebaseFirestore.collection("users").document(firebaseAuth.getUid());
                            userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String name = documentSnapshot.getString("name");

                                    Map<String,Object> gonderi = new HashMap<>();
                                    gonderi.put("imageUrl",download_Url.toString());
                                    gonderi.put("name", name);

                                    List<String> secililabel = getSelectedLabels();
                                    gonderi.put("label", secililabel);

                                    firebaseFirestore.collection("posts").document().set(gonderi)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG,"BAŞARILI BİR ŞEKİLDE YÜKLENDİ");
                                                    Toast.makeText(getActivity(),"Başarılı yükleme işlemi",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public  void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}


















