package com.e.toolplus.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.databinding.CustomAlertDialogBinding;

public class CustomAlertDialog {

    public static void internetWarning(final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        CustomAlertDialogBinding binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(context));
        builder.setView(binding.getRoot());

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.negative.setVisibility(View.INVISIBLE);

        binding.tvConfirmation.setText("Internet Connection Alert");

        binding.tvMessage.setText("Please Connect to Internet");
        binding.tvPositive.setText("Connect");

        binding.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            }
        });

        alertDialog.show();

    }
}
