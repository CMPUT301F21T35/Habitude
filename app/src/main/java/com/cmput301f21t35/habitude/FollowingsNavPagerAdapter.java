package com.cmput301f21t35.habitude;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FollowingsNavPagerAdapter extends FragmentPagerAdapter {
    private static int items = 3;

    public FollowingsNavPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return items;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // following tab
                return FollowingsNavFragment.newInstance("Following", 0);
            case 1: //  followers
                return FollowerNavFragment.newInstance("Followers", 1);
            case 2: // requests
                return RequestNavFragment.newInstance("Requests", 2);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // following tab
                return "Following";
            case 1: //  followers
                return "Followers";
            case 2: // requests
                return "Requests";
            default:
                return null;
        }
    }
}
