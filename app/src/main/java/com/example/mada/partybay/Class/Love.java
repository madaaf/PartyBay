package com.example.mada.partybay.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mada on 07/01/15.
 */
public class Love implements Serializable {

    private String nbr;
    private String lover;



    public Love(JSONObject obj) {
        try {
            nbr = obj.getString("id");
            lover = obj.getString("pseudo");;
        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }

}
