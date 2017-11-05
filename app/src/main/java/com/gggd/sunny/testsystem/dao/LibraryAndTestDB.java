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
    //切换试卷
    public ArrayList<Library> getAllLibrary(){
        ArrayList<Library> list = new ArrayList<>();
        String sql = "select DISTINCT(name) from library";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Library library = new Library();
            library.setLibrary_name(name);
            list.add(library);
        }
        return list;
    }
    //获取每个librarname的信息
    public ArrayList<Library> getLibraryInfo(ArrayList<Library> namelist){
        ArrayList<Library> list = new ArrayList<>();
        String sql0 = "select count(*) from library where name=? and flag=0";
        String sql1 = "select count(*) from library where name=? and flag=1";
        Cursor cursor;
        for(int i =  0;i<namelist.size();i++) {
            cursor = db.rawQuery(sql0, new String[]{namelist.get(i).getLibrary_name()});
            cursor.moveToFirst();
            String flag0 = cursor.getString(cursor.getColumnIndex("count(*)"));
            namelist.get(i).setScore(flag0);
            cursor = db.rawQuery(sql1, new String[]{namelist.get(i).getLibrary_name()});
            cursor.moveToFirst();
            String flag1 = cursor.getString(cursor.getColumnIndex("count(*)"));
            namelist.get(i).setTime(flag1);
            list.add(namelist.get(i));
        }
        return list;
    }
    public String existLibrary(String libraryname){
        String sql = "select count(*) from library where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{libraryname});
        cursor.moveToFirst();
        String flag1 = cursor.getString(cursor.getColumnIndex("count(*)"));
        return flag1;
    }
    //删除题库
    public int deleteLibrary(String libraryname){
        String sql = "select DISTINCT(num) from library where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{libraryname});
        int flag1 = -1;
        if(cursor.getCount() == 0){
            flag1 = -1;
        }else {
            cursor.moveToFirst();
            flag1 = cursor.getInt(cursor.getColumnIndex("num"));
        }
        return flag1;
    }
    public String deleteLibrary2(int num){
        String sql3 = "delete  from collect_wrong where question_id in(" +
                "select DISTINCT(question_id) from question where library_num=?)";
        String sql2 = "delete from question where library_num=?";
        String sql1 = "delete from library where num=?";
        db.execSQL(sql3,new Object[]{num});
        db.execSQL(sql2,new Object[]{num});
        db.execSQL(sql1,new Object[]{num});
        String flag1 = " ";
        String sql4 = "select name from library  order by num asc limit 1";
        Cursor cursor = db.rawQuery(sql4, null);
        if(!(cursor.getCount() == 0)){
            cursor.moveToFirst();
            flag1 = cursor.getString(cursor.getColumnIndex("name"));
        }
        return flag1;
    }
    public String getPercent(String libraryname){
        String tmp = "";
        String sql1 = "select count(*) from library where name=? and flag = 1";
        String sql2 = "select count(*) from library where name=?";
        Cursor cursor = db.rawQuery(sql1, null);
        if(!(cursor.getCount() == 0)){
            cursor.moveToFirst();
            tmp = cursor.getString(cursor.getColumnIndex("count(*)"));
            cursor = db.rawQuery(sql2, null);
            if(!(cursor.getCount() == 0)){
                cursor.moveToFirst();
                tmp = tmp+"/"+cursor.getString(cursor.getColumnIndex("count(*)"));
            }
        }
        return tmp;
    }
}
