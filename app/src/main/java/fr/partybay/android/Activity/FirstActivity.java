package fr.partybay.android.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.Token;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;
import fr.partybay.android.TimeLineManager.TimeLine;

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
            Intent i = new Intent(FirstActivity.this, TimeLine.class);
            startActivity(i);
            finish();
        }
    };


}
