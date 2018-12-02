package fr.utt.if26.if26_projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DresseurDAO extends PokeAppDBDAO {
    public DresseurDAO(Context context) {
        super(context);
    }

    public void save(Dresseur dresseur){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.PSEUDO_DRESSEUR, dresseur.getPseudo());
        values.put(DataBaseHelper.MOT_DE_PASSE_DRESSEUR, dresseur.getPassword());

        database.insert(DataBaseHelper.DRESSEUR_TABLE, null, values);
    }

    public Dresseur getDresseurByPseudo(String pseudo){
        String query = "SELECT * FROM " + DataBaseHelper.DRESSEUR_TABLE
                + " WHERE " + DataBaseHelper.DRESSEUR_TABLE + "." + DataBaseHelper.PSEUDO_DRESSEUR
                + " = '" + pseudo.trim() + "'";
        Dresseur dresseur = getDresseur(query);
        return dresseur;
    }

    public Dresseur getDresseurById(int id){
        String query = "SELECT * FROM " + DataBaseHelper.DRESSEUR_TABLE
                + " WHERE " + DataBaseHelper.DRESSEUR_TABLE + "." + DataBaseHelper.ID_DRESSEUR
                + " = '" + id + "'";
        Dresseur dresseur = getDresseur(query);
        return dresseur;
    }

    private Dresseur getDresseur(String query){
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            Dresseur dresseur = new Dresseur(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
            return dresseur;
        }
        cursor.close();
        return null;
    }
}
