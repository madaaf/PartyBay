package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.Token;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

/**
 * Created by mada on 06/11/2014.
 */
public class FirstActivity extends Activity {

    private Button go = null;
    private TextView pseudo = null;
    private SerializeurMono<User> serializeur;
    private SerializeurMono<Token> serializeur_token;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivity);
        ActionBar bar = this.getActionBar();
        bar.hide();

        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        serializeur_token = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));


        go = (Button) findViewById(R.id.fist_act_button_go);
        pseudo = (TextView) findViewById(R.id.first_act_pseudo);


        Bundle bundle = getIntent().getExtras();
        String pseudo_intent = bundle.getString("pseudo");


        pseudo.setText("SALUT "+pseudo_intent);
        go.setOnClickListener(ListenerGo);
    }


    View.OnClickListener ListenerGo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(FirstActivity.this, PostActivity.class);
            startActivity(i);
            finish();
        }
    };

/*
    View.OnClickListener ListenerGo = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            Intent i = new Intent(FirstActivity.this, Connexion.class);
            startActivity(i);
            finish();

            String p = pseudo.getText().toString();
            String m = mdp.getText().toString();


            RestClient client = new RestClient("https://api.partybay.fr/token");
            client.AddParam("grant_type", "password");
            client.AddParam("username", p);
            client.AddParam("password", m);
            String authorization = "Basic " + Base64.encodeToString(("partybay" + ":" + "Pb2014").getBytes(), Base64.NO_WRAP);
            client.AddHeader("Authorization",authorization);

            try {
                client.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = client.getReponsePost();
            System.out.println("Authentification "+response);

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
                Intent intent = new Intent(FirstActivity.this, popup.class);
                startActivity(intent);
                finish();

            }else{
                // 2ieme requetes
                RestClient client_connect = new RestClient("https://api.partybay.fr/login");
                client_connect.AddHeader("Authorization","Bearer "+access_token);
                client_connect.AddParam("ident",p);
                client_connect.AddParam("password",m);

                try {
                    client_connect.Execute("POST");
                    String user_data = client_connect.getReponsePost();
                    System.out.println("serial.user : "+user_data);
                    obj = new JSONObject(user_data);
                    User user = new User(obj);
                    // j'enregistre dans mon fichier user les info
                    serializeur.setObjet(user);

                    Intent intent = new Intent(FirstActivity.this, PostActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    };*/
}
