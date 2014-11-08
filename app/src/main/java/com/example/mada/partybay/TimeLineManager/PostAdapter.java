package com.example.mada.partybay.TimeLineManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 05/11/2014.
 */

public class PostAdapter extends ArrayAdapter<Post> {


    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context,R.layout.post, posts);
    }


    // View lookup cache
    // ViewHolder allow To improve performance which speeds up the population of the ListView considerably by caching view lookups for smoother
    private static class ViewHolder {
        TextView user_pseudo;
        TextView text;
        TextView lovers;
        TextView date;
        TextView latitude;
        ImageView link;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Post post = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post, parent, false);

            viewHolder.user_pseudo =(TextView) convertView.findViewById(R.id.post_pseudo);
            viewHolder.text = (TextView)convertView.findViewById(R.id.post_texte);
            viewHolder.lovers = (TextView)convertView.findViewById(R.id.post_like);
            viewHolder.date = (TextView)convertView.findViewById(R.id.post_time);
            viewHolder.latitude = (TextView)convertView.findViewById(R.id.post_lieu);
            viewHolder.link = (ImageView)convertView.findViewById(R.id.post_photo_fond);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.user_pseudo.setText(post.getUser_pseudo());
        viewHolder.text.setText(post.getText());
        viewHolder.lovers.setText(post.getLovers());
        viewHolder.date.setText(post.getDate());
        viewHolder.latitude.setText(post.getLatitude());
        UrlImageViewHelper.setUrlDrawable(viewHolder.link, "https://static.partybay.fr/images/posts/640x640_"+post.getLink());

        // Return the completed view to render on screen
        return convertView;
    }





}