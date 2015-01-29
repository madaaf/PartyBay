package fr.partybay.android.MenuManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.partybay.android.R;

/**
 * Created by mada on 29/01/15.
 */
public class Notification extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification, container, false);

        return rootView;
    }
}
