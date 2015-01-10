package com.example.mada.partybay.TimeLineManager;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.Class.MyDate;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by mada on 07/11/2014.
 */
public class Post {

    private String id = null;
    private String user_id = null;
    private String link = null;
    private String date = null;
    private String latitude = null;
    private String longitude = null;
    private String text = null;
    private String user_pseudo = null;
    private String lovers = null;
    private int totalLovers;
    private ArrayList<String> tabLovers = null; // Array list de string de tout les lover correspondant au post
    private ArrayList<Love> tabLover = null;  // array list de love de tout les lover correspondant au post
    private Boolean PostIsLoved = false;  // true si j'ai déjà liker se post
    private SerializeurMono<User> serializeur ;
    String myUser_id;



    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public Post(JSONObject obj) {

        serializeur = new SerializeurMono<User>("/storage/sdcard0/PartyBay2/user.serial");
        User user = serializeur.getObject();
        myUser_id = user.getId();
        tabLover = new ArrayList<Love>();
        try {
            // je recupere la reponse de l'api et je creer le post correspondant
            id = obj.getString("id");
            user_pseudo = obj.getString("user_pseudo");
            text = obj.getString("text") +" ID :"+ obj.getString("id");
            latitude = obj.getString("latitude");
            lovers = obj.getString("lovers");


            // Transforme l'arret liste en chiffre pour afficher le nbr de like
            // Je met à jour les données suivant :
            // lover = nbr de personne qui ont lové le poste
            // tabLover = ArrayListe de Love du post
            // postIsLove = true si le poste a été aimé par moi-meme
            if (lovers!="false"){
                tabLovers = jsonStringToArray(lovers);

                lovers = String.valueOf(tabLovers.size());
                totalLovers = tabLovers.size();

                Iterator<String> it = tabLovers.iterator();
                Love love = null;
                try {
                    while(it.hasNext()){
                        String s = it.next();
                        JSONObject objT = null;
                        objT = new JSONObject(s);
                        love = new Love(objT);
                        String test2 = String.valueOf(love.getUser_id());
                        tabLover.add(love);
                        if (test2.equals(myUser_id)) {
                            PostIsLoved=true;
                            break;
                        }else{
                            PostIsLoved=false;
                            tabLover.add(love);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(PostIsLoved==null){PostIsLoved=false;}

            }else{
                lovers  = "0";
                totalLovers = 0;
            }


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


            link = obj.getString("link");

        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }

    public int getTotalLovers() { return totalLovers;}

    public void setTotalLovers(int totalLovers) {this.totalLovers = totalLovers;}
    public void setPostIsLoved(Boolean postIsLoved) {PostIsLoved = postIsLoved;}

    public ArrayList<Love> getTabLover() { return tabLover;}

    public Boolean getPostIsLoved() { return PostIsLoved;}

    public ArrayList<String> getTabLovers() { return tabLovers;}

    public String getUser_pseudo() {
        return user_pseudo;
    }

    public void setUser_pseudo(String user_pseudo) {
        this.user_pseudo = user_pseudo;
    }

    public String getLovers() {
        return lovers;
    }

    public void setLovers(String lovers) {
        this.lovers = lovers;
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
