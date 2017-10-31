package com.gggd.sunny.testsystem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.tools.DBOpenHelper;

import java.util.ArrayList;

/**
 * Created by Sunny on 2017/10/13.
 */

public class LibraryAndTestDB {

    private SQLiteDatabase db;

    public LibraryAndTestDB(Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getReadableDatabase();
    }

    public String insertLibraryTestCount(Library library, int max) {
        String minnumsql = "select id,num from library where id=(select max(id) from library)";
        Cursor cursor = db.rawQuery(minnumsql, null);
        int minnum = 1;
        int minid = 0;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            minnum = Integer.parseInt(cursor.getString(1))+1;
            minid = Integer.parseInt(cursor.getString(0));
        }
        String insersql = "insert into library(name,num,flag,single_num,multiple_num,judge_num) values(?,?,?,?,?,?)";
        for (int i = 0; i < max; i++) {
            db.execSQL(insersql, new Object[]{library.getLibrary_name(), minnum, 0, library.getSingle_num(),
                    library.getMultiple_num(), library.getJudge_num()});
            Log.d("lz", "增加成功-" + library.toString());
        }
        return minnum + " " + minid;
    }
    //查看题库名是否重复
    public boolean libraryNameLimit(String libraryname) {
        boolean flag = false;
        String sql = "select name from library where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{libraryname});
        if (cursor.getCount() == 0) {
            flag = true;
        }
        return flag;
    }
    //开始考试，查询出一条未进行开始的记录
    public ArrayList<Library> getTestList(String sql1, String[] args){
        ArrayList<Library> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql1, args);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String num = cursor.getString(cursor.getColumnIndex("num"));
            String flag = cursor.getString(cursor.getColumnIndex("flag"));
            String single_num = cursor.getString(cursor.getColumnIndex("single_num"));
            String multiple_num = cursor.getString(cursor.getColumnIndex("multiple_num"));
            String judge_num = cursor.getString(cursor.getColumnIndex("judge_num"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            if(time == null )
                time = "";
            Library library = new Library(id,name,num,flag,single_num,multiple_num,judge_num,time);
            list.add(library);
        }
        return list;
    }

    //开始考试，查询出一条未进行开始的记录
    public ArrayList<Library> getTestsList(String sql1, String[] args){
        ArrayList<Library> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql1, args);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String score = cursor.getString(cursor.getColumnIndex("score"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Library library = new Library();
            library.setId(id);
            library.setScore(score);
            library.setTime(time);
            list.add(library);
        }
        return list;
    }

}
