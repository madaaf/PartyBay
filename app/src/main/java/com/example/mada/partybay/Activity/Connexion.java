package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.Token;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mada on 18/10/2014.
 */
public class Connexion extends Activity{
    private EditText pseudo = null;
    private EditText mdp = null;
    private Button connexion = null;
    private SerializeurMono<User> serializeur;
    private SerializeurMono<Token> serializeur_token;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);

        ActionBar bar = this.getActionBar();
        bar.hide();

        pseudo = (EditText) findViewById(R.id.connexion_ET_pseudo);
        mdp = (EditText) findViewById(R.id.connexion_ETpassword);
        connexion = (Button) findViewById(R.id.connexion_B_connexion);

        connexion.setOnClickListener(pseudoListener);
        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        serializeur_token = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));

    }

    View.OnClickListener pseudoListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            String p = pseudo.getText().toString();
            String m = mdp.getText().toString();


            RestClient client = new RestClient("https://api.partybay.fr/token");
            client.AddParam("grant_type", "password");
            client.AddParam("username", p);
            client.AddParam("password", m);
            String authorization = "Basic " + Base64.encodeToString(("android_app" + ":" + "MaD0u!ll3").getBytes(), Base64.NO_WRAP);
            client.AddHeader("Authorization",authorization);

            try {
                client.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = client.getReponsePost();

            JSONObject obj = null;
            Token token = null ;
            String access_token = null;
            try {
                obj = new JSONObject(response);
                token = new Token(obj);
                serializeur_token.setObjet(token);
                access_token = token.getAcess_token();


            } catch (JSONException e) {
                e.printStackTrace();
            }


            // si l'utilisateur n'existe pas dans la base de donné
            if(access_token==null) {
                Intent intent = new Intent(Connexion.this, popup.class);
                startActivity(intent);

            }else{
                // 2ieme requetes
                RestClient client_connect = new RestClient("https://api.partybay.fr/login");
                client_connect.AddHeader("Authorization","Bearer "+access_token);
                client_connect.AddParam("ident",p);
                client_connect.AddParam("password",m);

                try {
                    client_connect.Execute("POST");
                    String user_data = client_connect.getReponsePost();
                    obj = new JSONObject(user_data);
                    User user = new User(obj);
                    // j'enregistre dans mon fichier user les info
                    serializeur.setObjet(user);

                    Intent intent = new Intent(Connexion.this, PostActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    };


}