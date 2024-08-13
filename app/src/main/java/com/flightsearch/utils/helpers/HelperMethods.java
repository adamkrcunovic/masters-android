package com.flightsearch.utils.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperMethods {

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    static int BIRTHDAY_LENGTH = 10;
    public static boolean isValidBirthday(String target) {
        return target.replaceAll("[mdy]", "").length() == BIRTHDAY_LENGTH;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateStringToStringBackend(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date getDate = null;
        try {
            getDate = existingUTCFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requiredFormat.format(getDate);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (view instanceof EditText) {
            ((EditText) view).clearFocus();
        }
        if (view instanceof TextInputLayout) {
            ((TextInputLayout) view).clearFocus();
        }
    }

}
