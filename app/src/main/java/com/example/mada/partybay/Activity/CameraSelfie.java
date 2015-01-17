package com.example.mada.partybay.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mada.partybay.ProfileManager.ProfileViewPagerActivity;
import com.example.mada.partybay.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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



    //private Uri fileUri;
    //Intent intent =  null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera2);

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
       // Uri fileUri2 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE,"selfie_blr.jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);


        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                /***Transformer l'image en flouté et la réangistré ***/
                Intent intent = new Intent(CameraSelfie.this, ProfileViewPagerActivity.class);
                intent.putExtra("photo_path",path_photo);
                startActivity(intent);
                finish();
                // Image captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(CameraSelfie.this, ProfileViewPagerActivity.class);
                startActivity(intent);
                finish();

            } else {
                // Image capture failed, advise user
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
        System.out.println("PHOTO");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PartyBay");
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "selfie.jpg");

        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        path_photo  = mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg";
        System.out.println("mediaFile"+ mediaFile);
        System.out.println("mediaFile.toURI"+mediaFile.toURI());
        System.out.println("mediaStorageDir.getPath()"+mediaStorageDir.getPath());
        System.out.println("File.separator"+File.separator);
        return mediaFile;
    }



}