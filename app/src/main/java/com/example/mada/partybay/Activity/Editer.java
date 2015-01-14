package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mada.partybay.Class.GPSTracker;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.Token;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by mada on 07/11/2014.
 */
public class Editer extends Activity {

    final String IMAGE_PATH = "imageFile";
    private ImageButton poster;
    private Thread thread;
    private EditText desc;
    private ImageView image_toedit;
    private AlertDialog charge;
    private GPSTracker gps;
    private Double latitude;
    private Double longitude;
    private SerializeurMono<User> serializeur;
    private SerializeurMono<Token> token_serializeur;
    private String link = null;
    private ProgressDialog simpleWaitDialog;
    private ImageButton retour;
    private  String photo_path;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.editer);
        ActionBar bar = this.getActionBar();
        bar.hide();


        poster = (ImageButton) findViewById(R.id.button_poster);
        retour = (ImageButton) findViewById(R.id.editer_retour);
        desc = (EditText) findViewById(R.id.edit_description);
        image_toedit = (ImageView) findViewById((R.id.image_toedit));

        poster.setOnClickListener(posterListener);
        retour.setOnClickListener(retourListener);


        Bundle bundle = getIntent().getExtras();
        photo_path = bundle.getString("photo_path");
        Bitmap bmp = decodeSampledBitmapFromFile(photo_path, 500, 300);
        Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(temp);
        c.drawBitmap(bmp, 0, 0, null);
        image_toedit.setImageBitmap(temp);


        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        token_serializeur = new SerializeurMono<Token>(getResources().getString(R.string.sdcard_token));

    }

    View.OnClickListener posterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(internet()){
                poster.setImageResource(R.drawable.confirmer);
                gps=new GPSTracker(Editer.this);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                //Log.d("longitude : ", Double.toString(longitude));
               // Log.d("latitude : ", Double.toString(latitude));

                new ImageUploaderTask().execute();
            }else{
                afficherPopup("Connectez vous à internet pour pouvoir envoyer une photo." , new redirection());
            }

        }
    };

    View.OnClickListener retourListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent i = new Intent(Editer.this, PostActivity.class);
            startActivity(i);
            finish();
        }
    };

    class Envoie implements Runnable{

        @Override
        public void run() {
            envoyerPhoto();
        }
    }

    public void envoyerPhoto(){

        String text = desc.getText().toString();

        User user = serializeur.getObject();
        String user_id = user.getId();


        RestClient client = new RestClient("https://api.partybay.fr/users/"+user_id+"/posts");
        // Recupere un token valid
        String access_token = client.getTokenValid();

        String authorization = "Bearer " + access_token;
        client.AddHeader("Authorization",authorization);


        File file = new File(photo_path);
        FileBody fileBody = new FileBody(file);
        client.AddFile(fileBody);
        client.AddParamFile("filename", "photo.JPEG");
        client.AddParamFile("user_id", user_id);
        client.AddParamFile("longitude", Double.toString(longitude));
        client.AddParamFile("latitude", Double.toString(latitude));
        client.AddParamFile("text", text);


        String rep = null;

        try {
            rep = client.Execute("FILE");
            System.out.println("Execite file reponse  : "+rep);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            JSONObject obj = new JSONObject(rep);
            if(obj.has("success")){
                if(obj.getString("success").equals("post moment")){
                    System.out.println("SUCCESS ENVOIE FICHIER");
                    afficherPopup("Votre image a bien été envoyée !" + obj.get("description").toString(), new redirection());
                } else{
                    System.out.println("ERROR ENVOIE FICHIER");
                    afficherPopup("Erreur lors de l'envoie de l'image." + obj.get("description").toString() , new redirection());
                }

            }else{;
                System.out.println("IMAGE NON POSTER");
                afficherPopup("Erreur lors de l'envoie de l'image.",new redirection());
            }
        } catch (JSONException e) {
            System.out.println("err "+e.getMessage());

        }


    }

    public void afficherPopup(final String message, final android.content.DialogInterface.OnClickListener listener){

        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder popup = new AlertDialog.Builder(Editer.this);
                popup.setMessage(message);
                popup.setPositiveButton("Ok", listener);
                popup.show();
            }
        });
    }

    class redirection implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Editer.this, PostActivity.class);
            startActivity(intent);
            finish();

        }
    }


    public boolean internet() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }



    private class ImageUploaderTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute(){
            simpleWaitDialog = ProgressDialog.show(Editer.this, "Patientez", "Envoie de l'image en cour ...");
        }

        @Override
        protected Void doInBackground(String... params) {
            envoyerPhoto();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            simpleWaitDialog.dismiss();
        }
    }

    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
