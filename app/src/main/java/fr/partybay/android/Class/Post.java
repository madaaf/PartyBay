package fr.partybay.android.Class;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.R;

/**
 * Created by mada on 07/11/2014.
 */
public class Post {

    private Context context;
    private String id = null;
    private String user_id = null;
    private String link = null;
    private String date = null;
    private String latitude = null;
    private String longitude = null;
    private String user_picture = null;
    private String text = null;
    private String user_pseudo = null;
    private String nbrlovers = null;   // nbr de lober en String en string
    private int totalLovers; // nvr de lober
    private ArrayList<String> tabStringLovers = null; // Array list de string de tout les lover correspondant au post
    //private ArrayList<Love> tabLoverLovers = null;  // array list de love de tout les lover correspondant au post

    private Boolean PostIsLoved = false;  // true si j'ai déjà liker se post
    private SerializeurMono<User> serializeur ;
    String myUser_id;


    public Post(Context context, JSONObject obj) {
        this.context = context;
        serializeur = new SerializeurMono<User>(context.getResources().getString(R.string.sdcard_user));
        User user = serializeur.getObject();
        myUser_id = user.getId();
       // tabLoverLovers = new ArrayList<Love>();
        try {
            // je recupere la reponse de l'api et je creer le post correspondant
            if(obj.has("id")){
                id = obj.getString("id");
            }
            if(obj.has("user_id")){
                user_id = obj.getString("user_id");
            }
            if(obj.has("link")){
                link = obj.getString("link");
            }
            if(obj.has("user_pseudo")){
                user_pseudo = obj.getString("user_pseudo");
            }
            if(obj.has("text")){
                text = obj.getString("text") +" ID :"+ obj.getString("id");
            }
            if(obj.has("latitude")){
                latitude = obj.getString("latitude");
            }
            if(obj.has("longitude")){
                longitude = obj.getString("longitude");
            }
            if(obj.has("user_picture")){
                user_picture = obj.getString("user_picture");
            }
            if(obj.has("date")){
                // je transforme la date en bon format
                String test = obj.getString("date");
                int year =Integer.parseInt(test.substring(0,4));
                int month = Integer.parseInt(test.substring(5, 7));
                int day = Integer.parseInt(test.substring(8, 10));
                int hour = Integer.parseInt(test.substring(11, 13));
                int minute = Integer.parseInt(test.substring(14, 16));

                MyDate datePost = new MyDate(year,month,day,hour,minute);
                String time = datePost.getDifferenceDateToday();
                date =time;
            }
            if(obj.has("lovers")){
                nbrlovers = obj.getString("lovers");
                // Transforme l'arret liste en chiffre pour afficher le nbr de like
                // Je met à jour les données suivant :
                // lover = nbr de personne qui ont lové le poste
                // tabLover = ArrayListe de Love du post
                // postIsLove = true si le poste a été aimé par moi-meme

                if (nbrlovers!="false"){
                    tabStringLovers = jsonStringToArray(nbrlovers);
                    nbrlovers = String.valueOf(tabStringLovers.size());
                    totalLovers = tabStringLovers.size();

                    Iterator<String> it = tabStringLovers.iterator();
                    Love love = null;
                    Love lovetest = null;
                    try {
                        while(it.hasNext()){
                            String s = it.next();
                            JSONObject objT = null;
                            objT = new JSONObject(s);
                            love = new Love(objT,context);
                            String test2 = String.valueOf(love.getUser_id());
                            if (test2.equals(myUser_id)) {
                                PostIsLoved=true;
                                break; // pour sortir de la boucle quand il me trouve
                            }else{
                                PostIsLoved=false;
                                //tabLoverLovers.add(love);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(PostIsLoved==null){PostIsLoved=false;}

                }else{
                    nbrlovers  = "0";
                    totalLovers = 0;
                    //tabLoverLovers=null;
                }
            }



        } catch (JSONException e) {
            System.out.println("Err constructeur Post : "+e.getMessage());
        }
    }

    public String getUser_picture(){return user_picture;}
    public int getTotalLovers() { return totalLovers;}

    public void setTotalLovers(int totalLovers) {this.totalLovers = totalLovers;}
    public void setPostIsLoved(Boolean postIsLoved) {PostIsLoved = postIsLoved;}

   // public ArrayList<Love> getTabLoverLovers() { return tabLoverLovers;}

    public Boolean getPostIsLoved() { return PostIsLoved;}

    public ArrayList<String> getTabStringLovers() { return tabStringLovers;}

    public String getUser_pseudo() {
        return user_pseudo;
    }

    public void setUser_pseudo(String user_pseudo) {
        this.user_pseudo = user_pseudo;
    }

    public String getNbrLovers() { return nbrlovers; }

    public void setLovers(String nbrlovers) {
        this.nbrlovers = nbrlovers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException{
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
