package fr.partybay.android.Google;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by mada on 26/01/15.
 */
public class GoogleAdapter extends FragmentActivity {

    public static FragmentManager fragmentManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        fragmentManager = getSupportFragmentManager();



    }



}
