package com.flightsearch.ui.userEntry;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.flightsearch.ui.userEntry.fragment.RegisterFragment;
import com.flightsearch.ui.userEntry.fragment.SignInFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStateAdapter {

    boolean inversePages;

    public ViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setInversePages(boolean inversePages) {
        this.inversePages = inversePages;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (!inversePages) {
            switch (position) {
                case 0:
                    return new RegisterFragment();
                case 1:
                    return new SignInFragment();
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return new SignInFragment();
                case 1:
                    return new RegisterFragment();
                default:
                    return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
