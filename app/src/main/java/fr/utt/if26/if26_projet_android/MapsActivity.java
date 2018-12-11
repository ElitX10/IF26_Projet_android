package fr.utt.if26.if26_projet_android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, View.OnClickListener, LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ImageView image_pika;
    private Button show_map_button;
    private Button center_map_button;

    private boolean isCameraLock = false;
    private LocationManager locationManager;

    private Location myLocation;

    private Dresseur currentDresseur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // init data :
        PokemonDAO pokemonDAO = new PokemonDAO(this);
        if (pokemonDAO.getAllPokemons().size() == 0){
            pokemonDAO.loadFirstGen();
        }
        DresseurDAO dresseurDAO = new DresseurDAO(this);
        SharedPreferences myPrefs= getSharedPreferences("dresseur", MODE_PRIVATE);
        int dresseur_id = myPrefs.getInt("dresseur_id", 0);
        currentDresseur = dresseurDAO.getDresseurById(dresseur_id);
        if(currentDresseur == null){
            deconnexion();
        }

        // init views :
        image_pika = findViewById(R.id.map_Image);
        show_map_button = findViewById(R.id.show_map_button);
        center_map_button = findViewById(R.id.lock_location_button);

        //check permisions :
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
//              Deuxieme connection et suivantes :
                showSnabar();
            }else {
//              Premiere demande de droirs
                askMapPermission();
            }
        }else {
            image_pika.setVisibility(View.INVISIBLE);
            show_map_button.setVisibility(View.INVISIBLE);
            center_map_button.setVisibility(View.VISIBLE);
        }
    }


    /**
     * affiche le popup pour accepter la localisation
     */
    private void askMapPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constant.MY_ACCESS_FINE_LOCATION);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            // création des pokestops
            createPokestops();

            // ajout de pokestop avec un long click sur la carte :
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(final LatLng latLng) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Pokestop"); //R.string.app_name todo @string
                    builder.setMessage("Voulez vous ajouter un Pokestop ?"); // todo @string
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent intent = new Intent(MapsActivity.this, Edit_Pokestop_Activity.class);
                            intent.putExtra("Lat", latLng.latitude);
                            intent.putExtra("Lng", latLng.longitude);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            // set location listener
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,5, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,5, this);

            // zoom to the current location :
            zoomOnCurrentLocation();

            // Set a listener for info window events.
            mMap.setOnInfoWindowClickListener(this);
        }
    }

    /**
     * @param v bouton afficher la carte
     * Affiche la carte ou le boutton pour donner les autorisations de l'application pour la géolocaliation
     */
    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.recreate();
        }else{
            showSnabar();
        }
    }

    /**
     * Affiche la snackbar pour autorisé la glocalisation
     */
    private void showSnabar(){
        Snackbar.make(findViewById(R.id.map),
                Constant.request_geo_loc_text,
                Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askMapPermission();
            }
        }).show();
    }

    /**
     * @param location current localisation
     * Si la caméra est vérouillé, permet de suivre la position de l'utilisateur sur la carte
     */
    @Override
    public void onLocationChanged(Location location) {
        this.myLocation = location;
        if(isCameraLock){
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * @param v boutton pour lock
     * Permet de (dé)verouiller la camera sur l'utilisateur
     */
    public void onClickLockCamera(View v){
        // todo changer le comportement :
        isCameraLock = !isCameraLock;
        // changement de couleur du bouton :
        if(isCameraLock){
            center_map_button.setBackground(getResources().getDrawable(R.drawable.button_style_red));
        }else{
            center_map_button.setBackground(getResources().getDrawable(R.drawable.button_style));
        }

        // focus sur la position de l'utilisateur
        if(isCameraLock){
            zoomOnCurrentLocation();
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
        }else{
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }

    }

    /**
     * Zoom sur la position actuel de l'utilisateur
     */
    private void zoomOnCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Location current_location = myLocation;
            if (current_location == null) {
               current_location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            }
            if(current_location != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_location.getLatitude(), current_location.getLongitude()), 15));
            }
        }
    }

    private void deconnexion(){
        SharedPreferences myPrefs= getSharedPreferences("dresseur", MODE_PRIVATE);
        myPrefs.edit().remove("dresseur_id").apply();
        Intent intent = new Intent(MapsActivity.this, ConnexionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Déconnexion"); //R.string.app_name todo @string
        builder.setMessage("Voulez vous vous déconnecter ?"); // todo @string
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                deconnexion();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createPokestops(){
        PokestopDAO pokestopDAO = new PokestopDAO(this);
        ArrayList<Pokestop> pokestops = pokestopDAO.getAllPokestop(this);

        // create markers on map :
        for (Pokestop pokestop : pokestops){
            BitmapDescriptor bitmapDescriptor;
            // couleur du marker :
            if (pokestop.get_Is_gym()){
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker((int) BitmapDescriptorFactory.HUE_GREEN);
            } else {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker((int) BitmapDescriptorFactory.HUE_CYAN);
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pokestop.getLatitude(), pokestop.getLongitude()))
                    .title(pokestop.getNom())
                    .icon(bitmapDescriptor));
            int pokestop_id = pokestop.getId();
            marker.setTag(pokestop_id);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int pokestopId = (int) marker.getTag();
        Intent intent = new Intent(MapsActivity.this, PokestopInformationsActivity.class);
        intent.putExtra("pokestop_id", pokestopId);
        startActivity(intent);
    }
}
