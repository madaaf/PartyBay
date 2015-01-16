package com.example.mada.partybay.ProfileManager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mada on 11/01/15.
 */
public class MomentsViewPagerActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {

    private MomentViewPagerAdapter adapter;
    private ArrayList<Post> posts;
    private SerializeurMono<User> serializeur_user;
    private User user = null;
    private final int NBROFITEM = 15;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the posts list
        posts = new ArrayList<Post>();
        Resources resources = getResources();


        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        JSONObject obj = new JSONObject();
        user =new User(obj);
        user = serializeur_user.getObject();
        System.out.println("ID USER "+user.getId());

         // on recupere les 10 premier postes
        try {
            getPostFromApi(0,NBROFITEM);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
         // inflate the root view of the fragment
         View fragmentView = inflater.inflate(R.layout.moments, container, false);

         // initialize the adapter
         adapter = new MomentViewPagerAdapter(getActivity(), posts);

         // initialize the GridView
         GridView gridView = (GridView) fragmentView.findViewById(R.id.gridview);
         gridView.setAdapter(adapter);
         gridView.setOnItemClickListener(this);

         return fragmentView;

    }


    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString!=null){
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }


    // get posts from api
    public void getPostFromApi(int pos_debut, int nbr_item) throws Exception {

        // ThreadLoadPost = new LoadListenerThread(pos_debut,nbr_item);
        // ThreadLoadPost.start();

        RestClient client = new RestClient(getActivity(),"https://api.partybay.fr/users/"+user.getId()+"/posts?limit="+nbr_item+"&offset=0&side=desc");
        //System.out.println("https://api.partybay.fr/users/"+user.getId()+"/posts?limit="+nbr_item+"&offset=0&side=desc");

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
                    post = new Post(getActivity(),obj);
                     if(post!=null){
                        //System.out.println("jajoute le poste numero = "+post.getId());
                         posts.add(post);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // retrieve the GridView item
        Post item = posts.get(position);

        Intent i = new Intent(getActivity(),ItemAlbum.class);
        i.putExtra("item_id", item.getId());
        i.putExtra("my_user_id",user.getId());
        i.putExtra("my_pseudo",user.getPseudo());
        startActivity(i);

        // do something
       // Toast.makeText(getActivity(), item.getId(), Toast.LENGTH_SHORT).show();
    }
}
