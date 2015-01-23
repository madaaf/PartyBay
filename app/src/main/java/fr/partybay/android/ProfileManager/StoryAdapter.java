package fr.partybay.android.ProfileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.partybay.android.R;
import fr.partybay.android.TimeLineManager.Post;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 13/01/15.
 */
public class StoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Post> posts;

    static class ViewHolder{
        TextView user_pseudo;
        TextView text;
        TextView lovers;
        TextView date;
        TextView latitude;
        ImageView link;
        TextView nbr_lovers;
        ImageButton loveButton;
    }


    public StoryAdapter(Context c, ArrayList<Post> posts) {
        System.out.println("jes suis dans l'adapteur ");
        this.mContext=c;
        this.posts=posts;
    }




    @Override
    public int getCount() { return posts.size();}

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {  // if it's not recycled, initialize some attributes

            // inflate the listview item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.story_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.link = (ImageView) convertView.findViewById(R.id.story_image_soiree);
            viewHolder.text = (TextView) convertView.findViewById(R.id.story_text);
            viewHolder.latitude = (TextView) convertView.findViewById(R.id.story_lieu);
            viewHolder.date = (TextView) convertView.findViewById(R.id.story_time);
            viewHolder.nbr_lovers =(TextView)convertView.findViewById(R.id.story_nbr_lover);


            convertView.setTag(viewHolder);

        } else {
            //((ViewHolder)convertView.getTag()).loveButton.setTag(posts.get(position).getId());
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Post item = posts.get(position);
        // Populate the data into the template view using the data object

        //viewHolder.latitude.setText(item.getLatitude());
        //viewHolder.text.setText(item.getText());
        viewHolder.date.setText(item.getDate());
        viewHolder.nbr_lovers.setText(String.valueOf(item.getTotalLovers()));


        UrlImageViewHelper.setUrlDrawable(viewHolder.link, "https://static.partybay.fr/images/posts/640x640_" + item.getLink());

       // String url = "https://static.partybay.fr/images/posts/640x640_" + item.getLink();
       // Ion.with(viewHolder.link).placeholder(R.drawable.photo_profil).error(R.drawable.photo_profil).load(url);

        return convertView;
    }



}
