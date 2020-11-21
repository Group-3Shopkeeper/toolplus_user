package com.e.toolplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.R;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(!InternetConnection.isConnected(getContext())){
            CustomAlertDialog.internetWarning(getContext());
        }

        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}