package fr.partybay.android.Album;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.Post;


/**
 * Created by mada on 17/01/15.
 */
public class AlbumAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 5;
    private Context context;
    private ArrayList<Integer> tabId = new ArrayList<Integer>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private  String item_id;
    private String my_id;
    private int indexPostActuel;
    private int index ;
    private int compteur=0;

    public AlbumAdapter(FragmentManager fm,Context context,String item_id,String my_id) {
        super(fm);
        this.context=context;
        this.item_id = item_id;
        this.my_id = my_id;

        System.out.println("ALBUM ADAPTER  item id ==>"+ item_id);
        System.out.println("ALBUM ADAPTE R my id ==>"+ my_id);

        //récupere information du post sur l'api
        RestClient client = new RestClient(context,"https://api.partybay.fr/users/"+my_id+"/posts?limit=50&offset=0&side=desc");
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
                    post = new Post(context,obj);
                    if(post!=null){
                        tabId.add(Integer.valueOf(post.getId()));
                        posts.add(post);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        int itemI = Integer.parseInt(item_id);
        indexPostActuel = tabId.indexOf(itemI);


    }


    @Override
    public Fragment getItem(int pos) {
        AlbumFragment albumFragment = new AlbumFragment();
        Bundle data = new Bundle();
        data.putInt("current_page", pos+1);
        //data.putString("my_id", posts.get(index).getUser_id());
        data.putString("my_id", my_id);
        index = pos+indexPostActuel;

        //System.out.println("ALBUM ADAPTER  current_page=>"+ pos+1 );
       // System.out.println("ALBUM ADAPTER  index=>"+ index );

        // permet de voir tout l'album en cercle jusqu'a revenir à la 1 er photo qu'on a ouvert
       if(index<(posts.size())){
           data.putString("item_id",posts.get(index).getId());
           //System.out.println(" compteur "+index + "position "+pos+" id post "+posts.get(index).getId());
        }else{
          // System.out.println(" compteur "+index + "position "+pos+" id post "+posts.get(compteur).getId());
           data.putString("item_id",posts.get(compteur).getId());
           compteur++;
       }


        albumFragment.setArguments(data);
        return albumFragment;
    }


    @Override
    public int getCount() {
       // System.out.println("ALBUM ADAPTER  posts.size()=>"+  posts.size());
        return posts.size();
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
