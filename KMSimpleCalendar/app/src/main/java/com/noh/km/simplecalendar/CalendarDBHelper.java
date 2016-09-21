package com.noh.km.simplecalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nohkyoungmo on 2016. 9. 12..
 */
public class CalendarDBHelper extends SQLiteOpenHelper {
    private static String TAG = "CalendarDBHelper";

    public static CalendarDBHelper _instance;
    //Application Context 를 받기를 권장
    public static CalendarDBHelper getInstance(Context context) {
        if(_instance == null) {
            _instance = new CalendarDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return _instance;
    }

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "simple_calendar.db";

    public interface Tables {
        String MEMO_TABLE = "calendar_memo"; // sample
    }

    public interface MemoColumns {
        String _ID = "_id";
        String MEMO = "memo";
        String YEAR = "year";
        String MONTH = "month";
        String DAY = "day";
    }


    public CalendarDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createSampleTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: 아직 필요없음
    }

    private void createSampleTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.MEMO_TABLE + " ( "
                + MemoColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MemoColumns.MEMO + " TEXT, "
                + MemoColumns.YEAR + " INTEGER, "
                + MemoColumns.MONTH + " INTEGER, "
                + MemoColumns.DAY + " INTEGER"
                + ");"
        );
    }
}

