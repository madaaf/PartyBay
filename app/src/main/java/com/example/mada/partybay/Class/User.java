package com.example.mada.partybay.Class;

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

    public User(JSONObject obj) {
        try {
            id = obj.getString("id");
            pseudo = obj.getString("pseudo");
            email = obj.getString("email");
            country_code = obj.getString("country_code");
            phone = obj.getString("phone");
            password = obj.getString("password");
            firstname = obj.getString("firstname");
            lastname = obj.getString("lastname");
            sex= obj.getString("sex");
            birth = obj.getString("birth");
            last_visit_messages = obj.getString("last_visit_messages");
            registered = obj.getString("registered");
            hash = obj.getString("hash");
            active = obj.getString("active");
            phone = "0"+phone;

        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }

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

}
