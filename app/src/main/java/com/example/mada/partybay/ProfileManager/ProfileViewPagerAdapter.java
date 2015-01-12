package com.example.mada.partybay.ProfileManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mada on 11/01/15.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    public ProfileViewPagerAdapter(FragmentManager fm) {
        super(fm);
        System.out.println("je suis la ");
    }

    @Override
    public Fragment getItem(int position) {
        //System.out.println(" ma position "+position);
        switch(position){
            case 0 : {
                return new Moments();
            }
            case 1 : {
                return new Trackers();
            }
            case 2 : {
                return new Tracking();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
