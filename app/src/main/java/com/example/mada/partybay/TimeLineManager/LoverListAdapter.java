package com.example.mada.partybay.TimeLineManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 15/01/15.
 */
public class LoverListAdapter extends ArrayAdapter<Love> {

    private Context context;
    private ArrayList<Love> lovers = new ArrayList<Love>();


    //A ViewHolder object stores each of the component views inside the tag field of the Layout,
    // so you can immediately access them without the need to look them up repeatedly.
    static class ViewHolder{
        TextView user_id;
        TextView pseudo;
        ImageView picture;

    }


    public LoverListAdapter(Context context, int layoutResourceId, ArrayList<Love> lovers) {
        super(context,layoutResourceId,layoutResourceId, lovers);
        this.lovers = lovers;
        this.context = context;
       // System.out.println("DANS L4ADAPTEUR "+ lovers.size());
    }





    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //String idPost = String.valueOf(convertView.getTag());

       // System.out.println("je suis dans le getView "+lovers.get(position).getPseudo());
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trackers_item, parent, false);

            // initialize the view holder
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.tracker_item_pseudo);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.tracker_item_picture);

            convertView.setTag(viewHolder);

        }else{
            //viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        // Populate the data into the template view using the data object
        holder.pseudo.setText(lovers.get(position).getPseudo());
        UrlImageViewHelper.setUrlDrawable(holder.picture, "https://static.partybay.fr/images/posts/640x640_" + lovers.get(position).getPicture());

        return convertView;
    }

}
