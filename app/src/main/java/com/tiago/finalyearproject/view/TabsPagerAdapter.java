package com.tiago.finalyearproject.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Tiago on 21/03/2016.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        Fragment fragment = getRegisteredFragment(index);

        if(fragment==null) {
            switch (index) {
                case 0:
                    // Feed fragment activity
                    return new ActivitiesFragment();
                case 1:
                    // Events fragment activity
                    return new EventsFragment();
                case 2:
                    // Friends fragment activity
                    return new FriendsFragment();
            }
        }

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}