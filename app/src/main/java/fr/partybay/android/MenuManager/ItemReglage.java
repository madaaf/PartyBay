package fr.partybay.android.MenuManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.partybay.android.Class.RestClient;
import fr.partybay.android.R;

/**
 * Created by mada on 20/01/15.
 */
public class ItemReglage extends Activity {
    private String activity;
    private String title_m;
    private TextView title;
    private EditText edit;
    private Button button_valider;
    private Button button_annuler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_reglage);
        ActionBar bar = this.getActionBar();
        bar.hide();

        title = (TextView)findViewById(R.id.item_reglage_poppup_1a);
        edit = (EditText)findViewById(R.id.item_reglage_edit_mail);
        button_valider = (Button)findViewById(R.id.item_reglage_button_valider);
        button_annuler = (Button)findViewById(R.id.item_reglage_button_annuler);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            activity  = bundle.getString("activity");
            if(activity.equals("num")){
                title_m = "Modifier votre numero";
                edit.setHint("numéro");

            }
            if(activity.equals("email")){
                title_m = "Modifier votre adresse email";
                edit.setHint("adresse email");
            }

        }
        title.setText(title_m);
        button_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(v.getContext(),MenuViewPagerActivity.class);
                startActivity(i);
                finish();
            }
        });

        button_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread threadNum = new ThreadNum(v.getContext());
                threadNum.start();
            }
        });

        }

    public class ThreadNum extends Thread{
        private Context context;

        public ThreadNum(Context context){
            this.context = context;
        }
        @Override
        public void run() {
            String tel_s = edit.getText().toString();
            RestClient client = new RestClient(context,"https://api.partybay.fr/users/102");
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization", "Bearer " + access_token);
            client.AddParam("phone", tel_s);
            String rep = "";
/*
            try {
                 rep = client.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            System.out.println("REGLAGE "+ rep);

        }
    }
    }
