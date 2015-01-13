package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mada on 12/01/15.
 */
public class Activation extends Activity {

    private Button activation_button_valider;
    private EditText activation_code;
    String pseudo_intent = null;
    private SerializeurMono<User> serializeur_user;
    private SerializeurMono<Token> serializeur_token;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activation);
        ActionBar bar = this.getActionBar();
        bar.hide();

        activation_button_valider = (Button)findViewById(R.id.activation_button_valider);
        activation_code=(EditText)findViewById(R.id.activation_code);

        Bundle bundle = getIntent().getExtras();
        pseudo_intent= bundle.getString("pseudo");

        StringBuilder sb = new StringBuilder(pseudo_intent);
        for (int index = 0; index < sb.length(); index++) {
            char c = sb.charAt(index);
            if (Character.isLowerCase(c)) {
                sb.setCharAt(index, Character.toUpperCase(c));
            } else {
                sb.setCharAt(index, Character.toLowerCase(c));
            }
        }
        pseudo_intent=sb.toString();
        System.out.println(pseudo_intent);
        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        serializeur_token = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));
        activation_button_valider.setOnClickListener(ListenerButtonValider);
    }


    View.OnClickListener ListenerButtonValider = new View.OnClickListener(){

        @Override
        public void onClick(View v) {



            String code = activation_code.getText().toString();
            System.out.println("CODE : "+code);

            /*
            * Envoyer le code à l'api pour verifier s'il est bon
            */


            // Je récupere un token
            RestClient client_token = new RestClient("https://api.partybay.fr/token");
            String authorization = "Basic " + Base64.encodeToString(("partybay" + ":" + "Pb2014").getBytes(), Base64.NO_WRAP);
            client_token.AddHeader("Authorization",authorization);
            client_token.AddParam("grant_type", "client_credentials");
            String newTokenObjectString = null;
            Token newtoken = null;
            String access_token = null;
            try {
                newTokenObjectString = client_token.Execute("POST");
                JSONObject newTokenObject = new JSONObject(newTokenObjectString);
                newtoken = new Token(newTokenObject);
                // je je l'enregistre pas dans la carte sd car il a a tjs pas verifier que le code est bon
               // tokenSerializeur.setObjet(newtoken);
                access_token = newtoken.getAcess_token();

            } catch (Exception e) {
                e.printStackTrace();
            }


            // envoie le code a l'api pour verifier sa véracité
            RestClient client = new RestClient("https://api.partybay.fr/activate?code="+code);
            client.AddHeader("Authorization", "Bearer " + access_token);
            String rep = null;
            try {
                rep = client.Execute("GET");
                System.out.println("REPONSE ACTIVATION   : "+rep);

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                JSONObject obj = new JSONObject(rep);
                if(obj.has("error")){
                    // code faux
                    afficherPopup("Votre code n'est pas valide.",null);

                }else{
                    // code bon
                    // enregistrer les information du user et du token dans la carte SD
                    User user = new User(obj);
                    serializeur_user.setObjet(user);
                    serializeur_token.setObjet(newtoken);
                    Intent i = new Intent(Activation.this,FirstActivity.class);
                    i.putExtra("pseudo", pseudo_intent);
                    startActivity(i);
                    finish();

                }
            } catch (JSONException e) {
                System.out.println("err "+e.getMessage());

            }

        }
    };


    public void afficherPopup(final String message, final android.content.DialogInterface.OnClickListener listener){

        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder popup = new AlertDialog.Builder(Activation.this);
                popup.setMessage(message);
                popup.setPositiveButton("Ok", listener);
                popup.show();
            }
        });
    }



}


