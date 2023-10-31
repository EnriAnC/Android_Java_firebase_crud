package com.example.practice_firebase.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.practice_firebase.fragments.TabFragment1;
import com.example.practice_firebase.fragments.TabFragment2;
import com.example.practice_firebase.fragments.TabFragment3;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TabFragment1();
            case 1: return new TabFragment2();
            case 2: return new TabFragment3();
            default: return new TabFragment1();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
