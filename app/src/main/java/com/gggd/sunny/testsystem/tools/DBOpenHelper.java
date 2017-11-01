package com.gggd.sunny.testsystem.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sunny on 2017/10/11.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    //数据库的SQL语句
    private static final String CREATE_LIBRARY_NAME = "Testsystem.db";
    private static final int VSERSION = 1;
    private static final String CREATE_LIBRARY_TABLE = "CREATE TABLE \"library\" (" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "\"name\"  TEXT(10) NOT NULL," +
            "\"num\"  CHAR(3) NOT NULL," +
            "\"flag\"  CHAR(1) NOT NULL DEFAULT 0," +
            "\"single_num\"  CHAR(3)," +
            "\"multiple_num\"  CHAR(3)," +
            "\"judge_num\"  CHAR(3)," +
            "\"score\"  CHAR(3)," +
            "\"time\"  TEXT(20));";
    private static final String CREATE_QUESTION_TABLE = "CREATE TABLE \"question\" (" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "\"library_num\"  CHAR(1) NOT NULL," +
            "\"type\"  CHAT(1) NOT NULL," +
            "\"topic\"  TEXT(300) NOT NULL," +
            "\"option_a\"  TEXT(80) NOT NULL," +
            "\"option_b\"  TEXT(80) NOT NULL," +
            "\"option_c\"  VARCHAR(80)," +
            "\"option_d\"  VARCHAR(80)," +
            "\"option_e\"  VARCHAR(80)," +
            "\"option_f\"  VARCHAR(80)," +
            "\"option_t\"  CHAR(1) NOT NULL," +
            "\"answer\"  CHAR(1)," +
            "\"score\"  INTEGER," +
            "\"test_id\"  INTEGER,wrong_flag INTEGER,question_id INTEGER)";
    private static final  String CREATE_COLLECTION_TABLE = "CREATE TABLE \"collect_wrong\" (" +
            "\"question_id\"  INTEGER," +
            "\"wrong_flag\"  CHAR(1) ," +
            "\"collect_flag\"  CHAR(1))";

    public DBOpenHelper(Context context) {
        super(context, CREATE_LIBRARY_NAME, null, VSERSION);
        Log.d("lz  db","onCreate OK!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIBRARY_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        Log.d("lz  db","onCreateDB OK!");
        db.execSQL(CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
