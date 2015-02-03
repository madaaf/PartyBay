package fr.partybay.android.ProfileManager;

import android.content.Context;
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
    private String user_id = null;
    private Context context;



    public ProfileViewPagerAdapter(FragmentManager fm,String user_id, Context context) {
        super(fm);
        this.user_id=user_id;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        //System.out.println(" ma position "+position);
        switch(position){
            case 0 : {
                args.putString("user_id",user_id);
                Story story = new Story();
                story.setArguments(args);
                return story;
            }
            case 1 : {
                /*args.putString("user_id",user_id);
                MomentsActivity ma = new MomentsActivity();
                ma.setArguments(args);
                return ma;*/
                args.putString("user_id",user_id);
                Trackers trackers = new Trackers();
                trackers.setArguments(args);
                return trackers;
            }
            case 2 : {
                args.putString("user_id",user_id);
                Tracking tracking = new Tracking();
                tracking.setArguments(args);
                return tracking;
                /*args.putString("user_id",user_id);
                Trackers trackers = new Trackers();
                trackers.setArguments(args);
                return trackers;*/
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
