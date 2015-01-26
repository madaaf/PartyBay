package fr.partybay.android.Class;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mada on 07/01/15.
 */
public class Love {

    private int user_id;
    private String my_user_id;
    private String picture;
    private String pseudo;
    private Boolean doubleTrack = false;
    private Context context;

    public Love(JSONObject obj, Context context) {
        this.context = context;

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
/*
        serializeurUser = new SerializeurMono<User>(context.getResources().getString(R.string.sdcard_user));
        User user = serializeurUser.getObject();
        my_user_id = user.getId();
*/
    }

    public Boolean getDoubleTrack() { return doubleTrack; }
    public void setDoubleTrack(Boolean doubleTrack) { this.doubleTrack = doubleTrack; }
    public int getUser_id(){return user_id;}
    public String getPicture() {return picture;}
    public String getPseudo() {return pseudo;}










}
