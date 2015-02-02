package fr.partybay.android.Class;

/**
 * Created by mada on 06/11/2014.
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    private String id;
    private String pseudo;
    private String email;
    private String country_code;
    private String phone;
    private String password;
    private String str_password;
    private String firstname;
    private String lastname;
    private String sex;
    private String birth;
    private String last_visit_messages;
    private String registered;
    private String hash;
    private String active;
    private String access_token;
    private Boolean doubleTrack;
    private String picture;




    public User(JSONObject obj) {
        try {
            if(obj.has("id")){
                id = obj.getString("id");
            }
            if(obj.has("pseudo")){
                pseudo = obj.getString("pseudo");
                String chaineMaj=pseudo.replaceFirst(".",(pseudo.charAt(0)+"").toUpperCase());
                pseudo = chaineMaj;
            }
            if(obj.has("picture")){
                picture = obj.getString("picture");
            }
            if(obj.has("email")){
                email = obj.getString("email");
            }
            if(obj.has("country_code")){
                country_code = obj.getString("country_code");
            }
            if(obj.has("phone")){
                phone = obj.getString("phone");
            }/*
            if(obj.has("password")){
                password = obj.getString("password");
            }*/
            if(obj.has("firstname")){
                firstname = obj.getString("firstname");
            }
            if(obj.has("lastname")){
                lastname = obj.getString("lastname");
            }
            if(obj.has("sex")){
                sex= obj.getString("sex");
            }
            if(obj.has("birth")){
                birth= obj.getString("birth");
            }
            if(obj.has("last_visit_messages")){
                last_visit_messages = obj.getString("last_visit_messages");
            }
            if(obj.has("registered")){
                registered = obj.getString("registered");
            }
            if(obj.has("hash")){
                hash = obj.getString("hash");
            }
            if(obj.has("active")){
                active = obj.getString("active");
            }
            if(obj.has("phone")){
                phone = "0"+phone;
            }


        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }


    public String getPicture() { return picture;}

    public String getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSex() {
        return sex;
    }

    public String getBirth() {
        return birth;
    }

    public String getLast_visit_messages() {
        return last_visit_messages;
    }

    public String getRegistered() {
        return registered;
    }

    public String getHash() {
        return hash;
    }

    public String getActive() {
        return active;
    }

    public void setStr_password(String str_password){
        this.str_password=str_password;
    }

    public String getStr_password() {
        return str_password;
    }

    public void setDoubleTrack(Boolean doubleTrack) {
        this.doubleTrack = doubleTrack;
    }

    public Boolean getDoubleTrack(){return doubleTrack;}

}
