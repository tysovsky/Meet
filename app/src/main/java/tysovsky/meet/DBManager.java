package tysovsky.meet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tysovsky on 4/4/16.
 */
public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MeetDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER_ACCOUNT = "user_account";
    private static final String COLUMN_ID =  "id";

    public DBManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CONVERSATIONS_QUERY = "CREATE TABLE " + TABLE_USER_ACCOUNT + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, "+
                COLUMN_UNREAD_MESSAGES + " INTEGER, " +
                COLUMN_ENCRYPTED + " TEXT, " +
                COLUMN_PASSWORDHASH + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(CONVERSATIONS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
