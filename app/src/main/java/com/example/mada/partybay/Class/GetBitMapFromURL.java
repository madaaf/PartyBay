package com.example.mada.partybay.Class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mada on 21/01/15.
 */
public class GetBitMapFromURL extends AsyncTask<String, Integer, String> {

    byte[] tempByte;
    private Bitmap bmap;


    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String stringUrl = params[0];
        //bmap = null;
        try
        {
            URL url = new URL(stringUrl);
            InputStream is = (InputStream) url.getContent();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1)
            {
                output.write(buffer, 0, bytesRead);
            }
            tempByte = output.toByteArray();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        return "Success";
    }


    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        Bitmap tempBitMap = BitmapFactory.decodeByteArray(tempByte, 0, tempByte.length);
        //Log.d("Bitmap bmap value on PostExecute", "bmap="+bmap);
        setBitMap(tempBitMap);
        //imageView.setImageBitmap(bImg);

    }
    void setBitMap(Bitmap bitMapSet)
    {
        this.bmap  = bitMapSet;
        //Log.d("Bitmap bmap value", "bmap="+bmap);
    }
    Bitmap returnBitmap()
    {
        //Log.d("Bitmap bmap value", "bmap="+bmap);
        return bmap;
    }

}
