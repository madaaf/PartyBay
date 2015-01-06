package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mada.partybay.Class.SerializeurMono;
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

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivity);
        ActionBar bar = this.getActionBar();
        bar.hide();

        go = (Button) findViewById(R.id.fist_act_button_go);
        pseudo = (TextView) findViewById(R.id.first_act_pseudo);


        Bundle bundle = getIntent().getExtras();
        String pseudo_intent = bundle.getString("pseudo");

        StringBuilder sb = new StringBuilder(pseudo_intent);
        for (int index = 0; index < sb.length(); index++) {
            char c = sb.charAt(index);
            if (Character.isLowerCase(c)) {
                sb.setCharAt(index, Character.toUpperCase(c));
            } else {
                sb.setCharAt(index, Character.toLowerCase(c));
            }
        }

        pseudo.setText("SALUT "+sb.toString());
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
}
