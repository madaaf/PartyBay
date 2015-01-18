package com.example.mada.partybay.MenuManager;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.example.mada.partybay.R;

/**
 * Created by mada on 06/01/15.
 */
public class MenuViewPagerActivity extends FragmentActivity{

    private MenuViewPagerAdapter mSectionsPagerAdaptater;
    private ViewPager mViewPager;
    private Button activite_b;
    private Button reglage_b;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ActionBar bar = this.getActionBar();
        bar.hide();
        activite_b = (Button)findViewById(R.id.activite_b);
        reglage_b = (Button)findViewById(R.id.reglage_b);


        //set u pthe action bar
       // final ActionBar actionbar = getActionBar();
       // actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mSectionsPagerAdaptater = new MenuViewPagerAdapter(getSupportFragmentManager());

        // set up the ViewPager
        mViewPager = (ViewPager)findViewById(R.id.menuviewpager);
        mViewPager.setAdapter(mSectionsPagerAdaptater);
        // modifiler l'indicateur lorsque on swippe
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.

                if(position==1){
                    activite_b.setBackgroundResource(R.drawable.activite_inactive);
                    reglage_b.setBackgroundResource(R.drawable.reglage_active);
                }
                if(position==0){
                    activite_b.setBackgroundResource(R.drawable.activite_active);
                    reglage_b.setBackgroundResource(R.drawable.reglage_inactive);
                }

            }
        });
    }




}
