package com.example.mada.partybay.Class;

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

    public Love(JSONObject obj) {
        try {
            user_id = Integer.parseInt(obj.getString("id"));
            picture =obj.getString("picture");
            pseudo = obj.getString("pseudo");
        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }


    public int getUser_id(){return user_id;}
    public String getPicture() {return picture;}
    public String getPseudo() {return pseudo;}
    @Override
    public String toString(){
        //System.out.println("id: "+user_id+" picture: "+picture+" pseudo: "+pseudo);
        return null;
    }


}
