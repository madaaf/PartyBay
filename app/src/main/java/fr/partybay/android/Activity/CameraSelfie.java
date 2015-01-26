package fr.partybay.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.partybay.android.Class.Blur;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.ProfileManager.ProfileViewPagerActivity;
import fr.partybay.android.R;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by mada on 14/01/15.
 */

public class CameraSelfie extends Activity {
    /**
     * Called when the activity is first created.
     */

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public String  path_photo=null;
    private String user_id = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera2);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);// create a file to save the image

        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri); // set the image file name


        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                /***Transformer l'image en flouté et la réangistré ***/

                File imageBlur = new File(getResources().getString(R.string.sdcard_selfie_blr));
                File imageSelfie = new File(getResources().getString(R.string.sdcard_selfie));
                try {
                    copy(imageSelfie,imageBlur);
                    Bitmap blr = decodeSampledBitmapFromFile((getResources().getString(R.string.sdcard_selfie_blr)), 500, 300);
                    Blur blur = new Blur();
                    final Bitmap bitmap3 = blur.fastblur(blr,17);
                    FileOutputStream fOut = new FileOutputStream(imageBlur);
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread thread = new Thread(new EnvoieSelfie(this));
                thread.start();

                Intent intent = new Intent(CameraSelfie.this, ProfileViewPagerActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();
                // Image captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(CameraSelfie.this, ProfileViewPagerActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();

            } else {
                // Image capture failed, advise user
                afficherPopup("Une erreur est survenue lors de la prise de photo",null);
                Intent intent = new Intent(CameraSelfie.this, ProfileViewPagerActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Video saved to:\n" +  data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


    /** Create a file Uri for saving an image or video */
    private  Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(int type){

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

       // File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
       //         Environment.DIRECTORY_PICTURES), "PartyBay");
        File mediaStorageDir = new File(getResources().getString(R.string.sdcard_path));


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(getResources().getString(R.string.sdcard_selfie));

        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);

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

        return decodeFile(path, options);
    }

    public void copy(File src, File dst) throws IOException {
        System.out.println("je copie "+src+" jusqu'a "+dst);
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    class EnvoieSelfie implements Runnable{
        Context context;
        public EnvoieSelfie(Context context){
            this.context=context;
        }
        @Override
        public void run() {

            RestClient client = new RestClient(context,"https://api.partybay.fr/users/"+user_id);
            client.AddParam("picture", "password");
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization","Bearer "+access_token);

            File file = new File(getResources().getString(R.string.sdcard_selfie));
            FileBody fileBody = new FileBody(file);
            client.AddFile(fileBody);
            client.AddParamFile("filename", "selfie.jpeg");
            client.AddParamFile("which", "profile");
            String rep = null;

            try {
                rep = client.Execute("FILE");
                System.out.println("SELFIE REPONSE: "+rep);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void afficherPopup(final String message, final android.content.DialogInterface.OnClickListener listener){

        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder popup = new AlertDialog.Builder(CameraSelfie.this);
                popup.setMessage(message);
                popup.setPositiveButton("Ok", listener);
                popup.show();
            }
        });
    }


}