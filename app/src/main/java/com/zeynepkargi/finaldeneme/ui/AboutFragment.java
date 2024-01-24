package com.zeynepkargi.finaldeneme.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeynepkargi.finaldeneme.R;
import com.zeynepkargi.finaldeneme.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {
    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about,container,false);
        return root;

    }
}