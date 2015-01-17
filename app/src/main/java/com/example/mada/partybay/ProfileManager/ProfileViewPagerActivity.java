package com.example.mada.partybay.ProfileManager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mada.partybay.Activity.CameraSelfie;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        JSONObject obj = new JSONObject();
        User user = serializeur_user.getObject();


        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());

        markerMoments= (View)findViewById(R.id.markerMoments);
        markerTrackers=(View)findViewById(R.id.markerTrackers);
        markerTracking=(View)findViewById(R.id.markerTracking);
        pseudoTv = (TextView)findViewById(R.id.profile_pseudo);
        profile_photo = (ImageView)findViewById(R.id.profile_photo);
        font=(ImageView)findViewById(R.id.ItemPorfileFont);



        String path = getResources().getString(R.string.sdcard_selfie);
        String pathFont = getResources().getString(R.string.sdcard_selfie_blr);
        File fichierPhoto = new File(path);
        File fichierPhotoFont = new File(pathFont);

        boolean exist = fichierPhoto.exists();
        System.out.println("EXIST"+exist);
        Thread thread = null;

        // si la photo n'existe paq afficher une photo par default parmis le drawable
        if(fichierPhoto.length()==0){
            fichierPhoto.delete();
            fichierPhotoFont.delete();
            font.setImageResource(R.drawable.photo_fond);

        }else {
            Bitmap bmp = decodeSampledBitmapFromFile(path, 500, 300);
            Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(temp);
            c.drawBitmap(bmp, 0, 0, null);
            profile_photo.setImageBitmap(temp);

            Bitmap bmp2 = decodeSampledBitmapFromFile(path, 500, 300);
            Bitmap temp2 = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas c2 = new Canvas(temp2);
            c2.drawBitmap(bmp, 0, 0, null);
            Bitmap bitmap3 = fastblur(bmp2, 27);
            font.setImageBitmap(bitmap3);

        }
/*
        if(fichierPhotoFont.exists() && fichierPhotoFont.length()!=0){
            Bitmap bitmap = BitmapFactory.decodeFile(fichierPhotoFont.getAbsolutePath());
            font.setImageBitmap(bitmap);
        }else{
            thread = new Thread(new LoadImage());
            thread.start();
        }*/




        profile_photo.setOnClickListener(ListenerPhotoSelfie);

       // markerMoments.setBackgroundResource(0);
        markerTrackers.setBackgroundResource(0);
        markerTracking.setBackgroundResource(0);

        System.out.println("BIRTHDAY  : "+user.getBirth());
        String year = user.getBirth().substring(0, 4);
        String month = user.getBirth().substring(5, 7);
        String jour = user.getBirth().substring(8, user.getBirth().length());

        int year2 = Integer.parseInt(year);
        int month2 = Integer.parseInt(month);
        int jour2 = Integer.parseInt(jour);
        int age = getAge(year2,month2,jour2);


        pseudoTv.setText(user.getPseudo()+","+age);
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
            startActivity(i);
            finish();
        }
    };


    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    class LoadImage implements Runnable{
        Bitmap bmp2 = decodeSampledBitmapFromFile(getResources().getString(R.string.sdcard_selfie_blr), 500, 300);
        Bitmap temp2 = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), Bitmap.Config.RGB_565);
       // Canvas c2 = new Canvas(temp2);
       // c2.drawBitmap(bmp2, 0, 0, null);
        final Bitmap bitmap3 = fastblur(bmp2, 27);

        File image_blur = new File(getResources().getString(R.string.sdcard_selfie_blr));
        public void run() {
            try {
                image_blur.createNewFile();
                FileOutputStream out = new FileOutputStream(image_blur);
                bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }


           runOnUiThread(new Runnable(){
                public void run(){
                  font.setImageBitmap(bitmap3);
                }
            });
        }
    }





}
