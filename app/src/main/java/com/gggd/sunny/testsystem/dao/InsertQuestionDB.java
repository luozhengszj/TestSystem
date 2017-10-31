package com.gggd.sunny.testsystem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.tools.DBOpenHelper;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Sunny on 2017/10/13.
 */

public class InsertQuestionDB {

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public InsertQuestionDB(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getReadableDatabase();
    }

    public void insertSingleQuestion(Library library, LinkedList<Question> list) {
        int size = list.size();
        int single_score = Integer.parseInt(library.getSingle_score());
        String insertsinglesql = "insert into question(library_num,type,topic,option_a,option_b,option_c," +
                "option_d,option_t,score,test_id,collect_flag,wrong_flag) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] splitstr = library.getLibrary_num().split(" ");
        //2 2
        //题库的指定数字id
        int library_num = Integer.parseInt(splitstr[0]);
        //试卷的初始id,原来已经+1
        int testid = Integer.parseInt(splitstr[1]);
        //试卷的份数
        int testnum = Integer.parseInt(library.getScore());
        //每一份试卷要求的单选题题目数    8/3 = 2,但是要求是4份试卷。
        //notrepeat=2   testnum=4
        int singlenum = Integer.parseInt(library.getSingle_num());
        int notrepeat = size / singlenum;
        //循环份数
        int num = 0;
        Question question;
        for (int i = 0; i < notrepeat; i++) {
            //试卷id增加1
            testid++;
            //循环增加每一份试卷的题目
            for (int j = 0; j < singlenum; j++) {
                question = list.get(num);
                db.execSQL(insertsinglesql, new Object[]{library_num, 1, question.getTopic(), question.getOption_a(),
                        question.getOption_b(), question.getOption_c(), question.getOption_d(),
                        question.getOption_t(), single_score, testid,0,0});
                num++;
            }
        }
        //下面那一份是有未重复的，也有重复的题目
        if (size % singlenum != 0) {
            testid = testid + 1;
            for (int i = 0; i < singlenum; i++) {
                if (num < size - 1) {
                    question = list.get(num);
                    db.execSQL(insertsinglesql, new Object[]{library_num, 1, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_c(), question.getOption_d(),
                            question.getOption_t(), single_score, testid,0,0});
                    num++;
                } else {
                    Random random = new Random();
                    //重复的题目
                    int ramnum = random.nextInt(size);
                    question = list.get(ramnum);
                    db.execSQL(insertsinglesql, new Object[]{library_num, 1, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_c(), question.getOption_d(),
                            question.getOption_t(), single_score, testid,0,-10});
                }
            }
        }
        //这是单选题不是最多的情况
        if (!library.getTest_flag().equals("1")) {
            //剩下的试卷单选题，全部是重复的
            int lesstestnum = testnum - notrepeat - 1;
            for (int i = 0; i < lesstestnum; i++) {
                testid++;
                Random random = new Random();
                for (int j = 0; j < singlenum; j++) {
                    int num1 = random.nextInt(size);
                    while (true) {
                        question = list.get(num1);
                        db.execSQL(insertsinglesql, new Object[]{library_num, 1, question.getTopic(), question.getOption_a(),
                                question.getOption_b(), question.getOption_c(), question.getOption_d(),
                                question.getOption_t(), single_score, testid,0,-10});
                        break;
                    }
                }
            }
        }
    }
    public void insertMultipleQuestion(Library library, LinkedList<Question> list) {
        int size = list.size();
        int multiple_score = Integer.parseInt(library.getMultiple_score());
        String insertmultiplesql = "insert into question(library_num,type,topic,option_a,option_b,option_c," +
                "option_d,option_e,option_f,option_t,score,test_id,collect_flag,wrong_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] splitstr = library.getLibrary_num().split(" ");
        //2 2
        //题库的指定数字id
        int library_num = Integer.parseInt(splitstr[0]);
        //试卷的初始id,原来已经+1
        int testid = Integer.parseInt(splitstr[1]);
        //试卷的份数
        int testnum = Integer.parseInt(library.getScore());
        //每一份试卷要求的单选题题目数    8/3 = 2,但是要求是4份试卷。
        //notrepeat=2   testnum=4
        int multiplenum = Integer.parseInt(library.getMultiple_num());
        int notrepeat = size / multiplenum;
        //循环份数
        int num = 0;
        Question question;
        for (int i = 0; i < notrepeat; i++) {
            //试卷id增加1
            testid++;
            //循环增加每一份试卷的题目
            for (int j = 0; j < multiplenum; j++) {
                question = list.get(num);
                db.execSQL(insertmultiplesql, new Object[]{library_num, 2, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_c(), question.getOption_d(),question.getOption_e(),
                            question.getOption_f(), question.getOption_t(), multiple_score, testid,0,0});
                num++;
            }
        }
        //下面那一份是有未重复的，也有重复的题目
        if (size % multiplenum != 0) {
            testid = testid + 1;
            for (int i = 0; i < multiplenum; i++) {
                if (num < size - 1) {
                    question = list.get(num);
                    db.execSQL(insertmultiplesql, new Object[]{library_num, 2, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_c(), question.getOption_d(),question.getOption_e(),
                            question.getOption_f(), question.getOption_t(), multiple_score, testid,0,0});
                    num++;
                } else {
                    Random random = new Random();
                    //重复的题目
                    int ramnum = random.nextInt(size);
                    question = list.get(ramnum);
                    db.execSQL(insertmultiplesql, new Object[]{library_num, 2, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_c(), question.getOption_d(),question.getOption_e(),
                            question.getOption_f(), question.getOption_t(), multiple_score, testid,0,-10});
                }
            }
        }
        //这是单选题不是最多的情况
        if (!library.getTest_flag().equals("2")) {
            //剩下的试卷单选题，全部是重复的
            int lesstestnum = testnum - notrepeat - 1;
            for (int i = 0; i < lesstestnum; i++) {
                testid++;
                Random random = new Random();
                for (int j = 0; j < multiplenum; j++) {
                    int num1 = random.nextInt(size);
                    while (true) {
                        question = list.get(num1);
                        db.execSQL(insertmultiplesql, new Object[]{library_num, 2, question.getTopic(), question.getOption_a(),
                                question.getOption_b(), question.getOption_c(), question.getOption_d(),question.getOption_e(),
                                question.getOption_f(), question.getOption_t(), multiple_score, testid,0,-10});
                        break;
                    }
                }
            }
        }
    }
    public void insertJudgeQuestion(Library library, LinkedList<Question> list) {
        int size = list.size();
        int judge_score = Integer.parseInt(library.getJudge_score());
        String insertejudgesql = "insert into question(library_num,type,topic,option_a,option_b,option_t," +
                "score,test_id,collect_flag,wrong_flag) values(?,?,?,?,?,?,?,?,?,?)";
        String[] splitstr = library.getLibrary_num().split(" ");
        //2 2
        //题库的指定数字id
        int library_num = Integer.parseInt(splitstr[0]);
        //试卷的初始id,原来已经+1
        int testid = Integer.parseInt(splitstr[1]);
        //试卷的份数
        int testnum = Integer.parseInt(library.getScore());
        //每一份试卷要求的单选题题目数    8/3 = 2,但是要求是4份试卷。
        //notrepeat=2   testnum=4
        int judgenum = Integer.parseInt(library.getJudge_num());
        int notrepeat = size / judgenum;
        //循环份数
        int num = 0;
        Question question;
        for (int i = 0; i < notrepeat; i++) {
            //试卷id增加1
            testid++;
            //循环增加每一份试卷的题目
            for (int j = 0; j < judgenum; j++) {
                question = list.get(num);
                db.execSQL(insertejudgesql, new Object[]{library_num, 3, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_t(), judge_score, testid,0,0});
                num++;
            }
        }
        //下面那一份是有未重复的，也有重复的题目
        if (size % judgenum != 0) {
            testid = testid + 1;
            for (int i = 0; i < judgenum; i++) {
                if (num < size - 1) {
                    question = list.get(num);
                    db.execSQL(insertejudgesql, new Object[]{library_num, 3, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_t(), judge_score, testid,0,0});
                    num++;
                } else {
                    Random random = new Random();
                    //重复的题目
                    int ramnum = random.nextInt(size);
                    question = list.get(ramnum);
                    db.execSQL(insertejudgesql, new Object[]{library_num, 3, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_t(), judge_score, testid,0,-10});
                }
            }
        }
        //这是单选题不是最多的情况
        if (!library.getTest_flag().equals("3")) {
            //剩下的试卷单选题，全部是重复的
            int lesstestnum = testnum - notrepeat - 1;
            for (int i = 0; i < lesstestnum; i++) {
                testid++;
                Random random = new Random();
                for (int j = 0; j < judgenum; j++) {
                    int num1 = random.nextInt(size);
                    while (true) {
                        question = list.get(num1);
                        db.execSQL(insertejudgesql, new Object[]{library_num, 3, question.getTopic(), question.getOption_a(),
                            question.getOption_b(), question.getOption_t(), judge_score, testid,0,-10});
                        break;
                    }
                }
            }
        }
        dbClose();
    }

    public void dbClose() {
        if(db != null){
            db.close();
        }
        if (dbOpenHelper != null)
            dbOpenHelper.close();
    }
}
