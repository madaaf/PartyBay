package com.example.mada.partybay.ProfileManager;

import android.os.Bundle;
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
    String user_id = null;



    public ProfileViewPagerAdapter(FragmentManager fm,String user_id) {
        super(fm);
        this.user_id=user_id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString("user_id",user_id);
        //System.out.println(" ma position "+position);
        switch(position){
            case 0 : {
                Story story = new Story();
                story.setArguments(args);
                return story;
            }
            case 1 : {
                MomentsActivity ma = new MomentsActivity();
                ma.setArguments(args);
                return ma;
            }
            case 2 : {
                Trackers trackers = new Trackers();
                trackers.setArguments(args);
                return trackers;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
