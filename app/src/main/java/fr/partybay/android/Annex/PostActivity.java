package fr.partybay.android.Annex;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.Activity.CameraAc;
import fr.partybay.android.Activity.Chargement;
import fr.partybay.android.Class.CustomListView;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.Class.SerializeurMono;
import fr.partybay.android.Class.User;
import fr.partybay.android.Google.Google;
import fr.partybay.android.ProfileManager.ProfileViewPagerActivity;
import fr.partybay.android.R;
import fr.partybay.android.Class.Post;
import fr.partybay.android.TimeLineManager.PostAdapter;

// import android.widget.LinearLayout.LayoutParams;




/**
 * Created by mada on 05/11/2014.
 */
public class PostActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private ImageButton menu = null;
    private ImageButton profile = null;
    private ImageButton moment = null;
    private SwipeRefreshLayout layout;
    private ListView listView;
    private PostAdapter adapter;
    private boolean onScroolStateChange = false;
    private Thread ThreadLoadPost;
    private ArrayList<Post> posts = null;
    private Typeface font;
    private TextView entete;
    private int nbr_scroll = 0 ;
    private final static int NBROFITEM = 5;
    private SerializeurMono<User> serializeur_user;
    private String my_user_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_test);
        ActionBar bar = this.getActionBar();
        bar.hide();

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        User user = serializeur_user.getObject();
        my_user_id = user.getId();



        layout = (SwipeRefreshLayout) findViewById(R.id.swype);
        layout.setOnRefreshListener(this);
        layout.setColorScheme( R.color.swype_1, R.color.swype_2, R.color.swype_3, R.color.swype_4);

        listView = new CustomListView(this);
        font = Typeface.createFromAsset(getAssets(), "fonts/havana.otf");
        menu = (ImageButton) findViewById(R.id.reglage);
        profile = (ImageButton) findViewById(R.id.profile);
        moment = (ImageButton) findViewById(R.id.moment);
        listView = (ListView) findViewById(R.id.lvPost);
        entete = (TextView)findViewById(R.id.entete);
        entete.setTypeface(font);

        entete.setOnClickListener(enteteListener);
        menu.setOnClickListener(reglageListener);
        moment.setOnClickListener(momentListener);
        profile.setOnClickListener(profileListener);



        posts = new ArrayList<Post>();



        // on recupere les 10 premier postes
        try {
            String url = "https://api.partybay.fr/posts?limit="+NBROFITEM+"&offset="+0+"&side=desc&order=id";
            getPostFromApi(url,false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView v = new TextView(this);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(40);
        v.setHeight(500);
        listView.addHeaderView(v);
       // listView.addParallaxedHeaderView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostActivity.this,Google.class);
                startActivity(i);
            }
        });


        SeekBar seekBar = new SeekBar(this);
        seekBar.setMinimumHeight(150);
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(70);
        thumb.setIntrinsicWidth(70);
        thumb.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        seekBar.setThumb(thumb);
       //listView.addParallaxedHeaderView(seekBar);
        listView.addHeaderView(seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                System.out.println(".....111......."+arg0);

            }

            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                System.out.println(".....222......."+arg0);
            }

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
                System.out.println(".....333......."+arg0+ " " +arg1+" "+arg2);
            }
        });

       // Create the adapter to convert the array to array to views
        adapter = new PostAdapter(this,R.id.lvPost,posts);
        listView.setAdapter(adapter);



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                onScroolStateChange = true;
               /* System.out.println("onScrollStateChanged" );
                System.out.println("onScrollStateChanged view" +view);
                System.out.println("onScrollStateChanged scrollState" +scrollState);*/
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){

                int lastInScreen = firstVisibleItem + visibleItemCount;
                if(lastInScreen == (totalItemCount) && (onScroolStateChange==true)){
                    nbr_scroll ++;
                    try {

                        // on récupere les 10 post suivant car l'utilsateur a scroller jusqu'à la fin de la liste
                        System.out.println("je recupere "+nbr_scroll*NBROFITEM+" item ");
                        String url = "https://api.partybay.fr/posts?limit="+NBROFITEM+"&offset="+nbr_scroll*NBROFITEM+"&side=desc&order=id";
                        getPostFromApi(url,false);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onScroolStateChange = false;
                }

                if(firstVisibleItem ==0){
                    layout.setEnabled(true);
                }else{
                    layout.setEnabled(false);
                }
            }
        });

    }


    // get posts from api
    public void getPostFromApi(String urlapi, Boolean addTop) throws Exception {

        RestClient client = new RestClient(this,urlapi);
        // je recupere un token dans la sd carte
        String access_token = client.getTokenValid();

        client.AddHeader("Authorization","Bearer "+access_token);
        String rep = "";
        try {
            rep =  client.Execute("GET");

            try {
                JSONObject obj = new JSONObject(rep);
                if(obj.has("error")){deconnexion(); }

            } catch (JSONException e) {
                System.out.println("err getPostfromapi "+e.getMessage());

            }


            if (rep!=null && rep.length()>2){
                // System.out.println("je suis ici encore");
                ArrayList<String> stringArray = new ArrayList<String>();
                stringArray=jsonStringToArray(rep);

                Iterator<String> it = stringArray.iterator();
                Post post = null;
                while (it.hasNext()) {
                    String s = it.next();
                    // System.out.println("js : "+s.startsWith("["));
                    // if(s.startsWith("[")){}
                    JSONObject obj = new JSONObject(s);
                    post = new Post(this,obj);
                    if(post!=null){
                        if (addTop==true){
                            posts.add(0,post);
                        }else{
                            posts.add(post);
                        }

                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void delete(File file) throws IOException{

        if(file.isDirectory()){
            //directory is empty, then delete it
            if(file.list().length==0){
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());

            }else{
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public void  deconnexion(){
        File directory =new File(getResources().getString(R.string.sdcard_path));

        if(!directory.exists()){
            System.out.println("Directory does not exist.");
            System.exit(0);
        }else{
            try{

                delete(directory);

            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }

            Intent i = new Intent(PostActivity.this,Chargement.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {
        ArrayList<String> stringArray = new ArrayList<String>();
        if (jsonString!=null && jsonString.length()!=2){
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }

        }
        return stringArray;
    }

    @Override
    public void onRefresh() {
      //System.out.println("POSTACTIVITY RESFRESH" );
       layout.setRefreshing(true);

        // I create a handler to stop the refresh and show a message after 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(false);
                try {
                    // on récupere les 10 post suivant car l'utilsateur a scroller jusqu'à la fin de la liste
                    //posts.clear(); +posts.get(0).getId()
                    String lastPost = posts.get(0).getId();
                    int IlastPosst = Integer.parseInt(lastPost);
                    int nextPost = IlastPosst+1;
                    String url = "https://api.partybay.fr/posts?last="+nextPost+"&side=asc&order=id";
                    getPostFromApi(url, true);
                    adapter = new PostAdapter(PostActivity.this,R.id.lvPost,posts);
                    listView.setAdapter(adapter);
                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            onScroolStateChange = true;
                            /*
                            System.out.println("onScrollStateChanged" );
                            System.out.println("onScrollStateChanged view" +view);
                            System.out.println("onScrollStateChanged scrollState" +scrollState);*/
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
                            // System.out.println("LOAD" );
                            //System.out.println("LOAD  firstVisibleItem" + firstVisibleItem);
                            int lastInScreen = firstVisibleItem + visibleItemCount;
                            if(lastInScreen == (totalItemCount) && (onScroolStateChange==true)){
                                nbr_scroll ++;
                                try {

                                    // on récupere les 10 post suivant car l'utilsateur a scroller jusqu'à la fin de la liste
                                    System.out.println("je recupere "+nbr_scroll*NBROFITEM+" item ");
                                    String url = "https://api.partybay.fr/posts?limit="+NBROFITEM+"&offset="+nbr_scroll*NBROFITEM+"&side=desc&order=id";
                                    getPostFromApi(url,false);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                onScroolStateChange = false;
                            }

                            if(firstVisibleItem ==0){
                                //Log.d(" firstVisibleItem", "=0");
                                layout.setEnabled(true);
                            }else{
                                // Log.d(" firstVisibleItem", "!=0");
                                layout.setEnabled(false);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(PostActivity.this, "Cool !", Toast.LENGTH_LONG).show();
            }

        }, 3000);

    }

    View.OnClickListener reglageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, GestureFunActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }
    };

    View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, ProfileViewPagerActivity.class);
            intent.putExtra("user_id",my_user_id);
            startActivity(intent);
        }
    };

    View.OnClickListener momentListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PostActivity.this, CameraAc.class);
            startActivity(intent);
        }
    };

    View.OnClickListener enteteListener = new View.OnClickListener(){
        // revenir en haut de la liste
        @Override
        public void onClick(View v) {
            //listView.setSelection(0);
        }
    };




}