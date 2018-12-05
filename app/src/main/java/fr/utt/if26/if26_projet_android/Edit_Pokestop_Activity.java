package fr.utt.if26.if26_projet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Getion de la page de création et d'édition d'un pokestop
 */
public class Edit_Pokestop_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText nomEditText;
    private TextView infoTextView;
    private Button supprimerButton;
    private Button validerButton;
    private Button modifierButton;
    private Switch isGymSwith;

    private LatLng latLng;

    private Dresseur currentDresseur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__pokestop);

        // init data :
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            latLng = new LatLng(extra.getDouble("Lat"), extra.getDouble("Lng"));
        }
        DresseurDAO dresseurDAO = new DresseurDAO(this);
        SharedPreferences myPrefs= getSharedPreferences("dresseur", MODE_PRIVATE);
        int dresseur_id = myPrefs.getInt("dresseur_id", 0);
        currentDresseur = dresseurDAO.getDresseurById(dresseur_id);

        // init views :
        latitudeEditText = findViewById(R.id.latitudeEditPokestopEditText);
        longitudeEditText = findViewById(R.id.longitudeEditPokestopEditText);
        supprimerButton = findViewById(R.id.supprimerEditPokestopButton);
        validerButton = findViewById(R.id.valideEditPokestopButton);
        modifierButton = findViewById(R.id.modifierEditPokestopButton);
        nomEditText = findViewById(R.id.nomEditPokestopEditText);
        isGymSwith = findViewById(R.id.isAreneEditPokestopButtonSwitch);
        infoTextView = findViewById(R.id.informationEditPokestopTextView);
        infoTextView.setTextColor(Color.RED);
        infoTextView.setVisibility(View.GONE);
        if(latLng != null){
            latitudeEditText.setText(Double.toString(latLng.latitude));
            longitudeEditText.setText(Double.toString(latLng.longitude));
            modifierButton.setVisibility(View.GONE);
            supprimerButton.setVisibility(View.GONE);
            validerButton.setVisibility(View.VISIBLE);
        }

        // ajout d'une flèche de retour :
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //todo : changer en fct si nvx ou edit et mettre la variable dans @string
        actionBar.setTitle("Ajout d'un pokestop");
    }



    /**
     * @param item
     * @return
     * Ferme l'activité et revient sur l'activité parente
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param v bouton de validation
     * Sauvegarde le pokestop si tous les champs sont remplis
     */
    @Override
    public void onClick(View v) {
        // check inputs
        Double latitude, longitude;
        try{
            latitude = Double.parseDouble(latitudeEditText.getText().toString());
            longitude = Double.parseDouble(longitudeEditText.getText().toString());
        } catch (Exception e){
            infoTextView.setVisibility(View.VISIBLE);
            return;
        }
        String nom = nomEditText.getText().toString();

        if (nom.trim().length() == 0){
            infoTextView.setVisibility(View.VISIBLE);
        }else{
            // init pokestop
            Pokestop pokestop = new Pokestop();
            pokestop.setDresseur(currentDresseur);
            pokestop.setLatitude(latitude);
            pokestop.setLongitude(longitude);
            pokestop.setNom(nom);
            pokestop.set_Is_gym(isGymSwith.isChecked());
            // save pokestop
            PokestopDAO pokestopDAO = new PokestopDAO(this);
            pokestopDAO.save(pokestop);
            // back to the map
            Intent intent = new Intent(Edit_Pokestop_Activity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
