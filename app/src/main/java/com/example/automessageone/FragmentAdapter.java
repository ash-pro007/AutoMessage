package com.example.automessageone;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private int countTab;

    public FragmentAdapter(FragmentManager fragmentManager, int countTab) {
        super(fragmentManager);

        this.countTab = countTab;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new CreateFragment();
            case 1:
                return new HistoryFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return countTab;
    }
}
