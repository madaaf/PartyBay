package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Created by mada on 06/11/2014.
 */
public class Inscription extends Activity {

    private EditText pseudo = null;
    private EditText mail = null;
    private EditText tel = null;
    private EditText mdp = null;
    private Button birthday = null;
    private Button valider = null;
    private SerializeurMono<User> serializeur = null;
    private SerializeurMono<Token> serializeur_token = null;
    private AlertDialog charge;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        ActionBar bar = this.getActionBar();
        bar.hide();

        pseudo = (EditText) findViewById(R.id.inscription_ET_pseudo);
        mail = (EditText) findViewById(R.id.inscription_ET_mail);
        tel = (EditText) findViewById(R.id.inscription_ET_tel);
        mdp = (EditText) findViewById(R.id.inscription_ET_pass);
        birthday = (Button) findViewById(R.id.inscription_bouton_date_naissance);
        valider = (Button) findViewById(R.id.inscription_button_valider);

        valider.setOnClickListener(validerListener);
        //serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        serializeur_token = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));

    }

    View.OnClickListener validerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pseudo_s = pseudo.getText().toString();
            String mdp_s = mdp.getText().toString();
            String tel_s = tel.getText().toString();
            String mail_s = mail.getText().toString();

            // je recupere un token
            RestClient client_token = new RestClient("https://api.partybay.fr/token");
            String autho = "Basic " + Base64.encodeToString(("partybay" + ":" + "Pb2014").getBytes(), Base64.NO_WRAP);
            client_token.AddParam("grant_type","client_credentials");
            client_token.AddHeader("Authorization",autho);
            try {
                client_token.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = client_token.getReponsePost();
            System.out.println("CLIENT TOKEN"+response);
            JSONObject tokObj = null;
            Token token = null;
            String access_token = null;
            try {
                 tokObj = new JSONObject(response);
                 token = new Token(tokObj);
                access_token= token.getAcess_token();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // j'insere mon user dans la base de donnée

            RestClient client = new RestClient("https://api.partybay.fr/users");
            client.AddHeader("Authorization","Bearer "+access_token);
            client.AddParam("pseudo",pseudo_s);
            client.AddParam("email",mail_s);
            client.AddParam("phone",tel_s);
            client.AddParam("password",mdp_s);
            client.AddParam("from","app");

            try {
                client.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String user_data = client.getReponsePost();

            try {
                JSONObject obj = new JSONObject(user_data);
                if(obj.has("error")){
                    System.out.println("ERREUR INSCRIOTION1 "+ obj.get("description"));
                    String error = translateError(obj.get("description").toString());
                    afficherPopup(error, null);
                }else{
                    afficherPopup("Votre compte à été créer avec succes", new redirection());
                    Token tokenObj = new Token(obj);
                    // je creer mon fichier Token et je met mon nouv object token
                    serializeur_token.setObjet(tokenObj);
                }

            } catch (JSONException e) {
                System.out.println("ERREUR INSCRIOTION2 "+e.getMessage());
                afficherPopup(e.getMessage(), null);
                e.printStackTrace();
            }



        }
    };

    public void afficherPopup(final String message, final android.content.DialogInterface.OnClickListener listener){

        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder popup = new AlertDialog.Builder(Inscription.this);
                popup.setMessage(message);
                popup.setPositiveButton("Ok", listener);
                popup.show();
            }
        });
    }

    class redirection implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            String  pseudo_s = pseudo.getText().toString();
            // charge.cancel();
            Intent intent = new Intent(Inscription.this, FirstActivity.class);
            intent.putExtra("pseudo", pseudo_s);
            startActivity(intent);
            finish();

        }
    }

    public String translateError(String error){
        if(error.equals("invalid password, password must contain a minuscule letter, a capital letter, a number and a punctuation")){
            error = "Mot de passe invalide. Le mdp doit contenir au moins une lettre minuscule, une lettre majuscule, un chiffre, et une ponctuation. ";
        }else if (error.equals("pseudo can not be null")){
            error = "Veuillez remplir votre pseudo.";
        }else if(error.equals("invalid email address")){
            error = "L'adresse email est invalide.";
        }else if(error.equals("invalid phone number")){
            error ="Le numéro de téléphone est invalide";
        }else if(error.equals("this pseudo is already used by an account")){
            error = "Ce pseusdo est déjà utilisé par un autre utilsateur.";
        }else if(error.equals("pseudo, email or phone number already used by an account")){
            error = "Le pseudo, l'email ou le numéro de telphone sont déjà utilisés par un autre utilisateur.";
        }
        return error;
    }

}
