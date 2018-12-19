package fr.utt.if26.if26_projet_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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
    private Pokestop currentPokestop;
    private PokestopDAO pokestopDAO;

    private int pokestop_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pokestop);

        // init data :
        pokestopDAO = new PokestopDAO(this);
        Bundle extra = getIntent().getExtras();
        Intent intent = getIntent();
        if(extra != null) {
            if(intent.hasExtra("Lat")){
                latLng = new LatLng(extra.getDouble("Lat"), extra.getDouble("Lng"));
            }
            if(intent.hasExtra("pokestop_id")){
                pokestop_id = extra.getInt("pokestop_id");
                currentPokestop = pokestopDAO.getPokestop(pokestop_id, this);
            }
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
        }else{
            modifierButton.setVisibility(View.VISIBLE);
            supprimerButton.setVisibility(View.VISIBLE);
            validerButton.setVisibility(View.GONE);
            nomEditText.setText(currentPokestop.getNom());
            latitudeEditText.setText(Double.toString(currentPokestop.getLatitude()));
            longitudeEditText.setText(Double.toString(currentPokestop.getLongitude()));
            isGymSwith.setChecked(currentPokestop.get_Is_gym());
        }

        // ajout d'une flèche de retour :
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(latLng != null){
            actionBar.setTitle("Ajout d'un pokestop");//todo : @string
        }else{
            actionBar.setTitle("Modification d'un pokestop");//todo : @string
        }

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
            pokestopDAO.save(pokestop);
            // back to the map
            Intent intent = new Intent(Edit_Pokestop_Activity.this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void onClickModifierPokestop(View v){
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
            pokestop.setId(currentPokestop.getId());
            pokestop.setDresseur(currentDresseur);
            pokestop.setLatitude(latitude);
            pokestop.setLongitude(longitude);
            pokestop.setNom(nom);
            pokestop.set_Is_gym(isGymSwith.isChecked());
            // save pokestop
            pokestopDAO.update(pokestop);
            // back to the map
            Intent intent = new Intent(Edit_Pokestop_Activity.this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSupprimerPokestop(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Pokestop_Activity.this);
        builder.setTitle(currentPokestop.getNom()); //R.string.app_name todo @string
        builder.setMessage("Voulez vous vous supprimer ce pokestop ?"); // todo @string
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                pokestopDAO.deletePokestop(currentPokestop.getId());
                Intent intent = new Intent(Edit_Pokestop_Activity.this, MapsActivity.class);
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
}
