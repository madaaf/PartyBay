package com.example.mada.partybay.ProfileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.Post;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 12/01/15.
 */
public class MomentsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Post> posts;

    static class ViewHolder{
        TextView user_pseudo;
        TextView text;
        TextView lovers;
        TextView date;
        TextView latitude;
        ImageView link;
        ImageButton loveButton;
    }


    public MomentsAdapter(Context c, ArrayList<Post> posts) {
         this.mContext=c;
         this.posts=posts;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {  // if it's not recycled, initialize some attributes

            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.photo, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.link = (ImageView) convertView.findViewById(R.id.photo_moment);

            convertView.setTag(viewHolder);

        } else {
            //((ViewHolder)convertView.getTag()).loveButton.setTag(posts.get(position).getId());
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Post item = posts.get(position);

        //viewHolder.link.setImageDrawable(R.drawable.photo_fond);
        UrlImageViewHelper.setUrlDrawable(viewHolder.link, "https://static.partybay.fr/images/posts/640x640_" + item.getLink());


        return convertView;
    }


    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
