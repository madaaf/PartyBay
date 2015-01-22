package com.example.mada.partybay.ProfileManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mada on 11/01/15.
 */
public class Trackers extends Fragment {

    private ArrayList<Love> Trackers = null;
    private TrackersAdapter adapter = null;
    private String user_id;

    private TreeMap<Integer, Love> trackersTree = new TreeMap<Integer, Love>();
    private TreeMap<Integer, Love> trackedTree = new TreeMap<Integer, Love>();
    private ArrayList<Integer> doubleTrack = new ArrayList<Integer>();

/*

    Map<String, Love> trackersTrieAlpha = new TreeMap(Collator.getInstance(Locale.FRENCH));
    Map<String, Love> trackedTrieAlpha = new TreeMap(Collator.getInstance(Locale.FRENCH));

    private Map<Love,Boolean> arrayTrackers = new TreeMap<Love, Boolean>();

    private Map<Integer,Love> arrayDoubleTrack = new TreeMap<Integer,Love>();

//    private TreeMap<Integer,String,Boolean> trackersMap = new TreeMap<Integer,String,Boolean>();
*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Trackers = new ArrayList<Love>();

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();
        if (data != null) {
            user_id = data.getString("user_id", "ok");
        }


        try {
            getTrackersFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getTrackedFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Creer le tableau des doubleTrack
        for (Map.Entry<Integer, Love> entree : trackersTree.entrySet()){
            if(trackedTree.containsKey(entree.getKey())){
                doubleTrack.add(entree.getKey());
            }
        }




/*



        // Creer le tableau des doubleTrack
        for (Map.Entry<Integer, Love> entree : trackersTree.entrySet()){
            if(arrayDoubleTrack.containsKey(entree.getKey())){
               // System.out.println("TRACKER key true : "+entree.getKey()+" Valeur : "+entree.getValue());
                arrayTrackers.put(entree.getValue(),true);
            }else{
                System.out.println("TRACKER key false : "+entree.getKey()+" Valeur : "+entree.getValue());
                arrayTrackers.put(entree.getValue(),false);
            }

        }



        System.out.println("TRACKER PARCOURIRE TABLEAU "+ arrayDoubleTrack);
        System.out.println("TRACKER PARCOURIRE TABLEAU tracker array "+ arrayTrackers);

*/



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trackers, container, false);
        adapter = new TrackersAdapter(getActivity(),Trackers,doubleTrack);
       // adapter = new TrackersAdapter(getActivity(),trackersTree,trackedTree);
       // adapter = new LoverListAdapter(getActivity(), R.id.trackerslistView, trackersTree,trackedTree);
        ListView listView = (ListView) rootView.findViewById(R.id.trackerslistView);
        listView.setAdapter(adapter);
        return rootView;
    }


    public void getTrackersFromApi() throws JSONException {

        RestClient client = new RestClient(getActivity(), "https://api.partybay.fr/users/" + user_id + "/trackers");
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization", "Bearer " + access_token);

        String rep = "";
        try {
            rep = client.Execute("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<String> stringArray = new ArrayList<String>();
        try {
            stringArray = jsonStringToArray(rep);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> it = stringArray.iterator();
        Love tracker = null;

        while (it.hasNext()) {
            String s = it.next();
            JSONObject obj = new JSONObject(s);
            tracker = new Love(obj);
            Trackers.add(tracker);
            trackersTree.put(tracker.getUser_id(), tracker);

        }


    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString != null && jsonString.length() != 2) {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }

    public void getTrackedFromApi() throws JSONException {
        RestClient client = new RestClient(getActivity(), "https://api.partybay.fr/users/" + user_id + "/tracked");
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization", "Bearer " + access_token);

        String rep = "";
        try {
            rep = client.Execute("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<String> stringArray = new ArrayList<String>();
        try {
            stringArray = jsonStringToArray(rep);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> it = stringArray.iterator();
        Love tracker = null;

        while (it.hasNext()) {
            String s = it.next();
            JSONObject obj = new JSONObject(s);
            tracker = new Love(obj);
            //  Trackers.add(tracker);
            trackedTree.put(tracker.getUser_id(), tracker);
        }

    }

}