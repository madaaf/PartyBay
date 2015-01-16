package com.example.mada.partybay.TimeLineManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Class.RestClient;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by mada on 05/11/2014.
 */

public class PostAdapter extends ArrayAdapter<Post>  {

    private ArrayList<Post> posts = new ArrayList<Post>();
    private Thread threadLove = null;
    private String idPost;
    private SerializeurMono<User> serializeur ;
    private String myUser_id;
    private Context context;

    //A ViewHolder object stores each of the component views inside the tag field of the Layout,
    // so you can immediately access them without the need to look them up repeatedly.
    static class ViewHolder{
        TextView user_pseudo;
        TextView text;
        TextView lovers;
        TextView date;
        TextView latitude;
        ImageView link;
        ImageButton loveButton;
        ImageView loversList;
    }

    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> posts) {
        super(context,layoutResourceId, posts);
        this.posts = posts;
        this.context=context;
        Log.d("layoutResourceId ", String.valueOf(layoutResourceId));
        Log.d("posts in post adapter  ", String.valueOf(posts));
    }




    @Override
    public void add(Post post){
        super.add(post);
        posts.add(post);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        serializeur = new SerializeurMono<User>("/storage/sdcard0/PartyBay2/user.serial");
        User user = serializeur.getObject();
        myUser_id = user.getId();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.user_pseudo = (TextView) convertView.findViewById(R.id.post_pseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.post_texte);
            viewHolder.lovers = (TextView) convertView.findViewById(R.id.post_like);
            viewHolder.date = (TextView) convertView.findViewById(R.id.post_time);
            viewHolder.latitude = (TextView) convertView.findViewById(R.id.post_lieu);
            viewHolder.link = (ImageView) convertView.findViewById(R.id.post_photo_fond);
            viewHolder.loveButton = (ImageButton) convertView.findViewById(R.id.post_coeur);
            viewHolder.loversList = (ImageView) convertView.findViewById(R.id.spinnerLovers);

            viewHolder.loversList.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String infoLove = String.valueOf(v.getTag());
                    //String user_idPost = String.valueOf(posts.get(position).getUser_id());
                   // ArrayList<Love> tabLoveLovers = posts.get(position).getTabLoverLovers();
                    //ArrayList<String> tabStringLovers = posts.get(position).getTabStringLovers();

                   // System.out.println("position  "+ position + " id post "+idPost);
                   // System.out.println("dans le postAdapter "+tabStringLovers.toString());
                    //System.out.println("dans le postAdapter taille "+tabLoveLovers.size());
                   // System.out.println("loveListListener position"+position+ " id post "+idPost);

                    Intent i = new Intent(context,LoversListActivity.class);
                    i.putExtra("infoLove",infoLove);
                    context.startActivity(i);
                }
             });

            viewHolder.loveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(" id post "+view.getTag());
                    System.out.println("posts.get(position).getId() "+ posts.get(position).getId());
                    System.out.println("posts.get(position).getPostIsLoved() "+ posts.get(position).getPostIsLoved());

                    idPost = String.valueOf(view.getTag());
                    // Envoie une requete a l'API pour le prévenir que j'ai liké
                    threadLove = new LoveThread(view.getContext());
                    threadLove.start();
                    int test;
                    // Colorie le coeur en rouge si j'ai liké et blanc sinon
                    // rafrechie le chiffre qui indique le nombre de like
                    if(posts.get(position).getPostIsLoved()==true){
                        viewHolder.loveButton.setImageResource(R.drawable.coeur_unlike);
                        posts.get(position).setPostIsLoved(false);
                        test = (posts.get(position).getTotalLovers())-1;
                        String ok = String.valueOf(test);
                        posts.get(position).setTotalLovers(test);
                        viewHolder.lovers.setText(ok);

                    }else{
                        viewHolder.loveButton.setImageResource(R.drawable.coeur);
                        posts.get(position).setPostIsLoved(true);
                        test = (posts.get(position).getTotalLovers())+1;
                        String ok = String.valueOf(test);
                        posts.get(position).setTotalLovers(test);
                        viewHolder.lovers.setText(ok);
                    }
                 }

                class LoveThread extends Thread {
                    private Context context;

                    public LoveThread(Context context){
                        this.context=context;
                    }
                    public void run() {
                        // je préviens l'api que j'ai liké/unliké
                        RestClient client = new RestClient(context,"https://api.partybay.fr/users/" + myUser_id + "/love/" + idPost);
                        System.out.println("https://api.partybay.fr/users/1/love/" + idPost);
                        String access_token = client.getTokenValid();
                        client.AddHeader("Authorization", "Bearer " + access_token);
                        String rep = "";
                        try {
                            rep = client.Execute("POST");
                            System.out.println("REPONSE DU LOVE" + rep);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
             });

            convertView.setTag(viewHolder);
            viewHolder.loveButton.setTag(posts.get(position).getId());
            viewHolder.lovers.setTag(posts.get(position).getId());
            viewHolder.loversList.setTag(posts.get(position).getId() + "/"+posts.get(position).getUser_id());

        }else{
            ((ViewHolder)convertView.getTag()).loveButton.setTag(posts.get(position).getId());
            ((ViewHolder)convertView.getTag()).lovers.setTag(posts.get(position).getId());
            ((ViewHolder)convertView.getTag()).loversList.setTag(posts.get(position).getId() + "/"+posts.get(position).getUser_id());
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        // Populate the data into the template view using the data object
        holder.user_pseudo.setText(posts.get(position).getUser_pseudo());
        holder.text.setText(posts.get(position).getText()+" position "+position);
        holder.lovers.setText(String.valueOf(posts.get(position).getTotalLovers()));
        holder.date.setText(posts.get(position).getDate());
        holder.latitude.setText(posts.get(position).getLatitude());
        UrlImageViewHelper.setUrlDrawable(holder.link, "https://static.partybay.fr/images/posts/640x640_" + posts.get(position).getLink());

        if( posts.get(position)!=null) {
            String Sid =  posts.get(position).getId();
            if (Sid != null) {
                int i = Integer.parseInt( posts.get(position).getId());
                    if (i > 2 &&  posts.get(position).getId() != null) {
                    // System.out.println("le poste numero "+posts.get(position).getId()+" a une valeur de "+posts.get(position).getPostIsLoved()) ;
                    ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                    // je colorie les coeur en rouge si je les ai déjà liké
                    if(posts.get(position).getPostIsLoved()==true){
                        viewHolder.loveButton.setImageResource(R.drawable.coeur);
                    }else{
                        viewHolder.loveButton.setImageResource(R.drawable.coeur_unlike);
                    }
                }
            }
        }

        return convertView;

    }
}