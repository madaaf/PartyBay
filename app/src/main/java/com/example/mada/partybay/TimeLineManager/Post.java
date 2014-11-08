package com.example.mada.partybay.TimeLineManager;

import com.example.mada.partybay.Class.MyDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public Post(JSONObject obj) {
        try {
            user_pseudo = obj.getString("user_pseudo");
            text = obj.getString("text");
            latitude = obj.getString("latitude");
            lovers = obj.getString("lovers");

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

    public String getDate() {
        return date;
    }

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

    private String user_pseudo = null;
    private String lovers = null;







}
