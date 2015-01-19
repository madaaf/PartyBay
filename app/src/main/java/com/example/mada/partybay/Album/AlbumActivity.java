package com.example.mada.partybay.Album;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.example.mada.partybay.R;

/**
 * Created by mada on 17/01/15.
 */
public class AlbumActivity extends FragmentActivity {

    private ViewPager viewPager = null;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        ActionBar bar = this.getActionBar();
        bar.hide();


        Bundle bundle = getIntent().getExtras();
        String item_id = bundle.getString("item_id");
        String my_id = bundle.getString("my_user_id");

        viewPager = (ViewPager)findViewById(R.id.album_view_pager);

       /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();
       /** Instantiating FragmentPagerAdapter */

        System.out.println("ALBUM ACTIVITY "+ item_id);
        AlbumAdapter albumAdapter = new AlbumAdapter(fm,this,item_id);
        viewPager.setAdapter(albumAdapter);

    }
}
