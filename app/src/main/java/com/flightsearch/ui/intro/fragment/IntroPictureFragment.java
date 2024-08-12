package com.flightsearch.ui.intro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentIntroPictureBinding;
import com.flightsearch.utils.base.BaseFragment;

public class IntroPictureFragment extends BaseFragment implements ApplicationConstants {

    private FragmentIntroPictureBinding binding;
    private int imageIndex;

    public static IntroPictureFragment newInstance(int imageIndex) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_INDEX, imageIndex);
        IntroPictureFragment fragment = new IntroPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIntroPictureBinding.inflate(inflater, container, false);

        imageIndex = getArguments().getInt(IMAGE_INDEX);
        setImage();

        return binding.getRoot();
    }

    private void setImage() {
        int resourceId = getContext().getResources().getIdentifier("intro_" + imageIndex, "drawable", getContext().getPackageName());
        binding.imageView.setImageResource(resourceId);
    }
}