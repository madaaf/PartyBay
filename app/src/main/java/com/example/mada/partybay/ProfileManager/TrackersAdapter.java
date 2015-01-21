package com.example.mada.partybay.ProfileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Class.Love;
import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 20/01/15.
 */
public class TrackersAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Love> trackers;

    static class ViewHolder{
        TextView pseudo;
        ImageView picture;
        ImageButton trackButton;
    }

    public TrackersAdapter(Context context, ArrayList<Love> trackers){
        this.context = context;
        this.trackers = trackers;
    }

    @Override
    public int getCount() {
        return trackers.size();
    }

    @Override
    public Object getItem(int position) {
        return trackers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.trackers_item,parent,false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.tracker_item_picture);
            viewHolder.pseudo = (TextView)convertView.findViewById(R.id.tracker_item_pseudo);
            viewHolder.trackButton = (ImageButton)convertView.findViewById(R.id.tracker_item_button_track);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // update the item view
        Love item = trackers.get(position);
        viewHolder.pseudo.setText(item.getPseudo());
        //viewHolder.link.setImageDrawable(R.drawable.photo_fond);

        if(item.getPicture().equals("null")){
            viewHolder.picture.setImageResource(R.drawable.post);
        }else{
            UrlImageViewHelper.setUrlDrawable(viewHolder.picture, "https://static.partybay.fr/images/users/profile/160x160_" + item.getPicture());
           // String url =  "https://static.partybay.fr/images/users/profile/160x160_" + item.getPicture();
            //Ion.with(viewHolder.picture).placeholder(R.drawable.photo_profil).error(R.drawable.photo_profil).load(url);

        }


        return convertView;
    }
}
