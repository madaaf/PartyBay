package fr.partybay.android.TimeLineManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mada on 28/01/15.
 */
public class TimeLineAdapter extends FragmentPagerAdapter {

    public TimeLineAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {

        TrackTimeLineFragment ttlf = new TrackTimeLineFragment();
        ZoneTimeLineFragment tlf = new ZoneTimeLineFragment();

        switch(pos){
            case 0 : {
                return tlf;
            }
            case 1 : {
                return ttlf;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
