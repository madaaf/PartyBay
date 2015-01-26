package fr.partybay.android.ProfileManager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import fr.partybay.android.Class.Love;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.R;

/**
 * Created by mada on 20/01/15.
 */
public class TrackersAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Love> trackers;
    private String user_id;
    private SerializeurMono<User> serializeurUser ;
    private String my_user_id;

    static class ViewHolder{
        TextView pseudo;
        ImageView picture;
        ImageView trackButton;
        RelativeLayout intent_user_profile;
    }



    public TrackersAdapter(Context context, ArrayList<Love> trackers){
        this.context = context;
        this.trackers = trackers;

        serializeurUser = new SerializeurMono<User>(context.getResources().getString(R.string.sdcard_user));
        User user = serializeurUser.getObject();
        my_user_id = user.getId();


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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.trackers_item,parent,false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.tracker_item_picture);
            viewHolder.pseudo = (TextView)convertView.findViewById(R.id.tracker_item_pseudo);
            viewHolder.trackButton = (ImageView)convertView.findViewById(R.id.tracker_item_button_track);
            viewHolder.intent_user_profile =(RelativeLayout)convertView.findViewById(R.id.tracker_profile_user);


            convertView.setTag(viewHolder);
            viewHolder.intent_user_profile.setTag(trackers.get(position).getUser_id());
            viewHolder.trackButton.setTag(trackers.get(position).getDoubleTrack()+"/"+trackers.get(position).getUser_id());

        }else{
            ((ViewHolder)convertView.getTag()).intent_user_profile.setTag(trackers.get(position).getUser_id());
            ((ViewHolder)convertView.getTag()).trackButton.setTag(trackers.get(position).getDoubleTrack()+"/"+trackers.get(position).getUser_id());

        }



        final ViewHolder holder = (ViewHolder) convertView.getTag();
        // Populate the data into the template view using the data object
        holder.intent_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = String.valueOf(v.getTag());
                Intent i = new Intent (context, ProfileViewPagerActivity.class);
                i.putExtra("user_id",user_id);
                context.startActivity(i);
            }
        });


        holder.pseudo.setText(trackers.get(position).getPseudo());

        if(trackers.get(position).getPicture().equals("null")||trackers.get(position).getPicture().equals("")){
            holder.picture.setImageResource(R.drawable.post);
        }else{
            UrlImageViewHelper.setUrlDrawable(holder.picture, "https://static.partybay.fr/images/users/profile/160x160_" + trackers.get(position).getPicture());
        }


        if(trackers.get(position).getDoubleTrack()==true){
            holder.trackButton.setImageResource(R.drawable.button_track);
        }else{
            holder.trackButton.setImageResource(R.drawable.button_untrack);
        }

        holder.trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = String.valueOf(v.getTag());

                int index = info.indexOf('/');
                String doubleTrack =info.substring(0,index) ;
                String id_user=info.substring(index+1,info.length());

                if(trackers.get(position).getDoubleTrack()==true){
                   holder.trackButton.setImageResource(R.drawable.button_untrack);
                   trackers.get(position).setDoubleTrack(false);
               }else{
                   holder.trackButton.setImageResource(R.drawable.button_track);
                    trackers.get(position).setDoubleTrack(true);
               }

                Thread threadTrack = new ThreadTrack(context,id_user);
                threadTrack.start();

            }
        });


        return convertView;
    }


    class ThreadTrack extends Thread {
        private Context context;
        private String user_tracker_id;

        public ThreadTrack(Context context, String user_tracker_id){
            this.context=context;
            this.user_tracker_id= user_tracker_id;
        }
        @Override
        public void run() {
            System.out.println("TRACKERS RUN "+"https://api.partybay.fr/users/"+my_user_id+"/track/"+user_tracker_id);
            RestClient client  = new RestClient(context,"https://api.partybay.fr/users/"+my_user_id+"/track/"+user_tracker_id);
            String access_token = client.getTokenValid();
            client.AddHeader("Authorization","Bearer "+access_token);
            String rep = "";
            try {
               rep =  client.Execute("POST");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("TRACKERS "+rep);

        }
    }

}
