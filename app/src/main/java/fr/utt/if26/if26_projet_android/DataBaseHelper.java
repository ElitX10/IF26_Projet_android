package fr.utt.if26.if26_projet_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    // infos BDD :
    private static final String DATABASE_NAME = "pokeappdb";
    private static final int DATABASE_VERSION = 1;

    // nom des tables de la BDD :
    public static final String DRESSEUR_TABLE = "dresseur";
    public static final String POKEMON_TABLE = "pokemon";
    public static final String POKESTOP_TABLE = "pokestop";
    public static final String LOCALISATION_TABLE = "localisation";

    // nom colonnes table dresseur :
    public static final String ID_DRESSEUR = "id";
    public static final String PSEUDO_DRESSEUR = "pseudo";
    public static final String MOT_DE_PASSE_DRESSEUR = "password";

    // nom colonnes table pokemon :
    public static final String ID_POKEMON = "id";
    public static final String NOM_POKEMON = "nom";
//    public static final String IMAGE_POKEMON = "image";

    // nom colonnes table pokestop :
    public static final String ID_POKESTOP = "id";
    public static final String IS_GYM_POKESTOP = "is_gym";
    public static final String LATITUDE_POKESTOP = "latitude";
    public static final String LONGITUDE_POKESTOP = "longitude";
    public static final String ID_DRESSEUR_POKESTOP = "dresseur_id";

    // nom colonnes table localisation :
    public static final String ID_LOCALISATION = "id";
    public static final String ID_POKEMON_LOCALISATION = "pokemon_id";
    public static final String ID_DRESSEUR_LOCALISATION = "dresseur_id";
    public static final String ID_POKESTOP_LOCALISATION = "pokestop_id";
    public static final String TIME_LOCALISATION = "time";

    // commande de création des tables :
    public static final String CREATE_DRESSEUR_TABLE = "CREATE TABLE "+ DRESSEUR_TABLE +" ( "
            + ID_DRESSEUR +" INTEGER NOT NULL UNIQUE, "
            + PSEUDO_DRESSEUR +" TEXT NOT NULL UNIQUE, "
            + MOT_DE_PASSE_DRESSEUR +" TEXT, "
            + "PRIMARY KEY("+ ID_DRESSEUR +") )";
    public static final String CREATE_POKEMON_TABLE = "CREATE TABLE "+ POKEMON_TABLE +" ( "
            + ID_POKEMON +" INTEGER NOT NULL UNIQUE, "
            + NOM_POKEMON +" TEXT NOT NULL UNIQUE, "
//            + IMAGE_POKEMON +" BLOB NOT NULL, "
            + "PRIMARY KEY("+ ID_POKEMON +") )";
    public static final String CREATE_POKESTOP_TABLE = "CREATE TABLE "+ POKESTOP_TABLE +" ( "
            + ID_POKESTOP +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + IS_GYM_POKESTOP +" INTEGER NOT NULL DEFAULT 0, "
            + LATITUDE_POKESTOP +" NUMERIC NOT NULL, "
            + LONGITUDE_POKESTOP +" NUMERIC NOT NULL, "
            + ID_DRESSEUR_POKESTOP +" INTEGER NOT NULL, "
            + "FOREIGN KEY("+ ID_DRESSEUR_POKESTOP +") REFERENCES "+ DRESSEUR_TABLE +"("+ ID_DRESSEUR +") )";
    public static final String CREATE_LOCALISATION_TABLE = "CREATE TABLE "+ LOCALISATION_TABLE +" ( "
            + ID_LOCALISATION +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + ID_POKEMON_LOCALISATION +" INTEGER NOT NULL, "
            + ID_DRESSEUR_LOCALISATION +" INTEGER NOT NULL, "
            + ID_POKESTOP_LOCALISATION +" INTEGER NOT NULL, "
            + TIME_LOCALISATION +" TEXT NOT NULL, "
            + "FOREIGN KEY("+ ID_POKEMON_LOCALISATION +") REFERENCES "+ POKEMON_TABLE +"("+ ID_POKEMON +"), "
            + "FOREIGN KEY("+ ID_POKESTOP_LOCALISATION +") REFERENCES "+ POKESTOP_TABLE +"("+ ID_POKESTOP +"), "
            + "FOREIGN KEY("+ ID_DRESSEUR_LOCALISATION +") REFERENCES "+ DRESSEUR_TABLE +"("+ ID_DRESSEUR +") )";

    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if(instance==null){
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            // Enable foreign key constraints :
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRESSEUR_TABLE);
        db.execSQL(CREATE_POKEMON_TABLE);
        db.execSQL(CREATE_POKESTOP_TABLE);
        db.execSQL(CREATE_LOCALISATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
