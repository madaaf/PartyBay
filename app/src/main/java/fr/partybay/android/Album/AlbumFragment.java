package fr.partybay.android.Album;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.ProfileManager.ProfileViewPagerActivity;
import fr.partybay.android.R;
import fr.partybay.android.TimeLineManager.LoversListActivity;
import fr.partybay.android.Class.Post;

/*
 * Created by mada on 20/10/2014.
 */
public class AlbumFragment extends Fragment {
    private String item_id;
    private String id_user;
    private String my_id;
    private int mCurrentPage;
    private ArrayList<Integer> tabId = new ArrayList<Integer>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post  = null;
    private SerializeurMono<User> serializeurUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serializeurUser = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        User user = serializeurUser.getObject();
        my_id = user.getId();

        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getInt("current_page", 0);
        item_id = data.getString("item_id","null");
        id_user = data.getString("my_id","null");


        //récupere information du post sur l'api
        RestClient client = new RestClient(getActivity(),"https://api.partybay.fr/users/"+id_user+"/posts/"+item_id);
        System.out.println("https://api.partybay.fr/users/"+id_user+"/posts/"+item_id);
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();
        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";

        try {
            rep = client.Execute("GET");
            System.out.println(rep);
            JSONObject obj = new JSONObject(rep);
            post = new Post(getActivity(),obj);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.albumfragment, container,false);
        TextView user_pseudo = (TextView)v.findViewById(R.id.item_album_post_pseudo);
        TextView date = (TextView)v.findViewById(R.id.item_album_post_time);
        TextView texte = (TextView) v.findViewById(R.id.item_album_post_texte);
        final TextView nbr_liker =(TextView)v.findViewById(R.id.item_album_post_like);
        ImageView font = (ImageView)v.findViewById(R.id.item_album_post_photo_fond);
        final ImageView coeur = (ImageView)v.findViewById(R.id.item_album_post_coeur);
        final TextView listLovers = (TextView) v.findViewById(R.id.item_album_spinnerLovers);
        ImageView selfie = (ImageView)v.findViewById(R.id.item_album_selfie);

        Boolean PostIsLoved = post.getPostIsLoved();
        if(PostIsLoved==true){
            coeur.setImageResource(R.drawable.coeur);
        }else{
            coeur.setImageResource(R.drawable.coeur_unlike);
        }

        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileViewPagerActivity.class);
                i.putExtra("user_id", post.getUser_id());
                startActivity(i);

            }
        });

        listLovers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoLove = post.getId()+"/"+post.getUser_id()+"/"+my_id;
                System.out.println("INFOLOVE dans album fragment"+infoLove);
                Intent i = new Intent(v.getContext(),LoversListActivity.class);
                i.putExtra("infoLove",infoLove);
                startActivity(i);
            }
        });

        coeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //idPost = String.valueOf(view.getTag());
                // Envoie une requete a l'API pour le prévenir que j'ai liké
                Thread threadLove = new LoveThread(v.getContext());
                threadLove.start();

                if(post.getPostIsLoved()==true){
                    coeur.setImageResource(R.drawable.coeur_unlike);
                    post.setPostIsLoved(false);
                    int nbrLoversNew = (post.getTotalLovers())-1;
                    String ok = String.valueOf(nbrLoversNew);
                    post.setTotalLovers(nbrLoversNew);
                    nbr_liker.setText(ok);
                }else{
                    coeur.setImageResource(R.drawable.coeur);
                    post.setPostIsLoved(true);
                    int nbrLoversNew = (post.getTotalLovers())+1;
                    String ok = String.valueOf(nbrLoversNew);
                    post.setTotalLovers(nbrLoversNew);
                    nbr_liker.setText(ok);
                }


            }
        });


        user_pseudo.setText(post.getUser_pseudo());
        date.setText(post.getDate());
        texte.setText(post.getText());
        String totalLover = String.valueOf(post.getTotalLovers());
        nbr_liker.setText(totalLover);

        UrlImageViewHelper.setUrlDrawable(font, "https://static.partybay.fr/images/posts/640x640_" + post.getLink());


        if(post.getUser_picture()!=null){
            UrlImageViewHelper.setUrlDrawable(selfie, "https://static.partybay.fr/images/users/profile/160x160_" + post.getUser_picture());

        }


       // String url = "https://static.partybay.fr/images/posts/640x640_" + post.getLink();
        //Ion.with(font).placeholder(R.drawable.photo_profil).error(R.drawable.photo_profil).load(url);

        return v;

    }



    class LoveThread extends Thread {
        private Context context;

        public LoveThread(Context context){
            this.context=context;
        }
        public void run() {
            // je préviens l'api que j'ai liké/unliké
            RestClient client = new RestClient(context,"https://api.partybay.fr/users/" + my_id + "/love/" + item_id);
            System.out.println("BLOOM "+ "https://api.partybay.fr/users/" + my_id + "/love/" + item_id);
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

}
