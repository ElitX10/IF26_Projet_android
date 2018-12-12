package fr.utt.if26.if26_projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

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

    public ArrayList<Pokestop> getAllPokestop(Context context){
        ArrayList<Pokestop> pokestops = new ArrayList<Pokestop>();

        String query = "SELECT * FROM " + DataBaseHelper.POKESTOP_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()){
            Pokestop pokestop = new Pokestop();
            pokestop.setId(cursor.getInt(0));
            pokestop.setNom(cursor.getString(1));
            Boolean is_gym;
            if (cursor.getString(2).equals("1")) {
                is_gym = true;
            } else {
                is_gym = false;
            }
            pokestop.set_Is_gym(is_gym);
            Double latitude = Double.parseDouble(cursor.getString(3));
            pokestop.setLatitude(latitude);
            Double longitude = Double.parseDouble(cursor.getString(4));
            pokestop.setLongitude(longitude);

            DresseurDAO dresseurDAO= new DresseurDAO(context);
            Dresseur dresseur = dresseurDAO.getDresseurById(cursor.getInt(5));
            pokestop.setDresseur(dresseur);

            pokestops.add(pokestop);
        }
        return pokestops;
    }

    public void update(Pokestop pokestop){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NOM_POKESTOP, pokestop.getNom());
        values.put(DataBaseHelper.IS_GYM_POKESTOP, pokestop.get_Is_gym());
        values.put(DataBaseHelper.LATITUDE_POKESTOP, pokestop.getLatitude());
        values.put(DataBaseHelper.LONGITUDE_POKESTOP, pokestop.getLongitude());
        values.put(DataBaseHelper.ID_DRESSEUR_POKESTOP, pokestop.getDresseur().getId());

        System.out.println(pokestop.getId());
        database.update(DataBaseHelper.POKESTOP_TABLE, values,
                DataBaseHelper.ID_POKESTOP + " =?",
                new String[] { String.valueOf(pokestop.getId()) });
    }

    public Pokestop getPokestop(int id, Context context) {
        String query = "SELECT * FROM " + DataBaseHelper.POKESTOP_TABLE
                + " WHERE " + DataBaseHelper.POKESTOP_TABLE + "." + DataBaseHelper.ID_POKESTOP
                + " = '" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            DresseurDAO dresseurDAO = new DresseurDAO(context);
            Boolean is_gym;
            if (cursor.getString(2).equals("1")) {
                is_gym = true;
            } else {
                is_gym = false;
            }
            Pokestop pokestop = new Pokestop(cursor.getInt(0),
                    cursor.getString(1), is_gym,
                    cursor.getDouble(3), cursor.getDouble(4),
                    dresseurDAO.getDresseurById(cursor.getInt(5)));
            return pokestop;
        }
        cursor.close();
        return null;
    }

    public void deletePokestop(int id){
        database.delete(DataBaseHelper.POKESTOP_TABLE,
                DataBaseHelper.ID_POKESTOP + " =?",
                new String[] { String.valueOf(id) });
    }
}
