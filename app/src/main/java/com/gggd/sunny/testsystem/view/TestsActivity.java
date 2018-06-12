package com.gggd.sunny.testsystem.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gggd.sunny.testsystem.R;
import com.gggd.sunny.testsystem.TitleActivity;
import com.gggd.sunny.testsystem.bean.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sunny on 2017/10/27.
 */

public class TestsActivity extends TitleActivity implements RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private Button mbtnbutton_forward;

    private Button mbtnabovetquestion;
    private Button mbtnnextquestion;
    private CheckBox mcbncollectquestion;
    private TextView mtvquestionlist;
    private TextView mtvtopic;
    private RadioGroup mrgquestionsingle;
    private RadioButton mrbquestionsingle1;
    private RadioButton mrbquestionsingle2;
    private RadioButton mrbquestionsingle3;
    private RadioButton mrbquestionsingle4;
    private RadioGroup mrgquestionjedge;
    private RadioButton mrbquestionjedge1;
    private RadioButton mrbquestionjedge2;
    private View mlayoutcheck;
    private CheckBox mcbquestionmultiple1;
    private CheckBox mcbquestionmultiple2;
    private CheckBox mcbquestionmultiple3;
    private CheckBox mcbquestionmultiple4;
    private CheckBox mcbquestionmultiple5;
    private CheckBox mcbquestionmultiple6;
    private TextView mtvquestiontrue;

    private ArrayList<Question> list;
    //列表
    private DrawerLayout mdlquestionlist;
    private ListView mlvquestionlist;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private List<CheckBox> checkBoxList;
    private ActionBarDrawerToggle mdbtoggle;

    private int test_id;
    private String libraryname;
    private int questionselect = -1;
    private int nownum;
    private int size;
    private Question questionnow;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begintest_layout);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.testdrawer_layout);
        drawerLayout.setOnTouchListener(this);//将主容器的监听交给本activity，本activity再交给mGestureDetector
        drawerLayout.setLongClickable(true);   //必需设置这为true 否则也监听不到手势
        mGestureDetector = new GestureDetector(this, myGestureListener);
        mbtnbutton_forward = (Button) findViewById(R.id.button_forward);
        mtvquestionlist = (TextView) findViewById(R.id.tvquestionlist);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        list = bundle.getParcelableArrayList("questionlist");
        libraryname = bundle.getString("libraryname");
        setTitle(libraryname);
        showForwardView(true);
        showBackwardView(false);
        test_id = bundle.getInt("test_id");
        Log.d("tests lztest_id", "" + test_id);
        size = list.size();
        nownum = 1;
        mtvquestionlist.setText(nownum + "/" + size);

        mtvquestiontrue = (TextView) findViewById(R.id.tvquestiontruetext);
        mbtnabovetquestion = (Button) findViewById(R.id.btnabovetquestion);
        mbtnnextquestion = (Button) findViewById(R.id.btnnextquestion);
        mtvquestionlist = (TextView) findViewById(R.id.tvquestionlist);
        mtvtopic = (TextView) findViewById(R.id.tvtopic);
        mrgquestionsingle = (RadioGroup) findViewById(R.id.rgquestionsingle);
        mrbquestionsingle1 = (RadioButton) findViewById(R.id.rbquestionsingle1);
        mrbquestionsingle2 = (RadioButton) findViewById(R.id.rbquestionsingle2);
        mrbquestionsingle3 = (RadioButton) findViewById(R.id.rbquestionsingle3);
        mrbquestionsingle4 = (RadioButton) findViewById(R.id.rbquestionsingle4);
        mrgquestionjedge = (RadioGroup) findViewById(R.id.rgquestionjedge);
        mrbquestionjedge1 = (RadioButton) findViewById(R.id.rbquestionjedge1);
        mrbquestionjedge2 = (RadioButton) findViewById(R.id.rbquestionjedge2);
        mlayoutcheck = findViewById(R.id.layoutcheck);
        mcbquestionmultiple1 = (CheckBox) findViewById(R.id.cbquestionmultiple1);
        mcbquestionmultiple2 = (CheckBox) findViewById(R.id.cbquestionmultiple2);
        mcbquestionmultiple3 = (CheckBox) findViewById(R.id.cbquestionmultiple3);
        mcbquestionmultiple4 = (CheckBox) findViewById(R.id.cbquestionmultiple4);
        mcbquestionmultiple5 = (CheckBox) findViewById(R.id.cbquestionmultiple5);
        mcbquestionmultiple6 = (CheckBox) findViewById(R.id.cbquestionmultiple6);
        mcbncollectquestion = (CheckBox) findViewById(R.id.cbcollect);

        checkBoxList = new ArrayList<>();
        checkBoxList.add(mcbquestionmultiple1);
        checkBoxList.add(mcbquestionmultiple2);
        checkBoxList.add(mcbquestionmultiple3);
        checkBoxList.add(mcbquestionmultiple4);
        checkBoxList.add(mcbquestionmultiple5);
        checkBoxList.add(mcbquestionmultiple6);
        //事件
        mbtnabovetquestion.setOnClickListener(this);
        mbtnnextquestion.setOnClickListener(this);
        mtvquestionlist.setOnClickListener(this);
        mbtnbutton_forward.setOnClickListener(this);
        mcbncollectquestion.setOnCheckedChangeListener(this);

        //题目列表
        mdlquestionlist = (DrawerLayout) findViewById(R.id.testdrawer_layout);
        mlvquestionlist = (ListView) findViewById(R.id.test_drawer);
        mlvquestionlist.setOnItemClickListener(this);
        //拉出抽屉
        mtvquestionlist.setOnClickListener(this);
        mlvquestionlist.addHeaderView(new View(this));
        //test_id == 0,这是错题查看、所有题目查看的时候
        //浏览题目，只需要上一题、下一题、题目跳转、根据答案选题目
        //所以事件只需要上一题、下一题、题目跳转，不需要监控选项变化
        //同时设置“交卷”为“返回”，返回（包括返回键）即返回到主页
        //test_id !=0的时候，说明是考试（在这里就是错题考试）
        selectTypeShow(list.get(0));
        if (test_id == -2) {
            questionnow = list.get(0);
        }
        mbtnbutton_forward.setText("交卷");
        mrgquestionsingle.setOnCheckedChangeListener(this);
        mrgquestionjedge.setOnCheckedChangeListener(this);

        mdbtoggle = new ActionBarDrawerToggle(this, mdlquestionlist, R.drawable.delete,
                R.string.testdrawer_open, R.string.testdrawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mbtnbutton_forward.setText("列表");
                mbtnbutton_forward.setEnabled(false);
                arrayList = new ArrayList<>();
                for (int i = 1; i <= size; i++) {
                    String answer = list.get(i - 1).getAnswer();
                    if (answer.equals("")) {
                        arrayList.add("  " + i + "   未做");
                    } else {
                        arrayList.add("  " + i + "   " + answer);
                    }
                }
                adapter = new ArrayAdapter<>(TestsActivity.this, android.R.layout.simple_list_item_1, arrayList);
                mlvquestionlist.setAdapter(adapter);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mbtnbutton_forward.setText("交卷");
                mbtnbutton_forward.setEnabled(true);
            }
        };
        mdlquestionlist.setDrawerListener(mdbtoggle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnabovetquestion:
                if (nownum == 1) {
                    break;
                } else {
                    insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
                    nownum = nownum - 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                    break;
                }
            case R.id.btnnextquestion:
                if (nownum == size) {
                    break;
                } else {
                    insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
                    nownum = nownum + 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                    break;
                }
            case R.id.button_forward:
                checkAnswer();
                break;
        }
    }

    //这是题目的显示
    public void selectTypeShow(Question question) {

        String type = question.getType();
        String selectTypeShowanswer = question.getAnswer();
        mrgquestionjedge.clearCheck();
        mrgquestionsingle.clearCheck();
        if ("1" .equals(question.getCollect_flag())) {
            mcbncollectquestion.setChecked(true);
        } else {
            mcbncollectquestion.setChecked(false);
        }
        if (type.equals("1")) {
            mtvquestiontrue.setText("单选题");
            mlayoutcheck.setVisibility(View.GONE);
            mrgquestionjedge.setVisibility(View.GONE);
            mrgquestionsingle.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());
            mrbquestionsingle1.setText(question.getOption_a());
            mrbquestionsingle2.setText(question.getOption_b());
            mrbquestionsingle3.setText(question.getOption_c());
            mrbquestionsingle4.setText(question.getOption_d());
            if(question.getOption_d().length() == 0){
                mrbquestionsingle4.setVisibility(View.GONE);
            }else{
                mrbquestionsingle4.setVisibility(View.VISIBLE);
            }
            if (selectTypeShowanswer.equals("A")) {
                mrbquestionsingle1.setChecked(true);
            } else if (selectTypeShowanswer.equals("B")) {
                mrbquestionsingle2.setChecked(true);
            } else if (selectTypeShowanswer.equals("C")) {
                mrbquestionsingle3.setChecked(true);
            } else if (selectTypeShowanswer.equals("D")) {
                mrbquestionsingle4.setChecked(true);
            }
        } else if (type.equals("3")) {
            mtvquestiontrue.setText("判断题");
            mlayoutcheck.setVisibility(View.GONE);
            mrgquestionsingle.setVisibility(View.GONE);
            mrgquestionjedge.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());
            mrbquestionjedge1.setText(question.getOption_a());
            mrbquestionjedge2.setText(question.getOption_b());
            if (selectTypeShowanswer.equals("对")) {
                mrbquestionjedge1.setChecked(true);
            } else if (selectTypeShowanswer.equals("错")) {
                mrbquestionjedge2.setChecked(true);
            }
        } else if (type.equals("2")) {
            mtvquestiontrue.setText("多选题");
            mrgquestionjedge.setVisibility(View.GONE);
            mrgquestionsingle.setVisibility(View.GONE);
            mcbquestionmultiple1.setChecked(false);
            mcbquestionmultiple2.setChecked(false);
            mcbquestionmultiple3.setChecked(false);
            mcbquestionmultiple4.setChecked(false);
            mcbquestionmultiple5.setChecked(false);
            mcbquestionmultiple6.setChecked(false);
            mlayoutcheck.setVisibility(View.VISIBLE);
            mtvtopic.setText(nownum + "." + question.getTopic());
            mcbquestionmultiple1.setText(question.getOption_a());
            mcbquestionmultiple2.setText(question.getOption_b());
            mcbquestionmultiple3.setText(question.getOption_c());
            mcbquestionmultiple4.setText(question.getOption_d());
            mcbquestionmultiple5.setText(question.getOption_e());
            mcbquestionmultiple6.setText(question.getOption_f());
            if(question.getOption_d().length() == 0){
                mcbquestionmultiple4.setVisibility(View.GONE);
                mcbquestionmultiple5.setVisibility(View.GONE);
                mcbquestionmultiple6.setVisibility(View.GONE);
                if(question.getOption_e().length() == 0){
                    mcbquestionmultiple5.setVisibility(View.GONE);
                    mcbquestionmultiple6.setVisibility(View.GONE);
                    if(question.getOption_f().length() == 0){
                        mcbquestionmultiple6.setVisibility(View.GONE);
                    }else{
                        mcbquestionmultiple6.setVisibility(View.VISIBLE);
                    }
                }else{
                    mcbquestionmultiple5.setVisibility(View.VISIBLE);
                    if(question.getOption_f().length() == 0){
                        mcbquestionmultiple6.setVisibility(View.GONE);
                    }else{
                        mcbquestionmultiple6.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                mcbquestionmultiple4.setVisibility(View.VISIBLE);
                if(question.getOption_e().length() == 0){
                    mcbquestionmultiple5.setVisibility(View.GONE);
                    mcbquestionmultiple6.setVisibility(View.GONE);
                    if(question.getOption_f().length() == 0){
                        mcbquestionmultiple6.setVisibility(View.GONE);
                    }else{
                        mcbquestionmultiple6.setVisibility(View.VISIBLE);
                    }
                }else{
                    mcbquestionmultiple5.setVisibility(View.VISIBLE);
                    if(question.getOption_f().length() == 0){
                        mcbquestionmultiple6.setVisibility(View.GONE);
                    }else{
                        mcbquestionmultiple6.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (!selectTypeShowanswer.equals(" ")) {
                String[] sss = selectTypeShowanswer.split("");
                for (int i = 1; i < sss.length; i++) {
                    checkBoxList.get((sss[i].charAt(0) - 65)).setChecked(true);
                }
            }
        }
    }

    //插入答案
    public void insertAnswer(int questionnum, int selectnum, String type) {
        String questionanswer = "";
        if ("2" .equals(type)) {
            int checkBoxListsize = checkBoxList.size();
            for (int i = 0; i < checkBoxListsize; i++) {
                if (checkBoxList.get(i).isChecked()) {
                    char c1 = (char) (i + 65);
                    questionanswer = questionanswer + c1;
                }
            }
        } else if ("1" .equals(type)) {
            if (selectnum > -1 && selectnum < 4) {
                questionanswer = questionanswer + (char) (selectnum + 65);
            }
        } else if ("3" .equals(type)) {
            Log.d("lzje", selectnum + "");
            if (selectnum == 7) {
                questionanswer = "对";
            } else if (selectnum == 8) {
                questionanswer = "错";
            }
        }
        list.get(questionnum).setAnswer(questionanswer);
    }

    //检查答案是否填完
    public void checkAnswer() {
        boolean f = true;
        insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
        for (Question q : list) {
            if (q.getAnswer().equals("")) {
                f = false;
            }
        }
        String message;
        if (f == false) {
            message = "题目未做完，是否确定交卷？";
        } else {
            message = "是否确定交卷？";
        }
        new AlertDialog.Builder(TestsActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle(message)
                .setIcon(android.R.drawable.divider_horizontal_bright)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        Intent it = new Intent(TestsActivity.this, ShowGrageActivity.class);
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd hh:mm");
                        String date = sDateFormat.format(curDate);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("questionlist", list);
                        bundle.putString("libraryname", libraryname);
                        bundle.putString("date", date);
                        bundle.putInt("test_id", test_id);
                        it.putExtras(bundle);
                        TestsActivity.this.startActivity(it);
                        finish();
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long clickTime = 0;

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 1000) {
            Toast.makeText(getApplicationContext(), "1秒内再次点击退出,不会保存此次考试！", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
        nownum = position;
        mtvquestionlist.setText(position + "/" + size);
        selectTypeShow(list.get(position - 1));
    }

    //是否收藏，这是对
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            list.get(nownum - 1).setCollect_flag("1");
        } else {
            list.get(nownum - 1).setCollect_flag("0");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int i = -10;
        int radioButtonId;
        if (group.equals(mrgquestionsingle)) {
            radioButtonId = mrgquestionsingle.getCheckedRadioButtonId();
            i = mrgquestionsingle.indexOfChild(mrgquestionsingle.findViewById(radioButtonId));
        } else if (group.equals(mrgquestionjedge)) {
            radioButtonId = mrgquestionjedge.getCheckedRadioButtonId();
            i = mrgquestionjedge.indexOfChild(mrgquestionjedge.findViewById(radioButtonId));
            i = i + 7;
        }
        questionselect = i;
    }
    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 0;  //最小速度
    GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e1.getX()-e2.getX();
            float x2 = e2.getX()-e1.getX();
            //往左滑动
            if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                if (nownum != size) {
                    insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
                    nownum = nownum + 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                }
            }
            //往右滑动
            else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                if (nownum != 1) {
                    insertAnswer(nownum - 1, questionselect, list.get(nownum - 1).getType());
                    nownum = nownum - 1;
                    questionnow = list.get(nownum - 1);
                    mtvquestionlist.setText(nownum + "/" + size);
                    selectTypeShow(questionnow);
                }
            }
            return false;
        }
    };
    //下面是实现OnTouch方法 并将处理touch时间交给mGestureDetector
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }
}
