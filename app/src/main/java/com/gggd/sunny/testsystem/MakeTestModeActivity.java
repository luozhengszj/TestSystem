package com.gggd.sunny.testsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gggd.sunny.testsystem.bean.Library;
import com.gggd.sunny.testsystem.bean.Question;
import com.gggd.sunny.testsystem.dao.InsertQuestionDB;
import com.gggd.sunny.testsystem.dao.LibraryAndTestDB;
import com.gggd.sunny.testsystem.tools.FileTools;

import java.util.LinkedList;

import static java.lang.String.valueOf;

/**
 * Created by Sunny on 2017/10/12.
 */

public class MakeTestModeActivity extends TitleActivity {

    private EditText metlibraryname;
    private EditText metsinglenum;
    private EditText metsinglesorce;
    private EditText metmultiplenum;
    private EditText metmultiplesorce;
    private EditText metjudgenum;
    private EditText metjudgesorce;

    private Button mbtnmakelibrarydefault;
    private Button mbtnmakelibraryok;
    private Button mbuttonbackward;

    private String singlefilepath;
    private String multiplefilepath;
    private String judgefilepath;


    private String defaultliabraryname;

    //单选题是否有路径文件
    private int sflag = 0;
    private int mflag = 0;
    private int jflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_test_model);
        setTitle("设置试卷组成");
        showForwardView(false);

        metlibraryname = (EditText) findViewById(R.id.etlibraryname);
        metsinglenum = (EditText) findViewById(R.id.etsinglenum);
        metsinglesorce = (EditText) findViewById(R.id.etsinglesorce);
        metmultiplenum = (EditText) findViewById(R.id.etmultiplenum);
        metmultiplesorce = (EditText) findViewById(R.id.etmultiplesorce);
        metjudgenum = (EditText) findViewById(R.id.etjudgenum);
        metjudgesorce = (EditText) findViewById(R.id.etjudgesorce);
        mbtnmakelibrarydefault = (Button) findViewById(R.id.btnmakelibrarydefault);
        mbtnmakelibraryok = (Button) findViewById(R.id.btnmakelibraryok);
        mbuttonbackward = (Button) findViewById(R.id.button_backward);

        mbtnmakelibraryok.setEnabled(true);
        mbtnmakelibrarydefault.setEnabled(true);

        mbtnmakelibrarydefault.setOnClickListener(this);
        mbtnmakelibraryok.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        singlefilepath = extras.getString("singlepath");
        multiplefilepath = extras.getString("multiplefilepath");
        judgefilepath = extras.getString("judgefilepath");
        setLibraryName(singlefilepath, multiplefilepath, judgefilepath);
    }

    @Override
    public void onClick(View v) {
        //一份是多少题！
        int count = 100 / (sflag * 3 + mflag * 1 + jflag * 1);
        switch (v.getId()) {
            case R.id.button_backward:
                super.onClick(v);
                break;
            case R.id.btnmakelibrarydefault:
                metlibraryname.setText(defaultliabraryname);
                if (sflag == 1) {
                    metsinglesorce.setText("1");
                    int scount = count * 3;
                    if (scount == 99)
                        scount = 100;
                    metsinglenum.setText("" + scount);
                }
                if (mflag == 1) {
                    metmultiplesorce.setText("1");
                    metmultiplenum.setText("" + count);
                }
                if (jflag == 1) {
                    metjudgesorce.setText("1");
                    metjudgenum.setText("" + count);
                }
                break;
            case R.id.btnmakelibraryok:
                clickOK();
                break;
        }
    }

    //自动设置libraryname的值
    private void setLibraryName(String singlepath, String multiplefilepath, String judgefilepath) {
        if (!"".equals(singlepath)) {
            sflag = 1;
            int start = singlepath.lastIndexOf("/");
            int end = singlepath.lastIndexOf(".");
            String suffixname = singlepath.substring(start+1, end);
            if (suffixname.length() > 10)
                suffixname = suffixname.substring(1, 11);
            metlibraryname.setText(suffixname);
            defaultliabraryname = suffixname;
            metsinglenum.setFocusable(true);
            metsinglesorce.setFocusable(true);
            metsinglenum.setFocusableInTouchMode(true);
            metsinglesorce.setFocusableInTouchMode(true);
        }
        if (!"".equals(multiplefilepath)) {
            mflag = 1;
            int start = multiplefilepath.lastIndexOf("/");
            int end = multiplefilepath.lastIndexOf(".");
            String suffixname = multiplefilepath.substring(start+1, end);
            if (suffixname.length() > 10)
                suffixname = suffixname.substring(1, 11);
            metlibraryname.setText(suffixname);
            defaultliabraryname = suffixname;
            metmultiplenum.setFocusable(true);
            metmultiplesorce.setFocusable(true);
            metmultiplenum.setFocusableInTouchMode(true);
            metmultiplesorce.setFocusableInTouchMode(true);
        }
        if (!"".equals(judgefilepath)) {
            jflag = 1;
            int start = judgefilepath.lastIndexOf("/");
            int end = judgefilepath.lastIndexOf(".");
            String suffixname = judgefilepath.substring(start+1, end);
            if (suffixname.length() > 10)
                suffixname = suffixname.substring(1, 11);
            metlibraryname.setText(suffixname);
            defaultliabraryname = suffixname;
            metjudgenum.setFocusable(true);
            metjudgesorce.setFocusable(true);
            metjudgenum.setFocusableInTouchMode(true);
            metjudgesorce.setFocusableInTouchMode(true);
        }
    }

    public void clickOK() {
        //下面的函数是为了检查试卷的设置情况，有限制
        //限制1.试卷的每一类题目数目不得超过该类题目的总题目数
        //限制2.试卷的总分必须为100分。
        Library library = new Library();
        library.setLibrary_name(metlibraryname.getText().toString());
        library.setSingle_num("" + metsinglenum.getText().toString());
        library.setSingle_score("" + metsinglesorce.getText().toString());
        library.setMultiple_num("" + metmultiplenum.getText().toString());
        library.setMultiple_score("" + metmultiplesorce.getText().toString());
        library.setJudge_num("" + metjudgenum.getText().toString());
        library.setJudge_score("" + metjudgesorce.getText().toString());
        library.setSingle_path("" + singlefilepath);
        library.setMultiple_path("" + multiplefilepath);
        library.setJudge_path("" + judgefilepath);
        if (makeTestLimit()) {
            int limitnum = insertData(library);
            if (limitnum != 0) {
                SharedPreferences mySharedPreferences= this.getSharedPreferences("librarydata",
                        Activity.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                
                editor.putString("libraryname", library.getLibrary_name());
                //提交当前数据
                editor.commit();
                Toast.makeText(this, "增加成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MakeTestModeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public boolean makeTestLimit() {
        boolean limitflag = false;
        LibraryAndTestDB library = new LibraryAndTestDB(this);
        boolean namelimit = library.libraryNameLimit(metlibraryname.getText().toString());
        if (namelimit == false) {
            Toast.makeText(this, "题库名重复，请重新输入！", Toast.LENGTH_SHORT).show();
            limitflag = false;
        } else {
            int count = Integer.parseInt(0 + metsinglenum.getText().toString()) * Integer.parseInt(0 + metsinglesorce.getText().toString()) +
                    Integer.parseInt(0 + metmultiplenum.getText().toString()) * Integer.parseInt(0 + metmultiplesorce.getText().toString()) +
                    Integer.parseInt(0 + metjudgenum.getText().toString()) * Integer.parseInt(0 + metjudgesorce.getText().toString());
            if (count == 100)
                limitflag = true;
            else {
                Toast.makeText(this, "总分必须为100分！现在为:" + valueOf(count), Toast.LENGTH_SHORT).show();
                limitflag = false;
            }
        }
        return limitflag;
    }

    //数据库操作
    public int insertData(Library library) {
        FileTools filetools = new FileTools();
        LinkedList<Question> singleset = new LinkedList();
        LinkedList<Question> multipleset = new LinkedList();
        LinkedList<Question> judgeset = new LinkedList();
        int flagmax = 1;
        int maxflag = 0;
        //得到集合并且得出哪类题目最多
        //也就是说，算出最少需要多少次考试，才能把该类题目全部做过一次
        //一次在导入题库和设置试卷时，尽量保持比例相近！
        if (!"".equals(library.getSingle_path())) {
            singleset = filetools.readExcel(library.getSingle_path());
            int tmpnum = Integer.parseInt(library.getSingle_num());
            flagmax = (singleset.size() + tmpnum - 1) / tmpnum;
            maxflag = 1;
        }
        if (!"".equals(library.getMultiple_path())) {
            multipleset = filetools.readExcel(library.getMultiple_path());
            int tmpnum = Integer.parseInt(library.getMultiple_num());
            int j = (multipleset.size() + tmpnum - 1) / tmpnum;
            if (flagmax < j) {
                flagmax = j;
                maxflag = 2;
            }
        }
        if (!"".equals(library.getJudge_path())) {
            judgeset = filetools.readExcel(library.getJudge_path());
            int tmpnum = Integer.parseInt(library.getJudge_num());
            int j = (judgeset.size() + tmpnum - 1) / tmpnum;
            if (flagmax < j) {
                flagmax = j;
                maxflag = 3;
            }
        }
        if (singleset.size() >= Integer.parseInt(0 + library.getSingle_num()) &&
                multipleset.size() >= Integer.parseInt(0 + library.getMultiple_num()) &&
                judgeset.size() >= Integer.parseInt(0 + library.getJudge_num())) {
            LibraryAndTestDB libraryAndTestDB;
            libraryAndTestDB = new LibraryAndTestDB(this);
            String librarynum = libraryAndTestDB.insertLibraryTestCount(library, flagmax);
            library.setLibrary_num(librarynum);
            library.setTest_flag(valueOf(maxflag));
            library.setScore(valueOf(flagmax));

            InsertQuestionDB insertQuestionDB = new InsertQuestionDB(this);
            if (!library.getSingle_num().equals(""))
                insertQuestionDB.insertSingleQuestion(library, singleset);
            if (!library.getMultiple_num().equals(""))
                insertQuestionDB.insertMultipleQuestion(library, multipleset);
            if (!library.getJudge_num().equals(""))
                insertQuestionDB.insertJudgeQuestion(library, judgeset);
            insertQuestionDB.dbClose();
        } else {
            Toast.makeText(this, "导入失败存在单份某类题目大于该类总数！", Toast.LENGTH_SHORT).show();
            flagmax = 0;
        }
        return flagmax;
    }

}
