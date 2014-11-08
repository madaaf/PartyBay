package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mada.partybay.R;


public class MyActivity extends Activity {
    /* commentaire test */
    private Button connection = null;
    private Button inscription = null;
    private TextView result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ActionBar bar = this.getActionBar();
        bar.hide();

        connection = (Button) findViewById(R.id.activity_my_button_connexion);
        inscription = (Button) findViewById(R.id.activity_my_button_inscription);


        connection.setOnClickListener(connectionListener);
        inscription.setOnClickListener(inscriptionListener);
        //serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_path))
    }

    private View.OnClickListener connectionListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyActivity.this, Connexion.class);
            startActivity(intent);

        }
    };


    private View.OnClickListener inscriptionListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyActivity.this, Inscription.class);
            startActivity(intent);

        }
    };

}


