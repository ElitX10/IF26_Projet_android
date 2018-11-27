package fr.utt.if26.if26_projet_android;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PokeAppDBDAO {

    protected SQLiteDatabase database;
    private  DataBaseHelper dbHelper;
    private Context myContext;

    public PokeAppDBDAO(Context context){
        this.myContext = context;
        dbHelper = DataBaseHelper.getHelper(myContext);
        open();
    }

    public void open() throws SQLException {
        if(dbHelper == null){
            dbHelper = DataBaseHelper.getHelper(myContext);
        }
        database = dbHelper.getWritableDatabase();
    }
}
