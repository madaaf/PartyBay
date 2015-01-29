package fr.partybay.android.Annex;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.partybay.android.Activity.Editer;
import fr.partybay.android.R;
import fr.partybay.android.TimeLineManager.TimeLine;

/**
 * Created by mada on 07/11/2014.
 */


public class CameraActivity extends Activity implements TextureView.SurfaceTextureListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Camera mCamera;

    private ImageButton capture;
    private ImageButton quitter;
    private ImageButton swift;
    private FileOutputStream stream2;
    private TextureView texture;
    private Thread thread;
    public static boolean FLASH_AVAILABLE;
    public static int ID_FRONT_CAMERA;
    public static int ID_BACK_CAMERA;
    private boolean camera_back;
    private boolean photo_taken;
    private ImageButton flash;
    private boolean flash_active;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        ActionBar bar = this.getActionBar();
        bar.hide();

        texture = (TextureView)findViewById(R.id.textureView_camera_activity);
        capture = (ImageButton) findViewById(R.id.button_capture);
        quitter = (ImageButton) findViewById(R.id.quit);
        swift = (ImageButton) findViewById(R.id.swift);
        flash = (ImageButton) findViewById(R.id.flash);


        texture.setSurfaceTextureListener(this);
        ID_FRONT_CAMERA = indexOfCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        ID_BACK_CAMERA = indexOfCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        camera_back = true;


        // Create our Preview view and set it as the content of our activity.

        capture.setOnClickListener(captureListener);
        quitter.setOnClickListener(quitterListener);
        swift.setOnClickListener(swiftListener);
        flash.setOnClickListener(flashListener);
        photo_taken = false;

        flash_active = true;
        FLASH_AVAILABLE = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!FLASH_AVAILABLE){
            Toast.makeText(this, "Votre téléphone ne dispose pas de flash.", Toast.LENGTH_SHORT).show();
            flash.setEnabled(false);
            flash_active=false;
        }
    }

    private void releaseCamera(){
        Log.d("Camera ", "release Camera");
        if (mCamera != null){
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("Camera ", "flash Camera");
            if(flash_active){
                flash_active = false;
                flash.setImageResource(R.drawable.flashoff);
                Camera.Parameters p = mCamera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(p);
            }
            else{
                flash_active = true;
                flash.setImageResource(R.drawable.flash);
                Camera.Parameters p = mCamera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
            }
        }
    };

    View.OnClickListener captureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("Camera ", "capture Camera");
            // get an image from the camera
            try {
                File fichier = new File(getResources().getString(R.string.sdcard_photo));
                stream2 = new FileOutputStream(fichier);
                mCamera.takePicture(null, null, mPicture);
                photo_taken = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener quitterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("Camera ", "quitter Camera");
            releaseCamera();
            Intent intent = new Intent(CameraActivity.this, TimeLine.class);
            startActivity(intent);
            finish();
        }
    };

    View.OnClickListener swiftListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if (camera_back) {
                if (ID_FRONT_CAMERA != -1) {
                    swift.animate().setDuration(1000).rotationY(360).start();
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = Camera.open(ID_FRONT_CAMERA);
                    try {
                        mCamera.setPreviewTexture(texture.getSurfaceTexture());
                    } catch (IOException e) {
                        System.out.println("Err set texture : " + e.getMessage());
                    }
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                    camera_back = false;
                } else
                    Toast.makeText(CameraActivity.this, "La camera avant n'a pas pu être trouvé!", Toast.LENGTH_LONG).show();
            } else {
                if (ID_BACK_CAMERA != -1) {
                    swift.animate().setDuration(1000).rotationY(360).start();
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = Camera.open(ID_BACK_CAMERA);
                    try {
                        mCamera.setPreviewTexture(texture.getSurfaceTexture());
                    } catch (IOException e) {
                        System.out.println("Err set texture : " + e.getMessage());
                    }
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                    camera_back = true;
                } else
                    Toast.makeText(CameraActivity.this, "La camera arri�re n'a pas pu �tre trouv�!", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onResume() {
        Log.d("Camera ", "onResume Camera");
        super.onPause();
        if (mCamera == null){
            mCamera = Camera.open();

            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewTexture(texture.getSurfaceTexture());
            } catch (IOException e) {
                System.out.println("Err : "+e.getMessage());
            }
            Camera.Parameters p = mCamera.getParameters();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(p);

            if(!photo_taken){
                mCamera.startPreview();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        Log.d("Camera ", "onSurfaceTextureAvailable Camera");
        thread = new Thread(new ActiveCam(surfaceTexture, i, i2));
        thread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.d("Camera ", "onSurfaceTextureDestroyed Camera");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    class ActiveCam implements Runnable{
        private SurfaceTexture surface;
        private int width;
        private int height;

        public ActiveCam(SurfaceTexture surface, int width, int height){
            Log.d("Camera ", "ActiveCam class Camera");
            this.surface = surface;
            this.width = width;
            this.height = height;
        }

        public void run(){
            Log.d("Camera ", "ActiveCam class run Camera");
            if(mCamera==null)
                mCamera = Camera.open();
            try {
                Camera.Parameters p = mCamera.getParameters();
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                System.out.println("w - h : "+width+" - "+height);
                mCamera.setParameters(p);
                mCamera.setPreviewTexture(surface);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();


            } catch (IOException ioe) {
                System.out.println("Err : "+ioe.getMessage());
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("Camera ", "onPictureTaken class Camera");
            File pictureFile;
            FileOutputStream out = null;
            FileOutputStream stream = null;

            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("Error creating media file, check storage permissions:", "erreur");
                return;
            }
            try {
                out = new FileOutputStream(pictureFile);
                InputStream in = new ByteArrayInputStream(data);

                byte buf[] = new byte[1024];
                int n=0;

                while((n=in.read(buf))!=-1){
                    out.write(buf,0,n);
                }
                out.close();
                in.close();

            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }

            Bitmap bitmap = decodeSampledBitmapFromFile(getResources().getString(R.string.sdcard_photo), 500, 300);
            Matrix matrix = new Matrix();

            if(camera_back)
                matrix.postRotate(90);
            else
                matrix.postRotate(270);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            bitmap.recycle();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            rotatedBitmap.recycle();

            try {
                stream2.close();
            } catch (IOException e) {
                System.out.println("Err : "+e.getMessage());
            }

            releaseCamera();
            Intent intent = new Intent(CameraActivity.this, Editer.class);
            startActivity(intent);

        }
    };

    // BITMAP FROM FILE

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

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        Log.d("Camera ", "getOutputMediaFile class Camera");
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            String file = getResources().getString(R.string.sdcard_photo);
            mediaFile = new File(file);
        } else if(type == MEDIA_TYPE_VIDEO) {
            String file = getResources().getString(R.string.sdcard_video);
            mediaFile = new File(file);
        } else {
            return null;
        }
        return mediaFile;
    }

    private int indexOfCamera(int index){
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int n = Camera.getNumberOfCameras();
        for (int i=0; i< n; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == index) {
                return i;
            }
        }
        return -1;
    }


}
