package fr.utt.if26.if26_projet_android;

import android.content.ContentValues;
import android.content.Context;

public class PokestopDAO extends PokeAppDBDAO {
    public PokestopDAO(Context context) {
        super(context);
    }

    public void save(Pokestop pokestop){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NOM_POKESTOP, pokestop.getNom());
        values.put(DataBaseHelper.IS_GYM_POKESTOP, pokestop.get_Is_gym());
        values.put(DataBaseHelper.LATITUDE_POKESTOP, pokestop.getLatitude());
        values.put(DataBaseHelper.LONGITUDE_POKESTOP, pokestop.getLongitude());
        values.put(DataBaseHelper.ID_DRESSEUR_POKESTOP, pokestop.getDresseur().getId());

        database.insert(DataBaseHelper.POKESTOP_TABLE, null, values);
    }
}
