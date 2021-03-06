package fr.partybay.android.ProfileManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.Album.AlbumActivity;
import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.Post;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

/**
 * Created by mada on 11/01/15.
 */
public class MomentsActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {

    private MomentsAdapter adapter;
    private ArrayList<Post> posts;
    private SerializeurMono<User> serializeur_user;
    private final int NBROFITEM = 15;
    private String user_id = null;
    private Internet internet = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internet = new Internet(getActivity());
        // initialize the posts list
        posts = new ArrayList<Post>();

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        if(data!=null){
            user_id = data.getString("user_id","ok");
        }



         // on recupere les 10 premier postes
        try {
            getPostFromApi(0,50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
         // inflate the root view of the fragment
         View fragmentView = inflater.inflate(R.layout.moments, container, false);

         // initialize the adapter
         adapter = new MomentsAdapter(getActivity(), posts);

         // initialize the GridView
         GridView gridView = (GridView) fragmentView.findViewById(R.id.gridview);

         TextView v = new TextView(getActivity());
         v.setGravity(Gravity.CENTER);
         v.setHeight(1050);

         //gridView.addView(v);

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

        if(internet.internet()){
            RestClient client = new RestClient(getActivity(),"https://api.partybay.fr/users/"+user_id+"/posts?limit="+nbr_item+"&offset=0&side=desc");
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
                            posts.add(post);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // retrieve the GridView item
        Post item = posts.get(position);

        Intent i = new Intent(getActivity(),AlbumActivity.class);
        i.putExtra("item_id", item.getId());
        i.putExtra("my_user_id",user_id);

        startActivity(i);

        // do something
       // Toast.makeText(getActivity(), item.getId(), Toast.LENGTH_SHORT).show();
    }
}
