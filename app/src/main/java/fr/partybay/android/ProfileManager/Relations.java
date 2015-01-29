package fr.partybay.android.ProfileManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.partybay.android.R;

/**
 * Created by mada on 29/01/15.
 */
public class Relations extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.relations, container, false);
      //  adapter = new TrackersAdapter(getActivity(),Trackers);
      //  ListView listView = (ListView) rootView.findViewById(R.id.trackerslistView);
       // listView.setAdapter(adapter);
        return rootView;
    }


}
