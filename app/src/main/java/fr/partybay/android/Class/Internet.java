package fr.partybay.android.Class;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mada on 02/02/15.
 */
public class Internet extends Activity{
    Context context = null;
    public Internet (Context context){
        this.context = context;
    }

    public boolean internet() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}
