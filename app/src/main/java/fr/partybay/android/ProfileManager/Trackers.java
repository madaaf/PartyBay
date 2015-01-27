package fr.partybay.android.ProfileManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import fr.partybay.android.Class.Love;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.R;

/**
 * Created by mada on 11/01/15.
 */
public class Trackers extends Fragment{

    private ArrayList<Love> Trackers = null;
    private TrackersAdapter adapter = null;
    private String user_id;
    private String infolove;
    private String id_post;

    private TreeMap<Integer, Love> trackersTree = new TreeMap<Integer, Love>();
    private TreeMap<Integer, Love> trackedTree = new TreeMap<Integer, Love>();



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


        // Parcours des entrées (clef, valeur)
        for (Map.Entry<Integer, Love> entree  : trackersTree.entrySet()) {
            // clé +entree.getKey()
            // valeur entree.getValue()
            Love love =  entree.getValue();
            if(trackedTree.containsKey(entree.getKey())){
                love.setDoubleTrack(true);
            }else{
                love.setDoubleTrack(false);
            }

        }




    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trackers, container, false);
        adapter = new TrackersAdapter(getActivity(),Trackers);
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
            tracker = new Love(obj,getActivity());
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
            tracker = new Love(obj,getActivity());
            //  Trackers.add(tracker);
            trackedTree.put(tracker.getUser_id(), tracker);
        }

    }

}