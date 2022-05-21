package hanu.a2_1801040048.db;

import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_ID;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_NAME;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_QUANTITY;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_THUMBNAIL;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_COL_UNIT_PRICE;
import static hanu.a2_1801040048.utils.constants.KeyConstants.TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = TABLE_NAME + ".db";
    private final static int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                TABLE_COL_ID + " INTEGER PRIMARY KEY NOT NULL," +
                TABLE_COL_THUMBNAIL + " TEXT NOT NULL," +
                TABLE_COL_NAME + " TEXT NOT NULL," +
                TABLE_COL_UNIT_PRICE + " TEXT NOT NULL," +
                TABLE_COL_QUANTITY + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }
}
