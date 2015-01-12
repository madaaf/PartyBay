package com.example.mada.partybay.ProfileManager;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

/**
 * Created by mada on 10/11/2014.
 */
public class ProfileViewPagerActivity extends FragmentActivity{

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    ProfileViewPagerAdapter mAppSectionsPagerAdapter;
    View markerMoments;
    View markerTrackers;
    View markerTracking;
    int position = 0;
    ImageButton retour_b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());

        markerMoments= (View)findViewById(R.id.markerMoments);
        markerTrackers=(View)findViewById(R.id.markerTrackers);
        markerTracking=(View)findViewById(R.id.markerTracking);
        retour_b=(ImageButton)findViewById(R.id.PROFILEretour);

       // markerMoments.setBackgroundResource(0);
        markerTrackers.setBackgroundResource(0);
        markerTracking.setBackgroundResource(0);

       mViewPager = (ViewPager)findViewById(R.id.profileviewpager);
       mViewPager.setAdapter(mAppSectionsPagerAdapter);
       retour_b.setOnClickListener(retourListener);


        // modifier l'indicateur lorsqu'on swippe
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

                if(position==0){
                    System.out.println("position 0 ====+>"+position);
                    markerMoments.setBackgroundResource(R.color.red);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(0);

                }
               if(position==1){
                    System.out.println("position  1  ====+>"+position);
                   markerMoments.setBackgroundResource(0);
                   markerTrackers.setBackgroundResource(R.color.red);
                   markerTracking.setBackgroundResource(0);
                }
                if(position==2){
                    System.out.println("position 2 ====+>"+position);
                    markerMoments.setBackgroundResource(0);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(R.color.red);
                }


            }

        });


    }

    View.OnClickListener retourListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(ProfileViewPagerActivity.this, PostActivity.class );
            startActivity(i);

        }
    };






}
