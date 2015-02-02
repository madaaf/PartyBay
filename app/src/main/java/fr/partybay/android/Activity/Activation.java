package fr.partybay.android.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.PopupActivity;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

/**
 * Created by mada on 12/01/15.
 */
public class Activation extends Activity {

    private Button activation_button_valider;
    private EditText activation_code;
    private String pseudo_intent = null;
    private SerializeurMono<User> serializeur_user;
    private Button renvoyerCode = null;
    private String phone = null;
    private User user = null;
    private PopupActivity popupActivity = null;
    private Internet internet = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activation);
        ActionBar bar = this.getActionBar();
        bar.hide();
        popupActivity = new PopupActivity(this);
        activation_button_valider = (Button)findViewById(R.id.activation_button_valider);
        activation_code=(EditText)findViewById(R.id.activation_code);
        renvoyerCode = (Button)findViewById(R.id.activation_button);


        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        JSONObject obj = new JSONObject();
        user = new User(obj);
        user = serializeur_user.getObject();
        phone = user.getPhone();
        pseudo_intent = user.getPseudo();
        pseudo_intent = pseudo_intent.toUpperCase();

        activation_button_valider.setOnClickListener(ListenerButtonValider);
        renvoyerCode.setOnClickListener(ListenerButtonRenvoyerCode);
    }

    View.OnClickListener ListenerButtonRenvoyerCode = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            phone ="0610919713";

            if(internet.internet()){
                RestClient client = new RestClient(v.getContext(),"https://api.partybay.fr/activate?phone="+phone);
                System.out.println("https://api.partybay.fr/activate?phone="+phone + user.getId());
                try {

                    activation_button_valider.setTextColor(Color.WHITE);

                    // je recupere un token dans la sd carte
                    String accessToken = client.getTokenValid();
                    String authorization = "Bearer " + accessToken;
                    client.AddHeader("Authorization",authorization);
                    String rep = client.Execute("GET");
                    System.out.println("ACTIVATION "+rep);
                    JSONObject ob = new JSONObject(rep);

                    if(ob.has("error")){
                        popupActivity.afficherPopup(getResources().getString(R.string.activation_popup_error), null);
                    }else{
                        popupActivity.afficherPopup(getResources().getString(R.string.activation_popup_message) + phone, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                popupActivity.afficherPopup(getResources().getString(R.string.error_internet),null);
            }

        }
    };


    View.OnClickListener ListenerButtonValider = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String code = activation_code.getText().toString();

            /*
            * Envoyer le code à l'api pour verifier s'il est bon
            */

            if (internet.internet()){
                // je recupere un token
                RestClient client_token = new RestClient(v.getContext(),"https://api.partybay.fr/token");
                String accessToken = client_token.getTokenValid();
                // envoie le code a l'api pour verifier sa véracité
                RestClient client = new RestClient(v.getContext(),"https://api.partybay.fr/activate?code="+code);
                client.AddHeader("Authorization", "Bearer " + accessToken);
                String rep = null;
                try {
                    rep = client.Execute("GET");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject obj = new JSONObject(rep);

                    if(obj.has("error")){
                        // code faux
                        popupActivity.afficherPopup(getResources().getString(R.string.activation_popup_error2), null);

                    }else{
                        // code bon
                        // enregistrer les information du user et du token dans la carte SD
                        Intent i = new Intent(Activation.this,FirstActivity.class);
                        i.putExtra("pseudo", pseudo_intent);
                        startActivity(i);
                        finish();

                    }
                } catch (JSONException e) {
                    System.out.println("err "+e.getMessage());

                }
            }else{
                popupActivity.afficherPopup(getResources().getString(R.string.error_internet),null);
            }

        }
    };





}


