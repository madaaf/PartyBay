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
import java.util.TreeMap;

/**
 * Created by mada on 20/01/15.
 */
public class TrackersAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Love> trackers;
    private String user_id;
    private TreeMap<Integer, Love> trackedTree = new TreeMap<Integer, Love>();
    private ArrayList<Integer> doubleTrack = new ArrayList<Integer>();

    static class ViewHolder{
        TextView pseudo;
        ImageView picture;
        ImageButton trackButton;
    }

    public TrackersAdapter(Context context, ArrayList<Love> trackers, ArrayList<Integer> doubleTrack){
        this.context = context;
        this.trackers = trackers;
        this.doubleTrack=doubleTrack;

        System.out.println("CONSTRUCTEUR"+doubleTrack);

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
        if(doubleTrack.contains(item.getUser_id())){
            viewHolder.trackButton.setImageResource(R.drawable.button_track);
        }

        if(item.getPicture().equals("null")||item.getPicture().equals("")){
            viewHolder.picture.setImageResource(R.drawable.post);
        }else{
            UrlImageViewHelper.setUrlDrawable(viewHolder.picture, "https://static.partybay.fr/images/users/profile/160x160_" + item.getPicture());
            // String url =  "https://static.partybay.fr/images/users/profile/160x160_" + item.getPicture();
            //Ion.with(viewHolder.picture).placeholder(R.drawable.photo_profil).error(R.drawable.photo_profil).load(url);

        }


        return convertView;
    }




}
