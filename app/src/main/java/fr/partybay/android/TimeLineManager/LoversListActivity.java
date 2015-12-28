package fr.partybay.android.TimeLineManager;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.Love;
import fr.partybay.android.Class.Post;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.ProfileManager.TrackersAdapter;
import fr.partybay.android.R;

/**
 * Created by mada on 15/01/15.
 */
public class LoversListActivity extends Activity{

    private TrackersAdapter adapter = null;
    private ListView loversListView= null;
    private ArrayList<Love> Lovers = new ArrayList<Love>();
    private String id_post = null;
    private String id_user = null;
    private String my_user_id = null;
    private TreeMap<Integer, Love> trackedTree = new TreeMap<Integer, Love>();
    private TreeMap<Integer, Love> loversTree = new TreeMap<Integer, Love>();
    private SerializeurMono<User> serializeur_user;
    private TextView entete = null;
    private Typeface font;
    private Internet internet = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loverslist);
       // ActionBar bar = this.getActionBar();
        //bar.hide();

        internet = new Internet(this);
        Lovers = new ArrayList<Love>();
        loversListView = (ListView)findViewById(R.id.loversListView);
        entete = (TextView)findViewById(R.id.loversListPartyBay);


        Bundle bundle = getIntent().getExtras();
        String infoLove = bundle.getString("infoLove");
        System.out.println("INFOLOVE"+infoLove);

        int index = infoLove.indexOf('/');
        id_post =infoLove.substring(0,index) ;

        int secondIndex = IndexOfSecond(infoLove, "/");
        System.out.println("INFOLOVE secondIndex "+secondIndex);
        id_user = infoLove.substring(index+1,secondIndex);
        my_user_id=infoLove.substring(secondIndex+1,infoLove.length());



        try {
            getLoversFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int nbr_lover = Lovers.size();
        if(nbr_lover==0){
            entete.setText("0 lover");
        }else if (nbr_lover==1){
            entete.setText("1 lover");
        }else{
            entete.setText(nbr_lover+" lovers");
        }
        font = Typeface.createFromAsset(getAssets(), "fonts/havana.otf");
        entete.setTypeface(font);

        try {
            getTrackedFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Parcours des entrées (clef, valeur)
        for (Map.Entry<Integer, Love> entree  : loversTree.entrySet()) {
            // clé +entree.getKey()
            // valeur entree.getValue()
            Love love =  entree.getValue();
            if(trackedTree.containsKey(entree.getKey())){
                love.setDoubleTrack(true);
            }else{
                love.setDoubleTrack(false);
            }

        }


        //System.out.println("tabLoveLovers SIZE "+Lovers.size());
        adapter = new TrackersAdapter(this,Lovers,null);
        loversListView.setAdapter(adapter);


    }


    public void getLoversFromApi(){
        if(internet.internet()){
            RestClient client = new RestClient(this,"https://api.partybay.fr/users/" + id_user + "/posts/" + id_post);
            System.out.println("https://api.partybay.fr/users/" + id_user + "/posts/" + id_post);
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization", "Bearer " + access_token);
            ArrayList<String> stringArray = new ArrayList<String>();
            String rep = "";
            Post post = null;
            try {
                rep = client.Execute("GET");
                JSONObject obj = new JSONObject(rep);
                post = new Post(this,obj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<String> tabLoveLovers = post.getTabStringLovers();

            System.out.println("ook "+tabLoveLovers.toString());

            Iterator<String> it = tabLoveLovers.iterator();
            Love love = null;
            while(it.hasNext()){
                try {
                    String s = it.next();
                    JSONObject objT = null;
                    objT = new JSONObject(s);
                    love = new Love(objT,this);
                    Lovers.add(love);
                    loversTree.put(love.getUser_id(),love);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{

        }

    }


    public void getTrackedFromApi() throws JSONException {
        if(internet.internet()){
            RestClient client = new RestClient(this, "https://api.partybay.fr/users/" + my_user_id + "/tracked");
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
                tracker = new Love(obj,this);
                //  Trackers.add(tracker);
                trackedTree.put(tracker.getUser_id(), tracker);
            }

        }else{

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

   public  int IndexOfSecond(String theString, String toFind)
    {
        int first = theString.indexOf(toFind);

        if (first == -1) return -1;

        // Find the "next" occurrence by starting just past the first
        return theString.indexOf(toFind, first + 1);
    }

}
