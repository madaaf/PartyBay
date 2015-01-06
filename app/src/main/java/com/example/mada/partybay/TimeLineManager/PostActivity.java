package com.example.mada.partybay.TimeLineManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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


        // post.setImageDrawable();
        menu.setOnClickListener(reglageListener);
        profile.setOnClickListener(profileListener);
        moment.setOnClickListener(momentListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getPostFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // get posts from api
    public void getPostFromApi() throws Exception {

        RestClient client = new RestClient("https://api.partybay.fr/posts?order=id&side=desc?limit=10");
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();

        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = null;
        try {
            rep =  client.Execute("GET");
            System.out.println("RESPONSE DE EXECITE GET : " + rep);
            ArrayList<String> stringArray = new ArrayList<String>();
            stringArray =jsonStringToArray(rep);

            Iterator<String> it = stringArray.iterator();
            Post post = null;

             ArrayList<Post> posts = new ArrayList<Post>();

            while (it.hasNext()) {
                String s = it.next();
                JSONObject obj = new JSONObject(s);
                post = new Post(obj);
                posts.add(post);
            }


            // Create the adapter to convert the array to array to views
            PostAdapter adapter = new PostAdapter(this,posts);

            //Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.lvPost);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.print("REPONSE "+ postItem.getLink());
    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }


    @Override
    public void onRefresh() {
        // I create a handler to stop the refresh and show a message after 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(false);
                Toast.makeText(PostActivity.this, "Cool !", Toast.LENGTH_LONG).show();
            }

        }, 3000);

    }
}
