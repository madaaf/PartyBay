package com.example.mada.partybay.MenuManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mada.partybay.R;

/**
 * Created by mada on 06/01/15.
 */
public class MenuViewPagerAdapter extends FragmentPagerAdapter {


    public MenuViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos){
            case 0 : {
                return new Reglage();
            }
            case 1 : {
                return new getReglage();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public static class getReglage extends Fragment{
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile, container, false);
            return  rootView;
        }

    }
}
