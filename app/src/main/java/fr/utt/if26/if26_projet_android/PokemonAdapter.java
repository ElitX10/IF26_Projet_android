package fr.utt.if26.if26_projet_android;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PokemonAdapter extends ArrayAdapter {

    Context context;
    List<Localisation> localisations;
    int resource;

    public PokemonAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.localisations = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(resource, parent, false);

        Localisation localisation = localisations.get(position);

        TextView nomPokemon = v.findViewById(R.id.pokemonListItemTextView);
        nomPokemon.setText(localisation.getPokemon().getNom());

        TextView heurePokemon = v.findViewById(R.id.heureListItemTextView);
        heurePokemon.setText(localisation.getTime());

        Button supprimer = v.findViewById(R.id.supprimerListItemButton);
        supprimer.setTag(localisation.getId());
        return v;
    }
}
