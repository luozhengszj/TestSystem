package com.gggd.sunny.testsystem.bean;

/**
 * Created by Sunny on 2017/10/13.
 */

public class Library {

    private int id;
    //library_name表示题库的唯一名称
    private String library_name;
    //library_num表示题库的唯一num
    private String library_num;
    //test_flag   1表示未进行考试，2表示已进行考试，3表示错题考试
    private String test_flag;
    private String single_num;
    private String multiple_num;
    private String judge_num;
    private String single_score;
    private String multiple_score;
    private String judge_score;
    private String single_path;
    private String multiple_path;
    private String judge_path;
    private String score;
    private String time;

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", library_name='" + library_name + '\'' +
                ", library_num='" + library_num + '\'' +
                ", test_flag='" + test_flag + '\'' +
                ", single_num='" + single_num + '\'' +
                ", multiple_num='" + multiple_num + '\'' +
                ", judge_num='" + judge_num + '\'' +
                ", single_score='" + single_score + '\'' +
                ", multiple_score='" + multiple_score + '\'' +
                ", judge_score='" + judge_score + '\'' +
                ", single_path='" + single_path + '\'' +
                ", multiple_path='" + multiple_path + '\'' +
                ", judge_path='" + judge_path + '\'' +
                ", score='" + score + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Library() {
    }

    public Library(int id, String library_name, String library_num, String test_flag,
                   String single_num, String multiple_num, String judge_num, String time) {
        this.id = id;
        this.library_name = library_name;
        this.library_num = library_num;
        this.test_flag = test_flag;
        this.single_num = single_num;
        this.multiple_num = multiple_num;
        this.judge_num = judge_num;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibrary_name() {
        return library_name;
    }

    public void setLibrary_name(String library_name) {
        this.library_name = library_name;
    }

    public String getLibrary_num() {
        return library_num;
    }

    public void setLibrary_num(String library_num) {
        this.library_num = library_num;
    }

    public String getTest_flag() {
        return test_flag;
    }

    public void setTest_flag(String test_flag) {
        this.test_flag = test_flag;
    }

    public String getSingle_num() {
        return single_num;
    }

    public void setSingle_num(String single_num) {
        this.single_num = single_num;
    }

    public String getMultiple_num() {
        return multiple_num;
    }

    public void setMultiple_num(String multiple_num) {
        this.multiple_num = multiple_num;
    }

    public String getJudge_num() {
        return judge_num;
    }

    public void setJudge_num(String judge_num) {
        this.judge_num = judge_num;
    }

    public String getSingle_score() {
        return single_score;
    }
    public void setSingle_score(String single_score) {
        this.single_score = single_score;
    }

    public String getMultiple_score() {
        return multiple_score;
    }

    public void setMultiple_score(String multiple_score) {
        this.multiple_score = multiple_score;
    }

    public String getJudge_score() {
        return judge_score;
    }

    public void setJudge_score(String judge_score) {
        this.judge_score = judge_score;
    }

    public String getSingle_path() {
        return single_path;
    }

    public void setSingle_path(String single_path) {
        this.single_path = single_path;
    }

    public String getMultiple_path() {
        return multiple_path;
    }

    public void setMultiple_path(String multiple_path) {
        this.multiple_path = multiple_path;
    }

    public String getJudge_path() {
        return judge_path;
    }

    public void setJudge_path(String judge_path) {
        this.judge_path = judge_path;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
