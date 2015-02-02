package fr.partybay.android.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by mada on 02/02/15.
 */
public class PopupActivity extends Activity {
    Context context;

    public PopupActivity(Context context){
        this.context = context;
    }

    public void afficherPopup(final String message, final android.content.DialogInterface.OnClickListener listener){
        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder popup = new AlertDialog.Builder(context);
                popup.setMessage(message);
                popup.setPositiveButton("Ok", listener);
                popup.show();
            }
        });
    }

}
