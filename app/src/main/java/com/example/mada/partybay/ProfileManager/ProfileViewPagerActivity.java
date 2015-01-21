package com.example.mada.partybay.ProfileManager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Activity.CameraSelfie;
import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mada on 10/11/2014.
 */
public class ProfileViewPagerActivity extends FragmentActivity{

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    ProfileViewPagerAdapter mAppSectionsPagerAdapter;
    View markerMoments;
    View markerTrackers;
    View markerTracking;
    int position = 0;
    ImageButton retour_b;
    SerializeurMono<User> serializeur_user;
    TextView pseudoTv;
    ImageView profile_photo;
    ImageView font;


    User user  = null;
    String user_id_bundle = null;
    String my_user_id = null;
    Boolean itIsMe = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            user_id_bundle = bundle.getString("user_id");
        }

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        user = serializeur_user.getObject();
        my_user_id = user.getId();

        System.out.println("MY ID /"+my_user_id + "/ /"+user_id_bundle+"/");
        if(my_user_id.equals(user_id_bundle)){
            System.out.println("MY ID ok");
            itIsMe = true;
        }

        if(itIsMe==false){
        try {
            RestClient client = new RestClient(this,"https://api.partybay.fr/users/"+user_id_bundle);
            String accessToken = client.getTokenValid();
            client.AddHeader("Authorization","Bearer " + accessToken);
            String rep = client.Execute("GET");
            JSONObject ob = new JSONObject(rep);
            user = new User(ob);

        } catch (Exception e) {
            e.printStackTrace();
        }
        }




        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(),user.getId());

        markerMoments= (View)findViewById(R.id.markerMoments);
        markerTrackers=(View)findViewById(R.id.markerTrackers);
        markerTracking=(View)findViewById(R.id.markerTracking);
        pseudoTv = (TextView)findViewById(R.id.profile_pseudo);
        profile_photo = (ImageView)findViewById(R.id.profile_photo);
        font=(ImageView)findViewById(R.id.ItemPorfileFont);


        if(itIsMe==true){

            String path = getResources().getString(R.string.sdcard_selfie);
            String pathFont = getResources().getString(R.string.sdcard_selfie_blr);
            File fichierPhoto = new File(path);
            File fichierPhotoFont = new File(pathFont);
            boolean exist = fichierPhoto.exists();

            System.out.println("EXIST"+exist);
            Thread thread = null;
            if(fichierPhotoFont.length()==0){
                fichierPhotoFont.delete();
            }else{
                System.out.println("ELSE");
            }
            // si la photo n'existe paq afficher une photo par default parmis le drawable
            if(fichierPhoto.length()==0){
                fichierPhoto.delete();
                font.setImageResource(R.drawable.photo_fond);
                profile_photo.setImageResource(R.drawable.post);

            }else {
                Bitmap bmp = decodeSampledBitmapFromFile(path, 500, 300);
                Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                Canvas c = new Canvas(temp);
                c.drawBitmap(bmp, 0, 0, null);
                profile_photo.setImageBitmap(temp);

                Bitmap bmp2 = decodeSampledBitmapFromFile(pathFont, 500, 300);
                Bitmap temp2 = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), Bitmap.Config.RGB_565);
                Canvas c2 = new Canvas(temp2);
                c2.drawBitmap(bmp2, 0, 0, null);
                font.setImageBitmap(bmp2);

            }
        profile_photo.setOnClickListener(ListenerPhotoSelfie);
        }else{

            String url = "https://static.partybay.fr/images/users/profile/160x160_" + user.getPicture();
            System.out.println("URL ok "+url);
            RestClient client = new RestClient(this,url);
            String autho = "Basic " + Base64.encodeToString(("android_app" + ":" + "MaD0u!ll3").getBytes(), Base64.NO_WRAP);
            client.AddParam("grant_type", "client_credentials");
            client.AddHeader("Authorization", autho);
            try {
                Bitmap  bit = client.GetBitmapFromUrl();
                font.setImageBitmap(bit);
            } catch (Exception e) {
                e.printStackTrace();
            }





           /* try {
                Bitmap selfie = decodeSampledBitmapFromUrl(url,500,300);
                font.setImageBitmap(selfie);
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

           //// System.out.println("URL ok "+url);
             //
             //font.setImageBitmap(selfie);
             // Blur blur = new Blur();
             // final Bitmap bitmap3 = blur.fastblur(selfie,27);
             // font.setImageBitmap(bitmap3);
              /*
              Bitmap temp = Bitmap.createBitmap(selfie.getWidth(), selfie.getHeight(), Bitmap.Config.RGB_565);
              Canvas c = new Canvas(temp);
              c.drawBitmap(selfie, 0, 0, null);
              profile_photo.setImageBitmap(selfie);*/

              UrlImageViewHelper.setUrlDrawable(profile_photo, "https://static.partybay.fr/images/users/profile/160x160_" + user.getPicture());
             // font.setImageResource(R.drawable.photo_fond);

        }






        // markerMoments.setBackgroundResource(0);
        markerTrackers.setBackgroundResource(0);
        markerTracking.setBackgroundResource(0);


        String year="0";
        String month ="0";
        String jour ="0";

        if(user.getBirth()!="null"){

            year = user.getBirth().substring(0,4);
            month = user.getBirth().substring(5,7);
            jour = user.getBirth().substring(8, user.getBirth().length());

        }


        int year2 = Integer.parseInt(year);
        int month2 = Integer.parseInt(month);
        int jour2 = Integer.parseInt(jour);
        int age = getAge(year2,month2,jour2);


        pseudoTv.setText(user.getPseudo()+", "+age+"ans");
        mViewPager = (ViewPager)findViewById(R.id.profileviewpager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);




        // modifier l'indicateur lorsqu'on swippe
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

                if(position==0){
                    System.out.println("position 0 ====+>"+position);
                    markerMoments.setBackgroundResource(R.color.red);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(0);

                }
                if(position==1){
                    System.out.println("position  1  ====+>"+position);
                    markerMoments.setBackgroundResource(0);
                    markerTrackers.setBackgroundResource(R.color.red);
                    markerTracking.setBackgroundResource(0);
                }
                if(position==2){
                    System.out.println("position 2 ====+>"+position);
                    markerMoments.setBackgroundResource(0);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(R.color.red);
                }

            }

        });


    }



    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
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

    View.OnClickListener ListenerPhotoSelfie = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent i  = new Intent(ProfileViewPagerActivity.this,CameraSelfie.class);
            i.putExtra("user_id",user.getId());
            startActivity(i);
            finish();
        }
    };




}
