package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.Token;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

import java.io.File;

/**
 * Created by mada on 06/11/2014.
 */
public class Chargement extends Activity{

    private Thread thread;
    private File fichier;
    private SerializeurMono<User> serializeur;
    private SerializeurMono<Token> token_serializeur;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargement);

        ActionBar bar = this.getActionBar();
        bar.hide();


        thread = new chargementThread();
        thread.start();

        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        token_serializeur = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));

    }


    class chargementThread extends Thread{
        public void run(){

           File file = new File(getResources().getString(R.string.sdcard_path));
            file.mkdir();

            fichier = new File(getResources().getString(R.string.sdcard_user));

           boolean exist = fichier.exists();

            if(fichier.length()==0){
                fichier.delete();
                exist=false;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = null;

            if(exist){
                System.out.println("ancienne connexion");
                // je verifie que le user a ctivé son code
                User user = serializeur.getObject();
                String user_active = user.getActive();

                System.out.println("USER"+ user.getPseudo());
                System.out.println("USER"+ user.getActive());

                if(user_active.equals("1")){
                    System.out.println("key active");
                    i = new Intent(Chargement.this, PostActivity.class );
                    startActivity(i);
                    finish();
                }else{
                    System.out.println("key don't active");
                    i = new Intent(Chargement.this, MyActivity.class );
                    startActivity(i);
                    finish();
                }


            }else{
                System.out.println("premiere connexion");
                i = new Intent(Chargement.this, MyActivity.class );
                startActivity(i);
                finish();
            }

        }
    }




}
