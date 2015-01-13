package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mada.partybay.R;

/**
 * Created by mada on 06/11/2014.
 */
public class popup extends Activity {

    private EditText popup_edit_mail;
    private Button popup_button_annuler;
    private Button popup_button_valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        ActionBar bar = this.getActionBar();
        bar.hide();
        popup_edit_mail = (EditText)findViewById(R.id.popup_edit_mail);
        popup_button_annuler = (Button)findViewById(R.id.popup_button_annuler);
        popup_button_valider = (Button)findViewById(R.id.popup_button_valider);

        popup_button_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(popup.this,Connexion.class);
                startActivity(i);
                finish();
            }
        });


        popup_button_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String mail = popup_edit_mail.getText().toString();
                System.out.println(" POPUP : email rentré "+ mail);
            }
        });

    }


}