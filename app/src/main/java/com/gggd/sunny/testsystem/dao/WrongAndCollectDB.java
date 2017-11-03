package com.gggd.sunny.testsystem.dao;

import android.content.Context;
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
    public void updateWrongQuestion(ArrayList<Question> list, Library library){
        String sql = "update question set answer=?, wrong_flag=?,wrong_flag=? where id=?";
        String sql_wrong_collect = "update collect_wrong set wrong_flag=?,collect_flag=? where question_id=(" +
                "select question_id from question where id=?)";
        for(Question q:list) {
            db.execSQL(sql, new Object[]{q.getAnswer(),q.getCollect_flag(),q.getWrong_flag(),q.getId()});
            db.execSQL(sql_wrong_collect, new Object[]{q.getWrong_flag(),q.getCollect_flag(),q.getId()});
        }
        if(library.getId() != 0) {
            String sqltest = "update library set flag=1,score=?,time=? where id=?";
            db.execSQL(sqltest, new Object[]{library.getScore(), library.getTime(),library.getId()});
        }
    }
    //只更新错题和收藏集合，用于错题练习
    public void updateWrongCollect(ArrayList<Question> list){
        String sql_wrong_collect = "update collect_wrong set wrong_flag=?,collect_flag=? where question_id=(" +
                "select question_id from question where id=?)";
        for(Question q:list) {
            db.execSQL(sql_wrong_collect, new Object[]{q.getWrong_flag(),q.getCollect_flag(),q.getId()});
        }
    }

    //更新收藏
    public void updateCollectQuestion(ArrayList<Question> list){
        String sql = "update collect_wrong set collect_flag=? where question_id=(select question_id from question where id=?)";
        for(Question q:list) {
            db.execSQL(sql, new Object[]{q.getCollect_flag(),q.getId()});
        }
    }
}
