package ddwu.mobile.finalproject.ma02_20190940;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlantDBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "plantplan.db";
    public final static String TABLE_NAME = "my_plant_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "p_name";
    public final static String COL_POI = "point";
    public final static String COL_WSP = "wsp";
    public final static String COL_WSU = "wsu";
    public final static String COL_WA = "wa";
    public final static String COL_WW = "ww";
//    public final static String COL_PATH = "path";

    public PlantDBHelper(Context context) {super(context, DB_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_NAME + " TEXT, " + COL_POI + " TEXT, " + COL_WSP + " TEXT, " + COL_WSU + " TEXT, " +
                COL_WA + " TEXT, " + COL_WW + " TEXT);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
