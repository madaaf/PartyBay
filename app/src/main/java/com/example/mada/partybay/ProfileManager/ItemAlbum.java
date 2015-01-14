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
        String item_id = bundle.getString("item_id");
        String my_id = bundle.getString("my_user_id");
        String my_pseudo = bundle.getString("my_pseudo");


        //récupere information du post sur l'api
        RestClient client = new RestClient("https://api.partybay.fr/users/"+my_id+"/posts/"+item_id);
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
            item = new Post(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("item.getLink()"+item.getLink());
        System.out.println("item.getLink()"+item.getDate());
        System.out.println("item.getLink()"+item.getUser_id());
        System.out.println("item.getLink()"+item.getId());
        System.out.println("item.getLink()"+item.getText());
        System.out.println("item.getLink()"+item.getLatitude());
        System.out.println("item.getLink()"+item.getLongitude());

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

    /*
    static class Item {
        String id;
        String user_id;
        String link;
        String date;
        String latitude;
        String longitude;
        String text;

        Item(JSONObject obj) {
            try {
                id = obj.getString("id");
                user_id = obj.getString("user_id");
                link = obj.getString("link");
                date = obj.getString("date");
                latitude = obj.getString("latitude");
                longitude = obj.getString("longitude");
                text = obj.getString("text") +" ID :"+ obj.getString("id");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }*/


    View.OnClickListener suivantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
