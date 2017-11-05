package com.gggd.sunny.testsystem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.tools.DBOpenHelper;

import java.util.ArrayList;

/**
 * Created by Sunny on 2017/10/26.
 */

public class WrongAndCollectDB {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public WrongAndCollectDB(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getReadableDatabase();
    }

    //更新错题的标志
    public void updateWrongQuestion(ArrayList<Question> list, Library library) {
        String sql = "update question set answer=?, wrong_flag=?,wrong_flag=? where id=?";
        String sql_wrong_collect = "update collect_wrong set wrong_flag=?,collect_flag=? where question_id=(" +
                "select question_id from question where id=?)";
        for (Question q : list) {
            db.execSQL(sql, new Object[]{q.getAnswer(), q.getCollect_flag(), q.getWrong_flag(), q.getId()});
            db.execSQL(sql_wrong_collect, new Object[]{q.getWrong_flag(), q.getCollect_flag(), q.getId()});
        }
        if (library.getId() != 0) {
            String sqltest = "update library set flag=1,score=?,time=? where id=?";
            db.execSQL(sqltest, new Object[]{library.getScore(), library.getTime(), library.getId()});
        }
    }

    //只更新错题和收藏集合，用于错题练习
    public void updateWrongCollect(ArrayList<Question> list) {
        String sql_wrong_collect = "update collect_wrong set wrong_flag=?,collect_flag=? where question_id=(" +
                "select question_id from question where id=?)";
        for (Question q : list) {
            db.execSQL(sql_wrong_collect, new Object[]{q.getWrong_flag(), q.getCollect_flag(), q.getId()});
        }
    }

    //更新收藏
    public void updateCollectQuestion(ArrayList<Question> list) {
        String sql = "update collect_wrong set collect_flag=? where question_id=(select question_id from question where id=?)";
        for (Question q : list) {
            db.execSQL(sql, new Object[]{q.getCollect_flag(), q.getId()});
        }
    }

    //我的收藏和我的错题
    public ArrayList<Question> getWrongandCollect(String sql,int library_num){
        ArrayList<Question> list = new ArrayList();
        Cursor cursor = db.rawQuery(sql,new String[]{library_num+""});
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String topic = cursor.getString(cursor.getColumnIndex("topic"));
            String option_a = cursor.getString(cursor.getColumnIndex("option_a"));
            String option_b = cursor.getString(cursor.getColumnIndex("option_b"));
            String option_c = cursor.getString(cursor.getColumnIndex("option_c"));
            String option_d = cursor.getString(cursor.getColumnIndex("option_d"));
            String option_e = cursor.getString(cursor.getColumnIndex("option_e"));
            String option_f = cursor.getString(cursor.getColumnIndex("option_f"));
            String option_t = cursor.getString(cursor.getColumnIndex("option_t"));
            String wrong_flag = cursor.getString(cursor.getColumnIndex("wrong_flag"));
            String option_answer = "";
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            Question question = new Question(id, type, topic, option_a, option_b, option_c, option_d, option_e, option_f,
                    option_t, option_answer, score, wrong_flag);
            String question_id = cursor.getString(cursor.getColumnIndex("question_id"));
            String collectsql = "select collect_flag from collect_wrong where question_id=?";
            Cursor cursor1 = db.rawQuery(collectsql, new String[]{question_id});
            cursor1.moveToFirst();
            String collect_flag = cursor1.getString(cursor1.getColumnIndex("collect_flag"));
            question.setCollect_flag(collect_flag);
            list.add(question);
        }
        return list;
    }
    //获取题库的Num
    public int getLibraryNum(String libraryname){
        int num = 0;
        String sql = "select num from library where name=?";
        Cursor cursor1 = db.rawQuery(sql, new String[]{libraryname});
        cursor1.moveToFirst();
        if(cursor1.getCount() == 0) {
            num = -1;
        }else{
            num = cursor1.getInt(cursor1.getColumnIndex("num"));
        }
        return num;
    }
}
