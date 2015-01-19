package com.example.mada.partybay.ProfileManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class Story extends Fragment  implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{

    private StoryAdapter adapter;
    private ArrayList<Post> posts;
    private SerializeurMono<User> serializeur_user;
    private User user = null;
    private final int NBROFITEM = 15;
    private String user_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the posts list
        posts = new ArrayList<Post>();
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        if(data!=null){
            user_id = data.getString("user_id","ok");
        }else{
            //System.out.println( "ACTIVITY" + (ProfileViewPagerActivity) getActivity()).getResult());
            serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
            JSONObject obj = new JSONObject();
            user =new User(obj);
            user = serializeur_user.getObject();
            System.out.println("ID USER "+user.getId());
            user_id = user.getId();
        }




        // on recupere les 10 premier postes

        try {
            getPostFromApi(0,NBROFITEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.story, container, false);
        // inflate the root view of the fragment
        // initialize the adapter
        adapter = new StoryAdapter(getActivity(), posts);
        // initialize the GridView
        ListView lv = (ListView) rootView.findViewById(R.id.TrackinglistView);


        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Post item = posts.get(position);
        //System.out.println("ONITEMCLICK"+parent+ " "+view+" "+position+" "+id);
        //System.out.println("ONITEMCLICK"+ item.getUser_pseudo());
    }

    @Override
    public void onRefresh() {

    }

    // get posts from api
    public void getPostFromApi(int pos_debut, int nbr_item) throws Exception {
        // ThreadLoadPost = new LoadListenerThread(pos_debut,nbr_item);
        // ThreadLoadPost.start();

        RestClient client = new RestClient(getActivity(),"https://api.partybay.fr/users/"+user_id+"/posts?limit="+nbr_item+"&offset=0&side=desc");
        //System.out.println("https://api.partybay.fr/users/"+user.getId()+"/posts?limit="+nbr_item+"&offset=0&side=desc");

        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";
        try {
            rep =  client.Execute("GET");
            //System.out.println("RESPONSE DE EXECITE GET : " + rep.toString()+ "taille ="+rep.length()) ;
            if (rep!=null && rep.length()>2){
                 System.out.println("je suis ici encore" +rep);
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


}
