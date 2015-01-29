package fr.partybay.android.Class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mada on 06/11/2014.
 */
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;

    public Token(JSONObject obj) {
        try {
            if(obj.has("access_token")){
                access_token = obj.getString("access_token");
            }
            if(obj.has("token_type")){
                token_type = obj.getString("token_type");
            }
            if(obj.has("expires_in")){
                expires_in = obj.getString("expires_in");
            }
            if(obj.has("refresh_token")){
                refresh_token = obj.getString("refresh_token");
            }

        } catch (JSONException e) {
            System.out.println("Err : "+e.getMessage());
        }
    }

    public Token getTokenFromSd(){
        SerializeurMono<Token> tokenSerializeur = new SerializeurMono<Token>("/storage/sdcard0/PartyBay2/token.serial");
        JSONObject obj = new JSONObject();
        Token token = new Token(obj);
        token = tokenSerializeur.getObject();
        return token;
    }

    public String getAcess_token(){return access_token;}
    public String getToken_type(){return token_type;}
    public String getExpires_in(){return expires_in;}
    public String getRefresh_token(){return refresh_token;}


}
