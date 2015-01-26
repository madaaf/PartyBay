package fr.partybay.android.Google;

import android.app.ActionBar;
import android.app.Activity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.partybay.android.R;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);
        ActionBar bar = this.getActionBar();
        bar.hide();

        //googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapView)).getMap();
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
            while(continuer){
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
        String url = "http://interclass.livehost.fr/Track/track.php";
        List<Track> tracks = new ArrayList<Track>();

        Track track = new Track();
        track.setNom("mada");
        track.setLatitude(Double.valueOf("48.8489473"));//vaneau
        track.setLongitude(Double.valueOf("2.3214516"));
        tracks.add(track);
        Track tracker = new Track();
        tracker.setNom("amoros");
        tracker.setLatitude(Double.valueOf("48.8523221"));//ECE
        tracker.setLongitude(Double.valueOf("2.2857270"));
        tracks.add(tracker);

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


    @Override
    public void onResume(){
        continuer=true;
        thread = new Thread(new Recherche());
        thread.start();
        super.onResume();
    }

    public void zoomMarker(int pos){
        Marker marker = markers.get(pos);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
    }

}
