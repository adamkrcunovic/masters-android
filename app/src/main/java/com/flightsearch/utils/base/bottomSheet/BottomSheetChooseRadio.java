package com.flightsearch.utils.base.bottomSheet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flightsearch.databinding.BottomSheetChooseRadioBinding;
import com.flightsearch.utils.base.BaseActivity;
import com.flightsearch.utils.base.bottomSheet.Adapters.RVAdapterRadioButtonText;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BottomSheetChooseRadio extends BottomSheetDialogFragment {

    private List<Object> listOfObjects;
    private BaseActivity activity;
    private OnObjectSelectListener listener;
    private String title;
    private Object selectedItem;

    private BottomSheetChooseRadioBinding binding;

    public BottomSheetChooseRadio(List<Object> listOfObjects, BaseActivity activity, OnObjectSelectListener listener, String title, Object selectedItem) {
        this.listOfObjects = listOfObjects;
        this.activity = activity;
        this.listener = listener;
        this.title = title;
        this.selectedItem = selectedItem;
    }

    public interface OnObjectSelectListener {
        void onObjectSelectChangeListener(Object o);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, com.flightsearch.R.style.BottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetChooseRadioBinding.inflate(inflater, container, false);
        //setFullDialogHeight(BottomSheetBehavior.STATE_EXPANDED);
        return binding.getRoot();
    }

    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
    }

    /*protected void setFullDialogHeight(@BottomSheetBehavior.State int state) {
        FrameLayout bottomSheet = (FrameLayout) getDialog().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(state);
        } else {
            getDialog().setOnShowListener((DialogInterface.OnShowListener) dialog -> {
                FrameLayout bSheet = (FrameLayout) getDialog().findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bSheet).setState(state);
            });
        }
    }*/

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialog();
        setRecycler();
    }

    private void setDialog() {
        binding.textViewTitle.setText(title.toUpperCase());
    }

    private void setRecycler() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        RVAdapterRadioButtonText adapter = new RVAdapterRadioButtonText(listOfObjects, listener, selectedItem);
        binding.recyclerView.setAdapter(adapter);
    }

    public void show(@NotNull Activity activity) {
        try {
            super.show(((AppCompatActivity) activity).getSupportFragmentManager(), getTag());
        } catch (IllegalStateException ignored) {

        }
    }

}
