package com.example.mada.partybay.TimeLineManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mada.partybay.Activity.CameraActivity;
import com.example.mada.partybay.Activity.Profile;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.MenuManager.ViewPagerActivity;
import com.example.mada.partybay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by mada on 05/11/2014.
 */
public class PostActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private ImageButton menu = null;
    private ImageButton profile = null;
    private ImageButton moment = null;
    private SwipeRefreshLayout layout;
    private ListView listView;
    private PostAdapter adapter;
    private boolean onScroolStateChange = false;
    private Thread ThreadLoadPost;
    private ArrayList<Post> posts = null;

    private SwipeRefreshLayout swipeContainer;

    private int nbr_scroll = 0 ;

    private final static int NBROFITEM = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        ActionBar bar = this.getActionBar();
        bar.hide();

        layout = (SwipeRefreshLayout) findViewById(R.id.swype);
        layout.setOnRefreshListener(this);

        // Set the refresh swype color scheme
        layout.setColorScheme(
                R.color.swype_1,
                R.color.swype_2,
                R.color.swype_3,
                R.color.swype_4);

        menu = (ImageButton) findViewById(R.id.reglage);
        profile = (ImageButton) findViewById(R.id.profile);
        moment = (ImageButton) findViewById(R.id.moment);
        listView = (ListView) findViewById(R.id.lvPost);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swype);

        posts = new ArrayList<Post>();
        // post.setImageDrawable();
        menu.setOnClickListener(reglageListener);
        profile.setOnClickListener(profileListener);
        moment.setOnClickListener(momentListener);

        // on recupere les 10 premier postes
        try {
            getPostFromApi(0,NBROFITEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create the adapter to convert the array to array to views
        adapter = new PostAdapter(this,posts);
        //Attach the adapter to a ListView
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.d(" onScrollStateChanged", "Y00000000");
                onScroolStateChange = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
                int lastInScreen = firstVisibleItem + visibleItemCount;

                int tailleTab = posts.size();
                //System.out.println("taille de la table "+ tailleTab + "TOATL ITEM"+totalItemCount);

                if(lastInScreen == (totalItemCount) && (onScroolStateChange==true)){
                    Log.d(" firstVisibleItem", "YEEEEEEEEHHH");
                    nbr_scroll ++;
                    try {

                        // on récupere les 10 post suivant car l'utilsateur a scroller jusqu'à la fin de la liste
                        getPostFromApi(nbr_scroll*NBROFITEM,NBROFITEM);
                        // Create the adapter to convert the array to array to views
                        //adapter = new PostAdapter(PostActivity,posts);
                        //Attach the adapter to a ListView
                        //listView.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onScroolStateChange = false;
                }

                if(firstVisibleItem ==0){
                    //Log.d(" firstVisibleItem", "=0");
                    layout.setEnabled(true);
                }else{
                    // Log.d(" firstVisibleItem", "!=0");
                    layout.setEnabled(false);
                }
            }
        });



    }





    // get posts from api
    public void getPostFromApi(int pos_debut, int nbr_item) throws Exception {

       // ThreadLoadPost = new LoadListenerThread(pos_debut,nbr_item);
       // ThreadLoadPost.start();

        RestClient client = new RestClient("https://api.partybay.fr/posts?limit="+nbr_item+"&offset="+pos_debut+"&side=desc&order=id");
       // System.out.println("https://api.partybay.fr/posts?limit="+nbr_item+"&offset="+pos_debut+"&side=desc&order=id");
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();

        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";
        try {
            rep =  client.Execute("GET");
            //System.out.println("RESPONSE DE EXECITE GET : " + rep.toString()+ "taille ="+rep.length()) ;
            if (rep!=null && rep.length()>2){
               // System.out.println("je suis ici encore");
                ArrayList<String> stringArray = new ArrayList<String>();
                stringArray=jsonStringToArray(rep);

                Iterator<String> it = stringArray.iterator();
                Post post = null;
                while (it.hasNext()) {
                    String s = it.next();
                    // System.out.println("js : "+s.startsWith("["));
                    // if(s.startsWith("[")){}
                    JSONObject obj = new JSONObject(s);
                    post = new Post(obj);
                    // System.out.println("jajoute le poste numero = "+ post.getDate() + ' '+ post.getId());
                    posts.add(post);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException{
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString!=null){
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }


    @Override
    public void onRefresh() {
        layout.setRefreshing(true);
        Log.d(" onRefresh", "=onRefresh");
        // I create a handler to stop the refresh and show a message after 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(false);
                try {
                    // on récupere les 10 post suivant car l'utilsateur a scroller jusqu'à la fin de la liste
                    posts.clear();
                    getPostFromApi(0, NBROFITEM);
                    //adapter = new PostAdapter(this,posts);
                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(PostActivity.this, "Cool !", Toast.LENGTH_LONG).show();
            }

        }, 3000);

    }

    View.OnClickListener reglageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, ViewPagerActivity.class);
            startActivity(intent);

        }
    };

    View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, Profile.class);
            startActivity(intent);
        }
    };

    View.OnClickListener momentListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, CameraActivity.class);
            startActivity(intent);
        }
    };

/*
    class LoadListenerThread extends Thread{
        int nbr_item;
        int pos_debut;

        public LoadListenerThread(int pos_debut,int nbr_item){
            this.nbr_item=nbr_item;
            this.pos_debut=pos_debut;
        }

        public void run(){




        }
    }
*/

}