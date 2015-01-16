package com.example.mada.partybay.ProfileManager;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.Post;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mada on 13/01/15.
 */
public class ItemAlbum extends Activity{

    private TextView com = null;
    private ImageView fond = null;
    private TextView pseudo = null;
    private ImageView selfie = null;
    private TextView time = null;
    private TextView lieu = null;
    private ImageButton coeur = null;
    private TextView nbr_like = null;

    private ImageButton suivant;
    private ImageButton retour;
    private ArrayList<Post> posts = new ArrayList<Post>();

    String item_id;
    String my_id;
    String my_pseudo;

    private ArrayList<Integer> tabId = new ArrayList<Integer>();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_album);
        ActionBar bar = this.getActionBar();
        bar.hide();

        fond = (ImageView)findViewById(R.id.item_album_post_photo_fond);
        pseudo = (TextView)findViewById(R.id.item_album_post_pseudo);
        selfie = (ImageView)findViewById(R.id.item_album_post_photo);
        time = (TextView)findViewById(R.id.item_album_post_time);
        lieu = (TextView)findViewById(R.id.item_album_post_lieu);
        coeur = (ImageButton) findViewById(R.id.item_album_post_coeur);
        nbr_like = (TextView)findViewById(R.id.item_album_post_like);
        com = (TextView)findViewById(R.id.item_album_post_texte);

        suivant = (ImageButton)findViewById(R.id.item_album_droite);
        retour = (ImageButton)findViewById(R.id.item_album_gauche);


        suivant.setOnClickListener(suivantListener);
        //retour.setOnClickListener(retourListener);

        Bundle bundle = getIntent().getExtras();
        item_id = bundle.getString("item_id");
        my_id = bundle.getString("my_user_id");
        my_pseudo = bundle.getString("my_pseudo");


        //récupere information du post sur l'api
        RestClient client = new RestClient(this,"https://api.partybay.fr/users/"+my_id+"/posts/"+item_id);
        System.out.println("https://api.partybay.fr/users/"+my_id+"/posts/"+item_id);
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";

        Post item = null;
        try {
            rep = client.Execute("GET");
            System.out.println(rep);
            JSONObject obj = new JSONObject(rep);
            item = new Post(this,obj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        pseudo.setText(my_pseudo);
        time.setText(item.getDate());
        lieu.setText(item.getLatitude());
        com.setText(my_id+" "+item_id);
        //viewHolder.link.setImageDrawable(R.drawable.photo_fond);
        UrlImageViewHelper.setUrlDrawable(fond, "https://static.partybay.fr/images/posts/640x640_" + item.getLink());

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



    View.OnClickListener suivantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //récupere information du post sur l'api
            RestClient client = new RestClient(v.getContext(),"https://api.partybay.fr/users/"+my_id+"/posts?offset=0");

            // je recupere un token dans la sd carte
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization","Bearer "+access_token);
            String rep = "";
            try {
                rep =  client.Execute("GET");
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
                        post = new Post(v.getContext(),obj);
                        if(post!=null){
                            tabId.add(Integer.valueOf(post.getId()));
                            //System.out.println("jajoute le poste numero = "+post.getId());
                            posts.add(post);
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            int index = tabId.indexOf(item_id);
            //System.out.println("POST MTN  "+posts.get(index));
           // Post postSuiv = posts.get(index+1);
            //System.out.println("POST SUIVANT "+postSuiv.getId());

/*

            System.out.println("index DU POST mtn "+tabId.indexOf(item_id));
            System.out.println("ID DU POST mtn "+posts.get(tabId.indexOf(item_id)));

            System.out.println("index DU POST suivant "+tabId.indexOf(item_id)+1);
            System.out.println("ID DU POST suivant "+posts.get(tabId.indexOf(item_id)+1));


           // System.out.println("index DU POST avant "+ tabId.indexOf(item_id)-1);
            //System.out.println("ID DU POST suivant "+posts.get(tabId.indexOf(item_id)-1));

            int indexSuiv = tabId.indexOf(item_id)+1;

            System.out.println("index DU POST SUIVANT"+tabId.indexOf(49));
*/
            System.out.println("id derniere"+item_id);
            System.out.println("index DU POST 49"+tabId.indexOf(49));
            System.out.println("index DU POST 39"+tabId.indexOf(39));
            System.out.println("index DU POST 38 "+tabId.indexOf(38));
            System.out.println("index DU POST37 "+tabId.indexOf(37));

            System.out.println("POST 49"+posts.get(tabId.indexOf(49)).getId());
            System.out.println("POST 39"+posts.get(tabId.indexOf(39)).getId());
            System.out.println("POST 38 "+posts.get(tabId.indexOf(38)).getId());
            System.out.println("POST37 "+posts.get(tabId.indexOf(37)).getId());

            //Intent i = new Intent(ItemAlbum.this,ItemAlbum.class);
            //i.putExtra("item_id",posts.get())


        }
    };

}
