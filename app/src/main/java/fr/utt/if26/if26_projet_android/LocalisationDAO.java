package fr.utt.if26.if26_projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class LocalisationDAO extends PokeAppDBDAO {
    public LocalisationDAO(Context context) {
        super(context);
    }

    public void save(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.ID_DRESSEUR_LOCALISATION, localisation.getDresseur().getId());
        values.put(DataBaseHelper.ID_POKESTOP_LOCALISATION, localisation.getPokestop().getId());
        values.put(DataBaseHelper.ID_POKEMON_LOCALISATION, localisation.getPokemon().getId());
        values.put(DataBaseHelper.TIME_LOCALISATION, localisation.getTime());

        database.insert(DataBaseHelper.LOCALISATION_TABLE, null, values);
    }

    public ArrayList<Localisation> getLocalisations(int pokestop_id, Context context){
        ArrayList<Localisation> localisations = new ArrayList<Localisation>();

//        todo : order
        String query = "SELECT * FROM " + DataBaseHelper.LOCALISATION_TABLE
                + " WHERE " + DataBaseHelper.LOCALISATION_TABLE + "." + DataBaseHelper.ID_POKESTOP_LOCALISATION
                + " = '" + pokestop_id +"'";
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()){
            Localisation localisation = new Localisation();

            localisation.setId(cursor.getInt(0));

            PokestopDAO pokestopDAO = new PokestopDAO(context);
            Pokestop pokestop = pokestopDAO.getPokestop(cursor.getInt(3), context);
            localisation.setPokestop(pokestop);

            DresseurDAO dresseurDAO = new DresseurDAO(context);
            Dresseur dresseur = dresseurDAO.getDresseurById(cursor.getInt(2));
            localisation.setDresseur(dresseur);

            PokemonDAO pokemonDAO = new PokemonDAO(context);
            Pokemon pokemon = pokemonDAO.getPokemon(cursor.getInt(1));
            localisation.setPokemon(pokemon);

            localisation.setTime(cursor.getString(4));

            localisations.add(localisation);
        }
        return localisations;
    }

    public void delete(int id){
        database.delete(DataBaseHelper.LOCALISATION_TABLE, DataBaseHelper.ID_LOCALISATION + " =?", new String[] { id + "" });
    }
}
