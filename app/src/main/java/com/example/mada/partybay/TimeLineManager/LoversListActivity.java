package com.example.mada.partybay.TimeLineManager;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mada on 15/01/15.
 */
public class LoversListActivity extends Activity{

    private LoverListAdapter adapter = null;
    private ListView loversListView= null;
    private ArrayList<Love> Lovers = new ArrayList<Love>();
    private Love lover = null;
    private Post post = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loverslist);
        ActionBar bar = this.getActionBar();
        bar.hide();

        Lovers = new ArrayList<Love>();
        loversListView = (ListView)findViewById(R.id.loversListView);

        Bundle bundle = getIntent().getExtras();
        String infoLove = bundle.getString("infoLove");

        int index = infoLove.indexOf('/');
        String id_post =infoLove.substring(0,index) ;
        String id_user=infoLove.substring(index+1,infoLove.length());

        System.out.println("infoLove" + infoLove);
        System.out.println("id_user" + id_user);
        System.out.println("id_post" + id_post);

        RestClient client = new RestClient("https://api.partybay.fr/users/" + id_user + "/posts/" + id_post);
        System.out.println("https://api.partybay.fr/users/" + id_user + "/posts/" + id_post);
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization", "Bearer " + access_token);
        ArrayList<String> stringArray = new ArrayList<String>();
        String rep = "";
        Post post = null;
        try {
            rep = client.Execute("GET");
            JSONObject obj = new JSONObject(rep);
             post = new Post(obj);

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
                love = new Love(objT);
                Lovers.add(love);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("tabLoveLovers SIZE "+Lovers.size());

         adapter = new LoverListAdapter(this,R.id.loversListView,Lovers);
        loversListView.setAdapter(adapter);


    }



}