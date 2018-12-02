package fr.utt.if26.if26_projet_android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;

/**
 * Getion de la page de création et d'édition d'un pokestop
 */
public class Edit_Pokestop_Activity extends AppCompatActivity {

    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button supprimerButton;
    private Button validerButton;
    private Button modifierButton;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__pokestop);

        // init data :
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            latLng = new LatLng(extra.getDouble("Lat"), extra.getDouble("Lng"));
        }

        // init views :
        latitudeEditText = findViewById(R.id.latitudeEditPokestopEditText);
        longitudeEditText = findViewById(R.id.longitudeEditPokestopEditText);
        supprimerButton = findViewById(R.id.supprimerEditPokestopButton);
        validerButton = findViewById(R.id.valideEditPokestopButton);
        modifierButton = findViewById(R.id.modifierEditPokestopButton);
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
}
