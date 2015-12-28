package fr.partybay.android.ProfileManager;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import fr.partybay.android.Activity.CameraSelfie;
import fr.partybay.android.Class.Blur;
import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.Love;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

/**
 * Created by mada on 10/11/2014.
 */
public class ProfileViewPagerActivity extends FragmentActivity  {

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
    ImageView Buttontrack = null;
    Button bStory = null;
    Button bMoments = null;
    Button bRelation = null;
    private TreeMap<Integer, Love> trackedTree = new TreeMap<Integer, Love>();
    private Internet internet = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();
        internet = new Internet(this);
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            user_id_bundle = bundle.getString("user_id");
        }

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        user = serializeur_user.getObject();
        my_user_id = user.getId();


        if(my_user_id.equals(user_id_bundle)){
            itIsMe = true;
        }

        if(itIsMe==false){
        try {
            if(internet.internet()){
                RestClient client = new RestClient(this,"https://api.partybay.fr/users/"+user_id_bundle);
                String accessToken = client.getTokenValid();
                client.AddHeader("Authorization","Bearer " + accessToken);
                String rep = client.Execute("GET");
                JSONObject ob = new JSONObject(rep);
                user = new User(ob);
            }else{
                //itIsMe = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }



        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(),user.getId(),this);

        markerMoments= (View)findViewById(R.id.markerMoments);
        markerTrackers=(View)findViewById(R.id.markerTrackers);
        markerTracking=(View)findViewById(R.id.markerTracking);
        pseudoTv = (TextView)findViewById(R.id.profile_pseudo);
        bStory = (Button)findViewById(R.id.bStory);
        bMoments = (Button)findViewById(R.id.bMoments);
        bRelation=(Button)findViewById(R.id.bRelations);
        profile_photo = (ImageView)findViewById(R.id.profile_photo);
        Buttontrack = (ImageView)findViewById(R.id.profile_track);
        font=(ImageView)findViewById(R.id.ItemPorfileFont);

        bStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0,true);
            }
        });
        bMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1,true);
            }
        });
        bRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2,true);
            }
        });


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
            }
            if(fichierPhoto.length()==0) {
                fichierPhoto.delete();
            }

            if(fichierPhoto.length()!=0) {
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
            }else{
                // je cherche dans l'api si la photo existe
                if(internet.internet()){
                    String url = "https://static.partybay.fr/images/users/profile/160x160_" + user.getPicture();

                    // photo par defautl si l'image sur l'api n'existe pas
                    if(user.getPicture().equals("") || user.getPicture().equals("null")){
                        font.setImageResource(R.drawable.photo_fond);
                        profile_photo.setImageResource(R.drawable.profile_default);

                    }else{
                        // je transforme l'image de l'api en bitmap pour pouvoir l'inserer dans l'imageView
                        Ion.with(this).load(url).withBitmap().asBitmap()
                                .setCallback(new FutureCallback<Bitmap>() {
                                    @Override
                                    public void onCompleted(Exception e, Bitmap result) {
                                        Blur blur = new Blur();
                                        Bitmap bit = blur.fastblur(result,7);
                                        font.setImageBitmap(bit);
                                    }
                                });


                        UrlImageViewHelper.setUrlDrawable(profile_photo, url);
                    }
                }

            }
         profile_photo.setOnClickListener(ListenerPhotoSelfie);


        }else{
            String url = "https://static.partybay.fr/images/users/profile/160x160_" + user.getPicture();
            System.out.println("URL PROFILE "+url);
            if(user.getPicture().equals("") || user.getPicture().equals("null") || user.getPicture()==null){
                font.setImageResource(R.drawable.photo_fond);
                profile_photo.setImageResource(R.drawable.profile_default);

            }else{
                Ion.with(this).load(url).withBitmap().asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                Blur blur = new Blur();
                                Bitmap bit = blur.fastblur(result,7);
                                font.setImageBitmap(bit);
                            }
                        });


                UrlImageViewHelper.setUrlDrawable(profile_photo, url);
            }


        }



        try {
            getTrackedFromApi();
        } catch (Exception e) {
            e.printStackTrace();
        }



           int id = Integer.parseInt(user.getId());
            if(trackedTree.containsKey(id)){
                Buttontrack.setImageResource(R.drawable.bouton_tracker);
                user.setDoubleTrack(true);
            }else{
                Buttontrack.setImageResource(R.drawable.bouton_untracker);
                user.setDoubleTrack(false);
            }

        if(itIsMe==false){
            Buttontrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(user.getDoubleTrack()==true){
                        Buttontrack.setImageResource(R.drawable.bouton_untracker);
                        user.setDoubleTrack(false);
                    }else{
                        Buttontrack.setImageResource(R.drawable.bouton_tracker);
                        user.setDoubleTrack(true);
                    }

                    Thread threadTrack = new ThreadTrack(v.getContext(), user.getId());
                    threadTrack.start();
                }
            });
        }else{
            Buttontrack.setImageResource(R.drawable.bouton_edit);
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





    public void getTrackedFromApi() throws JSONException {
        if(internet.internet()){
            Thread threadConnexion = new ThreadConnexion();
            threadConnexion.start();

        }else{

        }

    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString != null && jsonString.length() != 2) {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }




    class ThreadTrack extends Thread {
        private Context context;
        private String user_tracker_id;

        public ThreadTrack(Context context, String user_tracker_id){
            this.context=context;
            this.user_tracker_id= user_tracker_id;
        }
        @Override
        public void run() {
            if(internet.internet()){
                System.out.println("TRACKERS RUN "+"https://api.partybay.fr/users/"+my_user_id+"/track/"+user_tracker_id);
                RestClient client  = new RestClient(context,"https://api.partybay.fr/users/"+my_user_id+"/track/"+user_tracker_id);
                String access_token = client.getTokenValid();
                client.AddHeader("Authorization","Bearer "+access_token);
                String rep = "";
                try {
                    rep =  client.Execute("POST");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("TRACKERS PO "+rep);

            }else{

            }

        }
    }

    public class ThreadConnexion extends Thread{
        public void run(){
            RestClient client = new RestClient(getApplication(), "https://api.partybay.fr/users/" + my_user_id + "/tracked");
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization", "Bearer " + access_token);

            String rep = "";
            try {
                rep = client.Execute("GET");
            } catch (Exception e) {
                e.printStackTrace();
            }


            ArrayList<String> stringArray = new ArrayList<String>();
            try {
                stringArray = jsonStringToArray(rep);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Iterator<String> it = stringArray.iterator();
            Love tracker = null;

            while (it.hasNext()) {
                String s = it.next();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                    tracker = new Love(obj,getApplication());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Trackers.add(tracker);
                trackedTree.put(tracker.getUser_id(), tracker);
            }

        }
    }


}
