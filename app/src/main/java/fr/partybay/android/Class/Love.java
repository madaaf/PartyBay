package fr.partybay.android.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mada on 07/01/15.
 */
public class Love implements Serializable {

    private int user_id;
    private String picture;
    private String pseudo;
    private Boolean doubleTrack;

    public Love(JSONObject obj) {
        try {

            if(obj.has("id")){
                user_id = Integer.parseInt(obj.getString("id"));
            }
            if(obj.has("picture")){
                picture =obj.getString("picture");
            }
            if(obj.has("pseudo")){
                pseudo = obj.getString("pseudo");
            }
            if(obj.has("doubleTrack")){
                doubleTrack = Boolean.valueOf(obj.getString("doubleTrack"));
            }


        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }

    public Boolean getDoubleTrack() { return doubleTrack; }
    public void setDoubleTrack(Boolean doubleTrack) { this.doubleTrack = doubleTrack; }
    public int getUser_id(){return user_id;}
    public String getPicture() {return picture;}
    public String getPseudo() {return pseudo;}
    @Override
    public String toString(){
        //System.out.println("id: "+user_id+" picture: "+picture+" pseudo: "+pseudo);
        return null;
    }


}
