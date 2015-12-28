package fr.partybay.android.TimeLineManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.partybay.android.Activity.Chargement;
import fr.partybay.android.Class.CustomListView;
import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.PopupActivity;
import fr.partybay.android.Class.Post;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.R;

/**
 * Created by mada on 28/01/15.
 */
public class TrackTimeLineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout layout;
    private ListView listView;
    private PostAdapter adapter;
    private boolean onScroolStateChange = false;
    private Thread ThreadLoadPost;
    private ArrayList<Post> posts = null;
    private int nbr_scroll = 0 ;
    private final static int NBROFITEM = 15;
    private Context context;
    private PopupActivity popupActivity = null;
    private Internet internet = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        popupActivity = new PopupActivity(getActivity());
        internet = new Internet(getActivity());

        // on recupere les 10 premier postes
        posts = new ArrayList<Post>();
        try {
            String url = "https://api.partybay.fr/posts?limit="+NBROFITEM+"&offset="+0+"&side=desc&order=id";
            getPostFromApi(url,false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.timelinetrack, container, false);

        layout = (SwipeRefreshLayout)rootView.findViewById(R.id.tlt_swype);
        layout.setOnRefreshListener(this);
        layout.setColorScheme( R.color.swype_1, R.color.swype_2, R.color.swype_3, R.color.swype_4);

        listView = new CustomListView(context);
        listView = (ListView)rootView.findViewById(R.id.tlt_lvPost);



        // Create the adapter to convert the array to array to views
        adapter = new PostAdapter(getActivity(),R.id.tlt_lvPost,posts);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                onScroolStateChange = true;

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




        return rootView;
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

    public static void delete(File file) throws IOException {

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

            Intent i = new Intent(context,Chargement.class);
            startActivity(i);
            getActivity().finish();
        }
    }

    @Override
    public void onRefresh() {
        layout.setRefreshing(false);

        if(internet.internet()){
            layout.setRefreshing(true);
            //System.out.println("POSTACTIVITY RESFRESH" );
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
                        adapter = new PostAdapter(getActivity(),R.id.lvPost,posts);
                        listView.setAdapter(adapter);
                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                onScroolStateChange = true;
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
                    Toast.makeText(context, "Cool !", Toast.LENGTH_LONG).show();
                }

            }, 3000);
        }else{
            popupActivity.afficherPopup(getResources().getString(R.string.error_internet),new redirection());
        }


    }

    class redirection implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(getActivity(), TimeLine.class);
            startActivity(intent);
            getActivity().finish();

        }
    }

    public void getPostFromApi(String urlapi, Boolean addTop) throws Exception {

        if(internet.internet()){
            RestClient client = new RestClient(context,urlapi);
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
                        post = new Post(context,obj);;
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

        }else{
            //upActivity.afficherPopup(getResources().getString(R.string.error_internet),null);
        }
    }

}
