package fr.partybay.android.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.PopupActivity;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.Token;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

/**
 * Created by mada on 06/11/2014.
 */
public class Inscription extends Activity {

    private EditText pseudo = null;
    private EditText mail = null;
    private EditText tel = null;
    private EditText mdp = null;
    private static Button birthday = null;
    private Button valider = null;
    private RadioGroup radioSexGroup = null;
    private RadioButton radioSexButton = null;

    private SerializeurMono<User> serializeur_user = null;
    private SerializeurMono<Token> serializeur_token = null;
    private AlertDialog charge;
    private static String date_birthday=null;
    private PopupActivity popupActivity = null;
    private Internet internet = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        ActionBar bar = this.getActionBar();
        bar.hide();
        popupActivity = new PopupActivity(this);
        pseudo = (EditText) findViewById(R.id.inscription_ET_pseudo);
        mail = (EditText) findViewById(R.id.inscription_ET_mail);
        tel = (EditText) findViewById(R.id.inscription_ET_tel);
        mdp = (EditText) findViewById(R.id.inscription_ET_pass);
        birthday = (Button) findViewById(R.id.inscription_bouton_date_naissance);
        valider = (Button) findViewById(R.id.inscription_button_valider);
        radioSexGroup = (RadioGroup)findViewById(R.id.inscription_radiouGroup);

        valider.setOnClickListener(validerListener);
        birthday.setOnClickListener(birthdayListener);

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        serializeur_token = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));

    }

    View.OnClickListener birthdayListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }
    };


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month+1;
            String month_s  = String.valueOf(month);
            String day_s = String.valueOf(day);
            if(month<10) {month_s = "0"+month_s;}
            if(day<10){day_s = "0"+ day;}
            birthday.setText(day_s+"/"+month_s+"/"+year);
            date_birthday = year+"-"+month_s+"-"+day_s;
            System.out.println("date_birthday"+date_birthday);
        }
    }

    View.OnClickListener validerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pseudo_s = pseudo.getText().toString();
            String mdp_s = mdp.getText().toString();
            String tel_s = tel.getText().toString();
            String mail_s = mail.getText().toString();
            String sex = null;

            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            if(selectedId!=-1){
                radioSexButton = (RadioButton) findViewById(selectedId);
                sex = (String) radioSexButton.getText();

                if(sex.equals("Femme")){
                    sex = "0";
                }else if(sex.equals("Homme")){
                    sex = "1";
                }
            }

            String error = null;
            if(pseudo_s.equals("")){
                error=getResources().getString(R.string.inscription_error1);
            }else if(mail_s.equals("")){
                error=getResources().getString(R.string.inscription_error2);
            }else if(tel_s.equals("")){
                error=getResources().getString(R.string.inscription_error3);
            }else if (mdp_s.equals("")){
                error=getResources().getString(R.string.inscription_error4);
            }else if(sex==null){
                error=getResources().getString(R.string.inscription_error5);
            }else if(date_birthday==null){
                error=getResources().getString(R.string.inscription_error6);
            }

            if(error!=null) {
               // popupActivity.afficherPopup(error, null);
            }else {

                if (internet.internet()) {
                    // je recupere un token
                    RestClient client_token = new RestClient(view.getContext(), "https://api.partybay.fr/token");
                    String autho = "Basic " + Base64.encodeToString(("android_app" + ":" + "MaD0u!ll3").getBytes(), Base64.NO_WRAP);
                    client_token.AddParam("grant_type", "client_credentials");
                    client_token.AddHeader("Authorization", autho);
                    try {
                        client_token.Execute("POST");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String response = client_token.getReponsePost();
                    System.out.println("CLIENT TOKEN" + response);
                    JSONObject tokObj = null;
                    Token token = null;
                    String access_token = null;
                    try {
                        tokObj = new JSONObject(response);
                        token = new Token(tokObj);
                        serializeur_token.setObjet(token);
                        access_token = token.getAcess_token();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // j'insere mon user dans la base de donnée
                    RestClient client = new RestClient(view.getContext(), "https://api.partybay.fr/users");
                    client.AddHeader("Authorization", "Bearer " + access_token);

                    client.AddParam("pseudo", pseudo_s);
                    client.AddParam("email", mail_s);
                    client.AddParam("phone", tel_s);
                    client.AddParam("password", mdp_s);
                    client.AddParam("from", "app");
                    client.AddParam("sex", sex);
                    client.AddParam("birth", date_birthday);

                    try {
                        client.Execute("POST");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String user_data = client.getReponsePost();
                    System.out.println(user_data);

                    try {
                        JSONObject obj = new JSONObject(user_data);

                        if (obj.has("error")) {
                            System.out.println("ERREUR INSCRIOTION1 " + obj.get("description"));
                            String error2 = translateError(obj.get("description").toString());
                           // popupActivity.afficherPopup(error2, null);
                        } else {
                           // popupActivity.afficherPopup(getResources().getString(R.string.inscription_compte_creer), new redirection());
                            // je creer mon fichier user et je met mon nouv object token
                            User user = new User(obj);
                            serializeur_user.setObjet(user);
                        }

                    } catch (JSONException e) {
                        System.out.println("ERREUR INSCRIOTION2 " + e.getMessage());
                       // popupActivity.afficherPopup(e.getMessage(), null);
                        e.printStackTrace();
                    }


                } else {
                    //popupActivity.afficherPopup(getResources().getString(R.string.error_internet),null);
                }
            }
        }
    };



    class redirection implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Inscription.this, Activation.class);
            startActivity(intent);
            finish();

        }
    }

    public String translateError(String error){
        if(error.equals("invalid password, password must contain a minuscule letter, a capital letter, a number and a punctuation")){
            error = getResources().getString(R.string.inscription_mdp_invalide);
        }else if (error.equals("pseudo can not be null")){
            error = getResources().getString(R.string.inscription_error1);
        }else if(error.equals("invalid email address")){
            error =  getResources().getString(R.string.inscription_error7);
        }else if(error.equals("invalid phone number")){
            error = getResources().getString(R.string.inscription_error8);
        }else if(error.equals("this pseudo is already used by an account")){
            error = getResources().getString(R.string.inscription_error9);
        }else if(error.equals("pseudo, email or phone number already used by an account")){
            error =getResources().getString(R.string.inscription_error10);
        }
        return error;
    }

}