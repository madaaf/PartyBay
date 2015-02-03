package fr.partybay.android.ProfileManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.Post;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

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
    private ParallaxListView lv;
    private Internet internet = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the posts list
        internet = new Internet(getActivity());
        posts = new ArrayList<Post>();
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        if(data!=null) {
            user_id = data.getString("user_id", "ok");
        }


        try {
            getPostFromApi(0,NBROFITEM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.story, container, false);
        View rootView2 = inflater.inflate(R.layout.profile, null, false);



        // inflate the root view of the fragment
        // initialize the adapter
        adapter = new StoryAdapter(getActivity(), posts);
        // initialize the GridView
        lv = (ParallaxListView) rootView.findViewById(R.id.TrackinglistView);

        TextView v = new TextView(getActivity());
        v.setGravity(Gravity.CENTER);
        v.setHeight(1050);
        lv.addHeaderView(v);

       // lv.addHeaderView(rootView2);


        //

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {

    }

    // get posts from api
    public void getPostFromApi(int pos_debut, int nbr_item) throws Exception {
        if(internet.internet()){
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
