package fr.partybay.android.Google;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import fr.partybay.android.Class.Internet;
import fr.partybay.android.Class.RestClient;
import fr.partybay.android.R;
import fr.partybay.android.Class.Post;


/**
 * Created by mada on 22/01/15.
 */
public class Google extends Activity {
    /** Local variables **/
    GoogleMap googleMap;
    private Thread thread;
    private List<Marker> markers = new ArrayList<Marker>();
    private List<String> notifs = new ArrayList<String>();
    private boolean continuer=true;
    private final LatLng LOCATION_SURRREY = new LatLng(48.8456853,2.3109669); // breteuil
    private threadGetPost threadGetPost;
    private Internet internet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("je suis dans GOOGLE");
        internet  = new Internet(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);
        ActionBar bar = this.getActionBar();
        bar.hide();

        googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapView)).getMap();
        createMapView();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_SURRREY, 13);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(update);
        googleMap.setMyLocationEnabled(true);

        thread = new Thread(new Recherche());
        thread.start();



    }

    class Recherche implements Runnable{
        public void run(){
            final List<Track> liste = getTracks();

            runOnUiThread(new Runnable(){
                public void run(){
                    addTracksToMap(liste);
                }
            });

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Err thread sleep : "+e.getMessage());
            }
         }

    }

    public void addTracksToMap(List<Track> tracks){
        for (int i=0 ; i<tracks.size() ; i++){
            addMarker(tracks.get(i).getNom(), tracks.get(i).getLatitude(), tracks.get(i).getLongitude());
        }
    }

    public void addMarker(String titre, Double lat, Double lng){
        //removeOldMarker(titre);
        if(lat>0.0 && lng>0.0){
            MarkerOptions options = new MarkerOptions();
            options.title(titre);
            options.snippet(adresse(lat, lng));
            options.position(new LatLng(lat, lng));
            Marker marker = googleMap.addMarker(options);
            markers.add(marker);
        }
       /* else if(lat<0.0 && lng<0.0){
            Random r = new Random();
            int n = r.nextInt(10000);
            GenererNotification(titre+" a désactive son service de localisation.", n);
            notifs.add(titre);
            updateTrack(titre, 0.0, 0.0);
        }*/
    }

    public String adresse(double latitude, double longitude)
    {
        String addressString = null;
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses  = gc.getFromLocation(latitude, longitude, 1);
            if (addresses.size() == 1) {
                Address address = addresses.get(0);
                addressString = address.getAddressLine(0) + " " + address.getPostalCode() + " " + address.getLocality();
            }
        } catch (IOException e) {
            System.out.println("Erreur adresse : "+e.getMessage());
        }
        return addressString;
    }


    public List<Track> getTracks(){

        threadGetPost = new threadGetPost(this);
        threadGetPost.start();

        try {
            threadGetPost.join();
        } catch (InterruptedException e) {
            Log.d("Erreur ", "join : " + e.getMessage());
        }

        List<Track> tracks = new ArrayList<Track>();

       /* Double ok = 48.8456853;
        Double okok =2.3109669;
        Track track = new Track("mada",ok,okok);*/

        //tracks.add(track);

        tracks = threadGetPost.getTracksTable();



        System.out.println("TRACKS google "+tracks);
        return tracks;
    }


    private void createMapView(){
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();


                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    class Track{
        private String nom;
        private Double latitude;
        private Double longitude;

        public Track(String nom, Double latitude,Double longitude){
            this.nom = nom;
            this.latitude= latitude;
            this.longitude  = longitude;
        }

        public String getNom() {
            return nom;
        }
        public void setNom(String nom) {
            this.nom = nom;
        }
        public Double getLatitude() {
            return latitude;
        }
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        public Double getLongitude() {
            return longitude;
        }
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

/*
    @Override
    public void onResume(){
        continuer=true;
        thread = new Thread(new Recherche());
        thread.start();
        super.onResume();
    }
*/
    public void zoomMarker(int pos){
        Marker marker = markers.get(pos);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
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
    class threadGetPost extends Thread{
        Context context;
        List<Track> tracks = new ArrayList<Track>();

        public threadGetPost(Context context){
            this.context=context;
        }

        public  List<Track> getTracksTable(){ return tracks; }

        public void run(){
            if(internet.internet()){
                // String url = "https://api.partybay.fr/users/150/posts";
                String url = "https://api.partybay.fr/posts";
                RestClient client = new RestClient(context,url);
                String access_token = client.getTokenValid();
                client.AddHeader("Authorization","Bearer "+access_token);

                String rep = "";
                try {
                    rep = client.Execute("GET");

                    if (rep!=null && rep.length()>2){
                        // System.out.println("je suis ici encore");
                        ArrayList<String> stringArray = jsonStringToArray(rep);
                        Iterator<String> it = stringArray.iterator();
                        Post post = null;
                        while (it.hasNext()) {
                            String s = it.next();
                            // System.out.println("js : "+s.startsWith("["));
                            // if(s.startsWith("[")){}
                            JSONObject obj = new JSONObject(s);
                            post = new Post(context,obj);
                            if(post!=null){

                                String nom =post.getUser_pseudo();
                                Double latitude = Double.valueOf(post.getLatitude());
                                Double longitude = Double.valueOf(post.getLongitude());
                                Track track = new Track(nom,latitude,longitude);
                                tracks.add(track);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

            }

        }
    }


}
