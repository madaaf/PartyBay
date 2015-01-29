package fr.partybay.android.TimeLineManager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.partybay.android.Activity.CameraAc;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.MenuManager.MenuViewPagerActivity;
import fr.partybay.android.ProfileManager.ProfileViewPagerActivity;
import fr.partybay.android.R;

/**
 * Created by mada on 28/01/15.
 */
public class TimeLine extends FragmentActivity {

    private ImageButton menu = null;
    private ImageButton profile = null;
    private ImageButton moment = null;
    private SerializeurMono<User> serializeur_user;
    private String my_user_id;
    private Typeface font;
    private TextView entete;
    private TimeLineAdapter mSectionsPagerAdaptater;
    private ViewPager mViewPager;
    private Button zone;
    private Button track;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        ActionBar bar = this.getActionBar();
        bar.hide();

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        User user = serializeur_user.getObject();
        my_user_id = user.getId();

        profile = (ImageButton) findViewById(R.id.profile);
         moment = (ImageButton) findViewById(R.id.moment);
        menu = (ImageButton) findViewById(R.id.reglage);
        zone = (Button)findViewById(R.id.timeline_button_zone);
        track = (Button)findViewById(R.id.timeline_button_track);
        font = Typeface.createFromAsset(getAssets(), "fonts/havana.otf");


        entete = (TextView)findViewById(R.id.entete);
        entete.setTypeface(font);
        entete.setOnClickListener(enteteListener);

        menu.setOnClickListener(reglageListener);

        profile.setOnClickListener(profileListener);
        moment.setOnClickListener(mmomentListener);


        mSectionsPagerAdaptater = new TimeLineAdapter(getSupportFragmentManager());
        System.out.println("mSectionsPagerAdaptater"+mSectionsPagerAdaptater);

        mViewPager = (ViewPager)findViewById(R.id.timelineviewpager);
        mViewPager.setAdapter(mSectionsPagerAdaptater);

       zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0, true);
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1,true);
            }
        });


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

        @Override
            public void onPageSelected(int position) {
            // When swiping between different app sections, select the corresponding tab.
            // We can also use ActionBar.Tab#select() to do this if we have a reference to the
            // Tab.

            if(position==1){
                System.out.println("TIMELINE 1 " + position);
                zone.setBackgroundResource(R.drawable.activite_inactive);
                track.setBackgroundResource(R.drawable.reglage_active);
            }
            if(position==0){
                System.out.println("TIMELINE 0 "+position);
                zone.setBackgroundResource(R.drawable.activite_active);
                track.setBackgroundResource(R.drawable.reglage_inactive);
            }

            }
        });
    }


    View.OnClickListener reglageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TimeLine.this, MenuViewPagerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        }
    };

    View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //  Animation rotation = AnimationUtils.loadAnimation(view.getContext(), R.anim.button_rotate);
           // rotation.setRepeatCount(Animation.INFINITE);
            Intent intent = new Intent(TimeLine.this, ProfileViewPagerActivity.class);
            intent.putExtra("user_id",my_user_id);
            startActivity(intent);
            overridePendingTransition(R.anim.rotate_in,R.anim.rotate_out);
            //profile.startAnimation(rotation);
          //  Thread threadPhotoAnim = new ThreadProfile();
          //  threadPhotoAnim.start();
            //profile.clearAnimation();


        }

        class ThreadProfile extends Thread{
            public void run(){


            }
        }
    };

    View.OnClickListener mmomentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*RotateAnimation ra =new RotateAnimation(0, 360);
            ra.setFillAfter(true);
            ra.setDuration(500);

            moment.startAnimation(ra);*/
/*
            Animation rotation = AnimationUtils.loadAnimation(view.getContext(), R.anim.button_rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            moment.startAnimation(rotation);*/
            Thread threadPhotoAnim = new ThreadMoments();
            threadPhotoAnim.start();


        }

        class ThreadMoments extends Thread{
            public void run(){

                runOnUiThread(new Runnable(){
                    public void run(){
                        Intent intent = new Intent(TimeLine.this, CameraAc.class);
                        startActivity(intent);

                    }
                });

                try {

                    Thread.sleep(10000);
                    //moment.clearAnimation();

                } catch (InterruptedException e) {
                    System.out.println("Err thread sleep : "+e.getMessage());
                }

            }
        }
    };




    View.OnClickListener enteteListener = new View.OnClickListener(){
        // revenir en haut de la liste
        @Override
        public void onClick(View v) {
            //listView.setSelection(0);
        }
    };
}
