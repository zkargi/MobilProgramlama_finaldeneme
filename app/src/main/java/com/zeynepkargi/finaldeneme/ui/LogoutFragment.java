package com.zeynepkargi.finaldeneme.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.zeynepkargi.finaldeneme.LoginActivity;
import com.zeynepkargi.finaldeneme.MainActivity;
import com.zeynepkargi.finaldeneme.R;
import com.zeynepkargi.finaldeneme.SignupActivity;
import com.zeynepkargi.finaldeneme.databinding.FragmentLogoutBinding;


public class LogoutFragment extends Fragment {
    private FragmentLogoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout,container,false);





        return root;
    }
}