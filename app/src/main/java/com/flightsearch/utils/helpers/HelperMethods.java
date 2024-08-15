package com.flightsearch.utils.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.flightsearch.R;
import com.flightsearch.utils.base.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @SuppressLint("SimpleDateFormat")
    public static String BackendStringToDateString(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date getDate = null;
        try {
            getDate = existingUTCFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requiredFormat.format(getDate);
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateStringToTime(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("HH:mm");
        Date getDate = null;
        try {
            getDate = existingUTCFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requiredFormat.format(getDate);
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateStringBEToDateWithName(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("EEE dd MMM");
        Date getDate = null;
        try {
            getDate = existingUTCFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requiredFormat.format(getDate);
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateStringToDateWithName(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("EEE dd MMM");
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

    public static TextWatcher setTextWatcher(TextInputEditText editText) {
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "mmddyyyy";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Calendar cal = Calendar.getInstance();

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        // This part makes sure that when we finish entering numbers
                        // the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? (cal.get(Calendar.MONTH) + 1) : mon;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : Math.min(year, cal.get(Calendar.YEAR));
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = day < 1 ? 1 : (day > cal.getActualMaximum(Calendar.DATE)) ? cal.get(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        return tw;
    }


    public static void setDateErrors(TextInputEditText inputEditText, TextInputLayout inputLayout, BaseActivity activity) {
        if (inputLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(inputEditText.getText())) inputLayout.setError(activity.getString(R.string.error_field_required));
            else if (!HelperMethods.isValidBirthday(inputEditText.getText().toString()))
                inputLayout.setError(activity.getString(R.string.error_invalid_date));
        }
    }

    public static void setOnFocusChangeListenerDate(TextInputEditText inputEditText, TextInputLayout inputLayout, TextWatcher tw, BaseActivity activity) {
        inputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputLayout.setError(null);
                inputEditText.addTextChangedListener(tw);
                if (inputEditText.getText().toString().length() == 0) {
                    inputEditText.setText("mm/dd/yyyy");
                    inputEditText.post(() -> inputEditText.setSelection(0));
                }
            } else {
                inputEditText.removeTextChangedListener(tw);
                if (inputEditText.getText().toString().equals("mm/dd/yyyy"))
                    inputEditText.setText("");
                setDateErrors(inputEditText, inputLayout, activity);
            }
        });
    }

    public static void setAirportFieldErrors(TextInputEditText inputEditText, TextInputLayout inputLayout, BaseActivity activity) {
        if (inputLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(inputEditText.getText())) inputLayout.setError(activity.getString(R.string.error_field_required));
        }
    }

    public static void setOnFocusChangeListenerAirportField(TextInputEditText inputEditText, TextInputLayout inputLayout, BaseActivity activity) {
        inputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputLayout.setError(null);
            } else {
                HelperMethods.setAirportFieldErrors(inputEditText, inputLayout, activity);
            }
        });
    }

}
