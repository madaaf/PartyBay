package com.example.mada.partybay.Class;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by mada on 06/11/2014.
 */
public class RestClient {

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;
    private MultipartEntityBuilder builder;
    private String url;

    private MonThreadGet threadGet;
    private String responseGet;

    private MonThreadPost threadPost;
    private String responsePost;

    private MonThreadPostFile threadPostFile;
    private String responsePostFile;

    private SerializeurMono<Token> tokenSerializeur;
    private SerializeurMono<User> userSerializeur;

    private String CURRENT_TOKEN;




    public RestClient(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public String getReponsePost(){return responsePost;}

    public String getResponseGet(){return responseGet;}

    public String getResponseFile(){return responsePostFile;}

    public void AddHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void AddParam(String name,  String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddFile(FileBody fileBody) {
        builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
    }

    public void AddParamFile(String name, String value) {
        System.out.println("param "+ name + " = " + value);
        builder.addTextBody(name, value);
    }


    public void setUrl(String url){this.url = url;}

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public String Execute(String method) throws Exception {
        if (method.equals("GET")) {

            threadGet = new MonThreadGet();
            threadGet.start();

            try {
                threadGet.join();
            } catch (InterruptedException e) {
                Log.d("Erreur ", "join : " + e.getMessage());
            }
            return threadGet.getReponseGet();

        } else if (method.equals("POST")) {

            threadPost = new MonThreadPost();
            threadPost.start();

            try {
                threadPost.join();
            } catch (InterruptedException e) {
                Log.d("Erreur ", "join : " + e.getMessage());
            }
            return threadPost.getReponsePost();

        }else if(method.equals("FILE")){

            threadPostFile = new MonThreadPostFile();
            threadPostFile.start();

            try {
                threadPostFile.join();
            } catch (InterruptedException e) {
                Log.d("Erreur ", "join : " + e.getMessage());
            }
            return threadPostFile.getReponsePostFile();

        }
        return null;
    }


    private class MonThreadPostFile extends Thread{

        public String getReponsePostFile(){ return responsePostFile;}
        public void run(){

            HttpPost request = new HttpPost(url);
            for (NameValuePair h : headers) {
                request.addHeader(h.getName(), h.getValue());
                System.out.println(h.getName() +h.getValue() );
            }

            if (builder!=null) {
                request.setEntity(builder.build());
            }

            HttpClient client = new DefaultHttpClient();

            HttpResponse httpResponse = null;

            try {
                httpResponse = client.execute(request);
                HttpEntity httpentity = httpResponse.getEntity();
                InputStream instream = httpentity.getContent();
                responsePostFile = convertStreamToString(instream);
            } catch (IOException e) {
                e.printStackTrace();
            }


         }
    }

    public Token getTokenFromSd(){
        tokenSerializeur = new SerializeurMono<Token>("/storage/sdcard0/PartyBay2/token.serial");
        JSONObject obj = new JSONObject();
        Token token = new Token(obj);
        token = tokenSerializeur.getObject();
        return token;
    }

    public Boolean validToken(){

        Boolean bool = false;
        Token token = getTokenFromSd();

        JSONObject obj = new JSONObject();
        System.out.println("CURRENT_TOKEN" + token.getAcess_token());
        CURRENT_TOKEN = token.getAcess_token();

       RestClient client= new RestClient("https://api.partybay.fr/users/1?oauth_token=");
       client.AddHeader("Authorization", "Bearer "+CURRENT_TOKEN);
       String rep = null;

        try {
            rep = client.Execute("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            obj = new JSONObject(rep);
            if(obj.has("error")){
                if(obj.get("description").equals("bad token") || obj.get("description").equals("token has expired"))
                    bool = false;
            }
            else
                bool=true;
        } catch (JSONException e) {
            System.out.println("Err valid token : "+e.getMessage());
            bool = false;
        }
        return bool;
    }

/**
 cette fonction est appelé avant chaque requete pour verifier la validiter du token
 si le token est encore valide, il retourn l'ancien token
 sinon il récupere un nouveau token et se charge d'enregisstre dans token.serial le nouveau token
 **/

    public String getTokenValid(){

        SerializeurMono<Token> serializeur = new SerializeurMono<Token>("/storage/sdcard0/PartyBay/token.serial");
        Token token = getTokenFromSd();
        String refresh_token = token.getRefresh_token();
        String access_token = token.getAcess_token();

        System.out.println("ancien access_token : "+ access_token);
        System.out.println("ancien refresh_token : "+ refresh_token);

        // validToken() == false : acess token non valide, je recupere le refresh token  pour recuper un nouveau AT et RT
        // validToken() == true : acess token encore valide

        Boolean validToken = validToken();
        if(validToken==false){

            System.out.println("je récupere un nouveau token ");
            RestClient client = new RestClient("https://api.partybay.fr/token");
            String authorization = "Basic " + Base64.encodeToString(("partybay" + ":" + "Pb2014").getBytes(), Base64.NO_WRAP);

            client.AddHeader("Authorization",authorization);
            client.AddParam("grant_type", "refresh_token");
            client.AddParam("refresh_token", refresh_token);

            String newTokenObjectString = null;
            Token newtoken = null;


            try {
                newTokenObjectString = client.Execute("POST");
                JSONObject newTokenObject = new JSONObject(newTokenObjectString);
                newtoken = new Token(newTokenObject);
                tokenSerializeur.setObjet(newtoken);
                access_token = newtoken.getAcess_token();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("Ancien Token encore valide, pas besoin de recyuper un nouveau tooken");
        }
        return access_token;
    }


    private class MonThreadGet extends Thread{

        public String getReponseGet() {
            return responseGet;
        }

        public void run() {
            HttpGet request = new HttpGet(url);

            for (NameValuePair h : headers) {
                Log.d("Name ", h.getName());
                Log.d("Name ", h.getValue());
                request.addHeader(h.getName(), h.getValue());
            }

            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;

            try {
                httpResponse = client.execute(request);
                HttpEntity httpentity = httpResponse.getEntity();
                InputStream instream = httpentity.getContent();
                responseGet = convertStreamToString(instream);

            } catch (IOException e) {
                Log.d("Erreur ", e.getMessage());
            }

        }
    }


    private class MonThreadPost extends Thread{

        public String getReponsePost() {
            return responsePost;
        }

        public void run() {
            HttpPost request = new HttpPost(url);

            for (NameValuePair h : headers) {
                System.out.println( h.getName() + h.getValue());
                request.addHeader(h.getName(), h.getValue());
            }

            HttpEntity entity = null;
            if (!params.isEmpty()) {
                System.out.println("param "+ params.toString());
                try {
                    entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                request.setEntity(entity);
            }


            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;

            try {
                httpResponse = client.execute(request);
                HttpEntity httpentity = httpResponse.getEntity();
                InputStream instream = httpentity.getContent();
                responsePost = convertStreamToString(instream);

            } catch (IOException e) {
                Log.d("Erreur ", e.getMessage());
            }

        }
    }

}
