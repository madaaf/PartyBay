package com.example.mada.partybay.TimeLineManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mada on 05/11/2014.
 */

public class PostAdapter extends ArrayAdapter<Post>  {
    TextView user_pseudo;
    TextView text;
    TextView lovers;
    TextView date;
    TextView latitude;
    ImageView link;
    ImageButton loveButton;
    ArrayList<Post> posts = new ArrayList<Post>();
    private boolean unlikeLove = false;
    Thread threadLove = null;
    private String idPost;
    SerializeurMono<User> serializeur ;
    String myUser_id;


    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> posts) {
        super(context,layoutResourceId, posts);
        this.posts = posts;
        Log.d("layoutResourceId ", String.valueOf(layoutResourceId));
        Log.d("posts in post adapter  ", String.valueOf(posts));
    }

    public PostAdapter(Context context, int id) {
        super(context,id);
    }

    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context,0,posts);
    }



    @Override
    public void add(Post post){
        super.add(post);
        posts.add(post);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        serializeur = new SerializeurMono<User>("/storage/sdcard0/PartyBay2/user.serial");
        User user = serializeur.getObject();
        myUser_id = user.getId();

         // System.out.println("THE POSTE ID  2: "+ p.getId());
        //System.out.println("PSIITION "+ position);
        
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.post, parent, false);
        }

            // object item based on the position
           // Post objectItem = data[position];

         user_pseudo =(TextView) row.findViewById(R.id.post_pseudo);
         text = (TextView)row.findViewById(R.id.post_texte);
         lovers = (TextView)row.findViewById(R.id.post_like);
         date = (TextView)row.findViewById(R.id.post_time);
         latitude = (TextView)row.findViewById(R.id.post_lieu);
         link = (ImageView)row.findViewById(R.id.post_photo_fond);
         loveButton = (ImageButton)row.findViewById(R.id.post_coeur);

        // Get the data item for this position
        Post post = getItem(position);
        // Post p = posts.get(position);
        //System.out.println("THE POSTE ID : "+ post.getId());

        // Populate the data into the template view using the data object
        user_pseudo.setText(post.getUser_pseudo());
        text.setText(post.getText());
        lovers.setText(post.getLovers());
        date.setText(post.getDate());
        latitude.setText(post.getLatitude());
        UrlImageViewHelper.setUrlDrawable(link, "https://static.partybay.fr/images/posts/640x640_"+post.getLink());


        //System.out.println("LE POST=> "+post.getId()+ "POST "+ post.size());

        if(post!=null){
            String Sid = post.getId();
            if(Sid!=null){
                int i = Integer.parseInt(post.getId());
                if(i > 2 && post.getId()!=null){
                    // System.out.println("je suis ici " + post.getId());
                    loveButton.setTag(Integer.valueOf(post.getId()));
                    loveButton.setOnClickListener(LoveListener);

                    //
                    //System.out.println("post.getLovers()  " + post.getLovers());
                    //if(post.getLovers())

                    ArrayList<String> tabLovers = post.getTabLovers();
                    System.out.println("tabLovers :"+tabLovers);
                    Iterator<String> it = tabLovers.iterator();
                    Love love = null;
                    try {
                        while(it.hasNext()){
                            String s = it.next();
                            JSONObject obj = null;
                            obj = new JSONObject(s);
                            love = new Love(obj);
                            // json decode

                            System.out.println("POST ID : /"+post.getId()+"myUser_id  /"+ myUser_id+"/User_id_Liker /"+love.getUser_id()+"/");
                            String test2 = String.valueOf(love.getUser_id());

                            if(test2.equals(myUser_id)){
                                System.out.println("J4AI LIKER CE POSTE NUMERO ");
                                loveButton.setImageResource(R.drawable.coeur);
                                break;
                            }else{
                                loveButton.setImageResource(R.drawable.coeur_unlike);
                            }
                           // System.out.println("LOVE PICTURE " + love.getPseudo());
                         }
                    } catch (JSONException e) {
                            e.printStackTrace();
                     }

                    ArrayList<Love> tabLovers2 = post.getTabLoves();
                    //System.out.println("ARRAYLIST DE LOVE "+ tabLovers2);

                    // condition si j'ai deja liker ce poste => coeur rouge
                    /*if(post.getLovers()!=null){
                        loveButton.setImageResource(R.drawable.coeur);
                    }*/
                }
            }

        }

        // Return the completed view to render on screen
        return row;
    }

    View.OnClickListener LoveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            //int id = Integer.valueOf((String)view.getTag());
            System.out.println ("POSITION ID 1 : "+ view.getTag());
            idPost= String.valueOf(view.getTag());
            // Envoie une requete a l'API pour le prevenir du like
            threadLove = new LoveThread();
            threadLove.start();


           // loveButton.setImageResource(R.drawable.coeur);
           /*System.out.println ("unlike 1  : "+ unlikeLove) ;

           if(unlikeLove == false){
                System.out.println ("unlike 2  : "+ unlikeLove) ;

                loveButton.setImageResource(R.drawable.coeur);
                unlikeLove = true;
            }else if(unlikeLove == true){
                System.out.println ("unlike 3  : "+ unlikeLove) ;
                loveButton.setImageResource(R.drawable.coeur_unlike);
                unlikeLove = false;
            }*/

            //System.out.println("POSITION ID 2 : "+id);
        }
    };


    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString!=null){
            JSONArray jsonArray = new JSONArray(jsonString);loveButton.setImageResource(R.drawable.coeur);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }


    class LoveThread extends Thread{
        public void run() {

            // j'ajoute un like a l'API du pose correspondant
            RestClient client = new RestClient("https://api.partybay.fr/users/"+myUser_id+"/love/"+idPost);
            System.out.println("https://api.partybay.fr/users/1/love/"+idPost);
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization","Bearer "+access_token);
            String rep = "";
            try{
                rep =  client.Execute("POST");
                System.out.println("REPONSE DU LOVE"+rep);
            }catch(Exception e) {
                e.printStackTrace();
            }

        }

    }


 }