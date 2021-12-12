package ddwu.mobile.finalproject.ma02_20190940;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "plantDiary.db";
    public final static String TABLE_NAME = "diary";
    public final static String COL_ID = "_id";
    public final static String COL_DATE = "date";
    public final static String COL_NAME = "name";
    public final static String COL_TITLE = "d_title";
    public final static String COL_MEMO = "memo";
    public final static String COL_PATH = "pic_path";

    public DiaryDBHelper(Context context) {super(context, DB_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_DATE + " TEXT, " + COL_NAME + " TEXT, " + COL_TITLE + " TEXT, " + COL_MEMO + " TEXT, " + COL_PATH + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
