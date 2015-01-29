package fr.partybay.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.partybay.android.R;
import fr.partybay.android.TimeLineManager.TimeLine;

/**
 * Created by mada on 14/01/15.
 */

public class CameraAc extends Activity {
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

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name


        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // oriente l'image correctement

                try {
                    File filePhotoNoRotated = new File(path_photo);
                    Bitmap bitmap  = getCorrectBitmap(null,path_photo);
                    FileOutputStream fOut = new FileOutputStream(filePhotoNoRotated);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(CameraAc.this, Editer.class);
                intent.putExtra("photo_path",path_photo);
                startActivity(intent);
                finish();
                // Image captured and saved to fileUri specified in the Intent
              // Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(CameraAc.this, TimeLine.class);
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
                    "IMG_"+ timeStamp + ".jpeg");


        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        path_photo  = mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpeg";
        System.out.println("mediaFile"+ mediaFile);
        System.out.println("mediaFile.toURI"+mediaFile.toURI());
        System.out.println("mediaStorageDir.getPath()"+mediaStorageDir.getPath());
        System.out.println("File.separator"+File.separator);
        return mediaFile;
    }



    public  Bitmap getCorrectBitmap(Bitmap bitmap, String photo_path) {


        Bitmap bmp = decodeSampledBitmapFromFile(photo_path, 500, 300);
        Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(temp);
        Matrix matrix = new Matrix();


        ExifInterface ei;
        try {
            ei = new ExifInterface(photo_path);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    matrix.setRotate(90,bmp.getWidth()/2,bmp.getHeight()/2);
                    canvas.drawBitmap(bmp, matrix, new Paint());
                    return temp;
                }


                case ExifInterface.ORIENTATION_ROTATE_180:{
                    matrix.setRotate(180,bmp.getWidth()/2,bmp.getHeight()/2);
                    canvas.drawBitmap(bmp, matrix, new Paint());
                    return temp;
                }


                case ExifInterface.ORIENTATION_ROTATE_270:{
                    matrix.setRotate(270,bmp.getWidth()/2,bmp.getHeight()/2);
                    canvas.drawBitmap(bmp, matrix, new Paint());
                    return temp;
                }

                default:{
                    canvas.drawBitmap(bmp, 0, 0,null);
                    return temp;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        //Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
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