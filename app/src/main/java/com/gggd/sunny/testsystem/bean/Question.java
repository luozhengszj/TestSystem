package com.gggd.sunny.testsystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sunny on 2017/10/13.
 */

public class Question implements Parcelable{

    private int id;
    private String library_num;
    private String type;
    private String topic;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String option_e;
    private String option_f;
    private String option_t;
    private String answer;
    private int score;
    private int test_id;
    private String collect_flag;
    private String wrong_flag;

    @Override
    public String toString() {
        return topic;
    }

    public Question() {
    }

    public Question(String topic, String option_a, String option_b, String option_c,
                    String option_d, String option_e, String option_f, String option_t) {
        this.topic = topic;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.option_e = option_e;
        this.option_f = option_f;
        this.option_t = option_t;
    }

    public Question(int id, String type, String topic, String option_a, String option_b, String option_c,
                    String option_d, String option_e, String option_f, String option_t,String answer,
                    int score,String wrong_flag) {
        this.id = id;
        this.type = type;
        this.topic = topic;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.option_e = option_e;
        this.option_f = option_f;
        this.option_t = option_t;
        this.answer = answer;
        this.score = score;
        this.wrong_flag = wrong_flag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLibrary_num(String library_num) {
        this.library_num = library_num;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public void setOption_e(String option_e) {
        this.option_e = option_e;
    }

    public void setOption_f(String option_f) {
        this.option_f = option_f;
    }

    public void setOption_t(String option_t) {
        this.option_t = option_t;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getId() {
        return id;
    }

    public String getLibrary_num() {
        return library_num;
    }

    public String getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public String getOption_a() {
        return option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public String getOption_e() {
        return option_e;
    }

    public String getOption_f() {
        return option_f;
    }

    public String getOption_t() {
        return option_t;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public int getTest_id() {
        return test_id;
    }

    public String getCollect_flag() {
        return collect_flag;
    }

    public void setCollect_flag(String collect_flag) {
        this.collect_flag = collect_flag;
    }

    public String getWrong_flag() {
        return wrong_flag;
    }

    public void setWrong_flag(String wrong_flag) {
        this.wrong_flag = wrong_flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }
//    private int id;
//    private String library_num;
//    private String type;
//    private String topic;
//    private String option_a;
//    private String option_b;
//    private String option_c;
//    private String option_d;
//    private String option_e;
//    private String option_f;
//    private String option_t;
//    private String answer;
//    private int score;
//    private int test_id;
//    private String collect_flag;
//    private int wrong_flag;
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(library_num);
        dest.writeString(type);
        dest.writeString(topic);
        dest.writeString(option_a);
        dest.writeString(option_b);
        dest.writeString(option_c);
        dest.writeString(option_d);
        dest.writeString(option_e);
        dest.writeString(option_f);
        dest.writeString(option_t);
        dest.writeString(answer);
        dest.writeInt(score);
        dest.writeInt(test_id);
        dest.writeString(collect_flag);
        dest.writeString(wrong_flag);
    }
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            Question question = new Question();
            question.id = source.readInt();
            question.library_num = source.readString();
            question.type = source.readString();
            question.topic = source.readString();
            question.option_a = source.readString();
            question.option_b = source.readString();
            question.option_c = source.readString();
            question.option_d = source.readString();
            question.option_e = source.readString();
            question.option_f = source.readString();
            question.option_t = source.readString();
            question.answer = source.readString();
            question.score = source.readInt();
            question.test_id = source.readInt();
            question.collect_flag = source.readString();
            question.wrong_flag = source.readString();
            return question;
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
