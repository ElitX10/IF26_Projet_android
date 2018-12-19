package fr.utt.if26.if26_projet_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PokestopInformationsActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner pokemonSpinner;
    private Button validerButton;
    private ListView pokemonListView;
    private Pokestop currentPokestop;
    private Dresseur currentDresseur;

    private DresseurDAO dresseurDAO;
    private PokemonDAO pokemonDAO;
    private PokestopDAO pokestopDAO;
    private LocalisationDAO localisationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokestop_informations);
        // todo add bouton edit pokestop
        // init dao :
        dresseurDAO = new DresseurDAO(this);
        pokemonDAO = new PokemonDAO(this);
        pokestopDAO = new PokestopDAO(this);
        localisationDAO = new LocalisationDAO(this);

        // init data :
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            currentPokestop = pokestopDAO.getPokestop(extra.getInt("pokestop_id"), this);
        }
        SharedPreferences myPrefs= getSharedPreferences("dresseur", MODE_PRIVATE);
        int dresseur_id = myPrefs.getInt("dresseur_id", 0);
        currentDresseur = dresseurDAO.getDresseurById(dresseur_id);

        // init views:
        pokemonSpinner = findViewById(R.id.listePokemonPokestopInformationSpinner);
        pokemonListView = findViewById(R.id.pokemonsListView);
        validerButton = findViewById(R.id.validerPokestopInformationButton);
        // init spinner :
        ArrayList<Pokemon> pokemons = pokemonDAO.getAllPokemons();
        List<String> pokemonString = new ArrayList<String>();
        for(Pokemon pokemon : pokemons){
            pokemonString.add(pokemon.spinnerVal());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                pokemonString);
        pokemonSpinner.setAdapter(arrayAdapter);

        // init pokemon list :
        ArrayList<Localisation> localisations = localisationDAO.getLocalisations( currentPokestop.getId(),this);
        PokemonAdapter pokemonAdapter = new PokemonAdapter(
                this,
                R.layout.pokemon_item,
                localisations);
        pokemonListView.setAdapter(pokemonAdapter);

        // ajout d'une flèche de retour + titre :
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(currentPokestop.getNom());
    }

    // create an action bar button (si le dresseur est celui qui a créé le pokestop)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentPokestop.getDresseur().getId() == currentDresseur.getId()) {
            getMenuInflater().inflate(R.menu.edit_button_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    /**
     * @param v bouton de validation d'ajout d'un pokemon
     * Ajout d'un pokemon sur le pokestop en fonction du spinner
     */
    @Override
    public void onClick(View v) {
        int pokemon_id = pokemonSpinner.getSelectedItemPosition() + 1;

        // création de l'objet localisation :
        Localisation localisation = new Localisation();
        localisation.setPokemon(pokemonDAO.getPokemon(pokemon_id));
        localisation.setDresseur(currentDresseur);
        localisation.setPokestop(currentPokestop);
        String currentTime = "Le " + new SimpleDateFormat("dd/MM/yyyy à HH:mm").format(Calendar.getInstance().getTime());
        localisation.setTime(currentTime);
        // save localisation :
        localisationDAO.save(localisation);

        this.recreate();
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
            case R.id.icon_modif_button:
                Intent intent = new Intent(PokestopInformationsActivity.this, Edit_Pokestop_Activity.class);
                intent.putExtra("pokestop_id", currentPokestop.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param v bouton de suppression d'un item de la liste
     * Supprime un pokemon sur le pokestop en fonction du bouton de la liste
     */
    public void onClickSupprimer(final View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PokestopInformationsActivity.this);
        builder.setTitle("Suppression d'un pokemon"); //R.string.app_name todo @string
        builder.setMessage("Voulez vous supprimer ce Pokemon ?"); // todo @string
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int delete_id = Integer.parseInt(v.getTag().toString());
                localisationDAO.delete(delete_id);
                PokestopInformationsActivity.this.recreate();
                dialog.dismiss();
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
