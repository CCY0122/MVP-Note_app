package com.example.ccy.mvp_note.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ccy on 2017-07-13.
 * 数据库创建类
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;   //版本号
    private static final String DATABASE_NAME = "Notes.db"; //数据库名称

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "entryid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IS_ACTIVE = "active";

    private static final String SQL_CREATE_ENTRIES =   //建表,该有的空格别忘敲
            "CREATE TABLE "+TABLE_NAME+"("+
                    COLUMN_ID+" TEXT PRIMARY KEY,"+
                    COLUMN_TITLE+" TEXT,"+
                    COLUMN_CONTENT+" TEXT,"+
                    COLUMN_IS_ACTIVE+" INTEGER"+
                    ")";



    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME , null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
