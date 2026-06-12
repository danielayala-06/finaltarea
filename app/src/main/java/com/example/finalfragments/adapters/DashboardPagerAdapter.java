package com.example.finalfragments.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalfragments.fragments.BuscadorFragment;
import com.example.finalfragments.fragments.ListadoFragment;
import com.example.finalfragments.fragments.RegistroFragment;

public class DashboardPagerAdapter extends FragmentStateAdapter {
    public DashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // En este punto se hace necesario el BACKEND (java)
        switch (position){
            case 0: return new ListadoFragment();
            case 1: return new BuscadorFragment();
            default: return new RegistroFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
