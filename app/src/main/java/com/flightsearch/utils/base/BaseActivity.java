package com.flightsearch.utils.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flightsearch.ui.custom.MyProgressDialog;

public class BaseActivity extends AppCompatActivity {

    private MyProgressDialog dialog;

    public void showDialog() {
        if (dialog == null) dialog = new MyProgressDialog(this);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
