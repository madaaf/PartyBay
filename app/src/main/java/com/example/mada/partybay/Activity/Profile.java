package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.example.mada.partybay.R;

/**
 * Created by mada on 10/11/2014.
 */
public class Profile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();

    }
}
