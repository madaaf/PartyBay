package com.example.mada.partybay.Activity;
///


///CETTE CLASSE NEST PAS UTILISER

///
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mada.partybay.MenuManager.ViewPagerActivity;
import com.example.mada.partybay.R;

/*
 * Created by mada on 18/10/2014.
 */
public class TimeLine extends Activity {
    private ImageButton reglage = null;
    private ImageButton profile = null;
    private ImageButton moment = null;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        ActionBar bar = getActionBar();
        bar.hide();

        reglage = (ImageButton) findViewById(R.id.reglage);
        profile = (ImageButton) findViewById(R.id.profile);
        moment = (ImageButton) findViewById(R.id.moment);


        // post.setImageDrawable();
        reglage.setOnClickListener(reglageListener);
        profile.setOnClickListener(profileListener);
        moment.setOnClickListener(momentListener);
    }


    View.OnClickListener reglageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TimeLine.this, ViewPagerActivity.class);
            startActivity(intent);

        }
    };

    View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TimeLine.this, Profile.class);
            startActivity(intent);
        }
    };

    View.OnClickListener momentListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(TimeLine.this, CameraActivity.class);
            startActivity(intent);
        }
    };



}
