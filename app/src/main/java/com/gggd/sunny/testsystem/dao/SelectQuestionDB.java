package com.gggd.sunny.testsystem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.tools.DBOpenHelper;

import java.util.ArrayList;

/**
 * Created by Sunny on 2017/10/20.
 */

public class SelectQuestionDB {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public SelectQuestionDB(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getReadableDatabase();
    }

    //选择考题，List集合，每次进入考试界面都读一次，其中使用SharedPreferences
    //来记住每一次切出去在进入的那一道题。
    public ArrayList<Question> selectAllQuestion(String sql, String test_id) {
        ArrayList<Question> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{test_id});
        while (cursor.moveToNext()) {
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
            String option_answer = cursor.getString(cursor.getColumnIndex("answer"));
            int wrong_flag = cursor.getInt(cursor.getColumnIndex("wrong_flag"));
            if (option_answer == null)
                option_answer = "";
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            Question question = new Question(id, type, topic, option_a, option_b, option_c, option_d, option_e, option_f,
                    option_t, option_answer, score, wrong_flag);
            //查看是否已经收藏
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

    public ArrayList<Question> selectWrongQuestion(String librarname) {
        String sql0 = "select * from question where id in (" +
                "select question_id from collect_wrong where wrong_flag=1 and type=1 limit (" +
                "select single_num from library where name=? limit 1))";
        String sql1 = "select * from question where id in (" +
                "select question_id from collect_wrong where wrong_flag=1 and type=2 limit (" +
                "select multiple_num from library where name=? limit 1))";
        String sql2 = "select * from question where id in (" +
                "select question_id from collect_wrong where wrong_flag=1 and type=3 limit (" +
                "select judge_num from library where name=? limit 1))";
        ArrayList<Question> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql0, new String[]{librarname});
        while (cursor.moveToNext()) {
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
            int wrong_flag = cursor.getInt(cursor.getColumnIndex("wrong_flag"));
            String option_answer = "";
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            Question question = new Question(id, type, topic, option_a, option_b, option_c, option_d, option_e, option_f,
                    option_t, option_answer, score, wrong_flag);
            //查看是否已经收藏
            String question_id = cursor.getString(cursor.getColumnIndex("question_id"));
            String collectsql = "select collect_flag from collect_wrong where question_id=?";
            Cursor cursor1 = db.rawQuery(collectsql, new String[]{question_id});
            cursor1.moveToFirst();
            String collect_flag = cursor1.getString(cursor1.getColumnIndex("collect_flag"));
            question.setCollect_flag(collect_flag);
            list.add(question);
        }
        cursor = db.rawQuery(sql1, new String[]{librarname});
        while (cursor.moveToNext()) {
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
            int wrong_flag = cursor.getInt(cursor.getColumnIndex("wrong_flag"));
            String option_answer = "";
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            Question question = new Question(id, type, topic, option_a, option_b, option_c, option_d, option_e, option_f,
                    option_t, option_answer, score, wrong_flag);
            //查看是否已经收藏
            String question_id = cursor.getString(cursor.getColumnIndex("question_id"));
            String collectsql = "select collect_flag from collect_wrong where question_id=?";
            Cursor cursor1 = db.rawQuery(collectsql, new String[]{question_id});
            cursor1.moveToFirst();
            String collect_flag = cursor1.getString(cursor1.getColumnIndex("collect_flag"));
            question.setCollect_flag(collect_flag);
            list.add(question);
        }
        cursor = db.rawQuery(sql2, new String[]{librarname});
        while (cursor.moveToNext()) {
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
            int wrong_flag = cursor.getInt(cursor.getColumnIndex("wrong_flag"));
            String option_answer = "";
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            Question question = new Question(id, type, topic, option_a, option_b, option_c, option_d, option_e, option_f,
                    option_t, option_answer, score, wrong_flag);
            //查看是否已经收藏
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
}
