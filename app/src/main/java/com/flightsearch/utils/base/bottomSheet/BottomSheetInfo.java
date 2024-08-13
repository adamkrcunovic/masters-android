package com.flightsearch.utils.base.bottomSheet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flightsearch.databinding.BottomSheetInfoBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetInfo extends BottomSheetDialogFragment {

    private String title;
    private String info;

    private BottomSheetInfoBinding binding;

    public BottomSheetInfo(String title, String info) {
        this.title = title;
        this.info = info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, com.flightsearch.R.style.BottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialog();
    }

    private void setDialog() {
        binding.textViewInfo.setText(info);
        binding.textViewTitle.setText(title.toUpperCase());
    }

    public void show(@NotNull Activity activity) {
        try {
            super.show(((AppCompatActivity) activity).getSupportFragmentManager(), getTag());
        } catch (IllegalStateException ignored) {

        }
    }

}
