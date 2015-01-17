package com.example.mada.partybay.Album;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/*
 * Created by mada on 20/10/2014.
 */
public class AlbumFragment extends Fragment {
    String item_id;
    int mCurrentPage;
    ArrayList<Integer> tabId = new ArrayList<Integer>();
    ArrayList<Post> posts = new ArrayList<Post>();
    Post post  = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        item_id = data.getString("item_id","ok");

        //récupere information du post sur l'api
        RestClient client = new RestClient(getActivity(),"https://api.partybay.fr/users/102/posts/"+item_id);
        System.out.println("https://api.partybay.fr/users/102/posts/"+item_id);
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";


        try {
            rep = client.Execute("GET");
            System.out.println(rep);
            JSONObject obj = new JSONObject(rep);
            post = new Post(getActivity(),obj);

        } catch (Exception e) {
            e.printStackTrace();
        }



        System.out.println("MCURRENTPAGE + "+mCurrentPage+"ITEMID : "+item_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.albumfragment, container,false);
        TextView tv = (TextView) v.findViewById(R.id.item_album_post_texte);
        ImageView font = (ImageView)v.findViewById(R.id.item_album_post_photo_fond);

        UrlImageViewHelper.setUrlDrawable(font, "https://static.partybay.fr/images/posts/640x640_" + post.getLink());

        tv.setText("item_id  "+item_id+" "+post.getText()+"mCurrentPage"+mCurrentPage);


        return v;

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
